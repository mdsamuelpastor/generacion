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
package com.aionemu.gameserver.utils.audit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import javolution.util.FastMap;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
import com.aionemu.gameserver.world.World;

/**
 * @author MrPoke, Maestros
 */
public class GMService {

	public static final GMService getInstance() {
		return SingletonHolder.instance;
	}

	private Map<Integer, Player> gms = new FastMap<Integer, Player>();
	private boolean announceAny = false;
	private List<Byte> announceList;

	private GMService() {
		announceList = new ArrayList<Byte>();
		announceAny = AdminConfig.ANNOUNCE_LEVEL_LIST.equals("*");
		if (!announceAny) {
			try {
				for (String level : AdminConfig.ANNOUNCE_LEVEL_LIST.split(","))
					announceList.add(Byte.parseByte(level));
			}
			catch (Exception e) {
				announceAny = true;
			}
		}
	}

	public Collection<Player> getGMs() {
		return gms.values();
	}

	public void onPlayerLogin(Player player) {
		if (player.isGM()) {
			gms.put(player.getObjectId(), player);

			if (announceAny) {
				Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), "Info: " + player.getName() + LanguageHandler.translate(CustomMessageId.ANNOUNCE_GM_CONNECTION));
					}
			}
			else if (announceList.contains(Byte.valueOf(player.getAccessLevel()))) {
				Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), "Info: " + player.getName() + LanguageHandler.translate(CustomMessageId.ANNOUNCE_GM_CONNECTION));
					}
			}
		}
	}

	public void onPlayerLogedOut(Player player) {
		gms.remove(player.getObjectId());
	}

	public void broadcastMesage(String message) {
		SM_MESSAGE packet = new SM_MESSAGE(0, null, message, ChatType.YELLOW);
		for (Player player : gms.values()) {
			PacketSendUtility.sendPacket(player, packet);
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final GMService instance = new GMService();
	}
}
