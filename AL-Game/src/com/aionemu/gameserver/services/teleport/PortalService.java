/*
 * This file is part of NextGenCore <Ver:3.7>.
 *
 *  NextGenCore is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  NextGenCore is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with NextGenCore. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.aionemu.gameserver.services.teleport;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.siege.FortressLocation;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.templates.InstanceCooltime;
import com.aionemu.gameserver.model.templates.portal.ItemReq;
import com.aionemu.gameserver.model.templates.portal.PortalLoc;
import com.aionemu.gameserver.model.templates.portal.PortalPath;
import com.aionemu.gameserver.model.templates.portal.PortalReq;
import com.aionemu.gameserver.model.templates.portal.QuestReq;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author ATracer, xTz
 */
public class PortalService {

	private static Logger log = LoggerFactory.getLogger(PortalService.class);

	/**
	 * Add portation task to player with talkDelay delay to location specified by portalTemplate
	 */
	public static void port(PortalPath portalPath, Player player, int npcObjectId) {
		if (!CustomConfig.ENABLE_INSTANCES) {
			return;
		}

		PortalLoc loc = DataManager.PORTAL_LOC_DATA.getPortalLoc(portalPath.getLocId());
		if (loc == null) {
			log.warn("No portal loc for locId" + portalPath.getLocId());
			return;
		}

		boolean instanceTitleReq = false;
		boolean instanceLevelReq = false;
		boolean instanceRaceReq = false;
		boolean instanceQuestReq = false;
		boolean instanceGroupReq = false;
		boolean instanceItemReq = false;
		int instanceCooldownRate = 0;

		int mapId = loc.getWorldId();
		int playerSize = portalPath.getPlayerCount();
		boolean isInstance = portalPath.isInstance();

		if (player.getAccessLevel() < AdminConfig.INSTANCE_REQ) {
			instanceTitleReq = !player.havePermission(MembershipConfig.INSTANCES_TITLE_REQ);
			instanceLevelReq = !player.havePermission(MembershipConfig.INSTANCES_LEVEL_REQ);
			instanceRaceReq = !player.havePermission(MembershipConfig.INSTANCES_RACE_REQ);
			instanceQuestReq = !player.havePermission(MembershipConfig.INSTANCES_QUEST_REQ);
			instanceGroupReq = !player.havePermission(MembershipConfig.INSTANCES_GROUP_REQ);
			instanceItemReq = !player.havePermission(MembershipConfig.INSTANCES_ITEM_REQ);
			instanceCooldownRate = InstanceService.getInstanceRate(player, loc.getWorldId());
		}

		if (instanceRaceReq && !checkRace(player, portalPath.getRace())) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MOVE_PORTAL_ERROR_INVALID_RACE);
			return;
		}
		if (instanceGroupReq && (!checkPlayerSize(player, portalPath, npcObjectId))) {
			return;
		}
		int sigeId = portalPath.getSigeId();
		if (instanceRaceReq && sigeId != 0 && !checkSigeId(player, sigeId)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MOVE_PORTAL_ERROR_INVALID_RACE);
			return;
		}

		PortalReq portalReq = portalPath.getPortalReq();
		if (portalReq != null) {
			if (instanceLevelReq && !checkEnterLevel(player, mapId, portalReq, npcObjectId)) {
				return;
			}
			if (instanceQuestReq && !checkQuestsReq(player, npcObjectId, portalReq.getQuestReq())) {
				return;
			}
			int titleId = portalReq.getTitleId();
			if (instanceTitleReq && titleId != 0 && !checkTitle(player, titleId)) {
				PacketSendUtility.sendMessage(player, "You must have correct title.");
				return;
			}

			if (!checkKinah(player, portalReq.getKinahReq())) {
				return;
			}
			if (instanceItemReq && !checkItemReq(player, portalReq.getItemReq())) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_CANT_ENTER_WITHOUT_ITEM);
				return;
			}
		}

		boolean reenter = false;
		int useDelay = 0;
		int instanceCooldown = DataManager.INSTANCE_COOLTIME_DATA.getInstanceEntranceCooltime(mapId);
		if (instanceCooldownRate > 0) {
			useDelay = instanceCooldown / instanceCooldownRate;
		}
		WorldMapInstance instance = null;
		if (player.getPortalCooldownList().isPortalUseDisabled(mapId) && useDelay > 0) {
			switch (playerSize) {
				case 0:
					instance = InstanceService.getRegisteredInstance(mapId, player.getObjectId());
					break;
				case 6:
					if (player.getPlayerGroup2() != null)
						instance = InstanceService.getRegisteredInstance(mapId, player.getPlayerGroup2().getTeamId());
					break;
				default:
					if (player.isInAlliance2()) {
						instance = InstanceService.getRegisteredInstance(mapId, player.getPlayerAlliance2().getObjectId());
					}
					break;
			}

			if (instance == null) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_MAKE_INSTANCE_COOL_TIME);
				return;
			}

			if (!instance.isRegistered(player.getObjectId())) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_MAKE_INSTANCE_COOL_TIME);
				return;
			}

			reenter = true;
			log.debug(player.getName() + "has been in intance and also have cd, can reenter.");
		}
		else {
			log.debug(player.getName() + "doesn't have cd of this instance, can enter and will be registed to this intance");
		}
		PlayerGroup group = player.getPlayerGroup2();
		PlayerAlliance allianceGroup = player.getPlayerAlliance2();
		switch (playerSize) {
			case 0:
				if (group != null && !instanceGroupReq) {
					instance = InstanceService.getRegisteredInstance(mapId, group.getTeamId());
				}
				else {
					instance = InstanceService.getRegisteredInstance(mapId, player.getObjectId());
				}

				if (instance == null && group != null && !instanceGroupReq) {
					for (Player member : group.getMembers()) {
						instance = InstanceService.getRegisteredInstance(mapId, member.getObjectId());

						if (instance != null) {
							break;
						}
					}

					if (instance == null && isInstance) {
						instance = registerGroup(group, mapId);
					}
				}

				if (instance != null) {
					reenter = true;
					transfer(player, loc, instance, reenter);
					return;
				}
				port(player, loc, reenter, isInstance);
				break;
			case 6:
				if (group != null || !instanceGroupReq) {
					// If there is a group (whatever group requirement exists or not)...
					if (group != null) {
						instance = InstanceService.getRegisteredInstance(mapId, group.getTeamId());
					}
					// But if there is no group (and solo is enabled, of course)
					else {
						instance = InstanceService.getRegisteredInstance(mapId, player.getObjectId());
					}

					// No instance (for group), group on and default requirement off
					if (instance == null && group != null && !instanceGroupReq) {
						// For each player from group
						for (Player member : group.getMembers()) {
							// Get his instance
							instance = InstanceService.getRegisteredInstance(mapId, member.getObjectId());

							// If some player is soloing and I found no one else yet, I get his instance
							if (instance != null) {
								break;
							}
						}

						// No solo instance found
						if (instance == null)
							instance = registerGroup(group, mapId);
					}

					// No instance and default requirement on = Group on
					else if (instance == null && instanceGroupReq) {
						instance = registerGroup(group, mapId);
					}
					// No instance, default requirement off, no group = Register new instance with player ID
					else if (instance == null && !instanceGroupReq && group == null) {
						instance = InstanceService.getNextAvailableInstance(mapId);
					}

					transfer(player, loc, instance, reenter);
				}
				break;
			default:
				if (allianceGroup != null || !instanceGroupReq) {
					if (allianceGroup != null) {
						instance = InstanceService.getRegisteredInstance(mapId, allianceGroup.getObjectId());
					}
					else {
						instance = InstanceService.getRegisteredInstance(mapId, player.getObjectId());
					}

					if (instance == null && allianceGroup != null && !instanceGroupReq) {
						for (Player member : allianceGroup.getMembers()) {
							instance = InstanceService.getRegisteredInstance(mapId, member.getObjectId());
							if (instance != null) {
								break;
							}
						}
						if (instance == null) {
							instance = registerAlliance(allianceGroup, mapId);
						}
					}
					else if (instance == null && instanceGroupReq) {
						instance = registerAlliance(allianceGroup, mapId);
					}
					else if (instance == null && !instanceGroupReq && allianceGroup == null) {
						instance = InstanceService.getNextAvailableInstance(mapId);
					}
					if (instance.getPlayersInside().size() < playerSize)
						transfer(player, loc, instance, reenter);
				}
				break;
		}
	}

	private static boolean checkKinah(Player player, int kinah) {
		Storage inventory = player.getInventory();
		if (!inventory.tryDecreaseKinah(kinah)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_KINA(kinah));
			return false;
		}
		return true;
	}

	private static boolean checkEnterLevel(Player player, int mapId, PortalReq portalReq, int npcObjectId) {
		int enterMinLvl = portalReq.getMinLevel();
		int enterMaxLvl = portalReq.getMaxLevel();
		int lvl = player.getLevel();
		InstanceCooltime instancecooltime = DataManager.INSTANCE_COOLTIME_DATA.getInstanceCooltimeByWorldId(mapId);
		if (instancecooltime != null && player.isMentor()) {
			if (!instancecooltime.getCanEnterMentor()) {
				return false;
			}
		}
		else if (lvl > enterMaxLvl || lvl < enterMinLvl) {
			int errDialog = portalReq.getErrLevel();
			if (errDialog != 0) {
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcObjectId, errDialog));
			}
			else {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
			}
			return false;
		}
		return true;
	}

	private static boolean checkPlayerSize(Player player, PortalPath portalPath, int npcObjectId) {
		int playerSize = portalPath.getPlayerCount();
		if (playerSize == 6) {
			if (!player.isInGroup2()) {
				int errDialog = portalPath.getErrGroup();
				if (errDialog != 0) {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcObjectId, errDialog));
				}
				else {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ENTER_ONLY_PARTY_DON);
				}
				return false;
			}
		}
		else if (playerSize > 6 && playerSize <= 24) {
			if (!player.isInAlliance2()) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ENTER_ONLY_FORCE_DON);
				return false;
			}
		}
		else if ((playerSize > 24) && (!player.isInLeague())) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401251));
			return false;
		}

		return true;
	}

	private static boolean checkRace(Player player, Race portalRace) {
		return player.getRace().equals(portalRace) || portalRace.equals(Race.PC_ALL);
	}

	private static boolean checkSigeId(Player player, int sigeId) {
		FortressLocation loc = SiegeService.getInstance().getFortress(sigeId);
		if (loc != null && loc.getRace().getRaceId() != player.getRace().getRaceId()) {
			return false;
		}

		return true;
	}

	private static boolean checkTitle(Player player, int titleId) {
		return player.getCommonData().getTitleId() == titleId;
	}

	private static boolean checkQuestsReq(Player player, int npcObjectId, List<QuestReq> questReq) {
		if (questReq != null) {
			for (QuestReq quest : questReq) {
				int questId = quest.getQuestId();
				int questStep = quest.getQuestStep();
				QuestState qs = player.getQuestStateList().getQuestState(questId);
				if (qs == null || questStep == 0 && qs.getStatus() != QuestStatus.COMPLETE || qs.getQuestVarById(0) < quest.getQuestStep() && qs.getStatus() != QuestStatus.COMPLETE) {
					int errDialog = quest.getErrQuest();
					if (errDialog != 0) {
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcObjectId, errDialog));
					}
					else {
						PacketSendUtility.sendMessage(player, "You must complete the entrance quest.");
					}
					return false;
				}
			}
		}
		return true;
	}

	private static boolean checkItemReq(Player player, List<ItemReq> itemReq) {
		Storage inventory;
		if (itemReq != null) {
			inventory = player.getInventory();
			for (ItemReq item : itemReq) {
				if (inventory.getItemCountByItemId(item.getItemId()) < item.getItemCount()) {
					return false;
				}
			}
			for (ItemReq item : itemReq) {
				inventory.decreaseByItemId(item.getItemId(), item.getItemCount());
			}
		}
		return true;
	}

	private static void port(Player requester, PortalLoc loc, boolean reenter, boolean isInstance) {
		WorldMapInstance instance = null;

		if (isInstance) {
			instance = InstanceService.getNextAvailableInstance(loc.getWorldId(), requester.getObjectId());
			InstanceService.registerPlayerWithInstance(instance, requester);
			transfer(requester, loc, instance, reenter);
		}
		else {
			easyTransfer(requester, loc);
		}
	}

	private static WorldMapInstance registerGroup(PlayerGroup group, int mapId) {
		WorldMapInstance instance = InstanceService.getNextAvailableInstance(mapId);
		InstanceService.registerGroupWithInstance(instance, group);
		return instance;
	}

	private static WorldMapInstance registerAlliance(PlayerAlliance group, int mapId) {
		WorldMapInstance instance = InstanceService.getNextAvailableInstance(mapId);
		InstanceService.registerAllianceWithInstance(instance, group);
		return instance;
	}

	private static void transfer(Player player, PortalLoc loc, WorldMapInstance instance, boolean reenter) {
		player.setInstanceStartPos(loc.getX(), loc.getY(), loc.getZ());
		InstanceService.registerPlayerWithInstance(instance, player);
		TeleportService2.teleportTo(player, loc.getWorldId(), instance.getInstanceId(), loc.getX(), loc.getY(), loc.getZ(), loc.getH(), TeleportAnimation.BEAM_ANIMATION);

		int instanceCooldownRate = InstanceService.getInstanceRate(player, loc.getWorldId());
		int useDelay = 0;
		int instanceCoolTime = DataManager.INSTANCE_COOLTIME_DATA.getInstanceEntranceCooltime(instance.getMapId());
		if (instanceCooldownRate > 0) {
			useDelay = instanceCoolTime * 60 * 1000 / instanceCooldownRate;
		}
		if (useDelay > 0 && !reenter)
			player.getPortalCooldownList().addPortalCooldown(loc.getWorldId(), useDelay);
	}

	private static void easyTransfer(Player player, PortalLoc loc) {
		TeleportService2.teleportTo(player, loc.getWorldId(), loc.getX(), loc.getY(), loc.getZ(), loc.getH(), TeleportAnimation.BEAM_ANIMATION);
	}
}
