/*
 * This file is part of NextGenCore <Ver:3.9>.
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
package com.aionemu.gameserver.services.instance;

import org.joda.time.DateTime;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * 
 * @author Maestross
 *
 */
public class PvPArenaService {

	public static boolean isPvPArenaAvailable(Player player, int mapId) {
		if (!isPvPArenaAvailable()) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401306, new Object[] { Integer.valueOf(mapId) }));
			return false;
		}

		if (player.getInventory().getFirstItemByItemId(186000135) == null) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400219, new Object[] { Integer.valueOf(mapId) }));
			return false;
		}

		return true;
	}

	private static boolean isPvPArenaAvailable() {
		DateTime now = DateTime.now();
		int hour = now.getHourOfDay();
		int day = now.getDayOfWeek();
		if ((day == 6) || (day == 7)) {
				return true;
		}
		else if ((day == 1) || (day == 2) || (day == 3) || (day == 4) || (day == 5)) {
		  if ((hour == 0) || (hour == 1) || (hour == 12) || (hour == 13) || ((hour >= 18) && (hour <= 2))) {
			  return true;
		  }
		}
		return false;
	}
}
