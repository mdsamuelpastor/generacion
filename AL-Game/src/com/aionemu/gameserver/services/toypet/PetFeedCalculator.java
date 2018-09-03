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
package com.aionemu.gameserver.services.toypet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang.ArrayUtils;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.pet.PetFeedResult;
import com.aionemu.gameserver.model.templates.pet.PetFlavour;
import com.aionemu.gameserver.model.templates.pet.PetRewards;

public final class PetFeedCalculator {

	static byte ITEM_MAX_LEVEL = 60;
	static final byte[] fullCounts;
	static final byte[] itemLevels;
	static final byte[][] pointValues;

	static void calculate() {
		for (byte levelByte : itemLevels) {
			short level = (short) (levelByte & 0xFF);
			if (level >= 10) {
				int countIndex = 0;
				for (byte countByte : fullCounts) {
					short count = (short) (countByte & 0xFF);
					int finalLevel = level;
					if (finalLevel % 5 == 0)
						finalLevel--;
					int pointLevel = itemLevels[(finalLevel / 5)];
					int feedPoints = Math.max(0, pointLevel - 5) / 5 * 8;

					pointValues[(finalLevel / 5)][(countIndex++)] = getPoints(feedPoints, count);
				}
			}
		}
	}

	static byte getPoints(int feedPoints, int maxFeedCount) {
		int progress = 0;
		int points = 0;
		int state = 0;
		int consumed = 0;
		while (consumed < maxFeedCount) {
			int oldProgress = progress;
			if (state == 0 && consumed > maxFeedCount * 0.5F || state == 1 && consumed > maxFeedCount * 0.8F || state == 2 && consumed > maxFeedCount * 1.05D) {
				progress = 0;
				points++;
			}
			else {
				progress += feedPoints;
				while (progress > 255) {
					progress -= 255;
					points++;
				}
			}
			if (progress == 0) {
				state++;
				if (state == 1 && consumed <= 0.487F * maxFeedCount || state == 2 && consumed <= 0.78F * maxFeedCount) {
					progress = oldProgress;
					state--;
				}
			}
			consumed++;
		}

		return (byte) points;
	}

	public static void updatePetFeedProgress(@Nonnull PetFeedProgress progress, int itemLevel, int maxFeedCount) {
		PetHungryLevel currHungryLevel = progress.getHungryLevel();
		if (progress.isLovedFeeded()) {
			if (progress.getLovedFoodRemaining() == 0)
				return;
			progress.setHungryLevel(PetHungryLevel.FULL);
			progress.incrementCount(true);
			return;
		}

		short oldProgress = (short) (progress.getProgress() & 0xFF);

		if (currHungryLevel == PetHungryLevel.HUNGRY && progress.getRegularCount() > maxFeedCount * 0.5F || currHungryLevel == PetHungryLevel.CONTENT && progress.getRegularCount() > maxFeedCount * 0.8F
			|| currHungryLevel == PetHungryLevel.SEMIFULL && progress.getRegularCount() > maxFeedCount * 1.05D) {
			progress.setProgress((short) 0);
			progress.incrementPoints();
		}
		else {
			int finalLevel = itemLevel;
			if (finalLevel % 5 == 0) {
				finalLevel--;
			}
			byte pointLevel = itemLevels[(finalLevel / 5)];
			byte pointsEarned = (byte) (Math.max(0, pointLevel - 5) / 5 * 8);
			short feedProgress = (short) (oldProgress + pointsEarned);
			while (feedProgress > 255) {
				feedProgress = (short) (feedProgress - 255);
				progress.incrementPoints();
			}
			progress.setProgress(feedProgress);
		}

		if (progress.getProgress() == 0) {
			PetHungryLevel nextLevel = progress.getHungryLevel().getNextValue();
			if (nextLevel == PetHungryLevel.CONTENT && progress.getRegularCount() <= 0.487F * maxFeedCount || nextLevel == PetHungryLevel.SEMIFULL && progress.getRegularCount() <= 0.78F * maxFeedCount) {
				progress.setProgress(oldProgress);
			}
			else {
				progress.setHungryLevel(nextLevel);
			}
		}
		progress.incrementCount(false);
	}

	public static PetFeedResult getReward(int fullCount, PetRewards rewardGroup, PetFeedProgress progress, int playerLevel) {
		if (progress.getHungryLevel() != PetHungryLevel.FULL || rewardGroup.getResults().size() == 0) {
			return null;
		}
		int pointsIndex = ArrayUtils.indexOf(fullCounts, (byte) fullCount);
		if (pointsIndex == -1) {
			return null;
		}
		if (progress.isLovedFeeded()) {
			if (rewardGroup.getResults().size() == 1)
				return rewardGroup.getResults().get(0);
			List<PetFeedResult> validRewards = new ArrayList<PetFeedResult>();
			int maxLevel = 0;
			for (PetFeedResult result : rewardGroup.getResults()) {
				int resultLevel = DataManager.ITEM_DATA.getItemTemplate(result.getItem()).getLevel();
				if (resultLevel <= playerLevel) {
					if (resultLevel > maxLevel) {
						maxLevel = resultLevel;
						validRewards.clear();
					}
					validRewards.add(result);
				}
			}
			if (validRewards.size() == 0)
				return null;
			if (validRewards.size() == 1)
				return validRewards.get(0);
			return validRewards.get(Rnd.get(validRewards.size()));
		}

		int rewardIndex = 0;
		int totalRewards = rewardGroup.getResults().size();
		for (int row = 1; row < pointValues.length; row++) {
			byte[] points = pointValues[row];
			if (points[pointsIndex] <= progress.getTotalPoints()) {
				rewardIndex = Math.round(totalRewards / pointValues.length - 1 * row) - 1;
			}

		}

		if (rewardIndex < 0)
			rewardIndex = 0;
		else if (rewardIndex > rewardGroup.getResults().size() - 1) {
			rewardIndex = rewardGroup.getResults().size() - 1;
		}
		return rewardGroup.getResults().get(rewardIndex);
	}

	static {
		HashSet<Byte> counts = new HashSet<Byte>();
		for (PetFlavour flavour : DataManager.PET_FEED_DATA.getPetFlavours()) {
			if (flavour.getFullCount() > 0) {
				counts.add((byte) (flavour.getFullCount() & 0xFF));
			}
		}
		fullCounts = new byte[counts.size()];
		int i = 0;
		Iterator<Byte> countIter = counts.iterator();
		while (countIter.hasNext()) {
			fullCounts[(i++)] = countIter.next().byteValue();
		}
		itemLevels = new byte[ITEM_MAX_LEVEL / 5];
		itemLevels[0] = 5;
		for (int j = 1; j < itemLevels.length; j++) {
			itemLevels[j] = (byte) (itemLevels[(j - 1)] + 5);
		}
		pointValues = new byte[itemLevels.length][fullCounts.length];
		calculate();
	}
}
