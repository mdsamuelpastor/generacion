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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "building")
public class Building {

	@XmlAttribute(name = "default")
	protected Boolean isDefault;

	@XmlAttribute(name = "parts_match", required = true)
	protected String partsMatch;

	@XmlAttribute(required = true)
	protected String size;

	@XmlAttribute(required = true)
	protected BuildingType type;

	@XmlAttribute(required = true)
	protected int id;

	public boolean isDefault() {
		if (isDefault == null)
			return false;
		return isDefault.booleanValue();
	}

	public String getPartsMatchTag() {
		return partsMatch;
	}

	public String getSize() {
		return size;
	}

	public BuildingType getType() {
		return type;
	}

	public int getId() {
		return id;
	}
}
