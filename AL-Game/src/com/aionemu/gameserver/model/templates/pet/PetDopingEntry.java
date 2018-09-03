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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "dope")
@XmlAccessorType(XmlAccessType.NONE)
public class PetDopingEntry {

	@XmlAttribute(name = "id", required = true)
	private short id;

	@XmlAttribute(name = "usedrink", required = true)
	private boolean usedrink;

	@XmlAttribute(name = "usefood", required = true)
	private boolean usefood;

	@XmlAttribute(name = "usescroll", required = true)
	private int usescroll;

	public short getId() {
		return id;
	}

	public boolean isUseDrink() {
		return usedrink;
	}

	public boolean isUseFood() {
		return usefood;
	}

	public int getScrollsUsed() {
		return usescroll;
	}
}
