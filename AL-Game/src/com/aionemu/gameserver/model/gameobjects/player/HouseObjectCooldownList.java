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
package com.aionemu.gameserver.model.gameobjects.player;

import javolution.util.FastMap;

public class HouseObjectCooldownList {

	private FastMap<Integer, Long> houseObjectCooldowns;

	HouseObjectCooldownList(Player owner) {
	}

	public boolean isCanUseObject(int objectId) {
		if (houseObjectCooldowns == null || !houseObjectCooldowns.containsKey(objectId)) {
			return true;
		}
		Long coolDown = houseObjectCooldowns.get(objectId);
		if (coolDown == null) {
			return true;
		}
		if (coolDown.longValue() < System.currentTimeMillis()) {
			houseObjectCooldowns.remove(objectId);
			return true;
		}

		return false;
	}

	public long getHouseObjectCooldown(int objectId) {
		if (houseObjectCooldowns == null || !houseObjectCooldowns.containsKey(objectId)) {
			return 0L;
		}
		return houseObjectCooldowns.get(objectId);
	}

	public FastMap<Integer, Long> getHouseObjectCooldowns() {
		return houseObjectCooldowns;
	}

	public void setHouseObjectCooldowns(FastMap<Integer, Long> houseObjectCooldowns) {
		this.houseObjectCooldowns = houseObjectCooldowns;
	}

	public void addHouseObjectCooldown(int objectId, int delay) {
		if (houseObjectCooldowns == null) {
			houseObjectCooldowns = new FastMap<Integer, Long>();
		}

		long nextUseTime = System.currentTimeMillis() + delay * 1000;
		houseObjectCooldowns.put(objectId, nextUseTime);
	}

	public int getReuseDelay(int objectId) {
		if (isCanUseObject(objectId))
			return 0;
		long cd = getHouseObjectCooldown(objectId);
		int delay = (int) ((cd - System.currentTimeMillis()) / 1000L);
		return delay;
	}
}
