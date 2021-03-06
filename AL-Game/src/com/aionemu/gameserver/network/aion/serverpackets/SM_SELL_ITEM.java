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
 * @author orz, Sarynth
 */
public class SM_SELL_ITEM extends AionServerPacket {

	private int targetObjectId;
	private int sellPercentage;

	public SM_SELL_ITEM(int targetObjectId, int sellPercentage) {

		this.sellPercentage = sellPercentage;
		this.targetObjectId = targetObjectId;

	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	protected void writeImpl(AionConnection con) {

		writeD(targetObjectId);
		writeD(sellPercentage); // Buy Price * (sellPercentage / 100) = Display price.

	}
}
