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
package ai.dimensionalVortex;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AI2Request;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;


/**
 * @author Rinzler, GoodT, Eloann
 */
@AIName("dimensionalvortexout")
public class DimensionalVortexOutAI2 extends NpcAI2
{
	@Override
	protected void handleDialogStart(Player player) {
		if (player.isInPlayerMode(PlayerMode.RIDE)) {
		    PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "You can not use a <VORTEX> being on a ride", ChatType.BRIGHT_YELLOW_CENTER), true);
			return;
        } 
		if (player.getLevel() >= 25) {
			  AI2Actions.addRequest(this, player, SM_QUESTION_WINDOW.STR_CONFIRM_INVADE_DIRECT_PORTAL_OUT, 0, new AI2Request() {
				 @Override
				 public void acceptRequest(Creature requester, Player responder) {
				 	 switch (responder.getRace()) {
				 	     case ASMODIANS:
				 		     transferAsmodians(responder);
				 		 case ELYOS:
				 		     transferElyos(responder);
				 		 break;
						default:
							break;
				 	 }
				 }
			});
		} 
		else {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_INVADE_DIRECT_PORTAL_LEVEL_LIMIT);
		}
	}
	
	private void transferAsmodians(Player player) {
        TeleportService2.teleportTo(player, 120080000, 562.88324f, 206.13414f, 93.479576f);
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INVADE_DIRECT_PORTAL_OUT_NOTICE);
	}
	
	private void transferElyos(Player player) {
        TeleportService2.teleportTo(player, 110070000, 451.7139f, 232.88655f, 126.97164f);
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INVADE_DIRECT_PORTAL_OUT_NOTICE);
	}
	
	@Override
	protected void handleDialogFinish(Player player) {
	}
}