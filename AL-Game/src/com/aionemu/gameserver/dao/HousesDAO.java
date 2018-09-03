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
package com.aionemu.gameserver.dao;

import java.util.Collection;
import java.util.Map;

import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.templates.housing.HousingLand;

public abstract class HousesDAO implements IDFactoryAwareDAO {

	@Override
	public String getClassName() {
		return HousesDAO.class.getName();
	}

	@Override
	public abstract boolean supports(String paramString, int paramInt1, int paramInt2);

	public abstract boolean isIdUsed(int paramInt);

	public abstract void storeNewHouse(House paramHouse);

	public abstract void storeHouse(House paramHouse);

	public abstract void insertHouse(House paramHouse);

	public abstract Map<Integer, Map<Integer, House>> loadHouses(Collection<HousingLand> paramCollection, boolean paramBoolean);

	public abstract void deleteStudio(int paramInt);
}
