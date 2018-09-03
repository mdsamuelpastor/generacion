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
package com.aionemu.gameserver.services;

import java.util.Map;

import javolution.util.FastMap;

import com.aionemu.gameserver.model.gameobjects.Kisk;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LEVEL_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SET_BIND_POINT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Sarynth
 */
public class KiskService {

	private static final KiskService instance = new KiskService();
	private final Map<Integer, Kisk> boundButOfflinePlayer = new FastMap<Integer, Kisk>().shared();
	private final Map<Integer, Kisk> ownerPlayer = new FastMap<Integer, Kisk>().shared();

	public void removeKisk(Kisk kisk) {

		for (int memberId : kisk.getCurrentMemberIds()) {
			boundButOfflinePlayer.remove(memberId);
		}

		for (Integer obj : ownerPlayer.keySet()) {
			if (ownerPlayer.get(obj).equals(kisk)) {
				ownerPlayer.remove(obj);
				break;
			}

		}

		/**
		 * Remove kisk references and containers.
		 * 
		 * @param kisk
		 */
		for (Player member : kisk.getCurrentMemberList()) {
			member.setKisk(null);
			PacketSendUtility.sendPacket(member, new SM_SET_BIND_POINT(0, 0f, 0f, 0f, member));
			if (member.getLifeStats().isAlreadyDead())
				member.getController().sendDie();
		}
	}

	/**
	 * @param kisk
	 * @param player
	 */
	public void onBind(Kisk kisk, Player player) {
		if (player.getKisk() != null) {
			player.getKisk().removePlayer(player);
		}

		kisk.addPlayer(player);

		// Send Bind Point Data
		TeleportService2.sendSetBindPoint(player);

		// Send System Message
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_BINDSTONE_REGISTER);

		// Send Animated Bind Flash
		PacketSendUtility.broadcastPacket(player, new SM_LEVEL_UPDATE(player.getObjectId(), 2, player.getCommonData().getLevel()), true);
	}

	/**
	 * @param player
	 */
	public void onLogin(Player player) {
		Kisk kisk = boundButOfflinePlayer.get(player.getObjectId());
		if (kisk != null) {
			kisk.addPlayer(player);
			boundButOfflinePlayer.remove(player.getObjectId());
		}
	}

	public void onLogout(Player player) {
		Kisk kisk = player.getKisk();

		if (kisk != null)
			boundButOfflinePlayer.put(player.getObjectId(), kisk);
	}

	public void regKisk(Kisk kisk, Integer objOwnerId) {
		ownerPlayer.put(objOwnerId, kisk);
	}

	public boolean haveKisk(Integer objOwnerId) {
		return ownerPlayer.containsKey(objOwnerId);
	}

	public static KiskService getInstance() {
		return instance;
	}
}
