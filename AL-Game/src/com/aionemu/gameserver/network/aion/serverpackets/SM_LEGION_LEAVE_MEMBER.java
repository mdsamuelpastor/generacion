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
package com.aionemu.gameserver.network.aion.serverpackets;


import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Simple, Maestross
 */
public class SM_LEGION_LEAVE_MEMBER extends AionServerPacket {

	private String name;
	private String name1;
	private int playerObjId;
	private int msgId;

	public SM_LEGION_LEAVE_MEMBER(int msgId, int playerObjId, String name) {
		this.msgId = msgId;
		this.playerObjId = playerObjId;
		this.name = name;
	}

	public SM_LEGION_LEAVE_MEMBER(int msgId, int playerObjId, String name, String name1) {
		this.msgId = msgId;
		this.playerObjId = playerObjId;
		this.name = name;
		this.name1 = name1;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(playerObjId);
		writeC(0); // isMember ? 1 : 0
		writeD(0); // unix time for log off
		writeD(msgId);
		writeS(name);
		writeS(name1);
	}
}
