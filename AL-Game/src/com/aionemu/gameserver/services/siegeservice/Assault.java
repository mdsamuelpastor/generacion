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
package com.aionemu.gameserver.services.siegeservice;

import java.util.concurrent.Future;

import com.aionemu.gameserver.model.gameobjects.siege.SiegeNpc;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.model.siege.SiegeRace;

public abstract class Assault<siege extends Siege<?>> {

	protected final SiegeLocation siegeLocation;
	protected final int locationId;
	protected final SiegeNpc boss;
	protected final int worldId;
	protected Future<?> dredgionTask;
	protected Future<?> spawnTask;

	public Assault(Siege<?> siege) {
		siegeLocation = siege.getSiegeLocation();
		boss = siege.getBoss();
		locationId = siege.getSiegeLocationId();
		worldId = siege.getSiegeLocation().getWorldId();
	}

	public int getWorldId() {
		return worldId;
	}

	public void startAssault(int delay) {
		scheduleAssault(delay);
	}

	public void finishAssault(boolean captured) {
		if (dredgionTask != null && !dredgionTask.isDone())
			dredgionTask.cancel(true);
		if (spawnTask != null && !spawnTask.isDone()) {
			spawnTask.cancel(true);
		}
		onAssaultFinish(captured && siegeLocation.getRace().equals(SiegeRace.BALAUR));
	}

	protected abstract void onAssaultFinish(boolean paramBoolean);

	protected abstract void scheduleAssault(int paramInt);
}
