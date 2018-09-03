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
package com.aionemu.gameserver.model.team.legion;

import java.sql.Timestamp;

/**
 * @author Simple
 */
public class LegionHistory {

	private LegionHistoryType legionHistoryType;
	private String name = "";
	private Timestamp time;
	private int tabId;
	private String description = "";

	public LegionHistory(LegionHistoryType legionHistoryType, String name, Timestamp time, int tabId, String description) {
		this.legionHistoryType = legionHistoryType;
		this.name = name;
		this.time = time;
		this.tabId = tabId;
		this.description = description;
	}

	/**
	 * @return the legionHistoryType
	 */
	public LegionHistoryType getLegionHistoryType() {
		return legionHistoryType;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the time
	 */
	public Timestamp getTime() {
		return time;
	}

	public int getTabId() {
		return tabId;
	}

	public String getDescription() {
		return description;
	}
}
