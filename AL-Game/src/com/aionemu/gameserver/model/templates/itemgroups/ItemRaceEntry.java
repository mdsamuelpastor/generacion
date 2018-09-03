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
package com.aionemu.gameserver.model.templates.itemgroups;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.rewards.IdLevelReward;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemRaceEntry")
@XmlSeeAlso({ IdLevelReward.class })
public class ItemRaceEntry {

	@XmlAttribute(name = "id", required = true)
	protected int id;

	@XmlAttribute(name = "race")
	protected Race race;

	public int getId() {
		return id;
	}

	public Race getRace() {
		return race;
	}

	public boolean checkRace(Race playerRace) {
		ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(id);
		return (template.getRace() == Race.PC_ALL && (race == null || race == playerRace)) || (template.getRace() != Race.PC_ALL && template.getRace() == playerRace);
	}
}
