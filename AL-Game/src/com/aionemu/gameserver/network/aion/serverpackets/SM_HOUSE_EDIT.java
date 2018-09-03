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

import com.aionemu.gameserver.model.gameobjects.HouseDecoration;
import com.aionemu.gameserver.model.gameobjects.HouseObject;
import com.aionemu.gameserver.model.gameobjects.UseableItemObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Maestross, A7Xatomic, Fennek
 */
 
public class SM_HOUSE_EDIT extends AionServerPacket {

	private int action;
	private int storeId;
	private int itemObjectId;
	private float x;
	private float y;
	private float z;
	private int rotation;
	public SM_HOUSE_EDIT(int action) {
		this.action = action;
	}
	
	public SM_HOUSE_EDIT(int action, int itemObjectId) {
		this.action = action;
		this.itemObjectId = itemObjectId;
	}

	public SM_HOUSE_EDIT(int action, int storeId, int itemObjectId) {
		this(action);
		this.itemObjectId = itemObjectId;
		this.storeId = storeId;
	}

	public SM_HOUSE_EDIT(int action, House address, int playerObjId, int itemObjectId, int templateId, float x, float y, float z, int rotation) {
		this(action);
		this.itemObjectId = itemObjectId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.rotation = rotation;
		
	}
	public SM_HOUSE_EDIT(int action, int itemObjectId, float x, float y, float z, int rotation) {
		this.action = action;
		this.itemObjectId = itemObjectId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.rotation = rotation;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		Player player = con.getActivePlayer();
		if ((player == null) || (player.getHouseRegistry() == null)) {
			return;
		}
		HouseObject<?> obj = player.getHouseRegistry().getObjectByObjId(itemObjectId);
		
		writeC(action);
        switch (action) {
        	case 1://checked
        	case 2://checked
        	break;
		    case 3:
			    int templateId = 0;
			    int typeId = 0;
			    if (obj == null) {
				    HouseDecoration deco = player.getHouseRegistry().getCustomPartByObjId(itemObjectId);
				    templateId = deco.getTemplate().getId();
			    }
			    else {
				    templateId = obj.getObjectTemplate().getTemplateId();
				    typeId = obj.getObjectTemplate().getTypeId();
			    }
			    writeC(storeId);
			    writeD(itemObjectId);
			    writeD(templateId);
			    if ((obj != null) && (obj.getUseSecondsLeft() > 0))
				    writeD(obj.getUseSecondsLeft());
			    else	{
				    writeD(0);
			    }
			    writeD(0);
			    writeD(0);
			    writeC(typeId);
				
				if ((obj != null) && ((obj instanceof UseableItemObject))) {
				    writeD(player.getObjectId());
				    ((UseableItemObject) obj).writeUsageData(getBuf());
			    }
				break;//checked
			case 4:
				writeC(storeId);
			    writeD(itemObjectId);
			    break;//checked
			case 5:
				  writeD(0);//new
			    writeD(player.getCommonData().getPlayerObjId());
			    writeD(itemObjectId);
			    writeD(obj.getObjectTemplate().getTemplateId());
				  writeF(x);
			    writeF(y);
			    writeF(z);
			    writeH(rotation);
			    writeD(player.getHouseObjectCooldownList().getReuseDelay(itemObjectId));
				if (obj.getUseSecondsLeft() > 0)
				    writeD(obj.getUseSecondsLeft());
			    else
			    {
				    writeD(0);
			    }
				if(obj.getItemColor() > 0)
				{
				writeD(obj.getItemColor());
				}
				else
				{
					writeD(0);
				}
				writeD(0);
                writeC(obj.getObjectTemplate().getTypeId());
				
				if ((obj instanceof UseableItemObject)) {
				    ((UseableItemObject) obj).writeUsageData(getBuf());
			    }
				break;//checked
			case 7://checked
			    writeD(itemObjectId);
			    break;//checked
			case 6:
		}
	}
}
