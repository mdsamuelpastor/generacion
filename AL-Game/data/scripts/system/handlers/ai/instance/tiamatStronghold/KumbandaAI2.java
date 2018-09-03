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
package ai.instance.tiamatStronghold;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ai.AggressiveNpcAI2;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.instance.handlers.*;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author A7XAtomic!
 */
 
@AIName("kumbanda")
public class KumbandaAI2 extends AggressiveNpcAI2 {

	protected boolean isInstanceDestroyed = false;
	private List<Integer> percents = new ArrayList<Integer>();
	private int stage = 0;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
		stage = 0;
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void checkPercentage(int hpPercentage) {
		int npcId = 0;
		int count = 0;
		if (hpPercentage > 95 && percents.size() < 1) {
			addPercent();
		}
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 85:
							if (stage == 0){
							ReductionTemps();
							Reduction1();
							Reduction2();
							Reduction3();
							Reduction4();
							npcId = 283221;
							count = 2;
							Despawn();
							stage++;
							break;
							}
					case 75:
							if (stage == 1){
							Ghost();
							stage++;
							break;
							}
					case 65:
							if (stage == 2){
							ReductionTemps();
							Reduction1();
							Reduction2();
							Reduction3();
							Reduction4();
							npcId = 283221;
							count = 2;
							Despawn();
							stage++;
							break;
							}
					case 50:
							if (stage == 3){
							ReductionTemps();
							Reduction1();
							Reduction2();
							Reduction3();
							Reduction4();
							npcId = 283221;
							count = 2;
							Despawn();
							Ghost();
							stage++;
							break;
							}
					case 35:
							if (stage == 4){
							ReductionTemps();
							Reduction1();
							Reduction2();
							Reduction3();
							Reduction4();
							npcId = 283221;
							count = 2;
							Despawn();
							stage++;
							break;
							}
					case 25:
							if (stage == 5){
							Ghost();
							stage++;
							break;
							}
					case 15:
							if (stage == 6){
							ReductionTemps();
							Reduction1();
							Reduction2();
							Reduction3();
							Reduction4();
							npcId = 283221;
							count = 2;
							Despawn();
							stage++;
							break;
							}
				}
				percents.remove(percent);
				break;
				}
			}
		randomSpawnHelpers1(npcId, count);
		randomSpawnHelpers2(npcId, count);
		}
		
	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 85, 75, 65, 50, 35, 25, 15});
	}

	private void randomSpawnHelpers1(int npcId, int count) {
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		for (Npc npc : instance.getNpcs(219403)){
		WorldPosition p = npc.getPosition();
		if (instance != null) {
			for (int i = 0; i < count; i++) {
				float x1 = Rnd.get(15);
				float y1 = Rnd.get(15);
				float X = p.getX();
				float Y = p.getY();
				float Z = p.getZ();
				switch (Rnd.get(1, 3)) {
					case 1:
						spawn(npcId, X + x1, Y + y1, Z, (byte) 0);
						break;
					case 2:
						spawn(npcId, X + x1, Y + y1, Z, (byte) 0);
						break;
					case 3:
						spawn(npcId, X + x1, Y + y1, Z, (byte) 0);
						break;
					}
				}
			}
		}
	}

	private void randomSpawnHelpers2(int npcId, int count) {
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		for (Npc npc : instance.getNpcs(219403)){
		WorldPosition p = npc.getPosition();
		if (instance != null) {
			for (int i = 0; i < count; i++) {
				float x1 = Rnd.get(15);
				float y1 = Rnd.get(15);
				float X = p.getX();
				float Y = p.getY();
				float Z = p.getZ();
				switch (Rnd.get(1, 3)) {
					case 1:
						spawn(npcId, X - x1, Y - y1, Z, (byte) 0);
						break;
					case 2:
						spawn(npcId, X - x1, Y - y1, Z, (byte) 0);
						break;
					case 3:
						spawn(npcId, X - x1, Y - y1, Z, (byte) 0);
						break;
					}
				}
			}
		}
	}

		private void Reduction1() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			public void run() {
				ReductionTemps();}
			}, 5000);
	}

		private void Reduction2() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			public void run() {
				ReductionTemps();}
			}, 10000);
	}

		private void Reduction3() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			public void run() {
				ReductionTemps();}
			}, 15000);
	}

	private void Ghost() {
	WorldMapInstance instance = getPosition().getWorldMapInstance();
	for (Npc npc : instance.getNpcs(219403)){
		WorldPosition p = npc.getPosition();
		float X = p.getX();
		float Y = p.getY();
		float Z = p.getZ();
		spawn(799342, X, Y, Z, (byte) 0);
		}
	}
	
		private void Reduction4() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			public void run() {
				ReductionTemps();}
			}, 20000);
	}

	private void ReductionTemps() {
		AI2Actions.useSkill(this, 20728);
	}

	private void Despawn() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			public void run() {
				deleteHelpers();}
			}, 25000);
	}

	private void deleteHelpers() {
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		if (instance != null) {
			deleteNpcs(instance.getNpcs(283221));
		}
	}

	@Override
	protected void handleDespawned() {
		percents.clear();
		super.handleDespawned();
	}

	@Override
	protected void handleDied() {
		percents.clear();
		stage = 0;
		super.handleDied();
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}

	@Override
	protected void handleBackHome() {
		addPercent();
		stage = 0;
		super.handleBackHome();
	}
}
