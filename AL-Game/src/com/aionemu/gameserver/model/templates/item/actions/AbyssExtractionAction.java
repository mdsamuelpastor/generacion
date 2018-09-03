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

import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.ObserverType;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.Acquisition;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Eloann
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbyssExtractionAction")
public class AbyssExtractionAction extends AbstractItemAction {

    private static final Logger log = LoggerFactory.getLogger(AbyssExtractionAction.class);
    @XmlAttribute(name = "return_percent")
    private float return_percent;
    @XmlAttribute(name = "to_category")
    private String to_category;

    @Override
    public boolean canAct(Player player, Item parentItem, Item targetItem) {
        if (targetItem.getItemTemplate().isAbyssExtractable() && targetItem.isEquipped() || !targetItem.getItemTemplate().isAbyssExtractable()) {
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401646)); //You cannot extract Abyss Points from an equipped item.
            return false;
        }
        if (!isEqualEquipment(targetItem)) {
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401643, new DescriptionId(targetItem.getNameID()), new DescriptionId(parentItem.getNameID())));
            return false;
        }
        if (!targetItem.getItemTemplate().isAbyssExtractable()) {
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401640, new DescriptionId(targetItem.getNameID()))); //You cannot extract Abyss Points from %0
            return false;
        }
        if (targetItem.getItemTemplate().getLevel() < parentItem.getItemTemplate().getLevel()) {
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401641, new DescriptionId(targetItem.getNameID()), new DescriptionId(parentItem.getNameID())));
            return false;
        }
        if (targetItem.getItemTemplate().getItemQuality().getQualityId() < parentItem.getItemTemplate().getItemQuality().getQualityId()) {
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401642, new DescriptionId(targetItem.getNameID()), new DescriptionId(parentItem.getNameID())));
            return false;
        }
        return true;
    }

    public boolean isEqualEquipment(Item target) {
        if ((to_category.equals("ACCESSORY")) || (to_category.equals("WING")) || (to_category.equals("EQUIPMENT"))) {
            if (target.getEquipmentType() != null && target.getEquipmentType().toString().equals("ARMOR")) {
                return true; //There's no specified equipment type for an accessories.
            }
        } else if (to_category.equals("WEAPON")) {
            if (target.getEquipmentType() != null && target.getEquipmentType().toString().equals("WEAPON")) {
                return true;
            }
        } else if (to_category.equals("ARMOR")) {
            if (target.getEquipmentType() != null && target.getEquipmentType().toString().equals("ARMOR")) {
                return true;
            }
        } else {
            log.error("Undefined Equipment Type " + to_category);
            return false;
        }
        return false;
    }

    @Override
    public void act(final Player player, final Item parentItem, final Item targetItem) {
        player.getController().cancelAllTasks();
        PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 5000, 0, 0));
        final ActionObserver observer = new ActionObserver(ObserverType.ALL_WITHATTACK) {
            @Override
            public void moved() {
                PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, 2, 0));
                player.getController().cancelTask(TaskId.ITEM_USE);
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401644, new DescriptionId(targetItem.getItemTemplate().getNameId()))); //You have cancelled Abyss Point extraction from %0.
            }
        };
        player.getObserveController().attach(observer);

        player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                Acquisition ac = targetItem.getItemTemplate().getAcquisition();
                if (ac == null) {
                    LoggerFactory.getLogger(AbyssExtractionAction.class).info("Unable to Extract Abyss Points from " + targetItem.getItemId());
                    return;
                }

                int ToGet = (int) (ac.getRequiredAp() * (return_percent / 100));
                PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, 2, 0));
                AbyssPointsService.addAp(player, ToGet);
                player.getInventory().decreaseByItemId(parentItem.getItemId(), 1);
                player.getInventory().decreaseByItemId(targetItem.getItemId(), 1);
                player.getObserveController().removeObserver(observer);
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401645, new DescriptionId(targetItem.getNameID())));
            }
        }, 5000));
    }
}