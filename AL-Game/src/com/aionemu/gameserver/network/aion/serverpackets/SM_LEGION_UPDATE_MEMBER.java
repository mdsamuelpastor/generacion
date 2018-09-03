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


import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Simple, Maestross
 */
public class SM_LEGION_UPDATE_MEMBER extends AionServerPacket {

	private static final byte OFFLINE = 0x00;
	private static final byte ONLINE = 0x01;
	private Player player;
	private int msgId = 0;
	private String text = "";
	private byte isOnline;

	public SM_LEGION_UPDATE_MEMBER(Player player, int msgId, String text) {
		this.player = player;
		this.msgId = msgId;
		this.text = text;
		this.isOnline = player.isOnline() ? ONLINE : OFFLINE;
	}
	
	public SM_LEGION_UPDATE_MEMBER(Player player)
	{
		this.player = player;
		this.isOnline = OFFLINE;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(player.getObjectId());
		writeC(player.getLegionMember().getRank().getRankId());
		writeC(player.getCommonData().getPlayerClass().getClassId());
		writeC(player.getLevel());
		writeD(player.getPosition().getMapId());
		writeC(isOnline);
		writeD(player.isOnline() ? 0 : player.getLastOnline());
		writeD(msgId);
		writeS(text);
		writeD(0);
	}
}