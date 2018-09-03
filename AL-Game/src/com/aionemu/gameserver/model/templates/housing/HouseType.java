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
package com.aionemu.gameserver.model.templates.housing;

public enum HouseType {
	ESTATE(0, 3, "a"),
	MANSION(1, 2, "b"),
	HOUSE(2, 1, "c"),
	STUDIO(3, 0, "d"),
	PALACE(4, 4, "s");

	private String abbrev;
	private int limitTypeIndex;
	private int id;

	private HouseType(int index, int id, String abbrev) {
		this.abbrev = abbrev;
		limitTypeIndex = index;
		this.id = id;
	}

	public String getAbbreviation() {
		return abbrev;
	}

	public int getLimitTypeIndex() {
		return limitTypeIndex;
	}

	public int getId() {
		return id;
	}

	public String value() {
		return name();
	}

	public static HouseType fromValue(String value) {
		return valueOf(value);
	}
}
