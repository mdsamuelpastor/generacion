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
package com.aionemu.gameserver.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javolution.util.FastList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.HousingConfig;
import com.aionemu.gameserver.controllers.HouseController;
import com.aionemu.gameserver.dao.HousesDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.HouseDecoration;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.HousingFlags;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.house.HouseStatus;
import com.aionemu.gameserver.model.templates.housing.Building;
import com.aionemu.gameserver.model.templates.housing.BuildingType;
import com.aionemu.gameserver.model.templates.housing.HouseAddress;
import com.aionemu.gameserver.model.templates.housing.HousePart;
import com.aionemu.gameserver.model.templates.housing.HousingLand;
import com.aionemu.gameserver.model.templates.housing.PartType;
import com.aionemu.gameserver.model.templates.spawns.SpawnType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_ACQUIRE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_OWNER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author Maestross
 */

public class HousingService {

	private static final Logger log = LoggerFactory.getLogger(HousingService.class);

	private static final Map<Integer, List<House>> housesByMapId = new HashMap<Integer, List<House>>();
	private static Map<Integer, Map<Integer, House>> customHouses = new HashMap<Integer, Map<Integer, House>>();
	private static final Map<Integer, House> studios = Collections.synchronizedMap(new HashMap<Integer, House>());
	private static House studioElyTemplate;
	private static House studioAsmoTemplate;

	public static final HousingService getInstance() {
		return SingletonHolder.instance;
	}

	private HousingService() {
		log.info("Loading housing data...");
		loadPlayerData();
		log.info("Housing Service loaded.");
	}

	public void spawnHouses(int worldId, int instanceId, int registeredId) {
		Set<HousingLand> lands = DataManager.HOUSE_DATA.getLandsForWorldId(worldId);
		if (lands == null) {
			if (registeredId > 0) {
				House studio = null;
				synchronized (studios) {
					studio = studios.get(Integer.valueOf(registeredId));
				}
				if (studio == null)
					return;
				HouseAddress addr = studio.getAddress();
				if (addr.getMapId() != worldId)
					return;
				VisibleObject existing = World.getInstance().findVisibleObject(studio.getObjectId());
				WorldPosition position = null;
				if (existing != null)
					position = existing.getPosition();
				if (position == null) {
					position = World.getInstance().createPosition(addr.getMapId(), addr.getX(), addr.getY(), addr.getZ(), (byte) 0, instanceId);
					studio.setPosition(position);
				}
				if (!position.isSpawned()) {
					SpawnEngine.bringIntoWorld(studio);
				}
				studio.spawn(instanceId);

				House template = null;
				if (studio.getAddress().getMapId() == 730010000)
					template = studioAsmoTemplate;
				else if (studio.getAddress().getMapId() == 720010000) {
					template = studioElyTemplate;
				}
				for (HouseDecoration templateDecor : template.getRegistry().getDefaultParts()) {
					HouseDecoration decor = studio.getRegistry().getDefaultPartByType(templateDecor.getTemplate().getType());

					if (decor == null) {
						HouseDecoration updatedDecor = new HouseDecoration(0, templateDecor.getTemplate().getId());
						studio.getRegistry().putDefaultPart(updatedDecor);
					}
					else if (templateDecor.getTemplate().getId() == decor.getTemplate().getId() && decor.getObjectId() != 0) {
						HouseDecoration updatedDecor = new HouseDecoration(0, templateDecor.getTemplate().getId());
						studio.getRegistry().putDefaultPart(updatedDecor);
						studio.getRegistry().removeCustomPart(decor.getObjectId());
					}
					else if (templateDecor.getTemplate().getId() != decor.getTemplate().getId() && decor.getObjectId() == 0) {
						HouseDecoration updatedDecor = new HouseDecoration(0, templateDecor.getTemplate().getId());
						studio.getRegistry().putDefaultPart(updatedDecor);
					}

				}

				Player enteredPlayer = World.getInstance().findPlayer(registeredId);
				if (enteredPlayer != null)
					enteredPlayer.setHouseRegistry(studio.getRegistry());
			}
			return;
		}

		int spawnedCounter = 0;
		for (HousingLand land : lands) {
			Building defaultBuilding = land.getDefaultBuilding();
			if (defaultBuilding.getType() != BuildingType.PERSONAL_INS) {
				for (HouseAddress address : land.getAddresses()) {
					if (address.getMapId() == worldId) {
						Map<Integer, House> addressBuildings = customHouses.get(address.getId());
						HashMap<Integer, Integer> addressHouseIds = new HashMap<Integer, Integer>();

						boolean isDefaultActive = false;
						for (Building building : land.getBuildings()) {
							House customHouse = null;
							if (addressBuildings == null || addressBuildings.get(building.getId()) == null) {
								int objectId = 0;
								if (addressHouseIds.containsKey(address.getId())) {
									objectId = addressHouseIds.get(address.getId());
								}
								customHouse = generateRandomHouse(objectId, building, address, instanceId);

								if (addressBuildings == null) {
									isDefaultActive = true;
									addressBuildings = new HashMap<Integer, House>();
									customHouses.put(address.getId(), addressBuildings);
								}

								boolean hasActiveHouse = false;
								for (House house : addressBuildings.values()) {
									if (house.getStatus() != HouseStatus.INACTIVE || house.getSellStarted() != null) {
										if (!isDefaultActive)
											isDefaultActive = defaultBuilding == building;
										hasActiveHouse = true;
										break;
									}
								}

								boolean isTempDisabled = customHouse.getSellStarted() != null && customHouse.getStatus() == HouseStatus.INACTIVE;
								if (!isTempDisabled) {
									if (isDefaultActive) {
										customHouse.setStatus(defaultBuilding == building ? HouseStatus.ACTIVE : HouseStatus.INACTIVE);
									}
									else {
										customHouse.setStatus(hasActiveHouse ? HouseStatus.INACTIVE : HouseStatus.ACTIVE);
									}
								}

								DAOManager.getDAO(HousesDAO.class).storeNewHouse(customHouse);
								addressBuildings.put(building.getId(), customHouse);
							}
							else {
								customHouse = addressBuildings.get(building.getId());
							}

							addressHouseIds.put(address.getId(), customHouse.getObjectId());

							if (customHouse.getStatus() != HouseStatus.INACTIVE || customHouse.getSellStarted() != null) {
								customHouse.spawn(instanceId);
								spawnedCounter++;
								List<House> housesForMap = housesByMapId.get(worldId);
								if (housesForMap == null) {
									housesForMap = new ArrayList<House>();
									housesByMapId.put(worldId, housesForMap);
								}
								housesForMap.add(customHouse);
							}
						}
					}
				}
			}
		}
		if (spawnedCounter > 0)
			log.info("Spawned houses " + worldId + " [" + instanceId + "] : " + spawnedCounter);
	}

	private House generateRandomHouse(int objectId, Building building, HouseAddress address, int instanceId) {
		House customHouse = null;
		if (objectId == 0)
			customHouse = new House(building, address, instanceId);
		else
			customHouse = new House(objectId, building, address, instanceId);
		Map<PartType, Integer> partsToRender = getBuildingDefaultParts(customHouse.getLand(), building);
		for (Integer partId : partsToRender.values())
			customHouse.getRegistry().putDefaultPart(new HouseDecoration(0, partId));
		return customHouse;
	}

	public Map<PartType, Integer> getBuildingDefaultParts(HousingLand land, Building building) {
		List<HousePart> parts = DataManager.HOUSE_PARTS_DATA.getPartsForBuilding(building);
		Map<PartType, List<Integer>> partsByType = new HashMap<PartType, List<Integer>>();
		for (HousePart part : parts) {
			List<Integer> partsForType = partsByType.get(part.getType());
			if (partsForType == null) {
				partsForType = new ArrayList<Integer>();
				partsByType.put(part.getType(), partsForType);
			}
			partsForType.add(Integer.valueOf(part.getId()));
		}

		Map<PartType, Integer> partsToRender = new HashMap<PartType, Integer>(9);
		for (PartType type : PartType.values())
			if (!partsToRender.containsKey(type) && partsByType.get(type) != null) {
				if (land.getCapabilities().canChangeFloor() || type != PartType.INFLOOR_ANY) {
					if (land.getCapabilities().canChangeRoom() || type != PartType.INWALL_ANY) {
						if (type != PartType.ADDON) {
							List<Integer> allValidParts = partsByType.get(type);

							partsToRender.put(type, allValidParts.get(Rnd.get(allValidParts.size())));
						}
					}
				}
			}
		return partsToRender;
	}

	private void loadPlayerData() {
		customHouses = DAOManager.getDAO(HousesDAO.class).loadHouses(DataManager.HOUSE_DATA.getLands(), false);
		Collection<Map<Integer, House>> loadedStudios = DAOManager.getDAO(HousesDAO.class).loadHouses(DataManager.HOUSE_DATA.getLands(), true).values();

		HousingLand land = DataManager.HOUSE_DATA.getLand(329001);
		studioElyTemplate = generateRandomHouse(0, land.getDefaultBuilding(), land.getAddresses().get(0), 0);
		studioElyTemplate.getRegistry().putDefaultPart(new HouseDecoration(0, 3524000));
		studioElyTemplate.getRegistry().putDefaultPart(new HouseDecoration(0, 3525000));
		studioElyTemplate.setStatus(HouseStatus.INACTIVE);

		land = DataManager.HOUSE_DATA.getLand(339001);
		studioAsmoTemplate = generateRandomHouse(0, land.getDefaultBuilding(), land.getAddresses().get(0), 0);
		studioAsmoTemplate.getRegistry().putDefaultPart(new HouseDecoration(0, 3524000));
		studioAsmoTemplate.getRegistry().putDefaultPart(new HouseDecoration(0, 3525000));
		studioAsmoTemplate.setStatus(HouseStatus.INACTIVE);

		for (Map<Integer, House> entry : loadedStudios) {
			Collection<House> houses = entry.values();
			for (House house : houses) {
				studios.put(house.getOwnerId(), house);
				if (house.getOwnerId() < 0)
					if (house.getAddress().getMapId() == 730010000)
						studioAsmoTemplate = house;
					else if (house.getAddress().getMapId() == 720010000)
						studioElyTemplate = house;
			}
		}
	}

	public List<House> getCloseHousesForPlayer(Player player) {
		int worldId = player.getWorldId();
		List<House> houses = housesByMapId.get(worldId);
		if (houses == null) {
			return null;
		}
		List<House> closeHouse = new ArrayList<House>();
		for (House house : houses) {
			HouseAddress address = house.getAddress();
			if (MathUtil.getDistance(player.getX(), player.getY(), player.getZ(), address.getX(), address.getY(), address.getZ()) <= HousingConfig.VISIBILITY_DISTANCE) {
				closeHouse.add(house);
			}
		}
		return closeHouse;
	}

	public List<House> searchPlayerHouses(Player player) {
		List<House> houses = new ArrayList<House>();
		synchronized (studios) {
			if (studios.containsKey(player.getObjectId())) {
				houses.add(studios.get(player.getObjectId()));
				return houses;
			}
		}
		for (Map<Integer, House> entry : customHouses.values()) {
			Collection<House> models = entry.values();
			for (House house : models)
				if (house.getStatus() != HouseStatus.INACTIVE) {
					if (house.getOwnerId() == player.getCommonData().getPlayerObjId())
						houses.add(house);
				}
		}
		return houses;
	}

	public int getPlayerAddress(int playerId) {
		synchronized (studios) {
			if (studios.containsKey(playerId)) {
				return (studios.get(playerId)).getAddress().getId();
			}
		}
		for (Map<Integer, House> entry : customHouses.values()) {
			Collection<House> models = entry.values();
			for (House house : models)
				if (house.getStatus() != HouseStatus.INACTIVE) {
					if (house.getOwnerId() == playerId && (house.getStatus() == HouseStatus.ACTIVE || house.getStatus() == HouseStatus.SELL_WAIT)) {
						return house.getAddress().getId();
					}
				}
		}
		return 0;
	}

	public void resetAppearance(House house) {
		FastList<HouseDecoration> customParts = house.getRegistry().getCustomParts();
		for (HouseDecoration deco : customParts) {
			deco.setPersistentState(PersistentState.DELETED);
		}
		house.save();
		for (HouseDecoration deco : customParts)
			house.getRegistry().removeCustomPart(deco.getObjectId());
	}

	public House getHouseByName(String houseName) {
		for (Map<Integer, House> entry : customHouses.values()) {
			Collection<House> models = entry.values();
			for (House house : models)
				if (house.getStatus() != HouseStatus.INACTIVE || house.getSellStarted() != null) {
					if (house.getName().equals(houseName))
						return house;
				}
		}
		return null;
	}

	public House getHouseByEditor(int editorId) {
		for (Map<Integer, House> entry : customHouses.values()) {
			Collection<House> models = entry.values();
			for (House house : models)
				if (house.getStatus() != HouseStatus.INACTIVE || house.getSellStarted() != null) {
					if (house.getAddress().getEditorId() == editorId)
						return house;
				}
		}
		return null;
	}

	public House getHouseByAddress(int address) {
		for (Map<Integer, House> entry : customHouses.values()) {
			Collection<House> models = entry.values();
			for (House house : models)
				if (house.getStatus() != HouseStatus.INACTIVE || house.getSellStarted() != null) {
					if (house.getAddress().getId() == address)
						return house;
				}
		}
		return null;
	}

	public House activateBoughtHouse(int playerId) {
		for (Map<Integer, House> entry : customHouses.values()) {
			Collection<House> models = entry.values();
			for (House house : models) {
				if (house.getOwnerId() == playerId && house.getStatus() == HouseStatus.INACTIVE && house.getSellStarted() != null) {
					house.revokeOwner();
					house.setOwnerId(playerId);
					return house;
				}
			}
		}
		return null;
	}

	public House getPlayerStudio(int playerId) {
		synchronized (studios) {
			if (studios.containsKey(playerId))
				return studios.get(playerId);
		}
		return null;
	}

	public void removeStudio(int playerId) {
		if (playerId != 0)
			synchronized (studios) {
				studios.remove(playerId);
			}
	}

	public void registerPlayerStudio(Player player, int instanceId) {
		createStudio(player);
	}

	public void recreatePlayerStudio(Player player) {
		HousingLand land = DataManager.HOUSE_DATA.getLand(329001);
		long fee = land.getSaleOptions().getGoldPrice();
		if (player.getInventory().getKinah() < fee) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_NOT_ENOUGH_MONEY);
			return;
		}
		createStudio(player);

		player.getInventory().decreaseKinah(fee);
	}

	private void createStudio(Player player) {
		House template = player.getRace() == Race.ASMODIANS ? studioAsmoTemplate : studioElyTemplate;
		House studio = new House(template.getBuilding(), template.getAddress(), 0);
		studio.setOwnerId(player.getObjectId());

		HouseDecoration templateDecor = template.getRegistry().getDefaultPartByType(PartType.INFLOOR_ANY);
		studio.getRegistry().putDefaultPart(new HouseDecoration(0, templateDecor.getTemplate().getId()));
		templateDecor = template.getRegistry().getDefaultPartByType(PartType.INWALL_ANY);
		studio.getRegistry().putDefaultPart(new HouseDecoration(0, templateDecor.getTemplate().getId()));

		synchronized (studios) {
			studios.put(player.getObjectId(), studio);
		}
		studio.setStatus(HouseStatus.ACTIVE);
		studio.setAcquiredTime(new Timestamp(System.currentTimeMillis()));
		studio.setFeePaid(true);
		studio.setNextPay(null);
		studio.setPersistentState(PersistentState.NEW);
		player.setHousingStatus(HousingFlags.HOUSE_OWNER.getId());
		PacketSendUtility.sendPacket(player, new SM_HOUSE_ACQUIRE(player.getObjectId(), studio.getAddress().getId(), true));
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_INS_OWN_SUCCESS);
		PacketSendUtility.sendPacket(player, new SM_HOUSE_OWNER_INFO(player, studio));
	}

	public boolean switchHouseBuilding(House currentHouse, int newBuildingId) {
		Map<Integer, House> models = customHouses.get(currentHouse.getAddress().getId());
		House newHouse = models.get(newBuildingId);
		if (newHouse == null || newHouse == currentHouse)
			return false;
		newHouse.setAcquiredTime(new Timestamp(System.currentTimeMillis()));
		newHouse.setFeePaid(currentHouse.isFeePaid());
		newHouse.setSettingFlags(currentHouse.getSettingFlags());
		newHouse.setNextPay(currentHouse.getNextPay());
		newHouse.setSellStarted(currentHouse.getSellStarted());
		newHouse.setOwnerId(currentHouse.getOwnerId());

		currentHouse.setStatus(HouseStatus.INACTIVE);
		currentHouse.setOwnerId(0);
		DAOManager.getDAO(HousesDAO.class).storeHouse(currentHouse);

		currentHouse.moveRequisitesTo(newHouse);
		newHouse.setStatus(HouseStatus.ACTIVE);
		DAOManager.getDAO(HousesDAO.class).storeHouse(newHouse);
		((HouseController) currentHouse.getController()).onDelete();
		newHouse.setPosition(currentHouse.getPosition());

		currentHouse.getPosition().getWorldMapInstance().removeObject(currentHouse);
		SpawnEngine.bringIntoWorld(newHouse);

		return true;
	}

	public House getHouseForBuilding(HouseAddress address, Building building) {
		Map<Integer, House> models = customHouses.get(address.getId());
		return models.get(building.getId());
	}

	public FastList<House> getCustomHouses() {
		FastList<House> houses = FastList.newInstance();
		for (List<House> mapHouses : housesByMapId.values())
			houses.addAll(mapHouses);
		return houses;
	}

	public void onInstanceDestroy(int ownerId) {
		House studio = null;
		synchronized (studios) {
			studio = studios.get(ownerId);
		}
		if (studio != null) {
			studio.setSpawn(SpawnType.MANAGER, null);
			studio.setSpawn(SpawnType.TELEPORT, null);
			studio.setSpawn(SpawnType.SIGN, null);
			studio.save();
		}
	}

	public void onPlayerLogin(Player player) {
		House activeHouse = null;
		House disabledHouse = null;
		House studio = null;
		byte housingStatus = HousingFlags.BUY_STUDIO_ALLOWED.getId();
		for (House house : player.getHouses()) {
			if (house.getStatus() == HouseStatus.ACTIVE || house.getStatus() == HouseStatus.SELL_WAIT) {
				activeHouse = house;
			}
			if (house.getStatus() == HouseStatus.INACTIVE && house.getSellStarted() != null) {
				disabledHouse = house;
			}
		}
		if (activeHouse == null) {
			QuestState state = null;
			if (player.getRace() == Race.ELYOS)
				state = player.getQuestStateList().getQuestState(18802);
			else
				state = player.getQuestStateList().getQuestState(28802);
			if (state != null)
				housingStatus = (byte) (housingStatus | HousingFlags.BIDDING_ALLOWED.getId());
		}
		else if (activeHouse.getStatus() == HouseStatus.SELL_WAIT) {
			housingStatus = HousingFlags.SELLING_HOUSE.getId();
		}
		else {
			housingStatus = HousingFlags.HOUSE_OWNER.getId();
		}
		player.setHousingStatus(housingStatus);

		PacketSendUtility.sendPacket(player, new SM_HOUSE_OWNER_INFO(player, activeHouse));
	}

	private static class SingletonHolder {

		protected static final HousingService instance = new HousingService();
	}
}