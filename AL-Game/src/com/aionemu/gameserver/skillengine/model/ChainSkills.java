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
package com.aionemu.gameserver.skillengine.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javolution.util.FastMap;

import com.aionemu.gameserver.model.gameobjects.player.Player;

public class ChainSkills {

	private Map<String, ChainSkill> multiSkills = new FastMap<String, ChainSkill>();
	private ChainSkill chainSkill = new ChainSkill("", 0, 0);

	public int getChainCount(Player player, SkillTemplate template, String category) {
		if (category == null) {
			return 0;
		}
		long nullTime = player.getSkillCoolDown(template.getCooldownId());
		if (multiSkills.get(category) != null) {
			if (System.currentTimeMillis() >= nullTime && multiSkills.get(category).getUseTime() <= nullTime) {
				multiSkills.get(category).setChainCount(0);
			}

			return multiSkills.get(category).getChainCount();
		}

		return 0;
	}

	public long getLastChainUseTime(String category) {
		if (multiSkills.get(category) != null) {
			return multiSkills.get(category).getUseTime();
		}
		if (chainSkill.getCategory().equals(category)) {
			return chainSkill.getUseTime();
		}
		return 0L;
	}

	public boolean chainSkillEnabled(String category, int time) {
		long useTime = 0L;
		if (multiSkills.get(category) != null) {
			useTime = multiSkills.get(category).getUseTime();
		}
		else if (chainSkill.getCategory().equals(category)) {
			useTime = chainSkill.getUseTime();
		}

		if (useTime + time >= System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	public void addChainSkill(String category, boolean multiCast) {
		if (multiCast) {
			if (multiSkills.get(category) != null) {
				if (multiCast) {
					multiSkills.get(category).increaseChainCount();
				}
				multiSkills.get(category).setUseTime(System.currentTimeMillis());
			}
			else {
				multiSkills.put(category, new ChainSkill(category, multiCast ? 1 : 0, System.currentTimeMillis()));
			}
		}
		else
			chainSkill.updateChainSkill(category);
	}

	public Collection<ChainSkill> getChainSkills() {
		Collection<ChainSkill> collection = new ArrayList<ChainSkill>();
		collection.add(chainSkill);
		collection.addAll(multiSkills.values());

		return collection;
	}
}
