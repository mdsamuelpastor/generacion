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
package com.aionemu.gameserver.services.instance;

import java.util.Iterator;

import javolution.util.FastList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.instance.InstanceEngine;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;
import com.aionemu.gameserver.network.aion.SystemMessageId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AutoGroupService2;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.services.global.FFAStruct;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.WorldMap2DInstance;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldMapInstanceFactory;
import com.aionemu.gameserver.world.WorldMapType;
import com.aionemu.gameserver.world.zone.ZoneInstance;

/**
 * @author ATracer, Maestross
 */
public class InstanceService {

	private static final Logger log = LoggerFactory.getLogger(InstanceService.class);
	private static final FastList<Integer> instanceAggro = new FastList<Integer>();
	private static final FastList<Integer> instanceCoolDownFilter = new FastList<Integer>();

	public static void load() {
		for (String s : CustomConfig.INSTANCES_MOB_AGGRO.split(",")) {
			instanceAggro.add(Integer.parseInt(s));
		}
		for (String s : CustomConfig.INSTANCES_COOL_DOWN_FILTER.split(",")) {
			instanceCoolDownFilter.add(Integer.parseInt(s));
		}
	}

	/**
	 * @param worldId
	 * @return
	 */
	public static synchronized WorldMapInstance getNextAvailableInstance(int worldId, int ownerId) {
		WorldMap map = World.getInstance().getWorldMap(worldId);

		if (!map.isInstanceType())
			throw new UnsupportedOperationException("Invalid call for next available instance  of " + worldId);

		int nextInstanceId = map.getNextInstanceId();
		WorldMapInstance worldMapInstance = WorldMapInstanceFactory.createWorldMapInstance(map, nextInstanceId, ownerId);
		if(worldMapInstance.isPersonal()){
      log.info("Calling Personal Instance " + worldId + " Owner:" + ownerId);
    } else {
      log.info("Calling Instance " + worldId + " Owner: " + ownerId);
    }
		map.addInstance(nextInstanceId, worldMapInstance);
		SpawnEngine.spawnInstance(worldId, worldMapInstance.getInstanceId(), 0, ownerId);
		InstanceEngine.getInstance().onInstanceCreate(worldMapInstance);

		if (map.isInstanceType() && worldId != FFAStruct.worldId) {
			startInstanceChecker(worldMapInstance);
		}

		return worldMapInstance;
	}

	public static synchronized WorldMapInstance getNextAvailableInstance(int worldId) {
		return getNextAvailableInstance(worldId, 0);
	}

	/**
	 * Instance will be destroyed All players moved to bind location All objects - deleted
	 */
	public static void destroyInstance(WorldMapInstance instance) {
	    int worldId = instance.getMapId();
		if (instance.getEmptyInstanceTask() != null) {
			instance.getEmptyInstanceTask().cancel(false);
		}
		WorldMap map = World.getInstance().getWorldMap(worldId);
		if (!map.isInstanceType())
			return;
		int instanceId = instance.getInstanceId();

		map.removeWorldMapInstance(instanceId);

		log.info("Destroying instance:" + worldId + " " + instanceId);

		Iterator<VisibleObject> it = instance.objectIterator();
		while (it.hasNext()) {
			VisibleObject obj = it.next();
			if (obj instanceof Player) {
				Player player = (Player) obj;
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.LEAVE_INSTANCE_NOT_PARTY));
				moveToExitPoint((Player) obj);
			}
			else {
				obj.getController().onDelete();
			}
		}
		instance.getInstanceHandler().onInstanceDestroy();
		if ((instance instanceof WorldMap2DInstance)) {
			WorldMap2DInstance w2d = (WorldMap2DInstance) instance;
			if (w2d.isPersonal())
				HousingService.getInstance().onInstanceDestroy(w2d.getOwnerId());
		}
	}

	/**
	 * @param instance
	 * @param player
	 */
	public static void registerPlayerWithInstance(WorldMapInstance instance, Player player) {
		Integer obj = player.getObjectId();
		instance.register(obj);
		instance.setSoloPlayerObj(obj);
	}

	/**
	 * @param instance
	 * @param group
	 */
	public static void registerGroupWithInstance(WorldMapInstance instance, PlayerGroup group) {
		instance.registerGroup(group);
	}

	/**
	 * @param instance
	 * @param group
	 */
	public static void registerAllianceWithInstance(WorldMapInstance instance, PlayerAlliance group) {
		instance.registerGroup(group);
	}

	/**
	 * @param worldId
	 * @param objectId
	 * @return instance or null
	 */
	public static WorldMapInstance getRegisteredInstance(int worldId, int objectId) {
		Iterator<WorldMapInstance> iterator = World.getInstance().getWorldMap(worldId).iterator();
		while (iterator.hasNext()) {
			WorldMapInstance instance = iterator.next();
			if (instance.isRegistered(objectId))
				return instance;
		}
		return null;
	}

	public static WorldMapInstance getPersonalInstance(int worldId, int ownerId) {
		if (ownerId == 0) {
			return null;
		}
		Iterator<WorldMapInstance> iterator = World.getInstance().getWorldMap(worldId).iterator();
		while (iterator.hasNext()) {
			WorldMapInstance instance = iterator.next();
			if ((instance.isPersonal()) && (instance.getOwnerId() == ownerId))
				return instance;
		}
		return null;
	}

	/**
	 * @param player
	 */
	public static void onPlayerLogin(Player player){
    int worldId = player.getWorldId();
    WorldMapTemplate worldTemplate = DataManager.WORLD_MAPS_DATA.getTemplate(worldId);
    if(worldTemplate.isInstance()){
       boolean isPersonal = WorldMapType.getWorld(worldId).isPersonal();
       int lookupId;
       if(player.isInGroup2()){
         lookupId = player.getPlayerGroup2().getTeamId();
        }
        else {
         if(player.isInAlliance2()){
            lookupId = player.getPlayerAlliance2().getTeamId();
          } else {
             if(isPersonal && player.getCommonData().getWorldOwnerId() != 0){
                lookupId = player.getCommonData().getWorldOwnerId();
             } else {
                lookupId = player.getObjectId();
             }
          }
        }
        WorldMapInstance registeredInstance = isPersonal ? getPersonalInstance(worldId, lookupId) : getRegisteredInstance(worldId, lookupId);
        if (isPersonal) {
            if (registeredInstance == null) {
                registeredInstance = getNextAvailableInstance(player.getWorldId(), lookupId);
             }
             if (!registeredInstance.isRegistered(player.getObjectId())) {
                  registerPlayerWithInstance(registeredInstance, player);
             }
         }     
         if (registeredInstance != null) {
              World.getInstance().setPosition(player, worldId, registeredInstance.getInstanceId(), player.getX(), player.getY(), player.getZ(), player.getHeading());
              player.getPosition().getWorldMapInstance().getInstanceHandler().onPlayerLogin(player);
              return;
         }           
         moveToExitPoint(player);                                                   
    }
}

	public static void moveToExitPoint(Player player) {
		TeleportService2.moveToInstanceExit(player, player.getWorldId(), player.getRace());
	}

	/**
	 * @param worldId
	 * @param instanceId
	 * @return
	 */
	public static boolean isInstanceExist(int worldId, int instanceId) {
		return World.getInstance().getWorldMap(worldId).getWorldMapInstanceById(instanceId) != null;
	}

	/**
	 * @param worldMapInstance
	 */
	private static void startInstanceChecker(WorldMapInstance worldMapInstance) {
		int delay = 150000; // 2.5 minutes
		int period = 60000; // 1 minute
		worldMapInstance.setEmptyInstanceTask(ThreadPoolManager.getInstance().scheduleAtFixedRate(new EmptyInstanceCheckerTask(worldMapInstance), delay, period));
	}

	public static void onLogOut(Player player) {
		player.getPosition().getWorldMapInstance().getInstanceHandler().onPlayerLogOut(player);
	}

	public static void onEnterInstance(Player player) {
		player.getPosition().getWorldMapInstance().getInstanceHandler().onEnterInstance(player);
		for (Item item : player.getInventory().getItems())
			if (item.getItemTemplate().getOwnershipWorld() != 0) {
				if (item.getItemTemplate().getOwnershipWorld() != player.getWorldId())
					player.getInventory().decreaseByObjectId(item.getObjectId(), item.getItemCount());
			}
	}

	public static void onLeaveInstance(Player player) {
		player.getPosition().getWorldMapInstance().getInstanceHandler().onLeaveInstance(player);
		for (Item item : player.getInventory().getItems()) {
			if (item.getItemTemplate().getOwnershipWorld() == player.getWorldId())
				player.getInventory().decreaseByObjectId(item.getObjectId(), item.getItemCount());
		}
		AutoGroupService2.getInstance().onLeaveInstance(player);
	}

	public static void onEnterZone(Player player, ZoneInstance zone) {
		player.getPosition().getWorldMapInstance().getInstanceHandler().onEnterZone(player, zone);
	}

	public static void onLeaveZone(Player player, ZoneInstance zone) {
		player.getPosition().getWorldMapInstance().getInstanceHandler().onLeaveZone(player, zone);
	}

	public static synchronized WorldMapInstance createBattleGroundInstance(
        int worldId) {
        WorldMap map = World.getInstance().getWorldMap(worldId);
        int nextInstanceId = map.getNextInstanceId();
        log.info("Creating new BG instance: " + worldId + " " + nextInstanceId);

        WorldMapInstance worldMapInstance = WorldMapInstanceFactory.createWorldMapInstance(map, nextInstanceId);
        startInstanceChecker(worldMapInstance);
        map.addInstance(nextInstanceId, worldMapInstance);

        return worldMapInstance;
    }
	
	public static boolean isAggro(int mapId) {
		return instanceAggro.contains(mapId);
	}

	public static int getInstanceRate(Player player, int mapId) {
		int instanceCooldownRate = player.havePermission(MembershipConfig.INSTANCES_COOLDOWN) && !instanceCoolDownFilter.contains(mapId) ? CustomConfig.INSTANCES_RATE : 1;
		if (instanceCoolDownFilter.contains(mapId)) {
			instanceCooldownRate = 1;
		}
		return instanceCooldownRate;
	}

	private static class EmptyInstanceCheckerTask implements Runnable {

		private WorldMapInstance worldMapInstance;

		private EmptyInstanceCheckerTask(WorldMapInstance worldMapInstance) {
			this.worldMapInstance = worldMapInstance;
		}

		@Override
		public void run() {
			int instanceId = worldMapInstance.getInstanceId();
			int worldId = worldMapInstance.getMapId();
			WorldMap map = World.getInstance().getWorldMap(worldId);
			PlayerGroup registeredGroup = worldMapInstance.getRegisteredGroup();
			if (registeredGroup == null && worldId != FFAStruct.worldId) {
				if (worldMapInstance.playersCount() == 0) {
					map.removeWorldMapInstance(instanceId);
					InstanceService.destroyInstance(worldMapInstance);
					return;
				}
				Iterator<Player> playerIterator = worldMapInstance.playerIterator();
				int mapId = worldMapInstance.getMapId();
				while (playerIterator.hasNext()) {
					Player player = playerIterator.next();
					if ((player.isOnline()) && (player.getWorldId() == mapId)) {
						return;
					}
				}
				map.removeWorldMapInstance(instanceId);
				InstanceService.destroyInstance(worldMapInstance);
			}
			else if (registeredGroup.size() == 0) {
				map.removeWorldMapInstance(instanceId);
				InstanceService.destroyInstance(worldMapInstance);
			}
		}
	}
}
