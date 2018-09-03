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

import com.aionemu.gameserver.model.Wedding;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.WeddingService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;

/**
 * @author synchro2, Maestros
 */
public class cmd_answer extends ChatCommand {

	public cmd_answer() {
		super("answer");
	}

	@Override
	public void execute(Player player, String... params) {
		Wedding wedding = WeddingService.getInstance().getWedding(player);

		if (params == null || params.length != 1) {
			PacketSendUtility.sendMessage(player, "syntax .answer yes/no.");
			return;
		}

		if (player.getWorldId() == 510010000 || player.getWorldId() == 520010000) {
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.WEDDINGNO1));
			return;
		}

		if (wedding == null) {
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.WEDDINGNO2));
		}

		if (params[0].toLowerCase().equals("yes")) {
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.WEDDINGYES));
			WeddingService.getInstance().acceptWedding(player);
		}

		if (params[0].toLowerCase().equals("no")) {
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.WEDDINGNO3));
			WeddingService.getInstance().cancelWedding(player);
		}

	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax .answer yes/no.");
	}
}
