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

public class CustomConfig {
	
	/**
	 * Show premium account details on login
	 */
	@Property(key = "gameserver.premium.notify", defaultValue = "false")
	public static boolean PREMIUM_NOTIFY;

	/**
	 * Enable announce when a player succes enchant item 15
	 */
	@Property(key = "gameserver.enchant.announce.enable", defaultValue = "true")
	public static boolean ENABLE_ENCHANT_ANNOUNCE;

	/**
	 * Enable speaking between factions
	 */
	@Property(key = "gameserver.chat.factions.enable", defaultValue = "false")
	public static boolean SPEAKING_BETWEEN_FACTIONS;

	/**
	 * Minimum level to use whisper
	 */
	@Property(key = "gameserver.chat.whisper.level", defaultValue = "10")
	public static int LEVEL_TO_WHISPER;

	/**
	 * Factions search mode
	 */
	@Property(key = "gameserver.search.factions.mode", defaultValue = "false")
	public static boolean FACTIONS_SEARCH_MODE;

	/**
	 * list gm when search players
	 */
	@Property(key = "gameserver.search.gm.list", defaultValue = "false")
	public static boolean SEARCH_GM_LIST;

	/**
	 * Minimum level to use search
	 */
	@Property(key = "gameserver.search.player.level", defaultValue = "10")
	public static int LEVEL_TO_SEARCH;

	/**
	 * Allow opposite factions to bind in enemy territories
	 */
	@Property(key = "gameserver.cross.faction.binding", defaultValue = "false")
	public static boolean ENABLE_CROSS_FACTION_BINDING;

	/**
	 * Enable second class change without quest
	 */
	@Property(key = "gameserver.simple.secondclass.enable", defaultValue = "false")
	public static boolean ENABLE_SIMPLE_2NDCLASS;

	/**
	 * Disable chain trigger rate (chain skill with 100% success)
	 */
	@Property(key = "gameserver.skill.chain.triggerrate", defaultValue = "true")
	public static boolean SKILL_CHAIN_TRIGGERRATE;

	/**
	 * Unstuck delay
	 */
	@Property(key = "gameserver.unstuck.delay", defaultValue = "3600")
	public static int UNSTUCK_DELAY;

	/**
	 * The price for using dye command
	 */
	@Property(key = "gameserver.admin.dye.price", defaultValue = "1000000")
	public static int DYE_PRICE;

	/**
	 * Base Fly Time
	 */
	@Property(key = "gameserver.base.flytime", defaultValue = "60")
	public static int BASE_FLYTIME;

	/**
	 * Disable prevention using old names with coupon & command
	 */
	@Property(key = "gameserver.oldnames.coupon.disable", defaultValue = "false")
	public static boolean OLD_NAMES_COUPON_DISABLED;
	@Property(key = "gameserver.oldnames.command.disable", defaultValue = "true")
	public static boolean OLD_NAMES_COMMAND_DISABLED;

	/**
	 * Friendlist size
	 */
	@Property(key = "gameserver.friendlist.size", defaultValue = "90")
	public static int FRIENDLIST_SIZE;

	/**
	 * Basic Quest limit size
	 */
	@Property(key = "gameserver.basic.questsize.limit", defaultValue = "40")
	public static int BASIC_QUEST_SIZE_LIMIT;

	/**
	 * Basic Quest limit size
	 */
	@Property(key = "gameserver.basic.cubesize.limit", defaultValue = "9")
	public static int BASIC_CUBE_SIZE_LIMIT;

	/**
	 * Npc Cube Expands limit size
	 */
	@Property(key = "gameserver.npcexpands.limit", defaultValue = "5")
	public static int NPC_CUBE_EXPANDS_SIZE_LIMIT;

	/**
	 * Enable instances
	 */
	@Property(key = "gameserver.instances.enable", defaultValue = "true")
	public static boolean ENABLE_INSTANCES;

	/**
	 * Enable instances mob always aggro player ignore level
	 */
	@Property(key = "gameserver.instances.mob.aggro", defaultValue = "300080000,300090000,300060000")
	public static String INSTANCES_MOB_AGGRO;

	/**
	 * Enable instances cooldown filtring
	 */
	@Property(key = "gameserver.instances.cooldown.filter", defaultValue = "0")
	public static String INSTANCES_COOL_DOWN_FILTER;

	/**
	 * Instances formula
	 */
	@Property(key = "gameserver.instances.cooldown.rate", defaultValue = "1")
	public static int INSTANCES_RATE;

	/**
	 * Enable Kinah cap
	 */
	@Property(key = "gameserver.enable.kinah.cap", defaultValue = "false")
	public static boolean ENABLE_KINAH_CAP;

	/**
	 * Kinah cap value
	 */
	@Property(key = "gameserver.kinah.cap.value", defaultValue = "999999999")
	public static long KINAH_CAP_VALUE;

	/**
	 * Enable AP cap
	 */
	@Property(key = "gameserver.enable.ap.cap", defaultValue = "false")
	public static boolean ENABLE_AP_CAP;

	/**
	 * AP cap value
	 */
	@Property(key = "gameserver.ap.cap.value", defaultValue = "1000000")
	public static long AP_CAP_VALUE;

	@Property(key = "gameserver.enable.exp.cap", defaultValue = "false")
	public static boolean ENABLE_EXP_CAP;

	@Property(key = "gameserver.exp.cap.value", defaultValue = "48000000")
	public static long EXP_CAP_VALUE;
	
	/**
	 * MB cap value
	 */
	@Property(key = "gameserver.mb.cap.value", defaultValue = "3600")
	public static int MB_CAP_VALUE;
	
	/**
	 * Enable no AP in mentored group.
	 */
	@Property(key = "gameserver.noap.mentor.group", defaultValue = "false")
	public static boolean MENTOR_GROUP_AP;

	/**
	 * .faction cfg
	 */
	@Property(key = "gameserver.faction.free", defaultValue = "true")
	public static boolean FACTION_FREE_USE;

	@Property(key = "gameserver.faction.prices", defaultValue = "10000")
	public static int FACTION_USE_PRICE;

	@Property(key = "gameserver.faction.cmdchannel", defaultValue = "true")
	public static boolean FACTION_CMD_CHANNEL;

	/**
	 * /** Show dialog id and quest id
	 */
	@Property(key = "gameserver.dialog.showid", defaultValue = "true")
	public static boolean ENABLE_SHOW_DIALOGID;

	/**
	 * Custom RiftLevels for Heiron and Beluslan
	 */
	@Property(key = "gameserver.rift.heiron_fm", defaultValue = "61")
	public static int HEIRON_FM;
	@Property(key = "gameserver.rift.heiron_gm", defaultValue = "61")
	public static int HEIRON_GM;
	@Property(key = "gameserver.rift.beluslan_fm", defaultValue = "61")
	public static int BELUSLAN_FM;
	@Property(key = "gameserver.rift.beluslan_gm", defaultValue = "61")
	public static int BELUSLAN_GM;
	
	@Property(key = "gameserver.rift.inggison", defaultValue = "61")
	public static int INGGISON_RIFT;
	@Property(key = "gameserver.rift.gelkmaros", defaultValue = "61")
	public static int GELKMAROS_RIFT;

	@Property(key = "gameserver.reward.service.enable", defaultValue = "false")
	public static boolean ENABLE_REWARD_SERVICE;

	/**
	 * Limits Config
	 */
	@Property(key = "gameserver.limits.enable", defaultValue = "true")
	public static boolean LIMITS_ENABLED;

	@Property(key = "gameserver.limits.update", defaultValue = "0 0 0 * * ?")
	public static String LIMITS_UPDATE;

	@Property(key = "gameserver.limits.rate", defaultValue = "1")
	public static int LIMITS_RATE;

	@Property(key = "gameserver.chat.text.length", defaultValue = "150")
	public static int MAX_CHAT_TEXT_LENGHT;

	@Property(key = "gameserver.abyssxform.afterlogout", defaultValue = "false")
	public static boolean ABYSSXFORM_LOGOUT;

	@Property(key = "gameserver.instance.duel.enable", defaultValue = "true")
	public static boolean INSTANCE_DUEL_ENABLE;

	@Property(key = "gameserver.ride.restriction.enable", defaultValue = "true")
	public static boolean ENABLE_RIDE_RESTRICTION;

	/**
	 * Player Level Reward Service
	 */
	@Property(key = "gameserver.reward.enable", defaultValue = "false")
	public static boolean REWARD_ENABLE;

	/**
	 * Announce Service
	 */
	@Property(key = "gameserver.announce.gm.connection", defaultValue = "false")
	public static boolean ANNOUNCE_GM_CONNECTION;
	
	/**
	 * Custom BossSpawnService
	 */
	@Property(key = "gameserver.custombosses.enable", defaultValue = "true")
	public static boolean CUSTOMBOSSES_ENABLE;
	
	@Property(key = "gameserver.custombosses.delay", defaultValue = "60")
    public static int CUSTOMBOSSES_DELAY;
	
	/**
	 * Battleground System
	 */
	@Property(key = "gameserver.battleground.enable", defaultValue = "false")
	public static boolean BATTLEGROUNDS_ENABLED;
	
	/**
	 * FFA System
	 */
	@Property(key = "gameserver.ffa.enable", defaultValue = "false")
	public static boolean FFA_ENABLED;
	
	/**
	 * Dimensional Vortex
	 */
	@Property(key = "gameserver.dimensional.vortex.enable", defaultValue = "true")
	public static boolean DIM_VORTEX_ENABLE;
	
	@Property(key = "gameserver.dimensional.vortex.hour", defaultValue = "22")
	public static int DIM_VORTEX_HOUR;

	@Property(key = "gameserver.dimensional.vortex.despawn.hour", defaultValue = "00")
	public static int DIM_VORTEX_DESPAWN_HOUR;

	@Property(key = "gameserver.dimensional.vortex.rule", defaultValue = "0 0 22 ? * *")
	public static String DIM_VORTEX_UPDATE_RULE;

	@Property(key = "gameserver.dimensional.vortex.despawn.rule", defaultValue = "0 0 00 ? * *")
	public static String DIM_VORTEX_UPDATE_RULE_DESPAWN;
	
	/**
	 * Duel Reward System
	 */
	@Property(key = "gameserver.duelreward.enable", defaultValue = "true")
    public static boolean DUEL_REWARD_ENABLE;
	
    @Property(key = "gameserver.duelreward.winner.item", defaultValue = "186000147")
    public static int DUEL_REWARD_WINNER_ITEM;
	
	@Property(key = "gameserver.duelreward.loser.item", defaultValue = "186000096")
	public static int DUEL_REWARD_LOSER_ITEM;
	
    @Property(key = "gameserver.duelreward.count", defaultValue = "2")
    public static int DUEL_REWARD_COUNT;
	
	/**
	 * RandomSpawn Manager
	 */
	@Property(key = "gameserver.randomspawn.time", defaultValue = "0:00:00")
	public static String RANDOM_RESPAWN_TIME;
 
	@Property(key = "gameserver.randomspawn.delay", defaultValue = "3")
	public static int RANDOM_RESPAWN_DELAY;
	
	/**
	 * PvPGrind System
	 */
	@Property(key = "gameserver.pvpgrind.update", defaultValue = "0 0 0 ? * *")
	public static String PVP_GRIND_UP_RULE;
	
	@Property(key = "gameserver.pvpgrind.enable", defaultValue = "true")
	public static boolean PVP_GRIND_ENABLE;
	
	/**
	 * World Channel
	 */
	@Property(key = "gameserver.worldchannel.costs", defaultValue = "5000")
	public static int WORLD_CHANNEL_AP_COSTS;
	
	/**
	 * Invasion Rift
	 */
	@Property(key = "gameserver.invasion.rift.enable", defaultValue = "false")
	public static boolean INVASION_RIFT_ENABLE;
	
	@Property(key = "gameserver.invasion.rift.elyos", defaultValue = "80")
	public static int MAX_INFLUENCE_ELYOS;
	
	@Property(key = "gameserver.invasion.rift.asmo", defaultValue = "80")
	public static int MAX_INFLUENCE_ASMO;
}