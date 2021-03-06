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
package com.aionemu.gameserver.model.templates.pet;

/**
 * @author IlBuono, Rolandas
 */
public enum PetFunctionType {
	WAREHOUSE(0, true),
	FOOD(2049, true),
	LOOT(259, true),
	DOPING(8194, true),

	APPEARANCE(1),
	NONE(4, true),

	BAG(-1),
	WING(-2);

	private short id;
	private boolean isPlayerFunc = false;

	private PetFunctionType(int id, boolean isPlayerFunc) {
		this(id);
		this.isPlayerFunc = isPlayerFunc;
	}

	private PetFunctionType(int id) {
		this.id = (short) (id & 0xFFFF);
	}

	public int getId() {
		return id;
	}

	public boolean isPlayerFunction() {
		return isPlayerFunc;
	}
}
