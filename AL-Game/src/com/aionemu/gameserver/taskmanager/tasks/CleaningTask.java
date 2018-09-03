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
package com.aionemu.gameserver.taskmanager.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.templates.tasks.TaskFromDBHandler;

/**
 * @author GoodT
 */
public class CleaningTask extends TaskFromDBHandler {

	private static final Logger log = LoggerFactory.getLogger(CleaningTask.class);	

	@Override
	public String getTaskName() {
		return "cleaning";
	}

	@Override
	public boolean isValid() {
		return params.length == 3;
	}

	@Override
	public void run() {
		log.info("Task[" + id + "] launched : cleaning the server !");
		setLastActivation();
		
		long time = System.currentTimeMillis();
    //log.info("RAM Used (Before): " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576));
    System.gc(); //for cleaning the system
    //log.info("RAM Used (After): " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576));
    System.runFinalization(); //for cleaning the system
    //log.info("RAM Used (Final): " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576));
    //log.info("Garbage Collection and Finalization finished in: " + (System.currentTimeMillis() - time) + " milliseconds...");		
	}
}
