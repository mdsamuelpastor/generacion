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

import java.util.Calendar;

import com.aionemu.commons.configuration.Property;

public class GSConfig {

	@Property(key = "gameserver.version", defaultValue = "3.5.0.12")
	public static String SERVER_VERSION;

	/**
	 * Server Country Code
	 */
	@Property(key = "gameserver.country.code", defaultValue = "1")
	public static int SERVER_COUNTRY_CODE;

	@Property(key = "gameserver.name", defaultValue = "NextGenCore")
	public static String SERVER_NAME;
	
	@Property(key = "gameserver.players.max.level", defaultValue = "60")
	public static int PLAYER_MAX_LEVEL;
	
	@Property(key = "gameserver.lang", defaultValue = "de")
	public static String LANG;

	/**
	 * Time Zone name (used for event config atm)
	 */
	@Property(key = "gameserver.timezone", defaultValue = "")
	public static String TIME_ZONE_ID = Calendar.getInstance().getTimeZone().getID();

	/**
	 * Enable chat server connection
	 */
	@Property(key = "gameserver.chatserver.enable", defaultValue = "true")
	public static boolean ENABLE_CHAT_SERVER;

	/**
	 * Server MOTD Display revision
	 */
	@Property(key = "gameserver.revisiondisplay.enable", defaultValue = "false")
	public static boolean SERVER_MOTD_DISPLAYREV;

	@Property(key = "gameserver.character.creation.mode", defaultValue = "0")
	public static int CHARACTER_CREATION_MODE;

	/**
	 * Server Mode
	 */
	@Property(key = "gameserver.character.limit.count", defaultValue = "8")
	public static int CHARACTER_LIMIT_COUNT;

	@Property(key = "gameserver.character.faction.limitation.mode", defaultValue = "0")
	public static int CHARACTER_FACTION_LIMITATION_MODE;

	@Property(key = "gameserver.ratio.limitation.enable", defaultValue = "false")
	public static boolean ENABLE_RATIO_LIMITATION;

	@Property(key = "gameserver.ratio.min.value", defaultValue = "60")
	public static int RATIO_MIN_VALUE;

	@Property(key = "gameserver.ratio.min.required.level", defaultValue = "10")
	public static int RATIO_MIN_REQUIRED_LEVEL;

	@Property(key = "gameserver.ratio.min.characters_count", defaultValue = "50")
	public static int RATIO_MIN_CHARACTERS_COUNT;

	@Property(key = "gameserver.ratio.high_player_count.disabling", defaultValue = "500")
	public static int RATIO_HIGH_PLAYER_COUNT_DISABLING;

	@Property(key = "gameserver.abyssranking.small.cache", defaultValue = "false")
	public static boolean ABYSSRANKING_SMALL_CACHE;

	@Property(key = "gameserver.character.reentry.time", defaultValue = "20")
	public static int CHARACTER_REENTRY_TIME;
	
	@Property(key = "gameserver.starting.level", defaultValue = "1")
	public static int STARTING_LEVEL;

	@Property(key = "gameserver.startClass.maxLevel", defaultValue = "10")
	public static int STARTCLASS_MAXLEVEL;
}
