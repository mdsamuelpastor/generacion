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
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dao.ServerVariablesDAO;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
import com.aionemu.gameserver.world.World;

/**
 * @author Maestross, Eloann
 * @rework for 3.7/3.9 Eloann
 */
 
public class DimensionalVortex {

    /**
	 * Just a logger
	 */
	private static final Logger log = LoggerFactory.getLogger(DimensionalVortex.class);

	/**
	 * SingletonHolder
	 */
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final DimensionalVortex instance = new DimensionalVortex();
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

		log.info("Starting Dimensional Vortex Checker: " + CustomConfig.DIM_VORTEX_UPDATE_RULE);
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				performUpdate();
			}
		}, CustomConfig.DIM_VORTEX_UPDATE_RULE, true);
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

		log.info("Starting Dimensional Vortex Despawn Checker: " + CustomConfig.DIM_VORTEX_UPDATE_RULE_DESPAWN);
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				performDespawn();
			}
		}, CustomConfig.DIM_VORTEX_UPDATE_RULE_DESPAWN, true);
	}

	/**
	 * Get Service
	 */
	public static final DimensionalVortex getInstance() {
		return SingletonHolder.instance;
	}
	
	/**
	 * The main Dimensional Vortex spawn system
	 */
	private FastMap<Integer, VisibleObject> vortexspawninely = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> vortexspawninasmo = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> vortexspawnoutely = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> vortexspawnoutasmo = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> brusthoninriftgenerator = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> theobomosriftgenerator = new FastMap<Integer, VisibleObject>();

	public void performUpdate() {
		Calendar calendar = Calendar.getInstance();
		Iterator<Player> iter = World.getInstance().getPlayersIterator();

		if (CustomConfig.DIM_VORTEX_ENABLE) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && calendar.get(Calendar.HOUR_OF_DAY) == CustomConfig.DIM_VORTEX_HOUR) {
				vortexspawninely.put(831073, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(110070000, 831073, 451.7139F, 232.88655F, 126.97164F, (byte) 30), 1));
				vortexspawnoutely.put(831074, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(220050000, 831074, 1696.4329F, 2901.0894F, 65.90677F, (byte) 116), 1));
				brusthoninriftgenerator.put(209486, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(220050000, 209486, 1696.4329F, 2901.0894F, 65.90677F, (byte) 116), 1));
				log.info("Dimensional Vortex was spawned for Elyos (Saturday)");
				while (iter.hasNext()) {
					PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), LanguageHandler.translate(CustomMessageId.DIM_VORTEX_SPAWNED_ELYOS));
				}
			scheduleUpdateDespawn();
			}
			else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && calendar.get(Calendar.HOUR_OF_DAY) == CustomConfig.DIM_VORTEX_HOUR) {
				vortexspawninasmo.put(831073, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(120080000, 831073, 562.88324F, 206.13414F, 93.479576F, (byte) 60), 1));
				vortexspawnoutasmo.put(831074, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(210060000, 831074, 952.89124F, 2424.5557F, 105.43102F, (byte) 63), 1));
				theobomosriftgenerator.put(209487, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(210060000, 209487, 952.89124F, 2424.5557F, 105.43102F, (byte) 63), 1));
				log.info("Dimensional Vortex was spawned for Asmodians (Sunday)");
				while (iter.hasNext()) {
					PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), LanguageHandler.translate(CustomMessageId.DIM_VORTEX_SPAWNED_ASMO));
				}
			scheduleUpdateDespawn();
			}
			else if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				log.info("No time for AsmodisDimensional Vortex");
				return;
			}
		}
		else {
			log.info("Dimensional Vortex is disabled");
			return;
		}
	}
	
	/**
	 * Despawn Service
	 */
	public void performDespawn() {
		Calendar calendar = Calendar.getInstance();
		Iterator<Player> iter = World.getInstance().getPlayersIterator();
		
		if (calendar.get(Calendar.HOUR_OF_DAY) == CustomConfig.DIM_VORTEX_DESPAWN_HOUR && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			vortexspawninely.clear();
			vortexspawnoutely.clear();
			brusthoninriftgenerator.clear();
			log.info("Dimensional Vortex for elyos dissapeared");
			while (iter.hasNext()) {
				PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), LanguageHandler.translate(CustomMessageId.DIM_VORTEX_DESPAWNED));
			}
		} else if (calendar.get(Calendar.HOUR_OF_DAY) == CustomConfig.DIM_VORTEX_DESPAWN_HOUR && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			vortexspawninasmo.clear();
			vortexspawnoutasmo.clear();
			theobomosriftgenerator.clear();
			log.info("Dimensional Vortex for asmodians dissapeared");
			while (iter.hasNext()) {
				PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), LanguageHandler.translate(CustomMessageId.DIM_VORTEX_DESPAWNED));
			}
		}
	}
}