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
package com.aionemu.gameserver.model.gameobjects;

import com.aionemu.gameserver.controllers.PlaceableObjectController;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.IExpirable;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.templates.housing.AbstractHouseObject;
import com.aionemu.gameserver.model.templates.housing.HouseType;
import com.aionemu.gameserver.model.templates.housing.HousingCategory;
import com.aionemu.gameserver.model.templates.housing.LimitType;
import com.aionemu.gameserver.model.templates.housing.PlaceArea;
import com.aionemu.gameserver.model.templates.housing.PlaceLocation;
import com.aionemu.gameserver.model.templates.housing.PlaceableHouseObject;
import com.aionemu.gameserver.model.templates.item.ItemQuality;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.PlayerAwareKnownList;

/**
 * 
 * @author Maestross, Fennek
 *
 * @param <T>
 */
public abstract class HouseObject<T extends PlaceableHouseObject> extends VisibleObject implements IExpirable {

	private int expireEnd;
	private int itemColor = 0;
	private float x;
	private float y;
	private float z;
	private byte heading;
	private int ownerUsedCount = 0;
	private int visitorUsedCount = 0;
	private House ownerHouse;
	private PersistentState persistentState = PersistentState.NEW;

	public HouseObject(House owner, int objId, int templateId) {
		super(objId, new PlaceableObjectController<T>(), null, DataManager.HOUSING_OBJECT_DATA.getTemplateById(templateId), null);

		ownerHouse = owner;
		getController().setOwner(this);
		setKnownlist(new PlayerAwareKnownList(this));
	}

	public PersistentState getPersistentState() {
		return persistentState;
	}

	public void setPersistentState(PersistentState persistentState) {
		// TODO Need to confirm
		switch (persistentState) {
			case DELETED:
				if (this.persistentState == PersistentState.NEW) {
					this.persistentState = PersistentState.NOACTION;
				}
				else if (this.persistentState != PersistentState.DELETED) {
					this.persistentState = PersistentState.DELETED;
					ownerHouse.getRegistry().setPersistentState(PersistentState.UPDATE_REQUIRED);
				}
				break;
			case UPDATE_REQUIRED:
				if (this.persistentState == PersistentState.NEW)
					break;
			default:
				if (this.persistentState != persistentState) {
					this.persistentState = persistentState;
					ownerHouse.getRegistry().setPersistentState(PersistentState.UPDATE_REQUIRED);
				}
				break;
		}
	}
	
	/**
	 * @return the itemColor
	 */
	public int getItemColor() {
		return itemColor;
	}

	/**
	 * @param itemColor
	 *          the itemColor to set
	 */
	public void setItemColor(int itemColor) {
		this.itemColor = itemColor;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}

	@Override
	public int getExpireTime() {
		return expireEnd;
	}

	public void setExpireTime(int time) {
		expireEnd = time;
	}

	@Override
	public void expireEnd(Player player) {
		setPersistentState(PersistentState.DELETED);
	}

	public int getUseSecondsLeft() {
		if (expireEnd == 0)
			return -1;
		int diff = expireEnd - (int) (System.currentTimeMillis() / 1000L);
		if (diff < 0)
			return 0;
		return diff;
	}

	@Override
	public void expireMessage(Player player, int time) {
	}

	@Override
	public String getName() {
		return String.valueOf(objectTemplate.getNameId());
	}

	@Override
	public T getObjectTemplate() {
		return (T) objectTemplate;
	}

	@Override
	public float getX() {
		return x;
	}

	public void setX(float x) {
		if (this.x != x) {
			this.x = x;
			setPersistentState(PersistentState.UPDATE_REQUIRED);
			if (position != null)
				position.setXYZH(Float.valueOf(x), null, null, null);
		}
	}

	@Override
	public float getY() {
		return y;
	}

	public void setY(float y) {
		if (this.y != y) {
			this.y = y;
			setPersistentState(PersistentState.UPDATE_REQUIRED);
			if (position != null)
				position.setXYZH(null, Float.valueOf(y), null, null);
		}
	}

	@Override
	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		if (this.z != z) {
			this.z = z;
			setPersistentState(PersistentState.UPDATE_REQUIRED);
			if (position != null)
				position.setXYZH(null, null, Float.valueOf(z), null);
		}
	}

	@Override
	public byte getHeading() {
		return heading;
	}

	public void setHeading(byte heading) {
		if (this.heading != heading) {
			this.heading = heading;
			setPersistentState(PersistentState.UPDATE_REQUIRED);
			if (position != null)
				position.setXYZH(null, null, null, Byte.valueOf(heading));
		}
	}

	public int getRotation() {
		int rotation = heading & 0xFF;
		return rotation * 3;
	}

	public void setRotation(int rotation) {
		setHeading((byte) (int) Math.ceil(rotation / 3.0F));
	}

	public PlaceLocation getPlaceLocation() {
		return ((PlaceableHouseObject) objectTemplate).getLocation();
	}

	public PlaceArea getPlaceArea() {
		return ((PlaceableHouseObject) objectTemplate).getArea();
	}

	public int getPlacementLimit(boolean trial) {
		LimitType limitType = ((PlaceableHouseObject) objectTemplate).getPlacementLimit();
		HouseType size = HouseType.fromValue(ownerHouse.getBuilding().getSize());
		if (trial)
			return limitType.getTrialObjectPlaceLimit(size);
		return limitType.getObjectPlaceLimit(size);
	}

	public ItemQuality getQuality() {
		return ((AbstractHouseObject) objectTemplate).getQuality();
	}

	public float getTalkingDistance() {
		return ((AbstractHouseObject) objectTemplate).getTalkingDistance();
	}

	public HousingCategory getCategory() {
		return ((AbstractHouseObject) objectTemplate).getCategory();
	}

	public House getOwnerHouse() {
		return ownerHouse;
	}

	public int getPlayerId() {
		return ownerHouse.getOwnerId();
	}

	public int getOwnerUsedCount() {
		return ownerUsedCount;
	}

	public void incrementOwnerUsedCount() {
		ownerUsedCount += 1;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}

	public void incrementVisitorUsedCount() {
		visitorUsedCount += 1;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}

	public void setOwnerUsedCount(int ownerUsedCount) {
		if (this.ownerUsedCount != ownerUsedCount) {
			this.ownerUsedCount = ownerUsedCount;
			setPersistentState(PersistentState.UPDATE_REQUIRED);
		}
	}

	public int getVisitorUsedCount() {
		return visitorUsedCount;
	}

	public void setVisitorUsedCount(int visitorUsedCount) {
		if (this.visitorUsedCount != visitorUsedCount) {
			this.visitorUsedCount = visitorUsedCount;
			setPersistentState(PersistentState.UPDATE_REQUIRED);
		}
	}

	public boolean isSpawnedByPlayer() {
		return (x != 0.0F) || (y != 0.0F) || (z != 0.0F);
	}

	@Override
	public PlaceableObjectController<T> getController() {
		return (PlaceableObjectController<T>) super.getController();
	}

	public void spawn() {
		if (!isSpawnedByPlayer())
			return;
		World w = World.getInstance();
		if ((position == null) || (!isSpawned())) {
			position = w.createPosition(ownerHouse.getWorldId(), x, y, z, heading, ownerHouse.getInstanceId());
			SpawnEngine.bringIntoWorld(this);
		}
		updateKnownlist();
	}

	public void removeFromHouse() {
		setX(0.0F);
		setY(0.0F);
		setZ(0.0F);
		setHeading((byte) 0);
	}

	public void onUse(Player player) {
	}

	public void onDialogRequest(Player player) {
		onUse(player);
	}

	public void onDespawn() {
	}
}