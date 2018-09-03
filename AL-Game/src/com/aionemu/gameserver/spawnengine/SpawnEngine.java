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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.configs.administration.DeveloperConfig;
import com.aionemu.gameserver.controllers.*;
import com.aionemu.gameserver.controllers.effect.EffectController;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.*;
import com.aionemu.gameserver.model.siege.SiegeModType;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.siegespawns.SiegeSpawnTemplate;
import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.knownlist.NpcKnownList;

/**
 * This class is responsible for NPCs spawn management. Current implementation is temporal and will be replaced in the
 * future.
 * 
 * @author Luno modified by ATracer, Source, Wakizashi, xTz, nrg, Maestros
 */
public class SpawnEngine {

	private static Logger log = LoggerFactory.getLogger(SpawnEngine.class);

	/**
	 * Creates VisibleObject instance and spawns it using given {@link SpawnTemplate} instance.
	 * 
	 * @param spawn
	 * @return created and spawned VisibleObject
	 */
	public static VisibleObject spawnObject(SpawnTemplate spawn, int instanceIndex) {
		final VisibleObject visObj = getSpawnedObject(spawn, instanceIndex);
		    if (spawn.isEventSpawn())
			    spawn.getEventTemplate().addSpawnedObject(visObj);

		    if (visObj != null) {
			    ThreadPoolManager.getInstance().schedule(new Runnable() { // to do remove

					    @Override
					    public void run() {
						    visObj.setIsNewSpawn(false);
					    }

				}, 1000);
		    }
		    return visObj;
	}

	private static VisibleObject getSpawnedObject(SpawnTemplate spawn, int instanceIndex) {
		int objectId = spawn.getNpcId();

		if (objectId > 400000 && objectId < 499999)
			return VisibleObjectSpawner.spawnGatherable(spawn, instanceIndex);
		else if (spawn instanceof SiegeSpawnTemplate)
			return VisibleObjectSpawner.spawnSiegeNpc((SiegeSpawnTemplate) spawn, instanceIndex);
		else
			return VisibleObjectSpawner.spawnNpc(spawn, instanceIndex);
	}

	/**
	 * @param worldId
	 * @param npcId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @return
	 */
	static SpawnTemplate createSpawnTemplate(int worldId, int npcId, float x, float y, float z, byte heading) {
		return new SpawnTemplate(new SpawnGroup2(worldId, npcId), x, y, z, heading, 0, null, 0, 0);
	}

	/**
	 * Should be used when you need to add a siegespawn through code and not from static_data spawns (e.g.
	 * CustomBalaurAssault)
	 */
	static SpawnTemplate createSpawnTemplate(int worldId, int npcId, float x, float y, float z, byte heading, int creatorId, String masterName) {
		SpawnTemplate template = createSpawnTemplate(worldId, npcId, x, y, z, heading);
		template.setCreatorId(creatorId);
		template.setMasterName(masterName);
		return template;
	}

	public static SiegeSpawnTemplate addNewSiegeSpawn(int worldId, int npcId, int siegeId, SiegeRace race, SiegeModType mod, float x, float y, float z, byte heading) {
		SiegeSpawnTemplate spawnTemplate = new SiegeSpawnTemplate(new SpawnGroup2(worldId, npcId), x, y, z, heading, 0, null, 0, 0);

		spawnTemplate.setSiegeId(siegeId);
		spawnTemplate.setSiegeRace(race);
		spawnTemplate.setSiegeModType(mod);
		return spawnTemplate;
	}

	/**
	 * Should be used when need to define whether spawn will be deleted after death Using this method spawns will not be
	 * saved with //save_spawn command
	 * 
	 * @param worldId
	 * @param npcId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @param respawnTime
	 * @param permanent
	 * @return SpawnTemplate
	 */
	public static SpawnTemplate addNewSpawn(int worldId, int npcId, float x, float y, float z, byte heading, int respawnTime) {
		SpawnTemplate spawnTemplate = createSpawnTemplate(worldId, npcId, x, y, z, heading);
		spawnTemplate.setRespawnTime(respawnTime);
		return spawnTemplate;
	}

	/**
	 * Create non-permanent spawn template with no respawn
	 * 
	 * @param worldId
	 * @param npcId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @return
	 */
	public static SpawnTemplate addNewSingleTimeSpawn(int worldId, int npcId, float x, float y, float z, byte heading) {
		return addNewSpawn(worldId, npcId, x, y, z, heading, 0);
	}

	public static SpawnTemplate addNewSingleTimeSpawn(int worldId, int npcId, float x, float y, float z, byte heading, int creatorId, String masterName) {
		SpawnTemplate template = addNewSpawn(worldId, npcId, x, y, z, heading, 0);
		template.setCreatorId(creatorId);
		template.setMasterName(masterName);
		return template;
	}

	static void bringIntoWorld(VisibleObject visibleObject, SpawnTemplate spawn, int instanceIndex) {
		bringIntoWorld(visibleObject, spawn.getWorldId(), instanceIndex, spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getHeading());
	}

	public static void bringIntoWorld(VisibleObject visibleObject, int worldId, int instanceIndex, float x, float y, float z, byte h) {
		World world = World.getInstance();
		world.storeObject(visibleObject);
		world.setPosition(visibleObject, worldId, instanceIndex, x, y, z, h);
		world.spawn(visibleObject);
	}

	public static void bringIntoWorld(VisibleObject visibleObject) {
		if (visibleObject.getPosition() == null)
			throw new IllegalArgumentException("Position is null");
		World world = World.getInstance();
		world.storeObject(visibleObject);
		world.spawn(visibleObject);
	}

	/**
	 * Spawn all NPC's from templates
	 */
	public static void spawnAll() {
		if (!DeveloperConfig.SPAWN_ENABLE) {
			log.info("Spawns are disabled");
			return;
		}
		for (WorldMapTemplate worldMapTemplate : DataManager.WORLD_MAPS_DATA) {
			if (worldMapTemplate.isInstance()) {
				continue;
			}
			spawnBasedOnTemplate(worldMapTemplate);
		}
		DataManager.SPAWNS_DATA2.clearTemplates();
		printWorldSpawnStats();
	}

	/**
	 * @param worldId
	 */
	public static void spawnWorldMap(int worldId) {
		WorldMapTemplate template = DataManager.WORLD_MAPS_DATA.getTemplate(worldId);
		if (template != null && !template.isInstance()) {
			spawnBasedOnTemplate(template);
		}
	}

	/**
	 * @param worldMapTemplate
	 */
	private static void spawnBasedOnTemplate(WorldMapTemplate worldMapTemplate) {
		int maxTwin = worldMapTemplate.getTwinCount();
		final int mapId = worldMapTemplate.getMapId();
		int numberToSpawn = maxTwin > 0 ? maxTwin : 1;

		for (int instanceId = 1; instanceId <= numberToSpawn; instanceId++)
			spawnInstance(mapId, instanceId, 0);
	}

	public static void spawnInstance(int worldId, int instanceId, int difficultId) {
		spawnInstance(worldId, instanceId, difficultId, 0);
	}

	/**
	 * @param worldId
	 * @param instanceId
	 */
	public static void spawnInstance(int worldId, int instanceId, int difficultId, int ownerId) {
		List<SpawnGroup2> worldSpawns = DataManager.SPAWNS_DATA2.getSpawnsByWorldId(worldId);
		StaticDoorSpawnManager.spawnTemplate(worldId, instanceId);

		int spawnedCounter = 0;
		if (worldSpawns != null) {
			for (SpawnGroup2 spawn : worldSpawns) {
				int difficult = spawn.getDifficultId();
				if ((difficult == 0) || (difficult == difficultId)) {
				    if (spawn.isPartialDaySpawn()) {
					DayTimeSpawnEngine.addSpawnGroup(spawn);
				}
					else if (spawn.getHandlerType() != null) {
						switch (spawn.getHandlerType()) {
							case RIFT:
								RiftSpawnManager.addRiftSpawnTemplate(spawn);
								break;
							case STATIC:
								StaticObjectSpawnManager.spawnTemplate(spawn, instanceId);
							default:
								break;
						}
					}
					else if (spawn.hasPool()) {
						for (int i = 0; i < spawn.getPool(); i++) {
							SpawnTemplate template = spawn.getRndTemplate();
							spawnObject(template, instanceId);
							spawnedCounter++;
						}
					}
					else {
						for (SpawnTemplate template : spawn.getSpawnTemplates()) {
							spawnObject(template, instanceId);
							spawnedCounter++;
						}
					}
				}
			}
			WalkerFormator.getInstance().organizeAndSpawn();
		}
		log.info("Spawned " + worldId + " [" + instanceId + "] : " + spawnedCounter);
		HousingService.getInstance().spawnHouses(worldId, instanceId, ownerId);
	}

	public static void printWorldSpawnStats() {
		StatsCollector visitor = new StatsCollector();
		World.getInstance().doOnAllObjects(visitor);
		log.info("Loaded " + visitor.getNpcCount() + " npc spawns");
		log.info("Loaded " + visitor.getGatherableCount() + " gatherable spawns");
	}
	
	public static BattleGroundHealer spawnBGHealer(SpawnTemplate spawn,
        int instanceId, Race race) {
        BattleGroundHealer healer = new BattleGroundHealer(IDFactory.getInstance()
                                                                    .nextId(),
                new BattleGroundHealerController(), spawn,
                DataManager.NPC_DATA.getNpcTemplate(((race == Race.ELYOS)
                    ? 278641 : 278140)));
        healer.setKnownlist(new NpcKnownList(healer));
        healer.setEffectController(new EffectController(healer));
        healer.getController().onRespawn();
        //healer.setMoveController(null);
        healer.setRace(race);
        bringIntoWorld(healer, spawn, instanceId);

        return healer;
    }
	
	public static BattleGroundFlag spawnBGFlag(SpawnTemplate spawn, int instanceId,
        Race race) {
        BattleGroundFlag flag = new BattleGroundFlag(IDFactory.getInstance()
                                                              .nextId(),
                new BattleGroundFlagController(), spawn,
                DataManager.NPC_DATA.getNpcTemplate(((race == Race.ELYOS)
                    ? 700336 : 700037)));
        flag.setKnownlist(new NpcKnownList(flag));
        flag.setEffectController(new EffectController(flag));
        flag.getController().onRespawn();
        //flag.setMoveController(null);
        flag.setRace(race);
        bringIntoWorld(flag, spawn, instanceId);

        return flag;
    }
	
	public static BattleGroundAgent spawnBGAgent(SpawnTemplate spawn, int instanceId,
        int npcId) {
        BattleGroundAgent agent = new BattleGroundAgent(IDFactory.getInstance()
                                                                 .nextId(),
                new BattleGroundAgentController(), spawn,
                DataManager.NPC_DATA.getNpcTemplate(npcId));
        agent.setKnownlist(new NpcKnownList(agent));
        agent.setEffectController(new EffectController(agent));
        agent.getController().onRespawn();
        //agent.setMoveController(null);
        bringIntoWorld(agent, spawn, instanceId);

        return agent;
    }

	static class StatsCollector implements Visitor<VisibleObject> {

		int npcCount;
		int gatherableCount;

		@Override
		public void visit(VisibleObject object) {
			if (object instanceof Npc) {
				npcCount++;
			}
			else if (object instanceof Gatherable) {
				gatherableCount++;
			}
		}

		public int getNpcCount() {
			return npcCount;
		}

		public int getGatherableCount() {
			return gatherableCount;
		}
	}

	/**
	 * @param mapId
	 * @param id
	 * @param x
	 * @param y
	 * @param z
	 * @param h
	 * @param respawnTime
	 * @param masterName
	 * @param masterId
	 * @return
	 */
	public static SpawnTemplate addNewSpawn(int mapId, int id, float x, float y, float z, byte h, int respawnTime, String masterName, int masterId) {
		SpawnTemplate spawnTemplate = createSpawnTemplate(mapId, id, x, y, z, h);
		spawnTemplate.setRespawnTime(respawnTime);
		spawnTemplate.setMasterName(masterName);
		spawnTemplate.setCreatorId(masterId);
		return spawnTemplate;
	}
}
