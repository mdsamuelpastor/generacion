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
package com.aionemu.gameserver.services;

import java.util.Calendar;
import java.util.Iterator;

import javolution.util.FastMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.dao.ServerVariablesDAO;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * @author Maestros
 * @modified Fennek
 */

public class MoltenusSpawnService {

	/**
	 * Just a logger
	 */
	private static final Logger log = LoggerFactory.getLogger(MoltenusSpawnService.class);

	/**
	 * SingletonHolder
	 */
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final MoltenusSpawnService instance = new MoltenusSpawnService();
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

		log.info("Starting Moltenus Checker: " + SiegeConfig.MOLTENUS_UPDATE_RULE);
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				performUpdate();
			}
		}, SiegeConfig.MOLTENUS_UPDATE_RULE, true);
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

		log.info("Starting Moltenus Despawn Checker: " + SiegeConfig.MOLTENUS_UPDATE_RULE_DESPAWN);
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				performDespawn();
			}
		}, SiegeConfig.MOLTENUS_UPDATE_RULE_DESPAWN, true);
	}

	/**
	 * Get Service
	 */
	public static final MoltenusSpawnService getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * The Moltenus Spawn Service Date Spawn Location Despawn
	 */
	private FastMap<Integer, VisibleObject> moltenusSpawn = new FastMap<Integer, VisibleObject>();

	public void performUpdate() {
		Calendar calendar = Calendar.getInstance();

		if (SiegeConfig.MOLTENUS_ENABLE && SiegeConfig.MOLTENUS_SPAWN_RULE == 1) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && calendar.get(Calendar.HOUR_OF_DAY) == SiegeConfig.MOLTENUS_HOUR) {
				if (calendar.get(Calendar.MONTH) == Calendar.JANUARY || calendar.get(Calendar.MONTH) == Calendar.MARCH) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 2458.4631F, 1584.1635F, 2880.28F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				else if (calendar.get(Calendar.MONTH) == Calendar.FEBRUARY || calendar.get(Calendar.MONTH) == Calendar.APRIL) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 2167.6511F, 2338.7268F, 2880.9075F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				else if (calendar.get(Calendar.MONTH) == Calendar.MAY || calendar.get(Calendar.MONTH) == Calendar.JULY) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 1584.9496F, 1787.6847F, 2882.1572F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				else if (calendar.get(Calendar.MONTH) == Calendar.JUNE || calendar.get(Calendar.MONTH) == Calendar.AUGUST) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 2458.4631F, 1584.1635F, 2880.28F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				else if (calendar.get(Calendar.MONTH) == Calendar.SEPTEMBER || calendar.get(Calendar.MONTH) == Calendar.NOVEMBER) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 2167.6511F, 2338.7268F, 2880.9075F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				else if (calendar.get(Calendar.MONTH) == Calendar.OCTOBER || calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 1584.9496F, 1787.6847F, 2882.1572F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				scheduleUpdateDespawn();
			}
			else if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				log.info("Moltenus will spawn at Sunday");
				return;
			}
		}
		else if (SiegeConfig.MOLTENUS_ENABLE && SiegeConfig.MOLTENUS_SPAWN_RULE == 2) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && calendar.get(Calendar.HOUR_OF_DAY) == SiegeConfig.MOLTENUS_HOUR) {
				if (calendar.get(Calendar.MONTH) == Calendar.JANUARY || calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 1584.9496F, 1787.6847F, 2882.1572F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				else if (calendar.get(Calendar.MONTH) == Calendar.FEBRUARY || calendar.get(Calendar.MONTH) == Calendar.OCTOBER) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 2458.4631F, 1584.1635F, 2880.28F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				else if (calendar.get(Calendar.MONTH) == Calendar.MARCH || calendar.get(Calendar.MONTH) == Calendar.NOVEMBER) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 1584.9496F, 1787.6847F, 2882.1572F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				else if (calendar.get(Calendar.MONTH) == Calendar.APRIL || calendar.get(Calendar.MONTH) == Calendar.SEPTEMBER) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 2167.6511F, 2338.7268F, 2880.9075F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				else if (calendar.get(Calendar.MONTH) == Calendar.MAY || calendar.get(Calendar.MONTH) == Calendar.JULY) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 2458.4631F, 1584.1635F, 2880.28F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				else if (calendar.get(Calendar.MONTH) == Calendar.JUNE || calendar.get(Calendar.MONTH) == Calendar.AUGUST) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 1584.9496F, 1787.6847F, 2882.1572F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				scheduleUpdateDespawn();
			}
			else if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				log.info("Moltenus will spawn at Sunday");
				return;
			}
		}
		else if (SiegeConfig.MOLTENUS_ENABLE && SiegeConfig.MOLTENUS_SPAWN_RULE == 3) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && calendar.get(Calendar.HOUR_OF_DAY) == SiegeConfig.MOLTENUS_HOUR) {
				if (calendar.get(Calendar.MONTH) == Calendar.JANUARY || calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 2167.6511F, 2338.7268F, 2880.9075F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				else if (calendar.get(Calendar.MONTH) == Calendar.FEBRUARY || calendar.get(Calendar.MONTH) == Calendar.NOVEMBER) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 2458.4631F, 1584.1635F, 2880.28F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				else if (calendar.get(Calendar.MONTH) == Calendar.MARCH || calendar.get(Calendar.MONTH) == Calendar.OCTOBER) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 1584.9496F, 1787.6847F, 2882.1572F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				else if (calendar.get(Calendar.MONTH) == Calendar.APRIL || calendar.get(Calendar.MONTH) == Calendar.SEPTEMBER) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 2167.6511F, 2338.7268F, 2880.9075F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				else if (calendar.get(Calendar.MONTH) == Calendar.MAY || calendar.get(Calendar.MONTH) == Calendar.AUGUST) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 2458.4631F, 1584.1635F, 2880.28F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				else if (calendar.get(Calendar.MONTH) == Calendar.JUNE || calendar.get(Calendar.MONTH) == Calendar.JULY) {
					moltenusSpawn.put(251045, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 251045, 2167.6511F, 2338.7268F, 2880.9075F, (byte) 0), 1));
					log.info("Moltenus spawned in the Abyss");
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut wurde im Abyss gesichtet");
					}
				}
				scheduleUpdateDespawn();
			}
			else if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				log.info("Moltenus will spawn at Sunday");
				return;
			}
		}
		else {
			log.info("Moltenus Spawn Service is disabled");
			return;
		}
	}

	/**
	 * Despawn Service
	 */
	public void performDespawn() {
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.HOUR_OF_DAY) == SiegeConfig.MOLTENUS_DESPAWN_HOUR) {
			moltenusSpawn.clear();
			log.info("Moltenus dissapeared");
			Iterator<Player> iter = World.getInstance().getPlayersIterator();
			while (iter.hasNext()) {
				PacketSendUtility.sendBrightYellowMessage(iter.next(), "Menotios: Fragment der Wut ist verschwunden");
			}
		}
	}
}
