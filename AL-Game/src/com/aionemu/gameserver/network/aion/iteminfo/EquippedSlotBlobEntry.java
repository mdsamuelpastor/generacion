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
package com.aionemu.gameserver.network.aion.iteminfo;

import java.nio.ByteBuffer;

import com.aionemu.gameserver.model.templates.item.EquipType;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;

/**
 * This block is sent for all items that can be equipped. If item is equipped. This block says to which slot it's
 * equipped. If not, then it says 0.
 * 
 * @author -Nemesiss-, Maestross
 *
 */
public class EquippedSlotBlobEntry extends ItemBlobEntry{

	EquippedSlotBlobEntry() {
		super(ItemBlobType.EQUIPPED_SLOT);
	}

	@Override
	public
	void writeThisBlob(ByteBuffer buf) {
		Item item = parent.item;
        if(item.getEquipmentType() != EquipType.STIGMA){
            writeD(buf, item.isEquipped() ? item.getEquipmentSlotInteger() : 0x00);
            writeD(buf, 0);
        }else{
            writeQ(buf, item.isEquipped() ? item.getEquipmentSlot() : 0x00);
        }
	}
}
