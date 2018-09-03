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

import com.aionemu.gameserver.configs.main.HousingConfig;
import com.aionemu.gameserver.model.templates.housing.HouseType;

/**
 * @author Maestross
 */
 
public class HouseBidEntry implements Cloneable {

	private int entryIndex;
	private int landId;
	private int address;
	private int buildingId;
	private HouseType houseType;
	private long bidPrice;
	private final long unk2 = 100000L;
	private int bidCount;
	private int secondsLeft;
	private int mapId;
	private int lastBiddingPlayer;
	private long lastBidTime;

	public HouseBidEntry(House house, int index, long initialBid, long validUntil) {
		entryIndex = index;
		landId = house.getLand().getId();
		address = house.getAddress().getId();
		mapId = house.getAddress().getMapId();
		buildingId = house.getBuilding().getId();
		houseType = house.getHouseType();
		bidPrice = initialBid;
		secondsLeft = (int) ((validUntil - System.currentTimeMillis()) / 1000);
		lastBiddingPlayer = 0;
		lastBidTime = 0L;
	}

	private HouseBidEntry() {
	}

	public int getEntryIndex() {
		return entryIndex;
	}

	public void setEntryIndex(int entryIndex) {
		this.entryIndex = entryIndex;
	}

	public int getLandId() {
		return landId;
	}

	public int getAddress() {
		return address;
	}

	public int getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}

	public long getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(long bidPrice) {
		this.bidPrice = bidPrice;
	}

	public int getBidCount() {
		return bidCount;
	}

	public void incrementBidCount() {
		bidCount += 1;
	}

	public int getSecondsLeft() {
		if (secondsLeft < 0)
			return 0;
		return secondsLeft;
	}

	public void setSecondsLeft(int secondsLeft) {
		this.secondsLeft = secondsLeft;
	}

	public final long getUnk2() {
		return unk2;
	}

	public HouseType getHouseType() {
		return houseType;
	}

	public int getMapId() {
		return mapId;
	}

	public int getLastBiddingPlayer() {
		return lastBiddingPlayer;
	}

	public void setLastBiddingPlayer(int lastBiddingPlayer) {
		this.lastBiddingPlayer = lastBiddingPlayer;
	}

	public long getRefundKinah() {
		return (long) ((float) bidPrice * HousingConfig.BID_REFUND_PERCENT);
	}

	public long getLastBidTime() {
		return lastBidTime;
	}

	public void setLastBidTime(long lastBidTime) {
		this.lastBidTime = lastBidTime;
	}

	public Object Clone() {
		HouseBidEntry cloned = new HouseBidEntry();
		cloned.address = address;
		cloned.bidCount = bidCount;
		cloned.bidPrice = bidPrice;
		cloned.buildingId = buildingId;
		cloned.entryIndex = entryIndex;
		cloned.houseType = houseType;
		cloned.landId = landId;
		cloned.mapId = mapId;
		cloned.secondsLeft = secondsLeft;
		cloned.lastBiddingPlayer = lastBiddingPlayer;
		return cloned;
	}
}
