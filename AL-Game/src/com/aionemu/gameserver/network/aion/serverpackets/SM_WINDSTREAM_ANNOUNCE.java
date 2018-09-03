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
 * @author LokiReborn
 * 
 */
public class SM_WINDSTREAM_ANNOUNCE extends AionServerPacket {
	private int bidirectional;
	private int mapId;
	private int streamId;
	private int boost;
	
	public SM_WINDSTREAM_ANNOUNCE(int bidirectional, int mapId, int streamId, int boost) {
		this.bidirectional = bidirectional;
		this.mapId = mapId;
		this.streamId = streamId;
		this.boost = boost;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(bidirectional);
		writeD(mapId);
		writeD(streamId);
		writeC(boost);
	}
}
