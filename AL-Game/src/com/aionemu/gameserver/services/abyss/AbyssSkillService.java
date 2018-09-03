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
package com.aionemu.gameserver.services.abyss;

import com.aionemu.gameserver.model.gameobjects.player.AbyssRank;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;

/**
 * @author ATracer
 */
public class AbyssSkillService {

	/**
	 * @param player
	 */
	public static final void updateSkills(Player player) {
		AbyssRank abyssRank = player.getAbyssRank();
		if (abyssRank == null) {
			return;
		}
		AbyssRankEnum rankEnum = abyssRank.getRank();
		// remove all abyss skills first
		//TODO: The old ones dont removed before you get the new, posible exploit chance @Maestross
		for (AbyssSkills abyssSkill : AbyssSkills.values()) {
			if (abyssSkill.getRace() == player.getRace()) {
				for (int skillId : abyssSkill.getSkills()) {
					player.getSkillList().removeSkill(skillId);
				}
			}
		}
		// add new skills
		if (abyssRank.getRank().getId() >= AbyssRankEnum.STAR5_OFFICER.getId()) {
			for (int skillId : AbyssSkills.getSkills(player.getRace(), rankEnum)) {
				player.getSkillList().addTemporarySkill(player, skillId, 1);
			}
		}
	}

	/**
	 * @param player
	 */
	public static final void onEnterWorld(Player player) {
		updateSkills(player);
	}
}
