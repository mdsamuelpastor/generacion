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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.world.World;

/**
 * @author Reso
 */
public class Unbug extends ChatCommand {

	public Unbug() {
		super("unbug");
	}

	@Override
	public void execute(Player player, String... params) {

		String playerName = params[0];
		PlayerCommonData pcd = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonDataByName(playerName);
		if (pcd == null) {
			PacketSendUtility.sendMessage(player, "Spieler " + playerName + "existiert nicht !");
			return;
		}

		Player partner = pcd.getPlayer();
		if (partner == null) {
			PacketSendUtility.sendMessage(player, "Spieler " + playerName + "muss online sein !");
			return;
		}

		World.getInstance().setPosition(partner, player.getWorldId(), player.getInstanceId(), player.getX(), player.getY(), player.getZ(), (byte) 0);
	}

}
