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
package com.aionemu.gameserver.services.craft;

import java.util.Calendar;
import java.util.Iterator;

import javolution.util.FastMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.CraftConfig;
import com.aionemu.gameserver.dao.ServerVariablesDAO;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * @author Maestros
 */

public class AdvCraftSpawn {

	/**
	 * Just a logger
	 */
	private static final Logger log = LoggerFactory.getLogger(AdvCraftSpawn.class);

	/**
	 * SingletonHolder
	 */
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final AdvCraftSpawn instance = new AdvCraftSpawn();
	}

	/**
	 * Update rule for spawn
	 */
	public void scheduleUpdate() {
		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		int nextTime = dao.load("abyssRankUpdate");
		if (nextTime < System.currentTimeMillis() / 1000) {
			performUpdate();
		}

		log.info("Starting Craft NPC Event: " + CraftConfig.CRAFT_NPC_UPDATE_RULE);
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				performUpdate();
			}
		}, CraftConfig.CRAFT_NPC_UPDATE_RULE, true);
	}

	/**
	 * Update rule for despawn
	 */
	public void scheduleUpdateDespawn() {
		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		int nextTime = dao.load("abyssRankUpdate");
		if (nextTime < System.currentTimeMillis() / 1000) {
			performDespawn();
		}

		log.info("Starting Craft NPC Event Despawn: " + CraftConfig.CRAFT_NPC_DESPAWN_RULE);
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				performDespawn();
			}
		}, CraftConfig.CRAFT_NPC_DESPAWN_RULE, true);
	}

	/**
	 * Get Service
	 */
	public static final AdvCraftSpawn getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * Advanced Craft NPC Spawn Service Date Spawn Location Despawn
	 */
	private FastMap<Integer, VisibleObject> alchemySpawn = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> wsmithSpawn = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> asmithSpawn = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> cookSpawn = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> FunitureSpawn = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> handiworkSpawn = new FastMap<Integer, VisibleObject>();

	private FastMap<Integer, VisibleObject> alchemySpawn2 = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> wsmithSpawn2 = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> asmithSpawn2 = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> cookSpawn2 = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> FunitureSpawn2 = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> handiworkSpawn2 = new FastMap<Integer, VisibleObject>();

	public void performUpdate() {
		Calendar calendar = Calendar.getInstance();

		if (CraftConfig.CRAFT_EVENT_ENABLE) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
				if (calendar.get(Calendar.HOUR_OF_DAY) == 15) {
					alchemySpawn.put(203304, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(110010000, 203304, 1843.3129F, 1528.8201F, 590.15826F, (byte) 0), 1));
					wsmithSpawn.put(203305, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(110010000, 203305, 1845.3689F, 1581.1968F, 590.10864F, (byte) 0), 1));
					asmithSpawn.put(203306, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(110010000, 203306, 1887.7933F, 1555.9802F, 590.10864F, (byte) 0), 1));
					cookSpawn.put(203307, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(110010000, 203307, 1896.1805F, 1460.6974F, 590.0707F, (byte) 0), 1));
					FunitureSpawn.put(203308, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(110010000, 203308, 1837.5762F, 1446.2203F, 590.10864F, (byte) 0), 1));
					handiworkSpawn.put(203309, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(110010000, 203309, 1890.0148F, 1529.0122F, 590.1345F, (byte) 0), 1));

					alchemySpawn2.put(203310, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(120010000, 203310, 1160.826F, 1523.4554F, 214.16386F, (byte) 0), 1));
					wsmithSpawn2.put(203311, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(120010000, 203311, 1184.7854F, 1594.3524F, 214.13577F, (byte) 0), 1));
					asmithSpawn2.put(203312, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(120010000, 203312, 1183.2709F, 1593.7777F, 214.13577F, (byte) 0), 1));
					cookSpawn2.put(203313, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(120010000, 203313, 1252.5864F, 1554.5419F, 214.13104F, (byte) 0), 1));
					FunitureSpawn2.put(203314, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(120010000, 203314, 1172.6512F, 1495.3778F, 214.25528F, (byte) 0), 1));
					handiworkSpawn2.put(203315, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(120010000, 203315, 1186.0135F, 1492.5901F, 215.10434F, (byte) 0), 1));

					log.info("Craft Event started");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Das Crafting Event ist gestartet");
					}
				}
				scheduleUpdateDespawn();
			}
			else if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
				log.info("Craft Event will start at Sunday, Wednesday or Friday");
				return;
			}
		}
		else {
			log.info("Craft NPC Event is disabled");
			return;
		}
	}

	/**
	 * Despawn Service
	 */
	public void performDespawn() {
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.HOUR_OF_DAY) == 18) {
			alchemySpawn.clear();
			wsmithSpawn.clear();
			asmithSpawn.clear();
			cookSpawn.clear();
			FunitureSpawn.clear();
			handiworkSpawn.clear();

			alchemySpawn2.clear();
			wsmithSpawn2.clear();
			asmithSpawn2.clear();
			cookSpawn2.clear();
			FunitureSpawn2.clear();
			handiworkSpawn2.clear();
			log.info("Craft NPC Event ended");
			Iterator<Player> iter = World.getInstance().getPlayersIterator();
			while (iter.hasNext()) {
				PacketSendUtility.sendBrightYellowMessage(iter.next(), "Das Crafting Event ist nun vorbei");
			}
		}
	}
}
