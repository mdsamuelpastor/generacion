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
package ai.worlds.sarpan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ai.AggressiveNpcAI2;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;

/**
 * @author Maestross
 */
@AIName("generalchunapa")
public class GeneralChunapaAI2 extends AggressiveNpcAI2 {

	private List<Integer> percents = new ArrayList<Integer>();

	@Override
	protected void handleSpawned() {
		addPercent();
		super.handleSpawned();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void checkPercentage(int hpPercentage) {
		if (hpPercentage > 80 && percents.size() < 3) {
			addPercent();
		}
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 75:
					    spawnHelpers();
						break;
					case 50:
					    spawnHelpers2();
						break;
					case 25:
						spawnHelpers3();
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 75, 50, 25 });
	}

	private void spawnHelpers() {
		spawn(282535, 2603.547f, 2205.587f, 494.786f, (byte) 0);
		spawn(282535, 2614.323f, 2206.663f, 494.291f, (byte) 0);
		spawn(282535, 2611.737f, 2204.038f, 493.809f, (byte) 0);
		spawn(282535, 2620.361f, 2193.828f, 492.614f, (byte) 0);
	}
	
	private void spawnHelpers2() {
		spawn(282535, 2617.674f, 2201.499f, 493.495f, (byte) 0);
		spawn(282535, 2612.414f, 2206.672f, 494.425f, (byte) 0);
		spawn(282535, 2619.405f, 2197.274f, 493.011f, (byte) 0);
		spawn(282535, 2627.421f, 2207.901f, 495.352f, (byte) 0);
	}
	
	private void spawnHelpers3() {
		spawn(282535, 2610.923f, 2209.002f, 495.289f, (byte) 0);
		spawn(282535, 2622.365f, 2195.461f, 492.831f, (byte) 0);
		spawn(282535, 2627.617f, 2209.122f, 495.488f, (byte) 0);
		spawn(282535, 2599.527f, 2225.664f, 497.349f, (byte) 0);
	}

	@Override
	protected void handleDespawned() {
		percents.clear();
		super.handleDespawned();
	}

	@Override
	protected void handleDied() {
		percents.clear();
		super.handleDied();
	}

	@Override
	protected void handleBackHome() {
		addPercent();
		super.handleBackHome();
	}

}
