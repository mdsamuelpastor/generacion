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
 * @author Rolandas
 */
public class EventsConfig {

	/**
	 * Event Enabled
	 */
	@Property(key = "gameserver.event.enable", defaultValue = "false")
	public static boolean EVENT_ENABLED;

	/**
	 * Event Rewarding Membership
	 */
	@Property(key = "gameserver.event.membership", defaultValue = "0")
	public static int EVENT_REWARD_MEMBERSHIP;

	@Property(key = "gameserver.event.membership.rate", defaultValue = "false")
	public static boolean EVENT_REWARD_MEMBERSHIP_RATE;

	/**
	 * Event Rewarding Period
	 */
	@Property(key = "gameserver.event.period", defaultValue = "60")
	public static int EVENT_PERIOD;

	/**
	 * Event Reward Values
	 */
	@Property(key = "gameserver.event.item.elyos", defaultValue = "141000001")
	public static int EVENT_ITEM_ELYOS;

	@Property(key = "gameserver.event.item.asmo", defaultValue = "141000001")
	public static int EVENT_ITEM_ASMO;

	@Property(key = "gameserver.events.givejuice", defaultValue = "160009017")
	public static int EVENT_GIVEJUICE;

	@Property(key = "gameserver.events.givecake", defaultValue = "160010073")
	public static int EVENT_GIVECAKE;

	@Property(key = "gameserver.event.count", defaultValue = "1")
	public static int EVENT_ITEM_COUNT;

	@Property(key = "gameserver.event.service.enable", defaultValue = "false")
	public static boolean ENABLE_EVENT_SERVICE;
	
	/**
	 * Legendary Raid Spawn Event
	 */
	@Property(key = "gameserver.legendary.raid.enable", defaultValue = "true")
	public static boolean LEGENDARY_RAID_EVENT_ENABLE;
	
	@Property(key = "gameserver.legendary.raid.asmo.hour", defaultValue = "19")
	public static int LEGENDARY_RAID_EVENT_ASMO_HOUR;

	@Property(key = "gameserver.legendary.raid.despawn.asmo.hour", defaultValue = "20")
	public static int LEGENDARY_RAID_EVENT_ASMO_DESPAWN_HOUR;
	
	@Property(key = "gameserver.legendary.raid.elyos.hour", defaultValue = "21")
	public static int LEGENDARY_RAID_EVENT_ELYOS_HOUR;
	
	@Property(key = "gameserver.legendary.raid.despawn.elyos.hour", defaultValue = "22")
	public static int LEGENDARY_RAID_EVENT_ELYOS_DESPAWN_HOUR;

	@Property(key = "gameserver.legendary.raid.asmo.update.rule", defaultValue = "0 0 19 ? * SUN")
	public static String LEGENDARY_RAID_EVENT_UPDATE_RULE_ASMO;

	@Property(key = "gameserver.legendary.raid.asmo.despawn.rule", defaultValue = "0 0 20 ? * SUN")
	public static String LEGENDARY_RAID_EVENT_UPDATE_RULE_DESPAWN_ASMO;
	
	@Property(key = "gameserver.legendary.raid.elyos.update.rule", defaultValue = "0 0 21 ? * SUN")
	public static String LEGENDARY_RAID_EVENT_UPDATE_RULE_ELYOS;
	
	@Property(key = "gameserver.legendary.raid.elyos.despawn.rule", defaultValue = "0 0 22 ? * SUN")
	public static String LEGENDARY_RAID_EVENT_UPDATE_RULE_DESPAWN_ELYOS;

	/*
	 * CrazyDaeva Event
	 */
	@Property(key = "gameserver.crazy.daeva.enable", defaultValue = "false")
	public static boolean ENABLE_CRAZY;
	
	@Property(key = "gameserver.crazy.daeva.tag", defaultValue = "<Crazy>")
	public static String CRAZY_TAG;
	
	@Property(key = "gameserver.crazy.daeva.lowest.rnd", defaultValue = "10")
	public static int CRAZY_LOWEST_RND;

	@Property(key = "gameserver.crazy.daeva.endtime", defaultValue = "300000")
	public static int CRAZY_ENDTIME;
}
