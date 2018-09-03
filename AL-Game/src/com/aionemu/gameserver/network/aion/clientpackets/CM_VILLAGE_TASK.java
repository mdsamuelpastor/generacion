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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_VILLAGE_TASK;


/**
 * @author Maestross
 *
 */
public class CM_VILLAGE_TASK extends AionClientPacket {

	private int type;
	private int type2;
	private int playerObjectId;
	private int villageId;
  private int timestamp;
  private int taskId;
	
	public CM_VILLAGE_TASK(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/* (non-Javadoc)
	 * @see com.aionemu.commons.network.packet.BaseClientPacket#readImpl()
	 */
	@Override
	protected void readImpl() {
		type = readC();
		villageId = readD();
		type2 = readC();//1 = legion, 2 = village
		playerObjectId = readD();
		timestamp = readD();
		
	}

	/* (non-Javadoc)
	 * @see com.aionemu.commons.network.packet.BaseClientPacket#runImpl()
	 */
	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
			sendPacket(new SM_VILLAGE_TASK(type, villageId, type2, playerObjectId, timestamp));
			switch(player.getLegion().getLegionLevel()) {
				case 5:
					if (player.getRace() == Race.ELYOS)
					sendPacket(new SM_VILLAGE_TASK(7, 300, villageId, type2, playerObjectId, timestamp));
					else
						sendPacket(new SM_VILLAGE_TASK(7, 400, villageId, type2, playerObjectId, timestamp));
				case 6:
					if (player.getRace() == Race.ELYOS) 
					sendPacket(new SM_VILLAGE_TASK(7, 301, villageId, type2, playerObjectId, timestamp));
					else
						sendPacket(new SM_VILLAGE_TASK(7, 401, villageId, type2, playerObjectId, timestamp));
				case 7:
					if (player.getRace() == Race.ELYOS)
					sendPacket(new SM_VILLAGE_TASK(7, 302, villageId, type2, playerObjectId, timestamp));
					else
						sendPacket(new SM_VILLAGE_TASK(7, 402, villageId, type2, playerObjectId, timestamp));
				case 8:
					if (player.getRace() == Race.ELYOS) {
					sendPacket(new SM_VILLAGE_TASK(7, 303, villageId, type2, playerObjectId, timestamp));
					sendPacket(new SM_VILLAGE_TASK(7, 304, villageId, type2, playerObjectId, timestamp));
					sendPacket(new SM_VILLAGE_TASK(7, 305, villageId, type2, playerObjectId, timestamp));
					} else {
						sendPacket(new SM_VILLAGE_TASK(7, 403, villageId, type2, playerObjectId, timestamp));
					  sendPacket(new SM_VILLAGE_TASK(7, 404, villageId, type2, playerObjectId, timestamp));
					  sendPacket(new SM_VILLAGE_TASK(7, 405, villageId, type2, playerObjectId, timestamp));
					}
				default:
					sendPacket(new SM_VILLAGE_TASK(7, taskId, villageId, type2, playerObjectId, timestamp));
			}
	}
}