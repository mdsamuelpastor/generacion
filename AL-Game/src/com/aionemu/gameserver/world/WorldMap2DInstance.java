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
package com.aionemu.gameserver.world;

import com.aionemu.gameserver.world.zone.ZoneInstance;

/**
 * @author ATracer
 */
public class WorldMap2DInstance extends WorldMapInstance {

	private int ownerId;

	public WorldMap2DInstance(WorldMap parent, int instanceId) {
		this(parent, instanceId, 0);
	}

	public WorldMap2DInstance(WorldMap parent, int instanceId, int ownerId) {
		super(parent, instanceId);
		this.ownerId = ownerId;
	}

	@Override
	protected MapRegion createMapRegion(int regionId) {
		float startX = RegionUtil.getXFrom2dRegionId(regionId);
		float startY = RegionUtil.getYFrom2dRegionId(regionId);
		ZoneInstance[] zones = filterZones(this.getMapId(), regionId, startX, startY, 0, 4000);
		return new MapRegion(regionId, this, zones);
	}

	@Override
	protected void initMapRegions() {
		int size = this.getParent().getWorldSize();
		// Create all mapRegion
		for (int x = 0; x <= size; x = x + regionSize) {
			for (int y = 0; y <= size; y = y + regionSize) {
				int regionId = RegionUtil.get2dRegionId(x, y);
				regions.put(regionId, createMapRegion(regionId));
			}
		}

		// Add Neighbour
		for (int x = 0; x <= size; x = x + regionSize) {
			for (int y = 0; y <= size; y = y + regionSize) {
				int regionId = RegionUtil.get2dRegionId(x, y);
				MapRegion mapRegion = regions.get(regionId);
				for (int x2 = x - regionSize; x2 <= x + regionSize; x2 += regionSize) {
					for (int y2 = y - regionSize; y2 <= y + regionSize; y2 += regionSize) {
						if (x2 == x && y2 == y)
							continue;
						int neighbourId = RegionUtil.get2dRegionId(x2, y2);
						MapRegion neighbour = regions.get(neighbourId);
						if (neighbour != null)
							mapRegion.addNeighbourRegion(neighbour);
					}
				}
			}
		}
	}

	@Override
	public MapRegion getRegion(float x, float y, float z) {
		int regionId = RegionUtil.get2dRegionId(x, y);
		return regions.get(regionId);
	}

	@Override
	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public boolean isPersonal() {
		return ownerId != 0;
	}
}
