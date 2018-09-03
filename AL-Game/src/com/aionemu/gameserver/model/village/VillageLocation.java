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
package com.aionemu.gameserver.model.village;

import com.aionemu.gameserver.model.templates.village.VillageLocationTemplate;

/**
 * 
 * @author -Enomine-
 *
 */

public class VillageLocation { 
	
	protected VillageLocationTemplate template;
	protected int villageId;
	protected int villageLevel;
	//protected int lastlevelUp;
	
	public VillageLocation() {
	}
	
	
	public VillageLocation(VillageLocationTemplate template) {
		this.template = template;
		this.villageId = template.getId();
	}
	
	public VillageLocationTemplate getTemplate() {
		return template;
	}
	
	public int getVillageId() {
		return this.villageId;
	}
	
	public int getVillageLevel(){
		return this.villageLevel;
	}
	
	public int setVillageLevel(int villageLevel){
		return this.villageLevel = villageLevel;
	}
	
	//public int getLastLevelUp(){
	//	return this.lastlevelUp;
		//}
	
	//public int setLastLevelUp(int lastLevelUp){
	//	return this.lastlevelUp = lastLevelUp;
	//}
}
