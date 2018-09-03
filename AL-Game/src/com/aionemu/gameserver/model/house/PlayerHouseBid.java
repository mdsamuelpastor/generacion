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
package com.aionemu.gameserver.model.house;

import java.sql.Timestamp;

/**
 * @author Maestross
 */
 
public class PlayerHouseBid implements Comparable<PlayerHouseBid> {

	private int playerId;
	private int houseId;
	private long offer;
	private Timestamp time;

	public PlayerHouseBid(int playerId, int houseId, long offer, Timestamp time) {
		this.playerId = playerId;
		this.houseId = houseId;
		this.offer = offer;
		this.time = time;
	}

	public int getPlayerId() {
		return playerId;
	}

	public int getHouseId() {
		return houseId;
	}

	public long getBidOffer() {
		return offer;
	}

	public Timestamp getTime() {
		return time;
	}

	@Override
	public int hashCode() {
		int result = playerId;
		result = 29 * result + houseId;
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof PlayerHouseBid))
			return false;
		PlayerHouseBid other = (PlayerHouseBid) o;
		return playerId == other.playerId && houseId == other.houseId;
	}

	@Override
	public int compareTo(PlayerHouseBid o) {
		return (int) (time.getTime() - o.getTime().getTime());
	}
}
