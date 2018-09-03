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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.chatserver.ChatServer;

/**
 * Client sends this only once.
 * 
 * @author Luno
 */
public class CM_CHAT_AUTH extends AionClientPacket {

	/**
	 * Constructor
	 * 
	 * @param opcode
	 */
	public CM_CHAT_AUTH(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		int objectId = readD(); // lol NC
		byte[] macAddress = readB(6);
	}

	@Override
	protected void runImpl() {
		if (GSConfig.ENABLE_CHAT_SERVER) {
			// this packet is sent sometimes after logout from world
			Player player = getConnection().getActivePlayer();
			if (!player.isInPrison()) {
				ChatServer.getInstance().sendPlayerLoginRequest(player);
			}
		}
	}
}
