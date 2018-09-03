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
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class CraftConfig {

	/**
	 * Enable craft skills unrestricted level-up
	 */
	@Property(key = "gameserver.craft.skills.unrestricted.levelup.enable", defaultValue = "false")
	public static boolean UNABLE_CRAFT_SKILLS_UNRESTRICTED_LEVELUP;

	/**
	 * Maximum number of expert skills a player can have
	 */
	@Property(key = "gameserver.craft.max.expert.skills", defaultValue = "2")
	public static int MAX_EXPERT_CRAFTING_SKILLS;

	/**
	 * Maximum number of master skills a player can have
	 */
	@Property(key = "gameserver.craft.max.master.skills", defaultValue = "1")
	public static int MAX_MASTER_CRAFTING_SKILLS;
	@Property(key = "gameserver.craft.critical.rate.regular", defaultValue = "15")
	public static int CRAFT_CRIT_RATE;

	@Property(key = "gameserver.craft.critical.rate.premium", defaultValue = "15")
	public static int PREMIUM_CRAFT_CRIT_RATE;

	@Property(key = "gameserver.craft.critical.rate.vip", defaultValue = "15")
	public static int VIP_CRAFT_CRIT_RATE;

	@Property(key = "gameserver.craft.combo.rate.regular", defaultValue = "25")
	public static int CRAFT_COMBO_RATE;

	@Property(key = "gameserver.craft.combo.rate.premium", defaultValue = "25")
	public static int PREMIUM_CRAFT_COMBO_RATE;

	@Property(key = "gameserver.craft.combo.rate.vip", defaultValue = "25")
	public static int VIP_CRAFT_COMBO_RATE;

	@Property(key = "gameserver.craft.event.update.rule", defaultValue = "0 0 15 ? * *")
	public static String CRAFT_NPC_UPDATE_RULE;

	@Property(key = "gameserver.craft.event.despawn.rule", defaultValue = "0 0 18 ? * *")
	public static String CRAFT_NPC_DESPAWN_RULE;

	@Property(key = "gameserver.craft.event.enable", defaultValue = "false")
	public static boolean CRAFT_EVENT_ENABLE;
}
