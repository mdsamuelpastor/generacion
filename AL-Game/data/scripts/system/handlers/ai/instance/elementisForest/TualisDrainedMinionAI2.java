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
package ai.instance.elementisForest;

import java.util.concurrent.Future;

import ai.AggressiveNpcAI2;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;

/**
 * @author xTz
 */
@AIName("tualis_drained_minion")
public class TualisDrainedMinionAI2 extends AggressiveNpcAI2 {

	private Future<?> lifeTask;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		startLifeTask();
	}

	private void startLifeTask() {
		lifeTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isAlreadyDead()) {
					AI2Actions.deleteOwner(TualisDrainedMinionAI2.this);
				}
			}

		}, 30000);
	}

	private void cancelTask() {
		if (lifeTask != null && !lifeTask.isDone()) {
			lifeTask.cancel(true);
		}
	}

	@Override
	protected void handleDied() {
		cancelTask();
		super.handleDied();
	}

	@Override
	protected void handleDespawned() {
		cancelTask();
		super.handleDespawned();
	}

}
