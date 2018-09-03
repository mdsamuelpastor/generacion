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
package com.aionemu.gameserver.services.item;

import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.trade.PricesService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class ItemRemodelService {

	public static void remodelItem(Player player, int keepItemObjId, int extractItemObjId) {
		Storage inventory = player.getInventory();
		Item keepItem = inventory.getItemByObjId(keepItemObjId);
		Item extractItem = inventory.getItemByObjId(extractItemObjId);

		long remodelCost = PricesService.getPriceForService(1000L, player.getRace());

		if ((keepItem == null) || (extractItem == null)) {
			return;
		}

		if (player.getLevel() < 20) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CHANGE_ITEM_SKIN_PC_LEVEL_LIMIT);
			return;
		}

		if (player.getInventory().getKinah() < remodelCost) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CHANGE_ITEM_SKIN_NOT_ENOUGH_GOLD(new DescriptionId(keepItem.getItemTemplate().getNameId())));
			return;
		}

		if (extractItem.getItemTemplate().getTemplateId() == 168100000) {
			if (keepItem.getItemTemplate() == keepItem.getItemSkinTemplate()) {
				PacketSendUtility.sendMessage(player, "That item does not have a remodeled skin to remove.");
				return;
			}

			player.getInventory().decreaseKinah(remodelCost);

			player.getInventory().decreaseItemCount(extractItem, 1L);

			keepItem.setItemSkinTemplate(keepItem.getItemTemplate());

			if (!keepItem.getItemTemplate().isItemDyePermitted()) {
				keepItem.setItemColor(0);
			}

			ItemPacketService.updateItemAfterInfoChange(player, keepItem);
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CHANGE_ITEM_SKIN_SUCCEED(new DescriptionId(keepItem.getItemTemplate().getNameId())));
			return;
		}

		if ((keepItem.getItemTemplate().getWeaponType() != extractItem.getItemSkinTemplate().getWeaponType())
			|| ((extractItem.getItemSkinTemplate().getArmorType() != ArmorType.CLOTHES) && (keepItem.getItemTemplate().getArmorType() != extractItem.getItemSkinTemplate().getArmorType()))
			|| (keepItem.getItemTemplate().getArmorType() == ArmorType.CLOTHES) || (keepItem.getItemTemplate().getItemSlot() != extractItem.getItemSkinTemplate().getItemSlot())) {
			PacketSendUtility.sendPacket(player,
				SM_SYSTEM_MESSAGE.STR_CHANGE_ITEM_SKIN_NOT_COMPATIBLE(new DescriptionId(keepItem.getItemTemplate().getNameId()), new DescriptionId(extractItem.getItemSkinTemplate().getNameId())));
			return;
		}

		if (!keepItem.isRemodelable(player)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300478, new Object[] { new DescriptionId(keepItem.getItemTemplate().getNameId()) }));

			return;
		}

		if (!extractItem.isRemodelable(player)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300482, new Object[] { new DescriptionId(extractItem.getItemTemplate().getNameId()) }));

			return;
		}

		player.getInventory().decreaseKinah(remodelCost);

		player.getInventory().decreaseItemCount(extractItem, 1L);

		keepItem.setItemSkinTemplate(extractItem.getItemSkinTemplate());

		keepItem.setItemColor(extractItem.getItemColor());

		ItemPacketService.updateItemAfterInfoChange(player, keepItem);
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300483, new Object[] { new DescriptionId(keepItem.getItemTemplate().getNameId()) }));
	}
}
