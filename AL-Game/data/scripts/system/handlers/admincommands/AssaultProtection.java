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

import com.aionemu.gameserver.ShutdownHook;
import com.aionemu.gameserver.ShutdownHook.ShutdownMode;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.world.World;

/**
 * @author Maestross
 * This command is to prevent leaks of our core. A simple user command that shutdown the whole server
 * in a few minutes. Also an information text will shown up.
 */
 
public class AssaultProtection extends ChatCommand {

	public AssaultProtection() {
		super("ngc");
	}

	@Override
	public void execute(Player player, String... params) {
	    try {
				int val = 60;
				int announceInterval = 60;
				ShutdownHook.getInstance().doShutdown(val, announceInterval, ShutdownMode.SHUTDOWN);
				PacketSendUtility.sendMessage(player, "Server will shutdown in " + val + " seconds.");
				Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), "THATS A LEEKED CORE! YOUR SERVER HOSTER HAS NO PERMISSIONS TO USE IT! SERVER WILL NO SHUTDOWN FOR EVER!");
					}
			}
			catch (ArrayIndexOutOfBoundsException e) {
				PacketSendUtility.sendMessage(player, "Something went wrong!");
			}
	}
	
	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Usage: .ngc");
	}
}