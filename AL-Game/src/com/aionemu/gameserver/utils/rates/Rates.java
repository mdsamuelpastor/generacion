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
package com.aionemu.gameserver.utils.rates;

/**
 * @author ATracer
 */
public abstract class Rates {

	public abstract float getGroupXpRate();

	public abstract float getXpRate();

	public abstract float getApNpcRate();

	public abstract float getApPlayerGainRate();

	public abstract float getXpPlayerGainRate();

	public abstract float getApPlayerLossRate();

	public abstract float getGatheringXPRate();

	public abstract int getGatheringCountRate();

	public abstract float getCraftingXPRate();

	public abstract float getDropRate();

	public abstract float getQuestXpRate();

	public abstract float getQuestKinahRate();

	public abstract float getQuestApRate();

	public abstract float getDpNpcRate();

	public abstract float getDpPlayerRate();

	public abstract int getCraftCritRate();

	public abstract int getComboCritRate();

	public abstract float getArenaSoloRewardRate();

	public abstract float getArenaFFARewardRate();

	/**
	 * @param membership
	 * @return Rates
	 */
	public static Rates getRatesFor(byte membership) {
		switch (membership) {
			case 0:
				return new RegularRates();
			case 1:
				return new PremiumRates();
			case 2:
				return new VipRates();
			default:
				return new VipRates();
		}
	}
}
