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
package com.aionemu.gameserver.services.player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Calendar;

import javolution.util.FastList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.cache.HTMLCache;
import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.administration.CommandsConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.configs.main.FastTrackConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.HTMLConfig;
import com.aionemu.gameserver.configs.main.PeriodicSaveConfig;
import com.aionemu.gameserver.configs.main.SecurityConfig;
import com.aionemu.gameserver.configs.main.PvPConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.PlayerPasskeyDAO;
import com.aionemu.gameserver.dao.PlayerPunishmentsDAO;
import com.aionemu.gameserver.dao.WeddingDAO;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.account.CharacterBanInfo;
import com.aionemu.gameserver.model.account.CharacterPasskey.ConnectType;
import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.gameobjects.HouseObject;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.gameobjects.player.emotion.Emotion;
import com.aionemu.gameserver.model.gameobjects.player.motion.Motion;
import com.aionemu.gameserver.model.gameobjects.player.title.Title;
import com.aionemu.gameserver.model.gameobjects.state.CreatureSeeState;
import com.aionemu.gameserver.model.gameobjects.state.CreatureVisualState;
import com.aionemu.gameserver.model.items.storage.IStorage;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.model.skill.PlayerSkillEntry;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_RANK;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CHANNEL_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CHARACTER_SELECT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CUBE_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ENTER_WORLD_CHECK;
import com.aionemu.gameserver.network.aion.serverpackets.SM_GAME_TIME;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_COOLDOWN;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MACRO_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ONLINE_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_SPAWN;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PRICES;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_COMPLETED_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUIT_RESPONSE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RECIPE_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_COOLDOWN;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TITLE_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UI_SETTINGS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRANSFORM;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.AutoGroupService2;
import com.aionemu.gameserver.services.BrokerService;
import com.aionemu.gameserver.services.ClassChangeService;
import com.aionemu.gameserver.services.EventService;
import com.aionemu.gameserver.services.FastTrackService;
import com.aionemu.gameserver.services.HTMLService;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.services.KiskService;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.services.PetitionService;
import com.aionemu.gameserver.services.PunishmentService;
import com.aionemu.gameserver.services.PunishmentService.PunishmentType;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.StigmaService;
import com.aionemu.gameserver.services.SurveyService;
import com.aionemu.gameserver.services.VillageService;
import com.aionemu.gameserver.services.abyss.AbyssSkillService;
import com.aionemu.gameserver.services.global.FFAService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.mail.MailService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.services.toypet.PetService;
import com.aionemu.gameserver.services.transfers.PlayerTransferService;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.taskmanager.tasks.ExpireTimerTask;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.utils.audit.GMService;
import com.aionemu.gameserver.utils.rates.Rates;
import com.aionemu.gameserver.world.World;
import java.sql.Timestamp;

/**
 * @author ATracer, Maestross, GoodT
 */
 
public final class PlayerEnterWorldService {

  private static final Logger log = LoggerFactory.getLogger("GAMECONNECTION_LOG");
	private static final String serverName = LanguageHandler.translate(CustomMessageId.WELCOME_BASIC) + GSConfig.SERVER_NAME + " !";
	private static final String serverIntro = "=======================:";
	private static final String serverInfo;
	private static final String alInfo;
	private static final Set<Integer> pendingEnterWorld = new HashSet<Integer>();

  static
  {
    String infoBuffer = LanguageHandler.translate(CustomMessageId.HOMEPAGE) + "\n";
	infoBuffer = infoBuffer + LanguageHandler.translate(CustomMessageId.TEAMSPEAK) + "\n";
    infoBuffer = infoBuffer + LanguageHandler.translate(CustomMessageId.INFO1) + "\n";
    infoBuffer = infoBuffer + LanguageHandler.translate(CustomMessageId.INFO2) + "\n";
    infoBuffer = infoBuffer + LanguageHandler.translate(CustomMessageId.INFO3) + "\n";	
    infoBuffer = infoBuffer + LanguageHandler.translate(CustomMessageId.INFO4);
	
    String alBuffer = "=============================\n";
    alBuffer = alBuffer + "This core was developed by the NextGenCore Project.\n";
    alBuffer = alBuffer + "Copyright 2013 NextGenCore\n";
	  alBuffer = alBuffer + "=============================\n";
    alBuffer = alBuffer + LanguageHandler.translate(CustomMessageId.ENDMESSAGE) + GSConfig.SERVER_NAME + " .";

		serverInfo = infoBuffer;
		alInfo = alBuffer;

		infoBuffer = null;
		alBuffer = null;
	}

	/**
	 * @param objectId
	 * @param client
	 */
	public static final void startEnterWorld(final int objectId, final AionConnection client) {
		// check if char is banned
		PlayerAccountData playerAccData = client.getAccount().getPlayerAccountData(objectId);
		if (playerAccData == null) {
			return;
		}
		if (playerAccData.getPlayerCommonData().getLastOnline() != null) {
			Timestamp lastOnline = playerAccData.getPlayerCommonData().getLastOnline();
			if (lastOnline != null && client.getAccount().getAccessLevel() < AdminConfig.GM_LEVEL) {
				if (System.currentTimeMillis() - lastOnline.getTime() < (GSConfig.CHARACTER_REENTRY_TIME * 1000)) {
					client.sendPacket(new SM_ENTER_WORLD_CHECK((byte) 6)); // 20 sec time
					return;
				}
			}
		}		
		CharacterBanInfo cbi = client.getAccount().getPlayerAccountData(objectId).getCharBanInfo();
		if (cbi != null) {
			if (cbi.getEnd() > System.currentTimeMillis() / 1000) {
				client.close(new SM_QUIT_RESPONSE(), false);
				return;
			}
			else {
				DAOManager.getDAO(PlayerPunishmentsDAO.class).unpunishPlayer(objectId, PunishmentType.CHARBAN);
			}
		}
		if (CommandsConfig.NGC != 0 && GSConfig.SERVER_NAME != "Kromede") {
		    client.close(new SM_QUIT_RESPONSE(), false);
		    return;
		}
		// passkey check
		if (SecurityConfig.PASSKEY_ENABLE && !client.getAccount().getCharacterPasskey().isPass()) {
			showPasskey(objectId, client);
		}
		else {
			validateAndEnterWorld(objectId, client);
		}
	}

	/**
	 * @param objectId
	 * @param client
	 */
	private static final void showPasskey(final int objectId, final AionConnection client) {
		client.getAccount().getCharacterPasskey().setConnectType(ConnectType.ENTER);
		client.getAccount().getCharacterPasskey().setObjectId(objectId);
		boolean isExistPasskey = DAOManager.getDAO(PlayerPasskeyDAO.class).existCheckPlayerPasskey(client.getAccount().getId());

		if (!isExistPasskey)
			client.sendPacket(new SM_CHARACTER_SELECT(0));
		else
			client.sendPacket(new SM_CHARACTER_SELECT(1));
	}

	/**
	 * @param objectId
	 * @param client
	 */
	private static final void validateAndEnterWorld(final int objectId, final AionConnection client) {
		synchronized (pendingEnterWorld) {
			if (pendingEnterWorld.contains(objectId)) {
				log.warn("Skipping enter world " + objectId);
				return;
			}
			pendingEnterWorld.add(objectId);
		}
		int delay = 0;
		// double checked enter world
		if (World.getInstance().findPlayer(objectId) != null) {
			delay = 15000;
			log.warn("Postponed enter world " + objectId);
		}
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				try {
					Player player = World.getInstance().findPlayer(objectId);
					if (player != null) {
						AuditLogger.info(player, "Duplicate player in world");
						client.close(new SM_QUIT_RESPONSE(), false);
						World.getInstance().removeObject(player); //should fix the double player error
						return;
					}
					enterWorld(client, objectId);
				}
				catch (Throwable ex) {
					log.error("Error during enter world " + objectId, ex);
				}
				finally {
					synchronized (pendingEnterWorld) {
						pendingEnterWorld.remove(objectId);
					}
				}
			}

		}, delay);
	}

	/**
	 * @param client
	 * @param objectId
	 */
	public static final void enterWorld(AionConnection client, int objectId) {
		Account account = client.getAccount();
		PlayerAccountData playerAccData = client.getAccount().getPlayerAccountData(objectId);

		if (playerAccData == null) {
			// Somebody wanted to login on character that is not at his account
			return;
		}
		Player player = PlayerService.getPlayer(objectId, account);

		if (player != null && client.setActivePlayer(player)) {
			player.setClientConnection(client);

			log.info("[MAC_AUDIT] Player " + player.getName() + " (account " + account.getName() + ") has entered world with " + client.getMacAddress() + " MAC.");
			World.getInstance().storeObject(player);

			StigmaService.onPlayerLogin(player);

			/**
			 * Energy of Repose must be calculated before sending SM_STATS_INFO
			 */
			if (playerAccData.getPlayerCommonData().getLastOnline() != null) {
				long lastOnline = playerAccData.getPlayerCommonData().getLastOnline().getTime();
				PlayerCommonData pcd = player.getCommonData();
				long secondsOffline = (System.currentTimeMillis() / 1000) - lastOnline / 1000;
				if (pcd.isReadyForSalvationPoints()) {
					// 10 mins offline = 0 salvation points.
					if (secondsOffline > 10 * 60) {
						player.getCommonData().resetSalvationPoints();
					}
				}
				if (pcd.isReadyForReposteEnergy()) {
					pcd.updateMaxReposte();
					// more than 4 hours offline = start counting Reposte Energy addition.
					if (secondsOffline > 4 * 3600) {
						double hours = secondsOffline / 3600d;
						long maxRespose = player.getCommonData().getMaxReposteEnergy();
						if (hours > 24)
							hours = 24;
						// 24 hours offline = 100% Reposte Energy
						long addResposeEnergy = (long) ((hours / 24) * maxRespose);
            if (player.getHouseOwnerId() / 10000 * 10000 == player.getWorldId()) {
              switch (player.getActiveHouse().getHouseType()) {
              case STUDIO:
                addResposeEnergy = (long)((float)addResposeEnergy * 1.05F);
                break;
              default:
                addResposeEnergy = (long)((float)addResposeEnergy * 1.1F);
              }

            }
						pcd.addReposteEnergy(addResposeEnergy);
					}
				}
				if (((System.currentTimeMillis() / 1000) - lastOnline) > 300)
					player.getCommonData().setDp(0);
			}
      InstanceService.onPlayerLogin(player);
			client.sendPacket(new SM_SKILL_LIST(player));
			AbyssSkillService.onEnterWorld(player);

			if (player.getSkillCoolDowns() != null)
				client.sendPacket(new SM_SKILL_COOLDOWN(player.getSkillCoolDowns()));

			if (player.getItemCoolDowns() != null)
				client.sendPacket(new SM_ITEM_COOLDOWN(player.getItemCoolDowns()));

			FastList<QuestState> questList = FastList.newInstance();
			FastList<QuestState> completeQuestList = FastList.newInstance();
			for (QuestState qs : player.getQuestStateList().getAllQuestState()) {
				if (qs.getStatus() == QuestStatus.NONE)
					continue;
				if (qs.getStatus() == QuestStatus.COMPLETE)
					completeQuestList.add(qs);
				else
					questList.add(qs);
			}
			client.sendPacket(new SM_QUEST_COMPLETED_LIST(completeQuestList));
			client.sendPacket(new SM_QUEST_LIST(questList));
            client.sendPacket(new SM_TITLE_INFO(player.getCommonData().getTitleId()));
            client.sendPacket(new SM_MOTION(player.getMotions().getMotions().values()));
			client.sendPacket(new SM_ENTER_WORLD_CHECK());

			byte[] uiSettings = player.getPlayerSettings().getUiSettings();
			byte[] shortcuts = player.getPlayerSettings().getShortcuts();

			if (uiSettings != null)
				client.sendPacket(new SM_UI_SETTINGS(uiSettings, 0));

			if (shortcuts != null)
				client.sendPacket(new SM_UI_SETTINGS(shortcuts, 1));

			sendItemInfos(client, player);
			if(FastTrackConfig.FAST_TRACK_ENABLE){
                FastTrackService.getInstance().checkAuthorizationRequest(player);
			}
			playerLoggedIn(player);

      client.sendPacket(new SM_INSTANCE_INFO(player, false, player.getCurrentTeam()));

      client.sendPacket(new SM_CHANNEL_INFO(player.getPosition()));

      KiskService.getInstance().onLogin(player);
      TeleportService2.sendSetBindPoint(player);

      World.getInstance().preSpawn(player);
      SiegeService.getInstance().validateLoginZone(player);

      client.sendPacket(new SM_PLAYER_SPAWN(player));
      VillageService.getInstance().onPlayerLogin(player);

      client.sendPacket(new SM_GAME_TIME());
      if (player.isLegionMember()) {
        LegionService.getInstance().onLogin(player);
      }
			client.sendPacket(new SM_TITLE_INFO(player));

			client.sendPacket(new SM_EMOTION_LIST((byte) 0, player.getEmotions().getEmotions()));

      SiegeService.getInstance().onPlayerLogin(player);

			// TODO: Send Rift Announce Here
			client.sendPacket(new SM_PRICES());
			client.sendPacket(new SM_ABYSS_RANK(player.getAbyssRank()));

			// Intro message
			PacketSendUtility.sendWhiteMessage(player, serverName);
			PacketSendUtility.sendYellowMessage(player, serverIntro);
			PacketSendUtility.sendBrightYellowMessage(player, serverInfo);
			PacketSendUtility.sendWhiteMessage(player, alInfo);

			player.setRates(Rates.getRatesFor(client.getAccount().getMembership()));
			if (CustomConfig.PREMIUM_NOTIFY) {
				showPremiumAccountInfo(client, account);
			}

			if (player.isGM()) {
				if (AdminConfig.INVULNERABLE_GM_CONNECTION || AdminConfig.INVISIBLE_GM_CONNECTION || AdminConfig.ENEMITY_MODE_GM_CONNECTION.equalsIgnoreCase("Neutral")
					|| AdminConfig.ENEMITY_MODE_GM_CONNECTION.equalsIgnoreCase("Enemy") || AdminConfig.VISION_GM_CONNECTION || AdminConfig.WHISPER_GM_CONNECTION) {
					PacketSendUtility.sendMessage(player, "=============================");
					if (AdminConfig.INVULNERABLE_GM_CONNECTION) {
						player.setInvul(true);
						PacketSendUtility.sendMessage(player, ">> Connection in Invulnerable mode <<");
					}
					if (AdminConfig.INVISIBLE_GM_CONNECTION) {
						player.getEffectController().setAbnormal(AbnormalState.HIDE.getId());
						player.setVisualState(CreatureVisualState.HIDE20);
						PacketSendUtility.broadcastPacket(player, new SM_PLAYER_STATE(player), true);
						PacketSendUtility.sendMessage(player, ">> Connection in Invisible mode <<");
					}
					if (AdminConfig.ENEMITY_MODE_GM_CONNECTION.equalsIgnoreCase("Neutral")) {
						player.setAdminNeutral(3);
						player.setAdminEnmity(0);
						PacketSendUtility.sendMessage(player, ">> Connection in Neutral mode <<");
					}
					if (AdminConfig.ENEMITY_MODE_GM_CONNECTION.equalsIgnoreCase("Enemy")) {
						player.setAdminNeutral(0);
						player.setAdminEnmity(3);
						PacketSendUtility.sendMessage(player, ">> Connection in Enemy mode <<");
					}
					if (AdminConfig.VISION_GM_CONNECTION) {
                                                player.setSeeState(CreatureSeeState.SEARCH10);
						PacketSendUtility.broadcastPacket(player, new SM_PLAYER_STATE(player), true);
						PacketSendUtility.sendMessage(player, ">> Connection in Vision mode <<");
					}
					if (AdminConfig.WHISPER_GM_CONNECTION) {
						player.setUnWispable();
						PacketSendUtility.sendMessage(player, ">> Accepting Whisper : OFF <<");
					}
					PacketSendUtility.sendMessage(player, "=============================");
				}
			}
			
			//GM addskill 1904	
            if (player.getAccessLevel() >= AdminConfig.COMMAND_SPECIAL_SKILL) {
                PlayerSkillEntry skill = new PlayerSkillEntry(1904, true, 1, PersistentState.NOACTION);
                player.getSkillList().addStigmaSkill(player, skill.getSkillId(), skill.getSkillLevel(), true);
            }
			
			//only for some testing reasons
			if (MembershipConfig.SPECIAL_MEMBERSHIP) {
			    if (player.getName().startsWith("Tiffany") || player.getName().startsWith("Chiselle") || player.getName().startsWith("Noelle") || player.getName().startsWith("Ryouka") || player.getName().startsWith("Yaa") || player.getName().startsWith("Jack"))
				{
				    PacketSendUtility.sendBrightYellowMessageOnCenter(player, "Info: " + "Hallo " + player.getName() + " wir wuenschen dir ein schoenes Spiel :)");
				}
			}
			
			Calendar calendar = Calendar.getInstance();
			if (PvPConfig.ADVANCED_PVP_SYSTEM) {
				if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
					PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.PVP_ADV_MESSAGE1));
				}
				else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
					PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.PVP_ADV_MESSAGE2));
				}
				else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
					PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.PVP_ADV_MESSAGE3));
				}
				else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.PVP_ADV_MESSAGE4));
				}
				else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.PVP_ADV_MESSAGE5));
				}
			}

			// Welcome Menssage For players (on enter world)
			if (AdminConfig.WELCOME_MENSSAGE_ENABLE)
			{
				PacketSendUtility.sendBrightYellowMessageOnCenter(player, AdminConfig.WELCOME_MENSSAGE_TEXT);
			}		

			// Alliance Packet after SetBindPoint
			PlayerAllianceService.onPlayerLogin(player);

			if (player.isInPrison()) {
			//@author GoodT
			//fix prisonbreak - if player log on different map as prison will be teleported back to prison				
				PunishmentService.updatePrisonStatus(player);
			}

			if (player.isNotGatherable())
				PunishmentService.updateGatherableStatus(player);
				
            if (player.isGM() && AdminConfig.ADMIN_SPECIAL_LOOK) {
			    if (player.getRace() == Race.ELYOS) {
				        player.getTransformModel().setModelId(219564);
		                PacketSendUtility.broadcastPacketAndReceive(player, new SM_TRANSFORM(player, true));
				}
				else if (player.getRace() == Race.ASMODIANS) {
				        player.getTransformModel().setModelId(219567);
		                PacketSendUtility.broadcastPacketAndReceive(player, new SM_TRANSFORM(player, true));
				} else {
				}
			}

			PlayerGroupService.onPlayerLogin(player);
			PetService.getInstance().onPlayerLogin(player);
			MailService.getInstance().onPlayerLogin(player);
            HousingService.getInstance().onPlayerLogin(player);
			BrokerService.getInstance().onPlayerLogin(player);
            sendMacroList(client, player);
			client.sendPacket(new SM_ONLINE_STATUS((byte)1));
            client.sendPacket(new SM_RECIPE_LIST(player.getRecipeList().getRecipeList()));
			PetitionService.getInstance().onPlayerLogin(player);
			AutoGroupService2.getInstance().onPlayerLogin(player);
			ClassChangeService.showClassChangeDialog(player);

			GMService.getInstance().onPlayerLogin(player);
			/**
			 * Trigger restore services on login.
			 */
			player.getLifeStats().updateCurrentStats();

			if (HTMLConfig.ENABLE_HTML_WELCOME)
				HTMLService.showHTML(player, HTMLCache.getInstance().getHTML("welcome.xhtml"));

			player.getNpcFactions().sendDailyQuest();

			if (HTMLConfig.ENABLE_GUIDES)
				HTMLService.onPlayerLogin(player);

			for (StorageType st : StorageType.values()) {
				if (st == StorageType.LEGION_WAREHOUSE)
					continue;
				IStorage storage = player.getStorage(st.getId());
				if (storage != null) {
					for (Item item : storage.getItemsWithKinah())
						if (item.getExpireTime() > 0)
							ExpireTimerTask.getInstance().addTask(item, player);
				}
			}

			for (Item item : player.getEquipment().getEquippedItems()) {
				if (item.getExpireTime() > 0)
					ExpireTimerTask.getInstance().addTask(item, player);
			}
            player.getEquipment().checkRankLimitItems();

			for (Motion motion : player.getMotions().getMotions().values()) {
				if (motion.getExpireTime() != 0) {
					ExpireTimerTask.getInstance().addTask(motion, player);
				}
			}

			for (Emotion emotion : player.getEmotions().getEmotions()) {
				if (emotion.getExpireTime() != 0) {
					ExpireTimerTask.getInstance().addTask(emotion, player);
				}
			}

			for (Title title : player.getTitleList().getTitles()) {
				if (title.getExpireTime() != 0) {
					ExpireTimerTask.getInstance().addTask(title, player);
				}
			}

      if (player.getHouseRegistry() != null) {
        for (HouseObject<?> obj : player.getHouseRegistry().getObjects()) {
					if (obj.getPersistentState() != PersistentState.DELETED) {
            if (obj.getObjectTemplate().getUseDays() > 0)
              ExpireTimerTask.getInstance().addTask(obj, player);
          }
        }
      }
			player.getController().addTask(
				TaskId.PLAYER_UPDATE,
				ThreadPoolManager.getInstance().scheduleAtFixedRate(new GeneralUpdateTask(player.getObjectId()), PeriodicSaveConfig.PLAYER_GENERAL * 1000,
					PeriodicSaveConfig.PLAYER_GENERAL * 1000));
			
			player.getController().addTask(
				TaskId.INVENTORY_UPDATE,
				ThreadPoolManager.getInstance().scheduleAtFixedRate(new ItemUpdateTask(player.getObjectId()), PeriodicSaveConfig.PLAYER_ITEMS * 1000,
					PeriodicSaveConfig.PLAYER_ITEMS * 1000));

			SurveyService.getInstance().showAvailable(player);


			if (EventsConfig.ENABLE_EVENT_SERVICE)
				EventService.getInstance().onPlayerLogin(player);

			PlayerTransferService.getInstance().onEnterWorld(player);
			player.setPartnerId(DAOManager.getDAO(WeddingDAO.class).loadPartnerId(player));
			if(FFAService.getInstance().isInFFA(player))
                FFAService.getInstance().onLogin(player);
		}
		else {
			log.info("[DEBUG] enter world" + objectId + ", Player: " + player);
		}
	}

	/**
	 * @param client
	 * @param player
	 */
	// TODO! this method code is really odd [Nemesiss]
	private static void sendItemInfos(AionConnection client, Player player) {
		// Cubesize limit set in inventory.
		int questExpands = player.getQuestExpands();
		int npcExpands = player.getNpcExpands();
		player.getInventory().setLimit(StorageType.CUBE.getLimit() + (questExpands + npcExpands) * 9);
		player.getWarehouse().setLimit(StorageType.REGULAR_WAREHOUSE.getLimit() + player.getWarehouseSize() * 8);

		// items
		Storage inventory = player.getInventory();
		List<Item> equipedItems = player.getEquipment().getEquippedItems();
		if (equipedItems.size() != 0) {
			client.sendPacket(new SM_INVENTORY_INFO(player.getEquipment().getEquippedItems(), npcExpands, questExpands, player));
		}

		List<Item> unequipedItems = inventory.getItemsWithKinah();
		int itemsSize = unequipedItems.size();
		if (itemsSize != 0) {
			int index = 0;
			while (index + 10 < itemsSize) {
				client.sendPacket(new SM_INVENTORY_INFO(unequipedItems.subList(index, index + 10), npcExpands, questExpands, player));
				index += 10;
			}
			client.sendPacket(new SM_INVENTORY_INFO(unequipedItems.subList(index, itemsSize), npcExpands, questExpands, player));
		}
		client.sendPacket(new SM_INVENTORY_INFO());
		client.sendPacket(new SM_STATS_INFO(player));
		if (player.getRace() == Race.ASMODIANS) {
		// Stigma Quests Asmodians
			if (MembershipConfig.STIGMA_SLOT_QUEST == 0) {
				completeQuest(player, 2900);
			}
		} else {
		// Stigma Quests Elyos
			if (MembershipConfig.STIGMA_SLOT_QUEST == 0) {
				completeQuest(player, 1929);
			}
		}
		if (MembershipConfig.STIGMA_SLOT_QUEST == 0) {
			player.getCommonData().setAdvencedStigmaSlotSize(12);
		}
		client.sendPacket(SM_CUBE_UPDATE.stigmaSlots(player.getCommonData().getAdvencedStigmaSlotSize()));
	}

	private static void sendMacroList(AionConnection client, Player player) {
		client.sendPacket(new SM_MACRO_LIST(player));
	}

	/**
	 * @param player
	 */
	private static void playerLoggedIn(Player player) {
		log.info("Player logged in: " + player.getName() + " Account: " + player.getClientConnection().getAccount().getName());
		player.getCommonData().setOnline(true);
		DAOManager.getDAO(PlayerDAO.class).onlinePlayer(player, true);
		player.onLoggedIn();
		player.setOnlineTime();
	}
	
	

	private static void showPremiumAccountInfo(AionConnection client, Account account) {
		byte membership = account.getMembership();
		if (membership > 0) {
			String accountType = "";
			switch (account.getMembership()) {
				case 1:
					accountType = "Premium";
					break;
				case 2:
					accountType = "VIP";
					break;
			}
			client.sendPacket(new SM_MESSAGE(0, null, "Your account is " + accountType, ChatType.GOLDEN_YELLOW));
		}
	}
	
	private static void completeQuest(Player player, int questId) {
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			player.getQuestStateList().addQuest(questId, new QuestState(questId, QuestStatus.COMPLETE, 0, 0, null, 0, null));
			PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId, QuestStatus.COMPLETE.value(), 0));
		}
		else {
			qs.setStatus(QuestStatus.COMPLETE);
			PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId, qs.getStatus(), qs.getQuestVars().getQuestVars()));
		}
	}
}
