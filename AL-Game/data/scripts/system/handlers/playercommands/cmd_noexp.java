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

package playercommands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;

/**
 * @author Kev, Maestros
 */
 
public class cmd_noexp extends ChatCommand {

	public cmd_noexp() {
		super("noexp");
	}

	@Override
	public void execute(Player player, String... params) {
		if (player.getCommonData().getNoExp()) {
			player.getCommonData().setNoExp(false);
			PacketSendUtility.sendMessage(player, "=============================");
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.EPACTIVATED));
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.ACTODE));
			PacketSendUtility.sendMessage(player, "=============================");			
		}
		else {
			player.getCommonData().setNoExp(true);
			PacketSendUtility.sendMessage(player, "=============================");
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.EPDEACTIVATED));
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.DETOAC));
			PacketSendUtility.sendMessage(player, "=============================");
		}
	}

	@Override
	public void onFail(Player player, String message) {
		// TODO Auto-generated method stub
	}
}
