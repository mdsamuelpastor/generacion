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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.drop.DropDistributionService;

/**
 * @author Rhys2002, Maestross
 */
public class CM_GROUP_LOOT extends AionClientPacket {

	@SuppressWarnings("unused")
	private int groupId;
	private int index;
	@SuppressWarnings("unused")
	private int unk1;
	private int itemId;
    private int npcId;
	private int distributionId;
	private int roll;
	private long bid;
    private final static Logger log = LoggerFactory.getLogger(CM_GROUP_LOOT.class);
	
	public CM_GROUP_LOOT(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		groupId = readD();
		index = readD();
		readD();
		itemId = readD();
        readC();
		npcId = readD();
		readC();
		readC(); //unk
		distributionId = readC();// 2: Roll 3: Bid
		roll = readD();// 0: Never Rolled 1: Rolled
		bid = readQ();// 0: No Bid else bid amount
		log.info("npcid: "+npcId+" roll: "+roll+" DistId: "+distributionId);
    
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null) {
			return;
		}
		switch (distributionId) {
			case 2:
				DropDistributionService.getInstance().handleRoll(player, roll, itemId, npcId, index);
				break;
			case 3:
				DropDistributionService.getInstance().handleBid(player, bid, itemId, npcId, index);
				break;
		}
	}
}
