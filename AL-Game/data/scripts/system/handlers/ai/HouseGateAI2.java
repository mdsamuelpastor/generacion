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
package ai;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AI2Request;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.templates.housing.BuildingType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RETURN_PLAYER;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author xTz, Rolandas, Maestross
 */
@AIName("housegate")
public class HouseGateAI2 extends NpcAI2 {

	
	@Override
	protected void handleDialogStart(Player player) {
		final int creatorId = getCreatorId();
		// Only group member and creator may use gate
		if (!player.getObjectId().equals(creatorId)) {
			if (player.getCurrentGroup() == null || !player.getCurrentGroup().hasMember(creatorId))
				return;
		}

		House house = HousingService.getInstance().getPlayerStudio(creatorId);
		if (house == null) {
			int address = HousingService.getInstance().getPlayerAddress(creatorId);
			house = HousingService.getInstance().getHouseByAddress(address);
		}
		
		final boolean onceGo = player.getWorldId() == 600030000; //You Can't return in Tiamaranta.
    int id = 0;
    if(onceGo)
        id = SM_QUESTION_WINDOW.STR_HOUSE_GATE_ACCEPT_MOVE_DONT_RETURN;
    else
        id = SM_QUESTION_WINDOW.STR_ASK_GROUP_GATE_DO_YOU_ACCEPT_MOVE;
    
		// Uses skill but doesn't have house
		if (house == null)
			return;

		AI2Actions.addRequest(this, player, id, 0, 9, new AI2Request() {

			private boolean decided = false;

			@Override
			public void acceptRequest(Creature requester, Player responder) {
				if (decided)
					return;

				House house = HousingService.getInstance().getPlayerStudio(creatorId);
				if (house == null) {
					int address = HousingService.getInstance().getPlayerAddress(creatorId);
					house = HousingService.getInstance().getHouseByAddress(address);
				}
				int instanceOwnerId = responder.getPosition().getWorldMapInstance().getOwnerId();

				int exitMapId = 0;
				float x = 0, y = 0, z = 0;
				byte heading = 0;
				int instanceId = 0;
				int playerMapId = 0;
				float playerX = 0, playerY = 0, playerZ = 0;
				byte playerHeading = 0;
        
				if (instanceOwnerId > 0) { // leaving
					house = HousingService.getInstance().getPlayerStudio(instanceOwnerId);
					exitMapId = house.getAddress().getExitMapId();
					instanceId = World.getInstance().getWorldMap(exitMapId).getWorldMapInstance().getInstanceId();
					x = house.getAddress().getExitX();
					y = house.getAddress().getExitY();
					z = house.getAddress().getExitZ();
				}
				else { // entering house
					exitMapId = house.getAddress().getMapId();
					if (house.getBuilding().getType() == BuildingType.PERSONAL_INS) { // entering studio
						WorldMapInstance instance = InstanceService.getPersonalInstance(exitMapId, creatorId);
						if (instance == null) {
							instance = InstanceService.getNextAvailableInstance(exitMapId, creatorId);
							InstanceService.registerPlayerWithInstance(instance, responder);
						}
						instanceId = instance.getInstanceId();
					}
					else { // entering ordinary house
						instanceId = house.getInstanceId();
					}
					x = house.getAddress().getX();
					y = house.getAddress().getY();
					z = house.getAddress().getZ();
					if (exitMapId == 710010000) {
						heading = 36;
					}
				}
				boolean isInstance = instanceOwnerId == 0 ? false : true;
        if(!onceGo && exitMapId != responder.getWorldId()){
                //Activate Return.
                WorldPosition pos = new WorldPosition();
                pos.setMapId(responder.getWorldId());
                pos.setXYZH(responder.getX(), responder.getY(), responder.getZ(), responder.getHeading());
                responder.setActiveReturn(pos);
                PacketSendUtility.sendPacket(responder, new SM_RETURN_PLAYER(responder.getActiveHouse().getAddress().getId()));
        }
				TeleportService2.teleportTo(responder, exitMapId, instanceId, x, y, z, heading, TeleportAnimation.JUMP_AIMATION_3);
				decided = true;
			}

			@Override
			public void denyRequest(Creature requester, Player responder) {
				decided = true;
			}

		});

	}

}