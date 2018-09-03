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

/**
 * @author Sarynth, xTz, Source, Maestros
 */
public class SiegeConfig {

	/**
	 * Siege Enabled
	 */
	@Property(key = "gameserver.siege.enable", defaultValue = "true")
	public static boolean SIEGE_ENABLED;

	/**
	 * Siege Reward Rate
	 */
	@Property(key = "gameserver.siege.medal.rate", defaultValue = "1")
	public static int SIEGE_MEDAL_RATE;

	/**
	 * Siege sield Enabled
	 */
	@Property(key = "gameserver.siege.shield.enable", defaultValue = "true")
	public static boolean SIEGE_SHIELD_ENABLED;

	@Property(key = "gameserver.siege.assault.enable", defaultValue = "false")
	public static boolean BALAUR_AUTO_ASSAULT;

	@Property(key = "gameserver.siege.assault.rate", defaultValue = "1")
	public static float BALAUR_ASSAULT_RATE;

	@Property(key = "gameserver.siege.protector.time", defaultValue = "0 0 21 ? * *")
	public static String RACE_PROTECTOR_SPAWN_SCHEDULE;

	@Property(key = "gameserver.sunayaka.time", defaultValue = "0 0 23 ? * *")
	public static String BERSERKER_SUNAYAKA_SPAWN_SCHEDULE;

	/**
	 * Moltenus Spawn Service (World Boss 3.1)
	 */
	@Property(key = "gameserver.moltenus.enable", defaultValue = "false")
	public static boolean MOLTENUS_ENABLE;

	@Property(key = "gameserver.moltenus.spawn.hour", defaultValue = "22")
	public static int MOLTENUS_HOUR;

	@Property(key = "gameserver.moltenus.despawn.hour", defaultValue = "23")
	public static int MOLTENUS_DESPAWN_HOUR;

	@Property(key = "gameserver.moltenus.update.rule", defaultValue = "0 0 22 ? * *")
	public static String MOLTENUS_UPDATE_RULE;

	@Property(key = "gameserver.moltenus.despawn.rule", defaultValue = "0 0 23 ? * *")
	public static String MOLTENUS_UPDATE_RULE_DESPAWN;

	@Property(key = "gameserver.moltenus.spawn.rule", defaultValue = "1")
	public static int MOLTENUS_SPAWN_RULE;
}
