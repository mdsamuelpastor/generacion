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
package com.aionemu.gameserver.dataholders;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.templates.spawns.HouseSpawn;
import com.aionemu.gameserver.model.templates.spawns.HouseSpawns;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "houseSpawnsData" })
@XmlRootElement(name = "house_npcs")
public class HouseNpcsData {

	@XmlElement(name = "house")
	protected List<HouseSpawns> houseSpawnsData;

	@XmlTransient
	private TIntObjectHashMap<List<HouseSpawn>> houseSpawnsByAddressId = new TIntObjectHashMap<List<HouseSpawn>>();

	public List<HouseSpawns> getHouseSpawns() {
		if (houseSpawnsData == null) {
			houseSpawnsData = new ArrayList<HouseSpawns>();
		}
		return houseSpawnsData;
	}

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (HouseSpawns houseSpawns : getHouseSpawns())
			houseSpawnsByAddressId.put(houseSpawns.getAddress(), houseSpawns.getSpawns());
	}

	public List<HouseSpawn> getSpawnsByAddress(int address) {
		return houseSpawnsByAddressId.get(address);
	}

	public int size() {
		return houseSpawnsByAddressId.size() * 3;
	}
}
