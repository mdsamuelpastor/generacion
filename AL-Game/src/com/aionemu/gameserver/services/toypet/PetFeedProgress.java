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

public final class PetFeedProgress {

	private byte totalPoints = 0;
	private byte feedProgress = 0;
	private byte consumed = 0;
	private PetHungryLevel hungryLevel = PetHungryLevel.HUNGRY;
	private byte lovedFoodMax = 0;
	private short lovedFoodCount = 0;
	private boolean lovedFeeded = false;

	public PetFeedProgress(byte lovedFoodLimit) {
		adjustLimits(lovedFoodLimit);
	}

	public short getTotalPoints() {
		return totalPoints;
	}

	public void incrementPoints() {
		totalPoints = (byte) (totalPoints + 1);
	}

	public void setTotalPoints(int points) {
		totalPoints = (byte) points;
	}

	public byte getProgress() {
		return feedProgress;
	}

	public void setProgress(short feedPoints) {
		feedProgress = (byte) (feedPoints & 0xFF);
	}

	public PetHungryLevel getHungryLevel() {
		return hungryLevel;
	}

	public void setHungryLevel(PetHungryLevel level) {
		hungryLevel = level;
	}

	public int getRegularCount() {
		return consumed & 0xFF;
	}

	public void setRegularCount(byte count) {
		consumed = count;
	}

	public short getLovedFoodRemaining() {
		byte disabled = (byte) (lovedFoodCount >> 8 & 0xFF);
		if (disabled == -1)
			return 0;
		short eaten = (short) (lovedFoodCount >> 4 & 0xF);
		return (short) (lovedFoodMax - eaten);
	}

	public boolean isLovedFeeded() {
		return lovedFeeded;
	}

	public void setIsLovedFeeded() {
		lovedFeeded = true;
	}

	private void adjustLimits(byte topLimit) {
		if ((topLimit <= 40) && (topLimit > 24)) {
			lovedFoodMax = 40;
			lovedFoodCount = 3840;
		}
		else if ((topLimit <= 24) && (topLimit > 8)) {
			lovedFoodMax = 24;
			lovedFoodCount = 3856;
		}
		else if (topLimit > 0) {
			lovedFoodMax = 8;
			lovedFoodCount = 3872;
		}
		else {
			lovedFoodMax = 0;
			lovedFoodCount = 4080;
		}
		lovedFoodCount = (short) (lovedFoodCount | lovedFoodMax - topLimit);
		lovedFoodCount = (short) (lovedFoodCount << 4);
	}

	public void incrementCount(boolean lovedFood) {
		if (lovedFood)
			adjustLimits((byte) (getLovedFoodRemaining() - 1));
		else
			consumed = (byte) (consumed + 1);
	}

	public void reset() {
		if (lovedFeeded) {
			lovedFeeded = false;
		}
		else {
			totalPoints = 0;
			feedProgress = 0;
			consumed = 0;
		}
	}

	public short getLovedFoodLimit() {
		return lovedFoodCount;
	}

	public int getDataForPacket() {
		int value = getRegularCount() & 0xFF;
		value <<= 8;
		value |= getProgress() & 0xFF;
		value <<= 16;
		value |= lovedFoodCount & 0xFFFF;
		return value;
	}

	public void setData(int savedData) {
		byte limitByte = (byte) (savedData >> 8 & 0xFF);
		if (limitByte == -1) {
			lovedFoodMax = 0;
			lovedFoodCount = -256;
		}
		else if (limitByte != 0) {
			lovedFoodCount = (short) (savedData & 0xFFFF);
			switch (limitByte) {
				case -16:
					lovedFoodMax = 40;
					break;
				case -15:
					lovedFoodMax = 24;
					break;
				case -14:
					lovedFoodMax = 8;
			}

		}

		savedData >>= 16;
		setProgress((byte) (savedData & 0xFF));
		savedData >>= 8;
		setRegularCount((byte) (savedData & 0xFF));
	}
}
