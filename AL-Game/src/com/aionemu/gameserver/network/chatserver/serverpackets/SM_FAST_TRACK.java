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
package com.aionemu.gameserver.network.chatserver.serverpackets;

import java.sql.Timestamp;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.transfers.FastTrack;

/**
 * 
 * @author -Enomine-
 *
 */

@SuppressWarnings("unused")
public class SM_FAST_TRACK extends AionServerPacket {
	
	private int serverId;
	private int time;
	private Timestamp dateTime;
	
	 public SM_FAST_TRACK(){
		 this.time = (int) dateTime.getTime();
     }
	 
	 @Override
     protected void writeImpl(AionConnection con) {
		 		Player player = con.getActivePlayer();
                 writeD(0); //unk
                 writeD(1); //unk
                 writeD(player.getObjectId()); //Player Object Id
                 writeD(serverId); //Server Id
                 writeD(time); //gametime
                 writeD(0); //unk
     }

}
