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

import java.util.Iterator;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.world.World;

/**
 * @author Ben, Ritsu Smart Matching Enabled //announce anon This will work. as well as //announce a This will work.
 *         Both will match the "a" or "anon" to the "anonymous" flag.
 * @author Maestross
 */
public class Announce extends ChatCommand {

	public Announce() {
		super("announce");
	}

	@Override
	public void execute(Player player, String... params) {
		String message = "";

		if (("anonymous").startsWith(params[0].toLowerCase())) {
			message += "[Info]: ";
		}
		else if (("name").startsWith(params[0].toLowerCase())) {
			if(AdminConfig.ADMIN_TAG_ENABLE)
			{
				if(player.getAccessLevel() == 1 )
				{
					message += "\uE042" + AdminConfig.ADMIN_TAG_1.trim() + "\uE043";
				}
				else if (player.getAccessLevel() == 2 )
				{
					message += "\uE042" + AdminConfig.ADMIN_TAG_2.trim() + "\uE043";
				}
				else if (player.getAccessLevel() == 3 )
				{
					message += "\uE042" + AdminConfig.ADMIN_TAG_3.trim() + "\uE043";
				}
				else if (player.getAccessLevel() == 4 )
				{
					message += "\uE042" + AdminConfig.ADMIN_TAG_4.trim() + "\uE043";
				}
				else if (player.getAccessLevel() == 5 )
				{
					message += "\uE042" + AdminConfig.ADMIN_TAG_5.trim() + "\uE043";
				}
				else if (player.getAccessLevel() == 6 )
				{
					message += "\uE042" + AdminConfig.ADMIN_TAG_6.trim() + "\uE043";
				}
				else if (player.getAccessLevel() == 7 )
				{
					message += "\uE042" + AdminConfig.ADMIN_TAG_7.trim() + "\uE043";
				}
				else if (player.getAccessLevel() == 8 )
				{
					message += "\uE042" + AdminConfig.ADMIN_TAG_8.trim() + "\uE043";
				}
				else if (player.getAccessLevel() == 9 )
				{
					message += "\uE042" + AdminConfig.ADMIN_TAG_9.trim() + "\uE043";
				}
				else if (player.getAccessLevel() == 10 )
				{
					message += "\uE042" + AdminConfig.ADMIN_TAG_10.trim() + "\uE043";
				}
			}

			message += player.getName() + " : ";
		}
		else {
			PacketSendUtility.sendMessage(player, "Syntax: //announce <anonymous|name> <message>");
			return;
		}

		// Add with space
		for (int i = 1; i < params.length - 1; i++)
			message += params[i] + " ";

		// Add the last without the end space
		message += params[params.length - 1];

		Iterator<Player> iter = World.getInstance().getPlayersIterator();

		while (iter.hasNext()) {
			PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), message);
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax: //announce <anonymous|name> <message>");
	}
}
