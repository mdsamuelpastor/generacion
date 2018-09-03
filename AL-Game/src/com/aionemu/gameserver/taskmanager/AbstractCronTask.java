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
package com.aionemu.gameserver.taskmanager;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronExpression;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.dao.ServerVariablesDAO;
import com.aionemu.gameserver.utils.ThreadPoolManager;

public abstract class AbstractCronTask implements Runnable {

	private static String cronExpressionString;
	private static CronExpression runExpression;
	private static int runTime;
	private static long period;

	public static final int getRunTime() {
		return runTime;
	}

	protected abstract long getRunDelay();

	protected void preInit() {
	}

	protected void postInit() {
	}

	public static final String getCronExpressionString() {
		return cronExpressionString;
	}

	protected abstract String getServerTimeVariable();

	protected void preRun() {
	}

	protected abstract void executeTask();

	protected void postRun() {
	}

	public AbstractCronTask(String cronExpression) throws ParseException {
		if (cronExpression == null) {
			throw new NullPointerException("cronExpressionString");
		}
		cronExpressionString = cronExpression;

		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		runTime = dao.load(getServerTimeVariable());

		preInit();
		runExpression = new CronExpression(cronExpressionString);
		Date nextDate = runExpression.getTimeAfter(new Date());
		Date nextAfterDate = runExpression.getTimeAfter(nextDate);
		period = nextAfterDate.getTime() - nextDate.getTime();
		postInit();

		if (getRunDelay() == 0L) {
			ThreadPoolManager.getInstance().schedule(this, 0L);
		}
		scheduleNextRun();
	}

	private void scheduleNextRun() {
		CronService.getInstance().schedule(this, cronExpressionString, true);
	}

	public long getPeriod() {
		return period;
	}

	@Override
	public final void run() {
		preRun();
		executeTask();

		Date nextDate = runExpression.getTimeAfter(new Date());
		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		runTime = (int) (nextDate.getTime() / 1000L);
		dao.store(getServerTimeVariable(), runTime);

		postRun();
	}
}
