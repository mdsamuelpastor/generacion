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
package com.aionemu.gameserver.geoEngine.collision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class CollisionResults implements Iterable<CollisionResult> {

	private final ArrayList<CollisionResult> results = new ArrayList<CollisionResult>();
	private boolean sorted = true;
	private boolean onlyFirst = false;

	public void clear() {
		results.clear();
	}

	@Override
	public Iterator<CollisionResult> iterator() {
		if (!sorted) {
			Collections.sort(results);
			sorted = true;
		}

		return results.iterator();
	}

	public void addCollision(CollisionResult result) {
		if (Float.isNaN(result.getDistance())) {
			return;
		}
		results.add(result);
		if (!onlyFirst)
			sorted = false;
	}

	public int size() {
		return results.size();
	}

	public CollisionResult getClosestCollision() {
		if (size() == 0)
			return null;

		if (!sorted) {
			Collections.sort(results);
			sorted = true;
		}

		return results.get(0);
	}

	public CollisionResult getFarthestCollision() {
		if (size() == 0)
			return null;

		if (!sorted) {
			Collections.sort(results);
			sorted = true;
		}

		return results.get(size() - 1);
	}

	public CollisionResult getCollision(int index) {
		if (!sorted) {
			Collections.sort(results);
			sorted = true;
		}

		return results.get(index);
	}

	/**
	 * Internal use only.
	 * 
	 * @param index
	 * @return
	 */
	public CollisionResult getCollisionDirect(int index) {
		return results.get(index);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CollisionResults[");
		for (CollisionResult result : results) {
			sb.append(result).append(", ");
		}
		if (results.size() > 0)
			sb.setLength(sb.length() - 2);

		sb.append("]");
		return sb.toString();
	}

	/**
	 * @param onlyFirst
	 *          The onlyFirst to set.
	 */
	public void setOnlyFirst(boolean onlyFirst) {
		this.onlyFirst = onlyFirst;
	}

	/**
	 * @return Returns the onlyFirst.
	 */
	public boolean isOnlyFirst() {
		return onlyFirst;
	}

}
