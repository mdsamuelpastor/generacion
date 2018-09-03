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
package com.aionemu.gameserver.dataholders;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.templates.item.bonuses.RandomBonus;
import com.aionemu.gameserver.model.templates.stats.ModifiersTemplate;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "randomBonuses" })
@XmlRootElement(name = "random_bonuses")
public class ItemRandomBonusData {

	@XmlElement(name = "random_bonus", required = true)
	protected List<RandomBonus> randomBonuses;

	@XmlTransient
	private TIntObjectHashMap<RandomBonus> randomBonusData = new TIntObjectHashMap<RandomBonus>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (RandomBonus bonus : randomBonuses) {
			randomBonusData.put(bonus.getId(), bonus);
		}
		randomBonuses.clear();
		randomBonuses = null;
	}

	public ModifiersTemplate getRandomModifiers(int rndOptionSet) {
		RandomBonus bonus = randomBonusData.get(rndOptionSet);
		if (bonus == null) {
			return null;
		}
		List<ModifiersTemplate> modifiersGroup = bonus.getModifiers();

		int chance = Rnd.get(10000);
		int current = 0;
		ModifiersTemplate template = null;
		for (ModifiersTemplate modifiers : modifiersGroup) {
			current = (int) (current + modifiers.getChance() * 100);
			if (current >= chance) {
				template = modifiers;
				break;
			}
		}
		return template;
	}

	public int size() {
		return randomBonusData.size();
	}
}
