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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.CleaningConfig;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.model.team.legion.LegionHistory;
import com.aionemu.gameserver.services.LegionService;

/**
 *
 * @author Eloann
 * 
 */
public class LegionHistoryCleaningService {

    private Logger log = LoggerFactory.getLogger(LegionHistoryCleaningService.class);
	
	private final int SECURITY_MINIMUM_PERIOD = 30;

	private static LegionHistoryCleaningService instance = new LegionHistoryCleaningService();
	
	private LegionHistoryCleaningService() {
		if (CleaningConfig.LEGION_HISTORY_CLEANING_ENABLE)
			runCleaning();
	}
	
    private void runCleaning() {
		log.info("LegionHistoryCleaningService: Executing legion history cleaning");
		System.currentTimeMillis();

		int periodInDays = CleaningConfig.LEGION_HISTORY_CLEANING_PERIOD;
		
		if (periodInDays > SECURITY_MINIMUM_PERIOD) {
			runLegionHistoryCleaning();
		}
		else {
			log.warn("The configured days for database cleaning is to low. For security reasons the service will only execute with periods over 30 days!");
		}
	}

    private void runLegionHistoryCleaning() {
        Iterator<Legion> it = LegionService.getInstance().getCachedLegionIterator();
        Legion l = null;
        int counter = 0;
        while (it.hasNext()) {
            l = it.next();
            Collection<LegionHistory> collection = l.getLegionHistory();
            if (collection.size() > 0) {
                List<LegionHistory> history = new ArrayList<LegionHistory>();
                for (LegionHistory h : collection) {
                    boolean allowed = false;
                    switch (h.getLegionHistoryType()) {
                        case KINAH_DEPOSIT:
                        case KINAH_WITHDRAW:
                        case ITEM_DEPOSIT:
                        case ITEM_WITHDRAW:
                            allowed = true;
                            break;
											default:
												break;
                    }
                    if (allowed) {
                        boolean isOutOfDate = ((h.getTime().getTime() - DateTime.now().getMillis()) / 1000) >= 14400 ? true : false; // Every 5 days
                        if (isOutOfDate) {
                            ++counter;
                            history.add(h);
                        }
                    }
                }
                if (history.size() > 0) {
                    collection.removeAll(history);
                }
            }
        }

        if (counter > 0) {
            log.info("Cleaned " + counter + " Legion Histories");
        } else {
            log.info("None Of Legion History is Out of Date.");
        }
    }
	
	public static LegionHistoryCleaningService getInstance() {
		return instance;
	}
}