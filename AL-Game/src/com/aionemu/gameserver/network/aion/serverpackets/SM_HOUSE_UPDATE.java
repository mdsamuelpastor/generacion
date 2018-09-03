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

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.HouseDecoration;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.templates.housing.BuildingType;
import com.aionemu.gameserver.model.templates.housing.HouseAddress;
import com.aionemu.gameserver.model.templates.housing.PartType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.HousingService;

/**
 * @author Maestross, A7Xatomic
 */
 
public class SM_HOUSE_UPDATE extends AionServerPacket {

	private HouseAddress address;
	private int buildingId;
	private int playerObjectId;

	public SM_HOUSE_UPDATE(HouseAddress address, int buildingId, int ownerId) {
		this.address = address;
		this.buildingId = buildingId;
		playerObjectId = ownerId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(1);
		writeH(0);
		writeH(1);

		writeD(0);
		writeD(address.getId());
		writeD(playerObjectId);

		House house = HousingService.getInstance().getPlayerStudio(playerObjectId);
		if (house == null) {
			house = HousingService.getInstance().getHouseByAddress(address.getId());
		}
		boolean isPersonal = house.getBuilding().getType() == BuildingType.PERSONAL_INS;
		writeD(isPersonal ? 1 : 2);
		writeC(0);
		writeD(buildingId);//new
		writeC(house.getHousingFlags());

		int doorState = house.getSettingFlags() >> 8;

		if (house.getSettingFlags() == 0) {
			writeC(isPersonal ? 1 : 3);
		}
		else {
			writeC(doorState);
		}
		writeS(house.getOwnerName());
    writeB(new byte[53 - (house.getOwnerName().length() * 2 + 2)]);
    writeS(house.getWelcomeText());
    writeB(new byte[130 - house.getWelcomeText().length() * 2 + 2]);
		writePartData(house, PartType.ROOF, true);
		writePartData(house, PartType.OUTWALL, true);
		writePartData(house, PartType.FRAME, true);
		writePartData(house, PartType.DOOR, true);
		writePartData(house, PartType.GARDEN, true);
		writePartData(house, PartType.FENCE, true);
		writePartData(house, PartType.INWALL_ANY, false);
		HouseDecoration defaultDecor = house.getDefaultPart(PartType.INWALL_ANY);
		writeD(defaultDecor == null ? 0 : defaultDecor.getTemplate().getId());
		writeD(defaultDecor == null ? 0 : defaultDecor.getTemplate().getId());
		writeD(defaultDecor == null ? 0 : defaultDecor.getTemplate().getId());
		writeD(defaultDecor == null ? 0 : defaultDecor.getTemplate().getId());
		writeD(defaultDecor == null ? 0 : defaultDecor.getTemplate().getId());
		writePartData(house, PartType.INFLOOR_ANY, false);
		defaultDecor = house.getDefaultPart(PartType.INFLOOR_ANY);
		writeD(defaultDecor == null ? 0 : defaultDecor.getTemplate().getId());
		writeD(defaultDecor == null ? 0 : defaultDecor.getTemplate().getId());
		writeD(defaultDecor == null ? 0 : defaultDecor.getTemplate().getId());
		writeD(defaultDecor == null ? 0 : defaultDecor.getTemplate().getId());
		writeD(defaultDecor == null ? 0 : defaultDecor.getTemplate().getId());
		writePartData(house, PartType.ADDON, true);
		writeD(0);
		writeD(0);
		writeD(0);
		writeH(0);

		if (doorState == 2 && house.getPlayerRace() != Race.NONE && house.getLand().getCapabilities().getEmblemId() != 0) {
			writeC(house.getPlayerRace() == Race.ASMODIANS ? 2 : 1);
		}
		else {
			writeC(0);
		}
	}

	private void writePartData(House house, PartType partType, boolean skipPersonal) {
		boolean isPersonal = house.getBuilding().getType() == BuildingType.PERSONAL_INS;
		HouseDecoration deco = house.getRenderPart(partType);
		if (skipPersonal && isPersonal)
			writeD(0);
		else if (deco != null)
			writeD(deco.getTemplate().getId());
		else
			writeD(0);
	}
}
