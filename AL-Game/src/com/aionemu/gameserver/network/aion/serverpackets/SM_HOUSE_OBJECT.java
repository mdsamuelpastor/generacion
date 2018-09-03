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

import com.aionemu.gameserver.model.gameobjects.HouseObject;
import com.aionemu.gameserver.model.gameobjects.NpcObject;
import com.aionemu.gameserver.model.gameobjects.UseableItemObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * 
 * @author Maestross, Fennek
 *
 */
public class SM_HOUSE_OBJECT extends AionServerPacket {

	HouseObject<?> houseObject;

	public SM_HOUSE_OBJECT(HouseObject<?> owner) {
		houseObject = owner;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		Player player = con.getActivePlayer();
		if (player == null) {
			return;
		}
		House house = houseObject.getOwnerHouse();
		int templateId = houseObject.getObjectTemplate().getTemplateId();
		
		writeD(house.getAddress().getId());//new
		writeD(player.getCommonData().getPlayerObjId());
		writeD(houseObject.getObjectId());
		writeD(houseObject.getItemColor());
		writeD(templateId);
		writeF(houseObject.getX());
		writeF(houseObject.getY());
		writeF(houseObject.getZ());
		writeH(houseObject.getRotation());

		writeD(player.getHouseObjectCooldownList().getReuseDelay(houseObject.getObjectId()));
		if (houseObject.getUseSecondsLeft() > 0)
			writeD(houseObject.getUseSecondsLeft());
		else {
			writeD(0);
		}
		if(houseObject.getItemColor() > 0)
		{
		writeD(houseObject.getItemColor());
		}
		else
		{
			writeD(0);
		}
		writeD(0);
    byte typeId = houseObject.getObjectTemplate().getTypeId();
		writeC(typeId);

		switch (typeId) {
			case 0:
			case 2:
			case 5:
			break;
			case 1:
				((UseableItemObject) houseObject).writeUsageData(getBuf());
				break;
			case 7:
				NpcObject npcObj = (NpcObject) houseObject;
				writeD(npcObj.getNpcObjectId());
		}
	}
}