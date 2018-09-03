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
package ai.instance.padmarashkaCave;

import ai.AggressiveNpcAI2;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;


@AIName("padmarashka")
public class PadmarashkaAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (Rnd.get(1, 100) < 3) {
			spawnThunderstormAndEnergy();
			spawnVotaicColumn();
			useRockSlide();
			despawnVotaicColumn();
		}
	}
	
	private void spawnThunderstormAndEnergy() {
		Npc thunderstorm = getPosition().getWorldMapInstance().getNpc(281453);
		Npc energy = getPosition().getWorldMapInstance().getNpc(281459);
		int random = Rnd.get(1, 2);
		if (thunderstorm == null && random == 1) {
			spawn(281453, getOwner().getX() + 15, getOwner().getY() - 15, getOwner().getZ() + 2, (byte) 0);
			spawn(281453, getOwner().getX() + 17, getOwner().getY() - 16, getOwner().getZ() + 2, (byte) 0);
			spawn(281453, getOwner().getX() + 13, getOwner().getY() - 12, getOwner().getZ() + 2, (byte) 0);
		} else if (energy == null) {
			spawn(281459, getOwner().getX() + 13, getOwner().getY() - 13, getOwner().getZ() + 2, (byte) 0);
			spawn(281459, getOwner().getX() + 16, getOwner().getY() - 16, getOwner().getZ() + 2, (byte) 0);
			spawn(281459, getOwner().getX() + 9, getOwner().getY() - 12, getOwner().getZ() + 2, (byte) 0);
		}
	}
	
	private void spawnVotaicColumn() {
		int hp = getOwner().getLifeStats().getHpPercentage();
		Npc votaicColumn = getPosition().getWorldMapInstance().getNpc(282120);
		if (votaicColumn == null && (hp <= 25) || (hp <= 75 && hp >= 60) || (hp <= 85 && hp >= 75)) {
			spawn(282120, getOwner().getX() - 37, getOwner().getY() - 23, getOwner().getZ() + 2, (byte) 0);
			spawn(282120, getOwner().getX() - 8, getOwner().getY() - 33, getOwner().getZ() + 2, (byte) 0);
			spawn(282120, getOwner().getX() - 11, getOwner().getY() + 28, getOwner().getZ() + 2, (byte) 0);
			spawn(282120, getOwner().getX() + 23, getOwner().getY() + 4, getOwner().getZ() + 2, (byte) 0);
			getOwner().getController().onStopMove();
			getOwner().setTarget(votaicColumn);
		}
	}
	
	private void despawnVotaicColumn() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				Npc votaicColumn = getPosition().getWorldMapInstance().getNpc(282120);
				if (votaicColumn != null) {
					votaicColumn.getController().onDelete();
				}
			}
		}, 15000);
	}
	
	private void useRockSlide() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				Npc votaicColumn = getPosition().getWorldMapInstance().getNpc(282120);
				if (votaicColumn != null) {
					getOwner().getController().useSkill(19179);
					votaicColumn.getController().onDespawn();
				}
			}
		}, 5000);
	}
}