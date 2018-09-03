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

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.world.WorldMapType;

/**
 * @author Centisgood(Barahime), Maestross
 */
public class SetRace extends ChatCommand {

	public SetRace() {
		super("setrace");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params == null || params.length < 1) {
			PacketSendUtility.sendMessage(admin, "syntax: //setrace <elyos | asmodians | balaur>");
			return;
		}

		VisibleObject visibleobject = admin.getTarget();

		if (visibleobject == null || !(visibleobject instanceof Player)) {
			PacketSendUtility.sendMessage(admin, "Wrong select target.");
			return;
		}

		Player target = (Player) visibleobject;
		if (params[0].equalsIgnoreCase("elyos")) {
			target.getCommonData().setRace(Race.ELYOS);
			TeleportService2.teleportTo(target, WorldMapType.SANCTUM.getId(), 1322, 1511, 568, 0);
			PacketSendUtility.sendMessage(target, "Has been moved to Sanctum.");
		}
		else if (params[0].equalsIgnoreCase("asmodians")) {
			target.getCommonData().setRace(Race.ASMODIANS);
			TeleportService2.teleportTo(target, WorldMapType.PANDAEMONIUM.getId(), 1679, 1400, 195, 0);
			PacketSendUtility.sendMessage(target, "Has been moved to Pandaemonium");
		}
		else if (params[0].equalsIgnoreCase("balaur")) {
		    target.getCommonData().setRace(Race.DRAKAN);
			PacketSendUtility.sendMessage(admin, "Please dont logout in this race state! It could be that you see your character the last time...");
		}
		PacketSendUtility.sendMessage(admin, target.getName() + " race has been changed to " + params[0] + ".\n" + target.getName() + " has been moved to town.");
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax: //setrace <elyos | asmodians | balaur>");
	}
}
