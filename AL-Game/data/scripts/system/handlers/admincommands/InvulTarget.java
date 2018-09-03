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

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;

/**
 * @author Andy
 * @author Divinity - update
 * @author Maestros
 */
 
public class InvulTarget extends ChatCommand {

	public InvulTarget() {
		super("invultarget");
	}

	@Override
	public void execute(Player player, String... params) {
	    VisibleObject target = player.getTarget();
		Creature creature = (Creature) target;
		if (target == null) {
			PacketSendUtility.sendMessage(player, "Kein Target");
			return;
		}
		if (!(target instanceof Creature)) {
			PacketSendUtility.sendMessage(player, "Target has to be Creature!");
			return;
		}
		if (player.isInvul()) {
		Player targetPlayer = (Player) creature;
			targetPlayer.setInvul(false);
			PacketSendUtility.sendMessage(targetPlayer, "Dein Status ist : Unsterblich(Off)");
		}
		else {
		Player targetPlayer = (Player) creature;
			targetPlayer.setInvul(true);
			PacketSendUtility.sendMessage(targetPlayer, "Dein Status ist : Unsterblich(On)");
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //invultarget <Player target>");
	}
}