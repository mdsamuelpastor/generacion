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
package com.aionemu.gameserver.spawnengine;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.lambdaj.group.Group;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.walker.WalkerTemplate;

/**
 * Forms the walker groups on initial spawn<br>
 * Brings NPCs back to their positions if they die<br>
 * Cleanup and rework will be made after tests and error handling<br>
 * To use only with patch!
 * 
 * @author vlog
 * @based on Imaginary's imagination
 * @modified Rolandas
 */
public class WalkerFormator {

	private static final Logger log = LoggerFactory.getLogger(WalkerFormator.class);
	private Map<String, List<ClusteredNpc>> groupedSpawnObjects;
	private Map<String, WalkerGroup> walkFormations = new HashMap<String, WalkerGroup>();

	public boolean processClusteredNpc(Npc npc, int instance) {
		SpawnTemplate spawn = npc.getSpawn();
		if (spawn.getWalkerId() != null) {
			if (walkFormations.containsKey(spawn.getWalkerId())) {
				WalkerGroup wg = walkFormations.get(spawn.getWalkerId());
				npc.setWalkerGroup(wg);
				wg.respawn(npc);
				return false;
			}
			WalkerTemplate template = DataManager.WALKER_DATA.getWalkerTemplate(spawn.getWalkerId());
			if (template == null) {
				log.warn("Missing walker ID: " + spawn.getWalkerId());
				return false;
			}
			if (template.getPool() < 2)
				return false;
			ClusteredNpc candidate = new ClusteredNpc(npc, instance, template);
			List<ClusteredNpc> candidateList = null;
			if (groupedSpawnObjects.containsKey(spawn.getWalkerId()))
				candidateList = groupedSpawnObjects.get(spawn.getWalkerId());
			else {
				candidateList = new ArrayList<ClusteredNpc>();
				groupedSpawnObjects.put(spawn.getWalkerId(), candidateList);
			}
			return candidateList.add(candidate);
		}
		return false;
	}

	public void organizeAndSpawn() {
		for (List<ClusteredNpc> candidates : groupedSpawnObjects.values()) {
			Group<ClusteredNpc> bySize = group(candidates, by(on(ClusteredNpc.class).getPositionHash()));
			Set<String> keys = bySize.keySet();
			int maxSize = 0;
			List<ClusteredNpc> npcs = null;
			for (String key : keys) {
				if (bySize.find(key).size() > maxSize) {
					npcs = bySize.find(key);
					maxSize = npcs.size();
				}
			}
			if (maxSize == 1) {
				for (ClusteredNpc snpc : candidates)
					snpc.spawn(snpc.getNpc().getZ());
			}
			else {
				WalkerGroup wg = new WalkerGroup(npcs);
				if (candidates.get(0).getWalkTemplate().getPool() != candidates.size())
					log.warn("Incorrect pool for route: " + candidates.get(0).getWalkTemplate().getRouteId());
				wg.form();
				wg.spawn();
				walkFormations.put(candidates.get(0).getWalkTemplate().getRouteId(), wg);
				for (ClusteredNpc snpc : candidates) {
					if (npcs.contains(snpc))
						continue;
					snpc.spawn(snpc.getNpc().getZ());
				}
			}
		}
		clear();
	}

	private void clear() {
		groupedSpawnObjects.clear();
	}

	private WalkerFormator() {
		groupedSpawnObjects = new HashMap<String, List<ClusteredNpc>>();
	}

	public static final WalkerFormator getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {

		protected static final WalkerFormator instance = new WalkerFormator();
	}
}
