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


import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Simple, Maestross
 */
public class SM_LEGION_EDIT extends AionServerPacket {

	private int type;
	private Legion legion;
	private int unixTime;
	private String announcement;

	public SM_LEGION_EDIT(int type) {
		this.type = type;
	}

	public SM_LEGION_EDIT(int type, Legion legion) {
		this.type = type;
		this.legion = legion;
	}

	public SM_LEGION_EDIT(int type, int unixTime) {
		this.type = type;
		this.unixTime = unixTime;
	}

	public SM_LEGION_EDIT(int type, int unixTime, String announcement) {
		this.type = type;
		this.announcement = announcement;
		this.unixTime = unixTime;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeC(type);
		switch (type) {
			/** Change Legion Level **/
			case 0:
				writeC(legion.getLegionLevel());
				break;
			/** Change Legion Rank **/
			case 1:
				writeD(legion.getLegionRank());
				break;
			/** Change Legion Permissions **/
			case 2:
				writeH(legion.getDeputyPermission());
				writeH(legion.getCenturionPermission());
				writeH(legion.getLegionaryPermission());
				writeH(legion.getVolunteerPermission());
				break;
			/** Change Legion Contributions **/
			case 3:
				writeQ(legion.getContributionPoints()); // get Contributions
				break;
			case 4:
				writeQ(legion.getLegionWarehouse().getKinah());
				break;
			/** Change Legion Announcement **/
			case 5:
				writeS(announcement);
				writeD(unixTime);
				break;
			/** Disband Legion **/
			case 6:
				writeD(unixTime);
				break;
			/** Recover Legion **/
			case 7:
				break;
			/** Refresh Legion Announcement? **/
			case 8:
				break;
			/** Maybe for Legions Tasks? **/
			case 0x09:
			case 0x10:
		}
	}
}
