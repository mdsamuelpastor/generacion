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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "PartType")
@XmlEnum
public enum PartType {
	ROOF("roof", 1, 1),
	OUTWALL("outwall", 2, 2),
	FRAME("frame", 3, 3),
	DOOR("door", 4, 4),
	GARDEN("garden", 5, 5),
	FENCE("fence", 6, 6),
	INWALL_ANY("inwall", 7, 12),
	INFLOOR_ANY("infloor", 13, 18),
	ADDON("addon", 19, 21);

	private String databaseName;
	private int lineNrStart;
	private int lineNrEnd;

	private PartType(String databaseName, int packetLineStart, int packetLineEnd) {
		this.databaseName = databaseName;
		lineNrStart = packetLineStart;
		lineNrEnd = packetLineEnd;
	}

	public String getDbName() {
		return databaseName;
	}

	public int getStartLineNr() {
		return lineNrStart;
	}

	public int getEndLineNr() {
		return lineNrEnd;
	}

	public static PartType getForLineNr(int lineNr) {
		for (PartType type : values()) {
			if (type.getStartLineNr() <= lineNr && type.getEndLineNr() >= lineNr)
				return type;
		}
		return null;
	}
}
