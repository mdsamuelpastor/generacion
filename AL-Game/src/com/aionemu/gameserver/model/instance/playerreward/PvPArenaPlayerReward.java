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
package com.aionemu.gameserver.model.instance.playerreward;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceBuff;

/**
 * @author xTz
 */
public class PvPArenaPlayerReward extends InstancePlayerReward {

	private int position;
	private int timeBonus;
	private float timeBonusModifier;
	private int abyssPoints;
	private int crucibleInsignia;
	private int courageInsignia;
	private long logoutTime;
	private boolean isRewarded = false;
	private InstanceBuff boostMorale = new InstanceBuff(8);

	public PvPArenaPlayerReward(Player player, int timeBonus) {
		super(player);
		super.addPoints(13000);
		this.timeBonus = timeBonus;
		timeBonusModifier = (this.timeBonus / 660000f);
		boostMorale.applyEffect(player, 20000);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getTimeBonus() {
		return timeBonus > 0 ? timeBonus : 0;
	}

	public void updateLogOutTime() {
		logoutTime = System.currentTimeMillis();
	}

	public void updateBonusTime() {
		int offlineTime = (int) (System.currentTimeMillis() - logoutTime);
		timeBonus = (int) (timeBonus - offlineTime * timeBonusModifier);
	}

	public boolean isRewarded() {
		return isRewarded;
	}

	public void setRewarded() {
		isRewarded = true;
	}

	public int getAbyssPoints() {
		return abyssPoints;
	}

	public void setAbyssPoints(int abyssPoints) {
		this.abyssPoints = abyssPoints;
	}

	public int getCrucibleInsignia() {
		return crucibleInsignia;
	}

	public void setCrucibleInsignia(int crucibleInsignia) {
		this.crucibleInsignia = crucibleInsignia;
	}

	public int getCourageInsignia() {
		return courageInsignia;
	}

	public void setCourageInsignia(int courageInsignia) {
		this.courageInsignia = courageInsignia;
	}

	public boolean hasBoostMorale() {
		return boostMorale.hasInstanceBuff();
	}

	public void applyBoostMoraleEffect() {
		boostMorale.applyEffect(player, 20000);
	}

	public void endBoostMoraleEffect() {
		boostMorale.endEffect(player);
	}
}
