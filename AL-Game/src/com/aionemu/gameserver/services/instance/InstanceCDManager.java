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
package com.aionemu.gameserver.services.instance;

import java.sql.Timestamp;
import java.util.concurrent.Future;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.instance.InstanceEngine;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.WorldMap2DInstance;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldMapInstanceFactory;
import com.aionemu.gameserver.world.WorldMapType;
import com.aionemu.gameserver.world.zone.ZoneInstance;

/**
 * @author Maestross
 */

public class InstanceCDManager {
    
	private static final Logger log = LoggerFactory.getLogger(InstanceCDManager.class);

	//Tiamat Stronghold : ReEntry 9 AM Sun, Mon, Wed, Fri, Sat
	//Dragon Lord Refuge : ReEntry 9 AM Sun, Tue, Thu, Sat
	
	/*@Override
	public void checkreEntryTime(Npc npc, Player player) {
		switch (npc.getNpcId()) {
			case 730627: //Dragon Lord Refuge Entrance 
				player.getPortalCooldown(300520000);
				PacketSendUtility.sendMessage(player, "You cannot use this portal before 9 AM tomorrow.");
				break;
			case 730628: //Dragon Lord Refuge Challenge Entrance 
				player.getPortalCooldown(300490000);
				PacketSendUtility.sendMessage(player, "You cannot use this portal before 9 AM tomorrow.");
				break;
			case 730716: //Tiamat Stronghold Entrance 
				player.getPortalCooldown(300510000);
				PacketSendUtility.sendMessage(player, "You cannot use this portal before 9 AM tomorrow.");
				break;
			case 730731: //The Hexway  9 AM daily
				player.getPortalCooldown(300700000);
				PacketSendUtility.sendMessage(player, "You cannot use this portal before 9 AM tomorrow.");
				break;
			case 730356: //Abyssal splinter  9 AM daily
				player.getPortalCooldown(300220000);
				PacketSendUtility.sendMessage(player, "You cannot use this portal before 9 AM tomorrow.");
				break;
		}
	}*/
	
	/*
	 * I will work on this 3.5 part. Instances like Tiamats Stronghold and Unstable Abyssal 
	 * Splinter have new CD Enum. If you leave the instance you can only re-entry it
	 * the next morning at 9 o'clock a.m. . I think a new service for it would be the
	 * best solution.
	 */
}