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
package com.aionemu.gameserver.dataholders;

import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import javolution.util.FastMap;

import com.aionemu.gameserver.model.templates.village.VillageLocationTemplate;
import com.aionemu.gameserver.model.village.VillageLocation;

/**
 * @author -Enomine-
 */

@XmlRootElement(name = "village_locations")
@XmlAccessorType(XmlAccessType.FIELD)
public class VillageLocationData {
	
	@XmlElement(name = "village_location")
	private List<VillageLocationTemplate> villageLocationTemplates;
	@XmlTransient
	private FastMap<Integer, VillageLocation> villageLocations = new FastMap<Integer, VillageLocation>();
	@XmlTransient
	private FastMap<Integer, VillageLocation> elyLocations = new FastMap<Integer, VillageLocation>();
	@XmlTransient
	private FastMap<Integer, VillageLocation> asmoLocations = new FastMap<Integer, VillageLocation>();
	
	void afterUnmarshal(Unmarshaller u, Object parent) {
		villageLocations.clear();
		elyLocations.clear();
		asmoLocations.clear();
		for (VillageLocationTemplate template : villageLocationTemplates){
			switch(template.getRace()){
			case ELYOS:
				VillageLocation elyosTown = new VillageLocation(template);
				elyLocations.put(template.getId(), elyosTown);
				villageLocations.put(template.getId(), elyosTown);
				break;
			case ASMODIANS:
				VillageLocation asmoTown = new VillageLocation(template);
				asmoLocations.put(template.getId(), asmoTown);
				villageLocations.put(template.getId(), asmoTown);
				break;	
			default:
				break;
			}
		}
	}
	
	public int size() {
		return villageLocations.size();
	}

	public FastMap<Integer, VillageLocation> getVillageLocations() {
		return villageLocations;
	}

	public Map<Integer, VillageLocation> getElyVillageLocations() {
		return elyLocations;
	}

	public Map<Integer, VillageLocation> getAsmoVillageLocations() {
		return asmoLocations;
	}
}
