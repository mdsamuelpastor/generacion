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
package com.aionemu.gameserver.world.zone;

public enum ZoneAttributes {
	BIND(1),
	RECALL(2),
	GLIDE(4),
	FLY(8),
	RIDE(16),
	FLY_RIDE(32),
	PVP_ENABLED(64),
	DUEL_SAME_RACE_ENABLED(128),
	DUEL_OTHER_RACE_ENABLED(256);

	private int id;

	private ZoneAttributes(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
