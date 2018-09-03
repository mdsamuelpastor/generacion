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
package com.aionemu.gameserver.model.templates.item.actions;

import java.util.Iterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.EnchantService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

/**
 * @author Nemiroff, Wakizashi, vlog, Maestross
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnchantItemAction")
public class EnchantItemAction extends AbstractItemAction {

	@XmlAttribute(name = "count")
	private int count;

	@XmlAttribute(name = "min_level")
	private Integer min_level;

	@XmlAttribute(name = "max_level")
	private Integer max_level;

	@XmlAttribute(name = "manastone_only")
	private boolean manastone_only;

	@XmlAttribute(name = "chance")
	private float chance;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (isSupplementAction())
			return false;
		if (targetItem == null) { // no item selected.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_COLOR_ERROR);
			return false;
		}
		if (parentItem == null) {
			return false;
		}
		int msID = parentItem.getItemTemplate().getTemplateId() / 1000000;
		int tID = targetItem.getItemTemplate().getTemplateId() / 1000000;
		if ((msID != 167 && msID != 166) || tID >= 120) {
			return false;
		}
		return true;
	}

	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem) {
		act(player, parentItem, targetItem, null, 1);
	}

	// necessary overloading to not change AbstractItemAction

	public void act(final Player player, final Item parentItem, final Item targetItem, final Item supplementItem, final int targetWeapon) {
		if ((supplementItem != null) && (!checkSupplementLevel(player, supplementItem.getItemTemplate(), targetItem.getItemTemplate()))) {
			return;
		}
		final int currentEnchant = targetItem.getEnchantLevel();
		final boolean isSuccess = isSuccess(player, parentItem, targetItem, supplementItem, targetWeapon, currentEnchant);
		player.getController().cancelUseItem();

		PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 5000, 0, 0));
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				ItemTemplate itemTemplate = parentItem.getItemTemplate();
				// Enchantment stone
				if (itemTemplate.getCategory() == ItemCategory.ENCHANTMENT) {
					EnchantService.enchantItemAct(player, parentItem, targetItem, supplementItem, currentEnchant, isSuccess);
				}
				else
					EnchantService.socketManastoneAct(player, parentItem, targetItem, supplementItem, targetWeapon, isSuccess);
				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, isSuccess ? 1
					: 2, 0));
				if (CustomConfig.ENABLE_ENCHANT_ANNOUNCE) {
					if (itemTemplate.getCategory() == ItemCategory.ENCHANTMENT && currentEnchant == 14 && isSuccess) {
						Iterator<Player> iter = World.getInstance().getPlayersIterator();
						while (iter.hasNext()) {
							Player player2 = iter.next();
							if (player2.getRace() == player.getRace()) {
								PacketSendUtility.sendPacket(player2, SM_SYSTEM_MESSAGE.STR_MSG_ENCHANT_ITEM_SUCCEEDED_15(player.getName(), targetItem.getItemTemplate().getNameId()));
							}
						}
					}
				}
			}

		}, 5000));
	}

	/**
	 * Check, if the item enchant will be successful
	 * 
	 * @param player
	 * @param parentItem
	 *          the enchantment-/manastone to insert
	 * @param targetItem
	 *          the current item to enchant
	 * @param supplementItem
	 *          the item to increase the enchant chance (if exists)
	 * @param targetWeapon
	 *          the fused weapon (if exists)
	 * @param currentEnchant
	 *          current enchant level
	 * @return true if successful
	 */
	private boolean isSuccess(final Player player, final Item parentItem, final Item targetItem, final Item supplementItem, final int targetWeapon, final int currentEnchant) {
		if (parentItem.getItemTemplate() != null) {
			// Id of the stone
			ItemTemplate itemTemplate = parentItem.getItemTemplate();
			// Enchantment stone
			if (itemTemplate.getCategory() == ItemCategory.ENCHANTMENT) {
				return EnchantService.enchantItem(player, parentItem, targetItem, supplementItem);
			}
			// Manastone
			return EnchantService.socketManastone(player, parentItem, targetItem, supplementItem, targetWeapon);
		}
		return false;
	}

	public int getCount() {
		return count;
	}

	public int getMaxLevel() {
		return max_level != null ? max_level : 0;
	}

	public int getMinLevel() {
		return min_level != null ? min_level : 0;
	}

	public boolean isManastoneOnly() {
		return manastone_only;
	}

	public float getChance() {
		return chance;
	}

	boolean isSupplementAction() {
		return getMinLevel() > 0 || getMaxLevel() > 0 || getChance() > 0.0f || isManastoneOnly();
	}

	private boolean checkSupplementLevel(Player player, ItemTemplate supplementTemplate, ItemTemplate targetItemTemplate) {
		if (supplementTemplate.getCategory() != ItemCategory.ENCHANTMENT) {
			int minEnchantLevel = targetItemTemplate.getLevel();
			int maxEnchantLevel = targetItemTemplate.getLevel();

			EnchantItemAction action = supplementTemplate.getActions().getEnchantAction();
			if (action != null) {
				if (action.getMinLevel() != 0)
					minEnchantLevel = action.getMinLevel();
				if (action.getMaxLevel() != 0) {
					maxEnchantLevel = action.getMaxLevel();
				}
			}
			if (minEnchantLevel <= targetItemTemplate.getLevel() && maxEnchantLevel >= targetItemTemplate.getLevel()) {
				return true;
			}
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_ENCHANT_ASSISTANT_NO_RIGHT_ITEM);
			return false;
		}
		return true;
	}
}
