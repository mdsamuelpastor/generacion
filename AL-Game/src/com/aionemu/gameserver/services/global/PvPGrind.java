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
package com.aionemu.gameserver.services.global;

import java.util.Calendar;
import java.util.Iterator;
import java.util.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dao.ServerVariablesDAO;
import com.aionemu.gameserver.model.gameobjects.*;
import com.aionemu.gameserver.model.gameobjects.player.*;
import com.aionemu.gameserver.spawnengine.*;
import com.aionemu.gameserver.utils.*;
import com.aionemu.gameserver.world.*;

/**
 * @author Maestross
 */
 
public class PvPGrind {
	
    /**
	 * Just a logger
	 */
	private static final Logger log = LoggerFactory.getLogger(PvPGrind.class);

	/**
	 * SingletonHolder
	 */
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final PvPGrind instance = new PvPGrind();
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

		log.info("Starting PvP Grind Checker: " + CustomConfig.PVP_GRIND_UP_RULE);
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				performUpdate();
			}
		}, CustomConfig.PVP_GRIND_UP_RULE, true);
	}

	/**
	 * Get Service
	 */
	public static final PvPGrind getInstance() {
		return SingletonHolder.instance;
	}
	
	/**
	 * The PvP Grind Service, based on Date/Time/Spawn
	 */
	public void performUpdate() {
		Calendar calendar = Calendar.getInstance();
		if (CustomConfig.PVP_GRIND_ENABLE && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || CustomConfig.PVP_GRIND_ENABLE && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY || CustomConfig.PVP_GRIND_ENABLE && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
		    spawnGrindPhase1();
			log.info("PvP Grind Mobs spawned");
			Iterator<Player> iter = World.getInstance().getPlayersIterator();
			while (iter.hasNext()) {
				PacketSendUtility.sendBrightYellowMessage(iter.next(), "Die PvP Grind Mobs wurden gesichtet!");
			}
		}
		else if (CustomConfig.PVP_GRIND_ENABLE && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY || CustomConfig.PVP_GRIND_ENABLE && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
		    spawnGrindPhase2();
			log.info("PvP Grind Mobs spawned");
			Iterator<Player> iter = World.getInstance().getPlayersIterator();
			while (iter.hasNext()) {
				PacketSendUtility.sendBrightYellowMessage(iter.next(), "Die PvP Grind Mobs wurden gesichtet!");
			}
		}
		else if (CustomConfig.PVP_GRIND_ENABLE && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
		    spawnGrindPhase3();
			log.info("PvP Grind Mobs spawned");
			Iterator<Player> iter = World.getInstance().getPlayersIterator();
			while (iter.hasNext()) {
				PacketSendUtility.sendBrightYellowMessage(iter.next(), "Die PvP Grind Mobs wurden gesichtet!");
			}
		}
		else if (CustomConfig.PVP_GRIND_ENABLE && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
		    spawnGrindPhase4();
			log.info("PvP Grind Mobs spawned");
			Iterator<Player> iter = World.getInstance().getPlayersIterator();
			while (iter.hasNext()) {
				PacketSendUtility.sendBrightYellowMessage(iter.next(), "Die PvP Grind Mobs wurden gesichtet!");
			}
		}
	}
	
	/**
	 * spawn phase 1
	 */
	public void spawnGrindPhase1() {
		    SpawnEngine.addNewSpawn(600040000, 218557, 886.3675f, 560.71173f, 1203.5981f, (byte) 10, 255);
		    SpawnEngine.addNewSpawn(600040000, 218554, 873.6774f, 552.9351f, 1203.5981f, (byte) 10, 255);
		    SpawnEngine.addNewSpawn(600040000, 218556, 876.8906f, 546.64105f, 1203.5981f, (byte) 99, 255);
		    SpawnEngine.addNewSpawn(600040000, 218559, 887.8906f, 552.32166f, 1203.5981f, (byte) 10, 255);
	}
	
	/**
	 * spawn phase 2
	 */
	public void spawnGrindPhase2() {
		    SpawnEngine.addNewSpawn(600040000, 218557, 581.3665f, 957.95764f, 1203.5981f, (byte) 10, 255);
		    SpawnEngine.addNewSpawn(600040000, 218554, 566.97577f, 943.5789f, 1203.5981f, (byte) 10, 255);
		    SpawnEngine.addNewSpawn(600040000, 218556, 575.54016f, 943.5789f, 1203.5981f, (byte) 99, 255);
		    SpawnEngine.addNewSpawn(600040000, 218559, 592.0601f, 957.95764f, 1203.5981f, (byte) 10, 255);
	}
	
	/**
	 * spawn phase 3
	 */
	public void spawnGrindPhase3() {
		    SpawnEngine.addNewSpawn(600040000, 218557, 590.69037f, 604.991f, 1203.5981f, (byte) 10, 255);
		    SpawnEngine.addNewSpawn(600040000, 218554, 597.50134f, 604.991f, 1203.5981f, (byte) 10, 255);
		    SpawnEngine.addNewSpawn(600040000, 218556, 587.25055f, 604.991f, 1203.5981f, (byte) 99, 255);
		    SpawnEngine.addNewSpawn(600040000, 218559, 579.4435f, 598.25684f, 1203.5981f, (byte) 10, 255);
	}
	
	/**
	 * spawn phase 4
	 */
	public void spawnGrindPhase4() {
		    SpawnEngine.addNewSpawn(600040000, 218557, 581.3665f, 957.95764f, 1203.5981f, (byte) 10, 255);
		    SpawnEngine.addNewSpawn(600040000, 218554, 566.97577f, 943.5789f, 1203.5981f, (byte) 10, 255);
		    SpawnEngine.addNewSpawn(600040000, 218556, 575.54016f, 943.5784f, 1203.5981f, (byte) 99, 255);
		    SpawnEngine.addNewSpawn(600040000, 218559, 592.0601f, 957.95764f, 1203.5981f, (byte) 10, 255);
	}
	
	/**
	 * Despawn of npc
	 */
	protected void despawnNpc(Npc npc) {
        if (npc != null) {
            npc.getController().onDelete();
        }
    }
	
	/**
	 * Despawn of npcs
	 */
	protected void despawnNpcs(List<Npc> npcs) {
        for (Npc npc: npcs) {
            npc.getController().onDelete();
        }
    }
}