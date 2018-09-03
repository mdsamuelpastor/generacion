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

import java.util.List;
import java.util.Iterator;

import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.team.legion.LegionMemberEx;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.HousingService;

/**
 * @author Simple, Maestross
 */
public class SM_LEGION_MEMBERLIST extends AionServerPacket {

	private static final int OFFLINE = 0x00;
	private static final int ONLINE = 0x01;
	private List<LegionMemberEx> legionMembers;
	private boolean isFirst;

	/**
	 * This constructor will handle legion member info when a List of members is given
	 * 
	 * @param ArrayList
	 *          <LegionMemberEx> legionMembers
	 */
	public SM_LEGION_MEMBERLIST(List<LegionMemberEx> legionMembers, boolean isFirst) {
		this.legionMembers = legionMembers;
		this.isFirst = isFirst;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		int size = legionMembers.size();
		int j = 1;
		if (size > 110)
		    size = 110;
		writeC(isFirst ? 0x01 : 0x00);
		writeH(65536 - size);
		Iterator localIterator = legionMembers.iterator();
        while (localIterator.hasNext()) {
			LegionMemberEx localLegionMemberEx = (LegionMemberEx)localIterator.next();
            if (j > size)
                break;
			writeD(localLegionMemberEx.getObjectId());
			writeS(localLegionMemberEx.getName());
			writeC(localLegionMemberEx.getPlayerClass().getClassId());
			writeD(localLegionMemberEx.getLevel());
			writeC(localLegionMemberEx.getRank().getRankId());
			writeD(localLegionMemberEx.getWorldId());
			writeC(localLegionMemberEx.isOnline() ? ONLINE : OFFLINE);
			writeS(localLegionMemberEx.getSelfIntro());
			writeS(localLegionMemberEx.getNickname());
			writeD(localLegionMemberEx.getLastOnline());
            int address = HousingService.getInstance().getPlayerAddress(localLegionMemberEx.getObjectId());
            if (address > 0) {
                House house = HousingService.getInstance().getPlayerStudio(localLegionMemberEx.getObjectId());
                if (house == null) {
                    house = HousingService.getInstance().getHouseByAddress(address);
                }
                writeD(address);
            }
            else {
                writeD(0);
            }
			writeD(0);
			writeD(NetworkConfig.GAMESERVER_ID);
			j++;
        }
    }
}