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
package com.aionemu.gameserver.model;

public enum EventType {
	NONE(0, ""),
	CHRISTMAS(1, "christmas"),
	HALLOWEEN(2, "halloween"),
	VALENTINE(4, "valentine");

	private int id;
	private String theme;

	private EventType(int id, String theme) {
		this.id = id;
		this.theme = theme;
	}

	public int getId() {
		return id;
	}

	public String getTheme() {
		return theme;
	}

	public static EventType getEventType(String theme) {
		for (EventType type : values()) {
			if (theme.equals(type.getTheme())) {
				return type;
			}
		}
		return NONE;
	}
}
