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

import java.util.ArrayList;
import java.util.List;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerRegisteredItemsDAO;
import com.aionemu.gameserver.model.gameobjects.HouseDecoration;
import com.aionemu.gameserver.model.gameobjects.HouseObject;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.templates.housing.PartType;

public class HouseRegistry {

	private static final Logger log = LoggerFactory.getLogger(HouseRegistry.class);
	private House owner;
	private FastMap<Integer, HouseObject<?>> objects;
	private FastMap<Integer, HouseDecoration> customParts;
	private FastMap<PartType, List<HouseDecoration>> defaultParts;
	private PersistentState persistentState = PersistentState.UPDATED;

	public HouseRegistry(House owner) {
		this.owner = owner;
		objects = FastMap.newInstance();
		customParts = FastMap.newInstance();
		defaultParts = FastMap.newInstance();
		for (PartType type : PartType.values())
			defaultParts.put(type, new ArrayList<HouseDecoration>());
	}

	public House getOwner() {
		return owner;
	}

	public void setOwner(House newModel) {
		owner = newModel;
	}

	public FastList<HouseObject<?>> getObjects() {
		FastList<HouseObject<?>> temp = FastList.newInstance();
		for (HouseObject<?> obj : objects.values()) {
			temp.add(obj);
		}
		return temp;
	}

	public FastList<HouseObject<?>> getSpawnedObjects() {
		FastList<HouseObject<?>> temp = FastList.newInstance();
		for (HouseObject<?> obj : objects.values()) {
			if (obj.isSpawnedByPlayer() && obj.getPersistentState() != PersistentState.DELETED)
				temp.add(obj);
		}
		return temp;
	}

	public FastList<HouseObject<?>> getNotSpawnedObjects() {
		FastList<HouseObject<?>> temp = FastList.newInstance();
		for (HouseObject<?> obj : objects.values()) {
			if (!obj.isSpawnedByPlayer() && obj.getPersistentState() != PersistentState.DELETED)
				temp.add(obj);
		}
		return temp;
	}

	public HouseObject<?> getObjectByObjId(int itemObjId) {
		return objects.get(itemObjId);
	}

	public boolean putObject(HouseObject<?> houseObject) {
		if (objects.containsKey(houseObject.getObjectId())) {
			return false;
		}
		if (houseObject.getPersistentState() != PersistentState.NEW) {
			log.error("Inserting not new HouseObject: " + houseObject.getObjectId());
			return false;
		}

		objects.put(houseObject.getObjectId(), houseObject);
		setPersistentState(PersistentState.UPDATE_REQUIRED);
		return true;
	}

	public HouseObject<?> removeObject(int itemObjId) {
		if (!objects.containsKey(Integer.valueOf(itemObjId))) {
			return null;
		}
		HouseObject<?> oldObject = objects.get(itemObjId);
		if (oldObject.getPersistentState() == PersistentState.NEW)
			discardObject(itemObjId);
		else
			oldObject.setPersistentState(PersistentState.DELETED);
		setPersistentState(PersistentState.UPDATE_REQUIRED);

		return oldObject;
	}

	public FastList<HouseDecoration> getCustomParts() {
		FastList<HouseDecoration> temp = FastList.newInstance();
		for (HouseDecoration decor : customParts.values()) {
			if (decor.getPersistentState() != PersistentState.DELETED)
				temp.add(decor);
		}
		return temp;
	}

	public HouseDecoration getCustomPartByType(PartType partType) {
		for (HouseDecoration deco : customParts.values()) {
			if (deco.getPersistentState() != PersistentState.DELETED && deco.getTemplate().getType() == partType) {
				return deco;
			}
		}
		return null;
	}

	public HouseDecoration getCustomPartByObjId(int itemObjId) {
		return customParts.get(itemObjId);
	}

	public HouseDecoration getCustomPartByPartId(int partId) {
		for (HouseDecoration deco : customParts.values()) {
			if (deco.getPersistentState() != PersistentState.DELETED && deco.getTemplate().getId() == partId) {
				return deco;
			}
		}
		return null;
	}

	public int getCustomPartCountByPartId(int partId) {
		int counter = 0;
		for (HouseDecoration deco : customParts.values()) {
			if (deco.getPersistentState() != PersistentState.DELETED && deco.getTemplate().getId() == partId) {
				counter++;
			}
		}
		return counter;
	}

	public boolean putCustomPart(HouseDecoration houseDeco) {
		if (customParts.containsKey(houseDeco.getObjectId()))
			return false;
		if (houseDeco.getPersistentState() != PersistentState.NEW) {
			log.error("Inserting not new HouseDecoration: " + houseDeco.getObjectId());
			return false;
		}

		customParts.put(houseDeco.getObjectId(), houseDeco);
		setPersistentState(PersistentState.UPDATE_REQUIRED);
		return true;
	}

	public HouseDecoration removeCustomPart(int itemObjId) {
		HouseDecoration obj = null;

		if (customParts.containsKey(itemObjId)) {
			obj = customParts.get(itemObjId);
			obj.setPersistentState(PersistentState.DELETED);
			setPersistentState(PersistentState.UPDATE_REQUIRED);
		}
		return obj;
	}

	public FastList<HouseDecoration> getDefaultParts() {
		FastList<HouseDecoration> temp = FastList.newInstance();
		for (List<HouseDecoration> partTypeEntries : defaultParts.values()) {
			for (HouseDecoration decor : partTypeEntries) {
				if (decor.getPersistentState() != PersistentState.DELETED) {
					temp.add(decor);
					break;
				}
			}
		}
		return temp;
	}

	public HouseDecoration getDefaultPartByType(PartType partType) {
		for (HouseDecoration decor : defaultParts.get(partType)) {
			if (decor.getPersistentState() != PersistentState.DELETED) {
				return decor;
			}
		}
		return null;
	}

	public boolean putDefaultPart(HouseDecoration houseDeco) {
		if (houseDeco.getPersistentState() != PersistentState.NEW) {
			log.error("Inserting not new HouseDecoration: " + houseDeco.getObjectId());
			return false;
		}

		List<HouseDecoration> defaultPartsForType = defaultParts.get(houseDeco.getTemplate().getType());
		HouseDecoration remove = null;
		for (HouseDecoration oldDeco : defaultPartsForType) {
			if (oldDeco.getPersistentState() == PersistentState.NEW)
				remove = oldDeco;
			else
				oldDeco.setPersistentState(PersistentState.DELETED);
		}
		if (remove != null) {
			discardPart(remove);
		}
		defaultPartsForType.add(houseDeco);
		setPersistentState(PersistentState.UPDATE_REQUIRED);
		return true;
	}

	public FastList<HouseDecoration> getAllParts() {
		FastList<HouseDecoration> temp = FastList.newInstance();
		for (List<HouseDecoration> partTypeEntries : defaultParts.values()) {
			for (HouseDecoration decor : partTypeEntries) {
				temp.add(decor);
			}
		}
		for (HouseDecoration decor : customParts.values()) {
			temp.add(decor);
		}
		return temp;
	}

	public HouseDecoration getRenderPart(PartType partType) {
		for (HouseDecoration decor : customParts.values()) {
			if (decor.getPersistentState() != PersistentState.DELETED && decor.getTemplate().getType() == partType && decor.isUsed()) {
				return decor;
			}
		}
		for (HouseDecoration decor : defaultParts.get(partType)) {
			if (decor.getPersistentState() != PersistentState.DELETED && decor.isUsed()) {
				return decor;
			}
		}

		HouseDecoration decor = getDefaultPartByType(partType);
		if (decor != null)
			decor.setUsed(true);
		return decor;
	}

	public void setPartInUse(HouseDecoration decorationUse) {
		for (HouseDecoration decor : defaultParts.get(decorationUse.getTemplate().getType())) {
			if (decor.getPersistentState() != PersistentState.DELETED) {
				decor.setUsed(decorationUse.equals(decor));
			}
		}
		for (HouseDecoration decor : customParts.values())
			if (decor.getPersistentState() != PersistentState.DELETED && decor.getTemplate().getType() == decorationUse.getTemplate().getType()) {
				if (decorationUse.equals(decor)) {
					decor.setUsed(true);
				}
				else {
					if (decor.isUsed())
						decor.setPersistentState(PersistentState.DELETED);
					decor.setUsed(false);
				}
			}
	}

	public void discardObject(Integer objectId) {
		objects.remove(objectId);
	}

	public void discardPart(HouseDecoration decor) {
		List<HouseDecoration> parts = defaultParts.get(decor.getTemplate().getType());
		if (parts.remove(decor))
			return;
		customParts.remove(decor.getObjectId());
	}

	public void save() {
		if (persistentState == PersistentState.UPDATE_REQUIRED)
			DAOManager.getDAO(PlayerRegisteredItemsDAO.class).store(this, getOwner().getOwnerId(), getOwner().getAddress().getId());
	}

	public final PersistentState getPersistentState() {
		return persistentState;
	}

	public final void setPersistentState(PersistentState persistentState) {
		this.persistentState = persistentState;
	}

	public int size() {
		return objects.size() + customParts.size();
	}
}
