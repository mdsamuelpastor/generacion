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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.Map;

import com.aionemu.gameserver.model.village.VillageLocation;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.VillageService;

/**
 * 
 * @author -Enomine-
 *
 */

public class SM_VILLAGE_LEVEL extends AionServerPacket {
	
	//private Map<Integer, VillageLocation> locations;
	private Map<Integer, VillageLocation> elyLocations;
	private Map<Integer, VillageLocation> asmoLocations;
	
	public SM_VILLAGE_LEVEL() {
		//locations = VillageService.getInstance().getVillageLocations();
		elyLocations = VillageService.getInstance().getElyVillageLocations();
		asmoLocations = VillageService.getInstance().getAsmoVillageLocations();
	}
	
	//TODO: FOr Update later
	//public SM_VILLAGE_LEVEL(VillageLocation loc) {
		//locations = new FastMap<Integer, VillageLocation>();
		//locations.put(loc.getVillageId(), loc);
	//}
	protected void writeImpl(AionConnection con){
		if(con.getActivePlayer().getPosition().getMapId() == 700010000){//Ely Oriel
			//writeH(locations.size());
			writeH(elyLocations.size());
			//for(VillageLocation loc : locations.values()){
		  for(VillageLocation loc : elyLocations.values()){
				writeD(loc.getVillageId());//villageId
				//int villageLevel = loc.getVillageLevel();
				writeD(5);//villageLevel
				//TODO:int lastLevelUp = loc.getLastLevelUp();
				//TODO:writeD(lastLevelUp);//lastLevelUp
				writeD(0);
			}
		}
		else if(con.getActivePlayer().getPosition().getMapId() == 710010000){// Asmo Pernon
			//writeH(locations.size());
			writeH(asmoLocations.size());
			//for(VillageLocation loc : locations.values()){
		  for(VillageLocation loc : asmoLocations.values()){
				writeD(loc.getVillageId());//villageId
				//int villageLevel = loc.getVillageLevel();
				writeD(5);//villageLevel
				//TODO:int lastLevelUp = loc.getLastLevelUp();
				//TODO:writeD(lastLevelUp);//lastLevelUp
				writeD(0);
			}
		}
		else
			writeH(0);
		}
} 