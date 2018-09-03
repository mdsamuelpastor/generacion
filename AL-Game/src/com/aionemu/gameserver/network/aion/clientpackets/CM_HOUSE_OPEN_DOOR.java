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

import com.aionemu.gameserver.configs.main.HousingConfig;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Maestross, A7Xatomic
 */
 
public class CM_HOUSE_OPEN_DOOR extends AionClientPacket {

	int prout;
	int worldId;
	boolean leave = false;

	public CM_HOUSE_OPEN_DOOR(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		prout = readD();
		if (readC() != 0)
			leave = true;
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null) {
			return;
		}
		//int worldId = prout / 10000 * 10000;
		int editorId = prout;
		worldId = player.getWorldId();
		if (player.getAccessLevel() >= 3 && HousingConfig.ENABLE_SHOW_HOUSE_DOORID) {
			PacketSendUtility.sendMessage(player, "House door id: " + editorId);
		}

		//House house = HousingService.getInstance().getHouseByEditor(editorId);
		int address = editorId; //HousingService.getInstance().getPlayerAddress(player.getObjectId());
		House house = HousingService.getInstance().getHouseByAddress(address);
		if (house == null) {
			return;
		}
		if (leave) {
			if (house.getAddress().getExitMapId() != null) {
				TeleportService2.teleportTo(player, house.getAddress().getExitMapId(), house.getAddress().getExitX(), house.getAddress().getExitY(), house.getAddress().getExitZ(), (byte) 0,
					TeleportAnimation.BEAM_ANIMATION);
			}
			else {
				double radian = Math.toRadians(MathUtil.convertHeadingToDegree(player.getHeading()));
				float x = (float) (Math.cos(radian) * 6.0D);
				float y = (float) (Math.sin(radian) * 6.0D);
				TeleportService2.teleportTo(player, worldId, player.getX() + x, player.getY() + y, player.getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
			}
		}
		else {
			if (house.getOwnerId() != player.getObjectId()) {
				int permission = house.getSettingFlags() >> 8;
				boolean allowed = false;
				if (permission == 2) {

					allowed = player.getFriendList().getFriend(house.getOwnerId()) != null || player.getLegion() != null && player.getLegion().isMember(house.getOwnerId());
				}

				if (!allowed && player.getAccessLevel() < HousingConfig.ENTER_HOUSE_ACCESSLEVEL) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_ENTER_NO_RIGHT2);
					return;
				}
			}

			double radian = Math.toRadians(MathUtil.convertHeadingToDegree(player.getHeading()));
			float x = (float) (Math.cos(radian) * 6.0D);
			float y = (float) (Math.sin(radian) * 6.0D);
			TeleportService2.teleportTo(player, worldId, player.getX() + x, player.getY() + y, house.getAddress().getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
		}
	}
}
