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
package com.aionemu.gameserver.model.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.instancereward.PvPArenaReward;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.templates.instance_bonusatrr.InstanceBonusAttr;
import com.aionemu.gameserver.model.templates.instance_bonusatrr.InstancePenaltyAttr;
import com.aionemu.gameserver.skillengine.change.Func;
import com.aionemu.gameserver.utils.ThreadPoolManager;

public class InstanceBuff implements StatOwner {

	private Future<?> task;
	private List<IStatFunction> functions = new ArrayList<IStatFunction>();
	private InstanceBonusAttr instanceBonusAttr;

	public InstanceBuff(int buffId) {
		instanceBonusAttr = DataManager.INSTANCE_BUFF_DATA.getInstanceBonusattr(buffId);
	}

	public void applyEffect(Player player, int time) {
		if (hasInstanceBuff() || instanceBonusAttr == null) {
			return;
		}
		if (time != 0) {
			task = ThreadPoolManager.getInstance().schedule(new InstanceBuffTask(player), time);
		}
		for (InstancePenaltyAttr instancePenaltyAttr : instanceBonusAttr.getPenaltyAttr()) {
			StatEnum stat = instancePenaltyAttr.getStat();
			int statToModified = player.getGameStats().getStat(stat, 0).getBase();
			int value = instancePenaltyAttr.getValue();
			int valueModified = instancePenaltyAttr.getFunc().equals(Func.PERCENT) ? statToModified * value / 100 : value;
			functions.add(new StatAddFunction(stat, valueModified, true));
		}
		player.getGameStats().addEffect(this, functions);
	}

	public void endEffect(Player player) {
		functions.clear();
		if (hasInstanceBuff()) {
			task.cancel(true);
		}
		player.getGameStats().endEffect(this);
		sendPacket(player);
	}

	private void sendPacket(Player player) {
		if (player.isOnline()) {
			InstanceReward<?> instanceReward = player.getPosition().getWorldMapInstance().getInstanceHandler().getInstanceReward();
			if (instanceReward != null && (instanceReward instanceof PvPArenaReward))
				((PvPArenaReward) instanceReward).sendPacket();
		}
	}

	public boolean hasInstanceBuff() {
		return task != null && !task.isDone();
	}

	private class InstanceBuffTask implements Runnable {

		private Player player;

		public InstanceBuffTask(Player player) {
			this.player = player;
		}

		@Override
		public void run() {
			endEffect(player);
		}
	}
}
