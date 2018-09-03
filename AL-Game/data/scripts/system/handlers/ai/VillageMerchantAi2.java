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

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.housing.HouseType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.utils.PacketSendUtility;


/**
 * @author Maestross
 *
 */
@AIName("housingmerchant")
public class VillageMerchantAi2 extends NpcAI2 {

	@Override
	public void handleDialogStart(Player player) {
		if (player.getActiveHouse() != null) {
			if (player.getActiveHouse().getHouseType().equals(HouseType.STUDIO)) {
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(player.getObjectId(), 1011));
			} else {
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(player.getObjectId(), 10));
			}
		}
	}
}
