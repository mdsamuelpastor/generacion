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
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;

/**
 * @author Tiger, Maestros
 */
 
public class cmd_toll extends ChatCommand {

	public cmd_toll() {
		super("toll");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params == null || params.length < 2) {
			PacketSendUtility.sendMessage(player, ".toll <ap | kinah> <value>" + "\nAp 1,000:1 : Kinah 150,000:1");
			return;
		}
		int toll;
		try {
			toll = Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e) {
			return;
		}
		if (toll > 1000) {
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.TOLLTOBIG));
			return;
		}
		if (params[0].equals("ap") && toll > 0) {
			int PlayerAbyssPoints = player.getAbyssRank().getAp();
			int pointsLost = (toll * 1000);
			if (PlayerAbyssPoints < pointsLost) {
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.TOLOWAP));
				return;
			}
			AbyssPointsService.addAp(player, -pointsLost);
			addtoll(player, toll);
		}
		else if (params[0].equals("kinah") && toll > 0) {
			int pointsLost = (toll * 150000);
			if (player.getInventory().getKinah() < pointsLost) {
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.TOLOWTOLL));
				return;
			}
			player.getInventory().decreaseKinah(pointsLost);
			addtoll(player, toll);
		}
		else {
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.WRONGTOLLNUM));
			return;
		}
	}

	private void addtoll(Player player, int toll) {
		InGameShopEn.getInstance().addToll(player, toll);
	}

	@Override
	public void onFail(Player player, String message) {
		// TODO Auto-generated method stub
	}
}
