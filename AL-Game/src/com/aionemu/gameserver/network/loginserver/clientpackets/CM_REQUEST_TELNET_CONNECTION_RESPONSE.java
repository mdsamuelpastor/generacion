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
package com.aionemu.gameserver.network.loginserver.clientpackets;

import com.aionemu.gameserver.network.loginserver.LsClientPacket;
import com.aionemu.gameserver.telnet.TelnetExecutor;

/**
 * @author Divinity
 */
public class CM_REQUEST_TELNET_CONNECTION_RESPONSE extends LsClientPacket {

	private String playerName;
	private int accesslevel;

	public CM_REQUEST_TELNET_CONNECTION_RESPONSE(int opcode) {
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		this.playerName = readS();
		this.accesslevel = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		TelnetExecutor.receiveAccesslevel(playerName, accesslevel);
	}
}
