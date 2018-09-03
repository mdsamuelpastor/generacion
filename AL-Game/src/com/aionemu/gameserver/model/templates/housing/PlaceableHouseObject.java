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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PlaceableHouseObject")
@XmlSeeAlso({ HousingJukeBox.class, HousingPicture.class, HousingPostbox.class, HousingChair.class, HousingStorage.class, HousingNpc.class, HousingMoveableItem.class, HousingUseableItem.class,
	HousingPassiveItem.class })
public abstract class PlaceableHouseObject extends AbstractHouseObject {

	@XmlAttribute(name = "use_days")
	protected Integer useDays;

	@XmlAttribute
	protected LimitType limit;

	@XmlAttribute
	protected PlaceLocation location;

	@XmlAttribute
	protected PlaceArea area;

	public int getUseDays() {
		if (useDays == null)
			return 0;
		return useDays;
	}

	public LimitType getPlacementLimit() {
		if (limit == null)
			return LimitType.NONE;
		return limit;
	}

	public PlaceLocation getLocation() {
		return location;
	}

	public PlaceArea getArea() {
		return area;
	}

	public abstract byte getTypeId();
}
