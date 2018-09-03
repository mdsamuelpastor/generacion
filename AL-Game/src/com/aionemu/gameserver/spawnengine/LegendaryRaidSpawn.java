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
package com.aionemu.gameserver.spawnengine;

import java.util.Calendar;
import java.util.Iterator;

import javolution.util.FastMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.dao.ServerVariablesDAO;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
import com.aionemu.gameserver.world.World;

/**
 * @author Eloann
 * Legendary Raid Monster spawn : Each Sunday legendary monsters Ragnarok and Omega will spawn in Beluslan and Heiron! 
 * Ragnarok spawns for Asmodians at 7pm; 
 * Omega spawns for Elyos at 9pm!
 * 
 */
 
public class LegendaryRaidSpawn {

    /**
	 * Just a logger
	 */
	private static final Logger log = LoggerFactory.getLogger(LegendaryRaidSpawn.class);

	/**
	 * SingletonHolder
	 */
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final LegendaryRaidSpawn instance = new LegendaryRaidSpawn();
	}

	/**
	 * Update rule for spawn Asmodians
	 */
	public void scheduleUpdateAsmodians() {
		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		int nextTime = dao.load("abyssRankUpdate");
		if (nextTime < System.currentTimeMillis() / 1000) {
			performUpdate();
		}

		log.info("Starting Legendary Raid Asmodians Spawn Event Checker: " + EventsConfig.LEGENDARY_RAID_EVENT_UPDATE_RULE_ASMO);
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				performUpdate();
			}
		}, EventsConfig.LEGENDARY_RAID_EVENT_UPDATE_RULE_ASMO, true);
	}
	
	/**
	 * Update rule for spawn Elyos
	 */
	public void scheduleUpdateElyos() {
		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		int nextTime = dao.load("abyssRankUpdate");
		if (nextTime < System.currentTimeMillis() / 1000) {
			performUpdate();
		}

		log.info("Starting Legendary Raid Elyos Spawn Event Checker: " + EventsConfig.LEGENDARY_RAID_EVENT_UPDATE_RULE_ELYOS);
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				performUpdate();
			}
		}, EventsConfig.LEGENDARY_RAID_EVENT_UPDATE_RULE_ELYOS, true);
	}

	/**
	 * Update rule for despawn Asmodians
	 */
	public void scheduleUpdateDespawnAsmodians() {
		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		int nextTime = dao.load("abyssRankUpdate");
		if (nextTime < System.currentTimeMillis() / 1000) {
			performDespawn();
		}

		log.info("Starting Legendary Raid Despawn Event Checker: " + EventsConfig.LEGENDARY_RAID_EVENT_UPDATE_RULE_DESPAWN_ASMO);
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				performDespawn();
			}
		}, EventsConfig.LEGENDARY_RAID_EVENT_UPDATE_RULE_DESPAWN_ASMO, true);
	}
	
	/**
	 * Update rule for despawn Elyos
	 */
	public void scheduleUpdateDespawnElyos() {
		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		int nextTime = dao.load("abyssRankUpdate");
		if (nextTime < System.currentTimeMillis() / 1000) {
			performDespawn();
		}

		log.info("Starting Legendary Raid Despawn Event Checker: " + EventsConfig.LEGENDARY_RAID_EVENT_UPDATE_RULE_DESPAWN_ELYOS);
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				performDespawn();
			}
		}, EventsConfig.LEGENDARY_RAID_EVENT_UPDATE_RULE_DESPAWN_ELYOS, true);
	}

	/**
	 * Get Service
	 */
	public static final LegendaryRaidSpawn getInstance() {
		return SingletonHolder.instance;
	}
	
	/**
	 * The main Legendary Raid Spawn system
	 */
	private FastMap<Integer, VisibleObject> Omegaely = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> Ragnarokasmo = new FastMap<Integer, VisibleObject>();

	public void performUpdate() {
		Calendar calendar = Calendar.getInstance();
		Iterator<Player> iter = World.getInstance().getPlayersIterator();

		if (EventsConfig.LEGENDARY_RAID_EVENT_ENABLE) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && calendar.get(Calendar.HOUR_OF_DAY) == EventsConfig.LEGENDARY_RAID_EVENT_ELYOS_HOUR) {
				Omegaely.put(216516, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(210040000, 216516, 1448.9F, 2476F, 123.88F, (byte) 116), 1));
				log.info("Legendary Raid Spawn : Ragnarok was spawned for Elyos at Heiron");
				while (iter.hasNext()) {
					PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), LanguageHandler.translate(CustomMessageId.LEGENDARY_RAID_SPAWNED_ELYOS));
				}
			scheduleUpdateDespawnElyos();
			}
			else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && calendar.get(Calendar.HOUR_OF_DAY) == EventsConfig.LEGENDARY_RAID_EVENT_ASMO_HOUR) {
				Ragnarokasmo.put(216576, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(220040000, 216576, 500F, 2213.13414F, 384.43506F, (byte) 60), 1));
				log.info("Legendary Raid Spawn : Ragnarok was spawned for Asmodians at Beluslan");
				while (iter.hasNext()) {
					PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), LanguageHandler.translate(CustomMessageId.LEGENDARY_RAID_SPAWNED_ASMO));
				}
			scheduleUpdateDespawnAsmodians();
			}
			else if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				log.info("No time for Legendary Raid Spawn Event");
				return;
			}
		}
		else {
			log.info("Legendary Raid Spawn Event is disabled");
			return;
		}
	}
	
	/**
	 * Despawn Service
	 */
	public void performDespawn() {
		Calendar calendar = Calendar.getInstance();
		Iterator<Player> iter = World.getInstance().getPlayersIterator();
		
		if (calendar.get(Calendar.HOUR_OF_DAY) == EventsConfig.LEGENDARY_RAID_EVENT_ELYOS_DESPAWN_HOUR && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			Omegaely.clear();
			log.info("Legendary Raid Spawn : Omega was unspawned.");
			while (iter.hasNext()) {
				PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), LanguageHandler.translate(CustomMessageId.LEGENDARY_RAID_DESPAWNED_ELYOS));
			}
		} else if (calendar.get(Calendar.HOUR_OF_DAY) == EventsConfig.LEGENDARY_RAID_EVENT_ASMO_DESPAWN_HOUR && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			Ragnarokasmo.clear();
			log.info("Legendary Raid Spawn : Ragnarok was unspawned.");
			while (iter.hasNext()) {
				PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), LanguageHandler.translate(CustomMessageId.LEGENDARY_RAID_DESPAWNED_ASMO));
			}
		}
	}
}