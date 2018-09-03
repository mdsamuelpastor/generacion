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
import java.util.Set;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;

/**
 * This blob is sending info about the item that were fused with 
 * current item.
 * 
 * @author -Nemesiss-
 *
 */
public class CompositeItemBlobEntry extends ItemBlobEntry{

	CompositeItemBlobEntry() {
		super(ItemBlobType.COMPOSITE_ITEM);
	}

	@Override
	public void writeThisBlob(ByteBuffer buf) {
		Item item = parent.item;

		writeD(buf, item.getFusionedItemId());
		writeFusionStones(buf);
		writeH(buf, item.hasOptionalFusionSocket() ? item.getOptionalFusionSocket() : 0x00);
/*		writeH(buf, 6);
		writeH(buf, 0);
		writeH(buf, 256);
		writeH(buf, 3);
		writeH(buf, 0);
		writeH(buf, 0);
		writeH(buf, 0);*/
	}

	private void writeFusionStones(ByteBuffer buf) {
		Item item = parent.item;
		int count = 0;

		if (item.hasFusionStones()) {
			Set<ManaStone> itemStones = item.getFusionStones();

			for (ManaStone itemStone : itemStones) {
				if (count == 6)
					break;

				StatFunction modifier = itemStone.getFirstModifier();
				if (modifier != null) {
					count++;
					writeD(buf, itemStone.getItemId());
				}
			}
			skip(buf, (6 - count) * 4);
		}
		else {
			skip(buf, 24);
		}
		// for now max 6 stones - write some junk
	}
}
