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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.controllers.HouseController;
import com.aionemu.gameserver.model.gameobjects.HouseDecoration;
import com.aionemu.gameserver.model.gameobjects.HouseObject;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.actions.DecorateAction;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_EDIT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_REGISTRY;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.services.item.HouseObjectFactory;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemDeleteType;
import com.aionemu.gameserver.utils.idfactory.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Maestross
 */
 
public class CM_HOUSE_EDIT extends AionClientPacket {

	int action;
	int itemObjectId;
	float x;
	float y;
	float z;
	int rotation;
	int buildingId;
	private static final Logger log = LoggerFactory.getLogger(CM_HOUSE_EDIT.class);

	public CM_HOUSE_EDIT(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		action = readC();
		switch (action) {
		    case 1:
			case 2:
			    break;
			case 3:
			case 4:
			case 7:
			    itemObjectId = readD();
				break;
			case 5:
			case 6:
			    itemObjectId = readD();
			    x = readF();
			    y = readF();
			    z = readF();
			    rotation = readH();
				break;
			case 14:
			case 15:
			    break;
			case 16:
			    buildingId = readD();
				break;
			case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
			default:
                log.warn("Unknown action from CM_HOUSE_EDIT: " + action);
		}
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null) {
			return;
		}
        switch (action) {
		    case 1:
			    sendPacket(new SM_HOUSE_EDIT(action));
			    sendPacket(new SM_HOUSE_REGISTRY(action));
			    sendPacket(new SM_HOUSE_REGISTRY(action + 1));
				break;
			case 2:
		        sendPacket(new SM_HOUSE_EDIT(action));
				break;
			case 3:
			    Item item = player.getInventory().getItemByObjId(itemObjectId);
			    if (item == null) {
				    return;
			    }
			    ItemTemplate template = item.getItemTemplate();
			    player.getInventory().delete(item, ItemDeleteType.REGISTER);
			    DecorateAction decorateAction = template.getActions().getDecorateAction();
			    if (decorateAction != null) {
				    HouseDecoration decor = new HouseDecoration(IDFactory.getInstance().nextId(), decorateAction.getTemplateId());
				    player.getHouseRegistry().putCustomPart(decor);
				    sendPacket(new SM_HOUSE_EDIT(action, 2, decor.getObjectId()));
			    }
			    else {
				    House house = player.getHouseRegistry().getOwner();
				    HouseObject<?> obj = HouseObjectFactory.createNew(house, template);
				    player.getHouseRegistry().putObject(obj);
				    sendPacket(new SM_HOUSE_EDIT(action, 1, obj.getObjectId()));
			    }
				break;
			case 4:
			    player.getHouseRegistry().removeObject(itemObjectId);
			    sendPacket(new SM_HOUSE_EDIT(action, 1, itemObjectId));
			    sendPacket(new SM_HOUSE_EDIT(4, 1, itemObjectId));
				break;
			case 5:
			    HouseObject<?> obj = player.getHouseRegistry().getObjectByObjId(itemObjectId);
			    if (obj == null) {
				    return;
			    }
			    obj.setX(x);
			    obj.setY(y);
			    obj.setZ(z);
			    obj.setRotation(rotation);
			    sendPacket(new SM_HOUSE_EDIT(action, itemObjectId, x, y, z, rotation));
			    obj.spawn();
			    player.getHouseRegistry().setPersistentState(PersistentState.UPDATE_REQUIRED);
			    sendPacket(new SM_HOUSE_EDIT(4, 1, itemObjectId));
			    QuestEngine.getInstance().onHouseItemUseEvent(new QuestEnv(null, player, 0, 0), obj.getObjectTemplate().getTemplateId());
			    break;
			case 6:
			    HouseObject<?> obj2 = player.getHouseRegistry().getObjectByObjId(itemObjectId);
			    if (obj2 == null)
				    return;
			    sendPacket(new SM_HOUSE_EDIT(action + 1, 0, itemObjectId));
			    obj2.getController().onDelete();
			    obj2.setX(x);
			    obj2.setY(y);
			    obj2.setZ(z);
			    obj2.setRotation(rotation);
			    if (obj2.getPersistentState() == PersistentState.UPDATE_REQUIRED)
				    player.getHouseRegistry().setPersistentState(PersistentState.UPDATE_REQUIRED);
			    sendPacket(new SM_HOUSE_EDIT(action - 1, itemObjectId, x, y, z, rotation));
			    obj2.spawn();
				break;
			case 7:
			    HouseObject<?> obj3 = player.getHouseRegistry().getObjectByObjId(itemObjectId);
			    if (obj3 == null)
				    return;
			    sendPacket(new SM_HOUSE_EDIT(action, 0, itemObjectId));
			    obj3.getController().onDelete();
			    obj3.removeFromHouse();
			    obj3.clearKnownlist();
			    player.getHouseRegistry().setPersistentState(PersistentState.UPDATE_REQUIRED);
			    sendPacket(new SM_HOUSE_EDIT(3, 1, itemObjectId));
				break;
			case 14:
			case 15:
			    sendPacket(new SM_HOUSE_EDIT(action));
				break;
			case 16:
			    House house = player.getHouseRegistry().getOwner();
			    HousingService.getInstance().switchHouseBuilding(house, buildingId);
			    player.resetHouses();
			    house = player.getHouseRegistry().getOwner();
			    ((HouseController) house.getController()).updateAppearance();
				break;
			case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
		}
	}
}
