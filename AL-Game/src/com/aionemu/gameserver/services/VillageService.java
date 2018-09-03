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

import java.util.Map;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.VillageDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.village.VillageLocation;
import com.aionemu.gameserver.network.aion.serverpackets.SM_VILLAGE_LEVEL;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * 
 * @author -Enomine-
 *
 */

public class VillageService {
	
	private static final VillageService instance = new VillageService();
	
	private Map<Integer, VillageLocation> locations;
	private Map<Integer, VillageLocation> elyLocations;
	private Map<Integer, VillageLocation> asmoLocations;
	
	public static VillageService getInstance() {
		return instance;
	}
	
	public void initVillageLocations() {
		elyLocations = DataManager.VILLAGE_LOCATION_DATA.getElyVillageLocations();
		asmoLocations = DataManager.VILLAGE_LOCATION_DATA.getAsmoVillageLocations();
		locations = DataManager.VILLAGE_LOCATION_DATA.getVillageLocations();
		DAOManager.getDAO(VillageDAO.class).loadVillageLocations(locations);
		}
	public Map<Integer, VillageLocation> getElyVillageLocations(){
		return elyLocations;
	}
	
	public Map<Integer, VillageLocation> getAsmoVillageLocations(){
		return asmoLocations;
	}
	
	public Map<Integer, VillageLocation> getVillageLocations() {
		return locations;
	}

	public VillageLocation getVillageLocation(int villageId) {
		return locations.get(villageId);
	}
	
	public void onPlayerLogin(Player player) {
			PacketSendUtility.sendPacket(player, new SM_VILLAGE_LEVEL());
		}
}


	

