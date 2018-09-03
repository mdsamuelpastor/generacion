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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Rhys2002, Maestross
 */
public class SM_GROUP_LOOT extends AionServerPacket {

	private int groupId;
	private int index;
	private int unk2;
	private int itemId;
	private int unk3;
	private int lootCorpseId;
	private int distributionId;
	private int playerId;
	private long luck;
	private static final Logger log = LoggerFactory.getLogger(SM_GROUP_LOOT.class);

	/**
	 * @param Player
	 *          Id must be 0 to start the Roll Options
	 */
	public SM_GROUP_LOOT(int groupId, int playerId, int itemId, int lootCorpseId, int distributionId, long luck, int index) {
		this.groupId = groupId;
		this.index = index;
		this.unk2 = 1;
		this.itemId = itemId;
		this.unk3 = 0;
		this.lootCorpseId = lootCorpseId;
		this.distributionId = distributionId;		
		this.playerId = playerId;
		if (luck == 1) this.luck = 0; 
		else if (luck == 0) this.luck=1;
		else this.luck = luck;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(groupId);
		writeD(index);
		writeD(unk2);
		writeD(itemId);
		writeC(unk3);
		writeD(lootCorpseId);
		writeH(0);
		writeD(lootCorpseId);
		writeC(distributionId);
		writeD(playerId);
		writeD((int) luck);
	
		log.info("GId: "+groupId+" ItemId: "+itemId+" DistId: "+distributionId+" PlayerId: "+playerId+" Luck:"+luck );
	}
}
