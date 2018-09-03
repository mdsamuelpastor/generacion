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

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.world.World;

/**
 * @author Imaginary
 */
public class AdminChat extends ChatCommand {

	public AdminChat() {
		super("s");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (!admin.isGM()) {
			PacketSendUtility.sendMessage(admin, "Vous devez etre au moins rang " + AdminConfig.GM_LEVEL + " pour utiliser cette commande");
			return;
		}
		if (admin.isGagged()) {
			PacketSendUtility.sendMessage(admin, "Vous avez ete reduit au silence ...");
			return;
		}

		StringBuilder sbMessage = new StringBuilder("[Admin] " + admin.getName() + " : ");

		for (String p : params)
			sbMessage.append(p + " ");
		String message = sbMessage.toString().trim();
		for (Player a : World.getInstance().getAllPlayers()) {
			if (a.isGM())
				PacketSendUtility.sendWhiteMessageOnCenter(a, message);
		}
	}

	@Override
	public void onFail(Player admin, String message) {
		PacketSendUtility.sendMessage(admin, "Syntax: //s <message>");
	}
}
