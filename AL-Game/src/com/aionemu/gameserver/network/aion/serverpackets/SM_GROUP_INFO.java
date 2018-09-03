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

import com.aionemu.gameserver.model.team2.common.legacy.LootGroupRules;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Lyahim, ATracer, xTz, Maestross
 */
public class SM_GROUP_INFO extends AionServerPacket {
	private LootGroupRules lootRules;
	private int groupId;
	private int leaderId;
	public SM_GROUP_INFO(PlayerGroup group) 
	{
		groupId = group.getObjectId();
		leaderId = group.getLeader().getObjectId();
		lootRules = group.getLootGroupRules();
		group.getGroupType();
	}

	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(groupId);
		writeD(leaderId);
		writeD(lootRules.getLootRule().getId());
		writeD(lootRules.getMisc()); //AutoDistribution
		writeD(lootRules.getCommonItemAbove());
		writeD(lootRules.getSuperiorItemAbove());
		writeD(lootRules.getHeroicItemAbove());
		writeD(lootRules.getFabledItemAbove());
		writeD(lootRules.getEthernalItemAbove());
		writeD(lootRules.getOver_ethernal());
		writeD(2); //for 3.7 writeD(lootRules.getOver_over_ethernal());
    writeD(16128);
    writeD(0x00);
    writeH(0x00);
    writeC(0x00);
    writeD(0x00);
	}
}