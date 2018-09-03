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
package admincommands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureVisualState;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;

/**
 * @author Divinity
 * @edit Kev
 */
public class Invis extends ChatCommand {

	public Invis() {
		super("invis");
	}

	@Override
	public void execute(Player player, String... params) {
		if(player.getAccessLevel() < 1 ){
			PacketSendUtility.sendMessage(player, "Du hast nicht genug Rechte um diesen Command zu benutzen .");
			return;
		}
		
		if (player.getVisualState() < 20) {
			player.getEffectController().setAbnormal(AbnormalState.HIDE.getId());
			player.setVisualState(CreatureVisualState.HIDE20);
			//admin.setInvisible(true); //In bearbeitung fuer das GMs keine Packete mehr senden 
			PacketSendUtility.broadcastPacket(player, new SM_PLAYER_STATE(player), true);
			PacketSendUtility.sendMessage(player, "===================");
			PacketSendUtility.sendMessage(player, "=  Du bist jetzt unsichtbar  =");
			PacketSendUtility.sendMessage(player, "===================");
		}
		else {
			player.getEffectController().unsetAbnormal(AbnormalState.HIDE.getId());
			player.unsetVisualState(CreatureVisualState.HIDE20);
		//admin.setInvisible(false); //In bearbeitung fuer das GMs keine Packete mehr senden 
			PacketSendUtility.broadcastPacket(player, new SM_PLAYER_STATE(player), true);
			PacketSendUtility.sendMessage(player, "===================");
			PacketSendUtility.sendMessage(player, "=    Du bist jetzt sichtbar    =");
			PacketSendUtility.sendMessage(player, "===================");
		}
	}

	@Override
	public void onFail(Player player, String message) {
		// TODO Auto-generated method stub
	}
}
