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

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Kisk;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.templates.item.ItemUseLimits;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.taskmanager.tasks.TeamMoveUpdater;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.WorldPosition;

import java.util.Calendar;

/**
 * @author Jego, xTz, Maestross
 */
public class PlayerReviveService {

	public static final void duelRevive(Player player) {
		revive(player, 30, 30, false, 0);
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		player.getGameStats().updateStatsAndSpeedVisually();
		player.unsetResPosState();
	}

	public static final void skillRevive(Player player) {
		if (!(player.getResStatus())) {
			cancelRes(player);
			return;
		}

		revive(player, 10, 10, true, player.getResurrectionSkill());

		if (player.getIsFlyingBeforeDeath())
			player.setState(CreatureState.FLYING);

		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		if (player.getIsFlyingBeforeDeath()) {
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();

		if (player.isInPrison())
			TeleportService2.teleportToPrison(player);

		if (player.isInResPostState())
			TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), player.getResPosX(), player.getResPosY(), player.getResPosZ());
		player.unsetResPosState();

		player.setIsFlyingBeforeDeath(false);
	}
	
	public static final void vortexRevive(Player player) {
		vortexRevive(player, 0);
	}
	
	//for new 3.5 dimensional vortex
	//TODO: It's not retail like, but it works fine
	public static final void vortexRevive(Player player, int skillId) {
	    Calendar calendar = Calendar.getInstance();
		if ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && calendar.get(Calendar.HOUR_OF_DAY) == CustomConfig.DIM_VORTEX_HOUR) || (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && calendar.get(Calendar.HOUR_OF_DAY) == CustomConfig.DIM_VORTEX_HOUR)) {
			revive(player, 25, 25, true, skillId);
		    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		    player.getGameStats().updateStatsAndSpeedVisually();
		    PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		    PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		    if (player.getRace() == Race.ELYOS) {
		        TeleportService2.teleportTo(player, 220050000, 1696.4329f, 2901.0894f, 65.90677f);
		    }
		    else {
		        TeleportService2.teleportTo(player, 210060000, 952.89124f, 2424.5557f, 105.43102f);
		    }
		    player.unsetResPosState();
	    } else if ((calendar.get(Calendar.HOUR_OF_DAY) == CustomConfig.DIM_VORTEX_DESPAWN_HOUR && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (calendar.get(Calendar.HOUR_OF_DAY) == CustomConfig.DIM_VORTEX_DESPAWN_HOUR && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
			revive(player, 25, 25, true, skillId);
		    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		    // TODO: It is not always necessary.
		    // sendPacket(new SM_QUEST_LIST(activePlayer));
		    player.getGameStats().updateStatsAndSpeedVisually();
		    PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		    PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		    if (player.isInPrison())
			    TeleportService2.teleportToPrison(player);
		    else
			    TeleportService2.moveToBindLocation(player, true);
		    player.unsetResPosState();
		} else {
		    revive(player, 25, 25, true, skillId);
		    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		    // TODO: It is not always necessary.
		    // sendPacket(new SM_QUEST_LIST(activePlayer));
		    player.getGameStats().updateStatsAndSpeedVisually();
		    PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		    PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		    if (player.isInPrison())
			    TeleportService2.teleportToPrison(player);
		    else
			    TeleportService2.moveToBindLocation(player, true);
		    player.unsetResPosState();
		}
	}

	public static final void rebirthRevive(Player player) {
		if (!player.canUseRebirthRevive()) {
			return;
		}
		if (player.getRebirthResurrectPercent() <= 0) {
			PacketSendUtility.sendMessage(player, "Error: Rebirth effect missing percent.");
			player.setRebirthResurrectPercent(5);
		}
		boolean soulSickness = true;
		int rebirthResurrectPercent = player.getRebirthResurrectPercent();
		if (player.getAccessLevel() >= AdminConfig.ADMIN_AUTO_RES) {
			rebirthResurrectPercent = 100;
			soulSickness = false;
		}
		// if (player.getRebirthResurrectPercent() > 0) {
		// player.setRebirthResurrectPercent(rebirthResurrectPercent);
		// soulSickness = false;
		// }
		// boolean isFlyingBeforeDeath = player.getIsFlyingBeforeDeath();
		revive(player, rebirthResurrectPercent, rebirthResurrectPercent, soulSickness, player.getRebirthSkill());

		if (player.getIsFlyingBeforeDeath())
			player.setState(CreatureState.FLYING);
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		if (player.getIsFlyingBeforeDeath()) {
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();

		if (player.isInPrison())
			TeleportService2.teleportToPrison(player);
		player.unsetResPosState();

		// if player was flying before res, start flying
		player.setIsFlyingBeforeDeath(false);
	}

	public static final void bindRevive(Player player) {
		bindRevive(player, 0);
	}

	public static final void bindRevive(Player player, int skillId) {
		revive(player, 25, 25, true, skillId);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		// TODO: It is not always necessary.
		// sendPacket(new SM_QUEST_LIST(activePlayer));
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		if (player.isInPrison())
			TeleportService2.teleportToPrison(player);
		else
			TeleportService2.moveToBindLocation(player, true);
		player.unsetResPosState();
	}

	public static final void kiskRevive(Player player) {
		kiskRevive(player, 0);
	}

	public static final void kiskRevive(Player player, int skillId) {
		Kisk kisk = player.getKisk();
		if (kisk == null) {
			return;
		}
		if (player.isInPrison())
			TeleportService2.teleportToPrison(player);
		else if (kisk.isActive()) {
			WorldPosition bind = kisk.getPosition();
			kisk.resurrectionUsed();
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
			revive(player, 25, 25, false, skillId);
			player.getGameStats().updateStatsAndSpeedVisually();

			player.unsetResPosState();
			TeleportService2.moveToKiskLocation(player, bind);
		}
	}

	public static final void instanceRevive(Player player) {
		instanceRevive(player, 0);
	}

	public static final void instanceRevive(Player player, int skillId) {
		// Revive in Instances
		if (player.getPosition().getWorldMapInstance().getInstanceHandler().onReviveEvent(player)) {
			return;
		}
		WorldMap map = World.getInstance().getWorldMap(player.getWorldId());
		if (map == null) {
			bindRevive(player);
			return;
		}
		revive(player, 25, 25, true, skillId);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		if (map.isInstanceType() && (player.getInstanceStartPosX() != 0 && player.getInstanceStartPosY() != 0 && player.getInstanceStartPosZ() != 0)) {
			TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceStartPosX(), player.getInstanceStartPosY(), player.getInstanceStartPosZ());
		}
		else {
			bindRevive(player);
		}
		player.unsetResPosState();
	}

	public static final void revive(Player player, int hpPercent, int mpPercent, boolean setSoulsickness, int resurrectionSkill) {
		boolean isNoResurrectPenalty = player.getController().isNoResurrectPenaltyInEffect();
		player.setPlayerResActivate(false);
		player.getLifeStats().setCurrentHpPercent(isNoResurrectPenalty ? 100 : hpPercent);
		player.getLifeStats().setCurrentMpPercent(isNoResurrectPenalty ? 100 : mpPercent);
		if (player.getCommonData().getDp() > 0 && !isNoResurrectPenalty)
			player.getCommonData().setDp(0);
		if (player.getWorldId() == 300350000)
			player.getCommonData().setDp(4000);
		player.getLifeStats().triggerRestoreOnRevive();
		if ((!isNoResurrectPenalty) && (setSoulsickness)) {
			player.getController().updateSoulSickness(resurrectionSkill);
		}
		player.setResurrectionSkill(0);
		player.getAggroList().clear();
		player.getController().onBeforeSpawn();
		if (player.isInGroup2()) {
			TeamMoveUpdater.getInstance().startTask(player);
		}
	}

	public static final void itemSelfRevive(Player player) {
		Item item = player.getSelfRezStone();
		if (item == null) {
			cancelRes(player);
			return;
		}

		// Add Cooldown and use item
		ItemUseLimits useLimits = item.getItemTemplate().getUseLimits();
		int useDelay = useLimits.getDelayTime();
		player.addItemCoolDown(useLimits.getDelayId(), System.currentTimeMillis() + useDelay, useDelay / 1000);

		player.getController().cancelUseItem();
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), item.getObjectId(), item.getItemTemplate().getTemplateId()), true);
		if (!player.getInventory().decreaseByObjectId(item.getObjectId(), 1)) {
			cancelRes(player);
			return;
		}

		// Tombstone Self-Rez retail verified 15%
		revive(player, 15, 15, true, player.getResurrectionSkill());

		if (player.getIsFlyingBeforeDeath())
			player.setState(CreatureState.FLYING);

		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		if (player.getIsFlyingBeforeDeath()) {
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();

		if (player.isInPrison())
			TeleportService2.teleportToPrison(player);
		player.unsetResPosState();

		player.setIsFlyingBeforeDeath(false);
	}

	private static final void cancelRes(Player player) {
		AuditLogger.info(player, "Possible selfres hack.");
		player.getController().sendDie();
	}
}