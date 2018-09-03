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
package com.aionemu.gameserver.model.house;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.HousingConfig;
import com.aionemu.gameserver.controllers.HouseController;
import com.aionemu.gameserver.dao.HouseScriptsDAO;
import com.aionemu.gameserver.dao.HousesDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.PlayerRegisteredItemsDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.gameobjects.HouseDecoration;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.SummonedHouseNpc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.HousingFlags;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerScripts;
import com.aionemu.gameserver.model.templates.housing.Building;
import com.aionemu.gameserver.model.templates.housing.BuildingType;
import com.aionemu.gameserver.model.templates.housing.HouseAddress;
import com.aionemu.gameserver.model.templates.housing.HouseType;
import com.aionemu.gameserver.model.templates.housing.HousingLand;
import com.aionemu.gameserver.model.templates.housing.PartType;
import com.aionemu.gameserver.model.templates.housing.Sale;
import com.aionemu.gameserver.model.templates.spawns.HouseSpawn;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnType;
import com.aionemu.gameserver.model.templates.zone.ZoneClassName;
import com.aionemu.gameserver.services.HousingBidService;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.spawnengine.VisibleObjectSpawner;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.knownlist.PlayerAwareKnownList;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneService;

/**
 * @author GoodT, Maestross
 */
 
public class House extends VisibleObject {

	private static final Logger log = LoggerFactory.getLogger(House.class);
	private HousingLand land;
	private HouseAddress address;
	private Building building;
	private String name;
	private int playerObjectId;
	private Timestamp acquiredTime;
	private int settingFlags;
	private HouseStatus status;
	private boolean feePaid = true;
	private Timestamp nextPay;
	private Timestamp sellStarted;
	private Map<SpawnType, Npc> spawns = new HashMap<SpawnType, Npc>(3);
	private HouseRegistry houseRegistry;
	private byte housingFlags = HousingFlags.SINGLE_HOUSE.getId();
	private PlayerScripts playerScripts;
	private PersistentState persistentState;
	private String notification;

	public House(Building building, HouseAddress address, int instanceId) {
		this(IDFactory.getInstance().nextId(), building, address, instanceId);
	}

	public House(int objectId, Building building, HouseAddress address, int instanceId) {
		super(objectId, new HouseController(), null, null, null);
		((HouseController) getController()).setOwner(this);
		this.address = address;
		this.building = building;
		name = ("HOUSE_" + address.getEditorId());
		setKnownlist(new PlayerAwareKnownList(this));
		setPersistentState(PersistentState.UPDATED);
	}

	public HousingLand getLand() {
		if (land == null)
			for (HousingLand housingland : DataManager.HOUSE_DATA.getLands()) {
				for (HouseAddress houseAddress : housingland.getAddresses())
					if (getAddress().getId() == houseAddress.getId()) {
						land = housingland;
						break;
					}
			}
		return land;
	}

	@Override
	public String getName() {
		return name;
	}

	public HouseAddress getAddress() {
		return address;
	}

	public Building getBuilding() {
		return building;
	}

	public synchronized void spawn(int instanceId) {
		playerScripts = DAOManager.getDAO(HouseScriptsDAO.class).getPlayerScripts(getObjectId());

		if (playerObjectId > 0) {
			DAOManager.getDAO(PlayerRegisteredItemsDAO.class).loadRegistry(playerObjectId, getAddress().getId());
		}

		fixHousingFlags();

		if (getPosition() == null || !getPosition().isSpawned()) {
			WorldPosition position = World.getInstance().createPosition(address.getMapId(), address.getX(), address.getY(), address.getZ(), (byte) 0, instanceId);

			setPosition(position);
			SpawnEngine.bringIntoWorld(this);
		}

		List<HouseSpawn> templates = DataManager.HOUSE_NPCS_DATA.getSpawnsByAddress(getAddress().getId());
		if (templates == null) {
			Collection<ZoneInstance> zones = ZoneService.getInstance().getZoneInstancesByWorldId(getAddress().getMapId()).values();
			String msg = null;
			for (ZoneInstance zone : zones) {
				if (zone.getZoneTemplate().getZoneType() == ZoneClassName.SUB && zone.getZoneTemplate().getPriority() <= 20) {
					if (zone.isInsideCordinate(getAddress().getX(), getAddress().getY(), getAddress().getZ())) {
						msg = "zone=" + zone.getZoneTemplate().getXmlName();
						break;
					}
				}
			}
			if (msg == null)
				msg = "address=" + getAddress().getId() + "; map=" + getAddress().getMapId();
			msg = msg + "; x=" + getAddress().getX() + ", y=" + getAddress().getY() + ", z=" + getAddress().getZ();
			log.warn("Missing npcs for house: " + msg);
			return;
		}

		int creatorId = getAddress().getId();
		String masterName = "";
		if (playerObjectId != 0) {
			ArrayList<Integer> players = new ArrayList<Integer>(1);
			players.add(playerObjectId);
			Map<Integer, String> playerNames = DAOManager.getDAO(PlayerDAO.class).getPlayerNames(players);
			if (playerNames.containsKey(playerObjectId)) {
				masterName = (String) playerNames.get(playerObjectId);
			}
			else {
				revokeOwner();
			}
		}
		for (HouseSpawn spawn : templates) {
			SpawnTemplate t = null;
			if (spawn.getType() == SpawnType.MANAGER && spawns.get(SpawnType.MANAGER) == null) {
				t = SpawnEngine.addNewSingleTimeSpawn(getAddress().getMapId(), getLand().getManagerNpcId(), spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getH());

				SummonedHouseNpc npc = VisibleObjectSpawner.spawnHouseNpc(t, getPosition().getInstanceId(), this, masterName);
				spawns.put(SpawnType.MANAGER, npc);
			}
			else if (spawn.getType() == SpawnType.TELEPORT && spawns.get(SpawnType.TELEPORT) == null) {
				t = SpawnEngine.addNewSingleTimeSpawn(getAddress().getMapId(), getLand().getTeleportNpcId(), spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getH());

				SummonedHouseNpc npc = VisibleObjectSpawner.spawnHouseNpc(t, getPosition().getInstanceId(), this, masterName);
				spawns.put(SpawnType.TELEPORT, npc);
			}
			else if (spawn.getType() == SpawnType.SIGN && spawns.get(SpawnType.SIGN) == null) {
				t = SpawnEngine.addNewSingleTimeSpawn(getAddress().getMapId(), getCurrentSignNpcId(), spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getH(), creatorId, "");

				spawns.put(SpawnType.SIGN, (Npc) SpawnEngine.spawnObject(t, getPosition().getInstanceId()));
			}
		}
	}

	@Override
	public float getVisibilityDistance() {
		return HousingConfig.VISIBILITY_DISTANCE;
	}

	@Override
	public float getMaxZVisibleDistance() {
		return HousingConfig.VISIBILITY_DISTANCE;
	}

	public int getOwnerId() {
		return playerObjectId;
	}

	public void setOwnerId(int playerObjectId) {
		this.playerObjectId = playerObjectId;
		fixHousingFlags();
	}
	
	public String getOwnerName() {
		Player player = World.getInstance().findPlayer(getAddress().getId());
		if (player == null)
			return "";
		return player.getName();
	}

	public Timestamp getAcquiredTime() {
		return acquiredTime;
	}

	public void setAcquiredTime(Timestamp acquiredTime) {
		this.acquiredTime = acquiredTime;
	}

	public int getSettingFlags() {
		return settingFlags;
	}

	public void setSettingFlags(int settingFlags) {
		this.settingFlags = settingFlags;
	}

	public HouseStatus getStatus() {
		return status;
	}

	public synchronized void setStatus(HouseStatus status) {
		if (this.status != status) {
			this.status = status;
			fixHousingFlags();

			if ((status != HouseStatus.INACTIVE || getSellStarted() != null) && spawns.get(SpawnType.SIGN) != null) {
				Npc sign = spawns.get(SpawnType.SIGN);
				int oldNpcId = sign.getNpcId();
				int newNpcId = getCurrentSignNpcId();

				if (newNpcId != oldNpcId) {
					SpawnTemplate t = sign.getSpawn();
					sign.setSpawn(null);
					sign.getController().onDelete();
					t = SpawnEngine.addNewSingleTimeSpawn(t.getWorldId(), newNpcId, t.getX(), t.getY(), t.getZ(), t.getHeading());
					sign = (Npc) SpawnEngine.spawnObject(t, getPosition().getInstanceId());
					spawns.put(SpawnType.SIGN, sign);
				}
			}
		}
	}

	public boolean isFeePaid() {
		return feePaid;
	}

	public void setFeePaid(boolean feePaid) {
		this.feePaid = feePaid;
	}

	public Timestamp getNextPay() {
		return nextPay;
	}

	public void setNextPay(Timestamp nextPay) {
		this.nextPay = nextPay;
	}

	public Timestamp getSellStarted() {
		return sellStarted;
	}

	public void setSellStarted(Timestamp sellStarted) {
		this.sellStarted = sellStarted;
	}

	public boolean isInGracePeriod() {
        return playerObjectId > 0 && (status == HouseStatus.ACTIVE || status == HouseStatus.SELL_WAIT) && sellStarted != null
            && sellStarted.getTime() < HousingBidService.getAuctionStartTime();
    }

	public synchronized Npc getButler() {
		return spawns.get(SpawnType.MANAGER);
	}

	public Race getPlayerRace() {
		if (getButler() == null)
			return Race.NONE;
		if (getButler().getTribe() == TribeClass.GENERAL)
			return Race.ELYOS;
		return Race.ASMODIANS;
	}

	public synchronized Npc getTeleport() {
		return spawns.get(SpawnType.TELEPORT);
	}

	public synchronized Npc getCurrentSign() {
		return spawns.get(SpawnType.SIGN);
	}

	public synchronized void setSpawn(SpawnType type, Npc npc) {
		if (npc == null) {
			npc = spawns.remove(type);
			if (npc != null)
				npc.getController().onDelete();
		}
		else {
			spawns.put(type, npc);
		}
	}

	public int getCurrentSignNpcId() {
		int npcId = getLand().getWaitingSignNpcId();
		if (status == HouseStatus.NOSALE)
			npcId = getLand().getNosaleSignNpcId();
		else if (status == HouseStatus.SELL_WAIT) {
			if (HousingBidService.isBiddingAllowed())
				npcId = getLand().getSaleSignNpcId();
		}
		else if (playerObjectId != 0 && status == HouseStatus.ACTIVE) {
			npcId = getLand().getHomeSignNpcId();
		}
		return npcId;
	}

	public synchronized boolean revokeOwner() {
		if (playerObjectId == 0)
			return false;
		DAOManager.getDAO(PlayerRegisteredItemsDAO.class).deleteHouseItems(getAddress().getId());
		if (getBuilding().getType() == BuildingType.PERSONAL_INS) {
			HousingService.getInstance().removeStudio(playerObjectId);
			DAOManager.getDAO(HousesDAO.class).deleteStudio(playerObjectId);
			return true;
		}
		houseRegistry = null;
		acquiredTime = null;
		sellStarted = null;
		nextPay = null;
		feePaid = true;

		Building defaultBuilding = getLand().getDefaultBuilding();
		if (defaultBuilding == building) {
			setStatus(HouseStatus.ACTIVE);
			HousingService.getInstance().resetAppearance(this);
		}
		else {
			HousingService.getInstance().switchHouseBuilding(this, defaultBuilding.getId());
		}
		setOwnerId(0);
		return true;
	}

	public HouseRegistry getRegistry() {
		if (houseRegistry == null) {
			houseRegistry = new HouseRegistry(this);
		}
		return houseRegistry;
	}

	public synchronized void moveRequisitesTo(House house) {
		HouseRegistry cleanRegistry = new HouseRegistry(this);
		for (HouseDecoration defaultDecor : houseRegistry.getDefaultParts()) {
			cleanRegistry.putDefaultPart(new HouseDecoration(0, defaultDecor.getTemplate().getId()));
		}

		for (HouseDecoration defaultDecor : house.houseRegistry.getDefaultParts()) {
			HouseDecoration cloned = new HouseDecoration(0, defaultDecor.getTemplate().getId());
			houseRegistry.putDefaultPart(cloned);
			houseRegistry.setPartInUse(cloned);
		}
		house.houseRegistry = houseRegistry;
		house.houseRegistry.setOwner(house);
		houseRegistry = cleanRegistry;
		house.playerScripts = playerScripts;
		playerScripts = null;
		house.spawns = spawns;
		for (Npc npc : house.spawns.values()) {
			if (npc instanceof SummonedHouseNpc) {
				SummonedHouseNpc summon = (SummonedHouseNpc) npc;
				summon.setCreator(house);
			}
		}
		spawns = new HashMap<SpawnType, Npc>(3);
	}

	public HouseDecoration getRenderPart(PartType partType) {
		return getRegistry().getRenderPart(partType);
	}

	public HouseDecoration getDefaultPart(PartType partType) {
		return getRegistry().getDefaultPartByType(partType);
	}

	public PlayerScripts getPlayerScripts() {
		return playerScripts;
	}

	public HouseType getHouseType() {
		return HouseType.fromValue(getBuilding().getSize());
	}

	public synchronized void save() {
		if (persistentState == PersistentState.UPDATED) {
			DAOManager.getDAO(HousesDAO.class).storeHouse(this);
		}
		else {
			DAOManager.getDAO(HousesDAO.class).insertHouse(this);
		}

		houseRegistry.save();
	}

	@Override
	public String toString() {
		return name;
	}

	public PersistentState getPersistentState() {
		return persistentState;
	}

	public void setPersistentState(PersistentState persistentState) {
		this.persistentState = persistentState;
	}

	public byte getHousingFlags() {
		return housingFlags;
	}

	public boolean isInHousingStatus(HousingFlags status) {
		return (housingFlags & status.getId()) != 0;
	}

	public void fixHousingFlags() {
		housingFlags = HousingFlags.SINGLE_HOUSE.getId();
		if (playerObjectId != 0) {
			housingFlags = (byte) (housingFlags | HousingFlags.HAS_OWNER.getId());
			if (status == HouseStatus.ACTIVE) {
				housingFlags = (byte) (housingFlags | HousingFlags.BIDDING_ALLOWED.getId());
				housingFlags = (byte) (housingFlags & (HousingFlags.SINGLE_HOUSE.getId() ^ 0xFFFFFFFF));
			}
		}
		else if (status == HouseStatus.SELL_WAIT) {
			housingFlags = HousingFlags.SELLING_HOUSE.getId();
		}
	}
	
	public String getWelcomeText() {
		if (notification == null)
			return "";
		return notification;
	}

	public void setWelcomeText(String newNotify) {
		this.notification = newNotify;
	}

	public final long getDefaultAuctionPrice() {
		Sale saleOptions = getLand().getSaleOptions();
		switch (getHouseType()) {
			case HOUSE:
				if (saleOptions.getGoldPrice() != HousingConfig.HOUSE_MIN_BID)
					return HousingConfig.HOUSE_MIN_BID;
				break;
			case MANSION:
				if (saleOptions.getGoldPrice() != HousingConfig.MANSION_MIN_BID)
					return HousingConfig.MANSION_MIN_BID;
				break;
			case ESTATE:
				if (saleOptions.getGoldPrice() != HousingConfig.ESTATE_MIN_BID)
					return HousingConfig.ESTATE_MIN_BID;
				break;
			case PALACE:
				if (saleOptions.getGoldPrice() != HousingConfig.PALACE_MIN_BID)
					return HousingConfig.PALACE_MIN_BID;
				break;
			default:
				break;
		}
		return saleOptions.getGoldPrice();
	}
}
