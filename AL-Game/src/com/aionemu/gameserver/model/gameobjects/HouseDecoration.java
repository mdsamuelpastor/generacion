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

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.housing.HousePart;

public class HouseDecoration extends AionObject {

	private int templateId;
	private boolean isUsed;
	private PersistentState persistentState;

	public HouseDecoration(int objectId, int templateId) {
		super(objectId);
		this.templateId = templateId;
		persistentState = PersistentState.NEW;
	}

	public HousePart getTemplate() {
		return DataManager.HOUSE_PARTS_DATA.getPartById(templateId);
	}

	public PersistentState getPersistentState() {
		return persistentState;
	}

	public void setPersistentState(PersistentState persistentState) {
		this.persistentState = persistentState;
	}

	@Override
	public String getName() {
		return getTemplate().getName();
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		if (this.isUsed != isUsed && persistentState != PersistentState.DELETED) {
			this.isUsed = isUsed;
			if (persistentState != PersistentState.NEW)
				persistentState = PersistentState.UPDATE_REQUIRED;
		}
	}
}
