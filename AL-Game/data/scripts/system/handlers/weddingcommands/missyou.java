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
package weddingcommands;

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;

/**
 * @author synchro2
 */
public class missyou extends ChatCommand {

	public missyou() {
		super("missyou");
	}

	@Override
	public void execute(final Player player, String... params) {

		Player partner = player.findPartner();

		if (partner == null) {
			PacketSendUtility.sendMessage(player, "Nicht online.");
			return;
		}
		if (player.getWorldId() == 510010000 || player.getWorldId() == 520010000) {
			PacketSendUtility.sendMessage(player, "Du kannst dieses Kommando nicht im Gefaengnis nutzen.");
			return;
		}

		if (partner.getWorldId() == 510010000 || partner.getWorldId() == 520010000) {
			PacketSendUtility.sendMessage(player, "Du kannst dich nicht zu " + partner.getName() + "porten, dein Partner ist im Gefaengnis.");
			return;
		}

		if (partner.isInInstance()) {
			PacketSendUtility.sendMessage(player, "Du kannst dich nicht zu " + partner.getName() + "porten, dein Partner ist in einer Instanz.");
			return;
		}

		if (!player.isCommandInUse()) {
			TeleportService2.teleportTo(player, partner.getWorldId(), partner.getInstanceId(), partner.getX(), partner.getY(), partner.getZ(), partner.getHeading(), TeleportAnimation.BEAM_ANIMATION);
			PacketSendUtility.sendMessage(player, "Teleportiert zu " + partner.getName() + ".");
			player.setCommandUsed(true);

			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					player.setCommandUsed(false);
				}
			}, 60 * 60 * 1000);
		}
		else
			PacketSendUtility.sendMessage(player, "Nur 1 Teleport pro Stunde.");
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Fehlgeschlagen");
	}
}
