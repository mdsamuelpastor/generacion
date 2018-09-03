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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_GAME_TIME;
import com.aionemu.gameserver.spawnengine.DayTimeSpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.utils.gametime.GameTimeManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Pan, Maestros
 */
 
public class Time extends ChatCommand {

	public Time() {
		super("time");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params == null || params.length < 1) {
			onFail(admin, null);
			return;
		}

		// Getting current hour and minutes
		int time = GameTimeManager.getGameTime().getHour();
		int min = GameTimeManager.getGameTime().getMinute();
		int hour;

		// If the given param is one of these four, get the correct hour...
		if (params[0].equals("night")) {
			hour = 22;
		}
		else if (params[0].equals("dusk")) {
			hour = 18;
		}
		else if (params[0].equals("day")) {
			hour = 9;
		}
		else if (params[0].equals("dawn")) {
			hour = 4;
		}
		else {
			// If not, check if the param is a number (hour)...
			try {
				hour = Integer.parseInt(params[0]);
			}
			catch (NumberFormatException e) {
				onFail(admin, null);
				return;
			}

			// A day have only 24 hours!
			if (hour < 0 || hour > 23) 
			{
				onFail(admin, null);
				PacketSendUtility.sendMessage(admin, "Ein Tag hat nur 24 Stunden\n" + "Min value : 0 - Max value : 23");
				return;
			}
		}

		// Calculating new time in minutes...
		time = hour - time;
		time = GameTimeManager.getGameTime().getTime() + (60 * time) - (min);

		// Reloading the time, restarting the clock...
		GameTimeManager.reloadTime(time);

		// Checking the new daytime
		GameTimeManager.getGameTime().calculateDayTime();

		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_GAME_TIME());
			}
		});
		DayTimeSpawnEngine.spawnAll();
		
		PacketSendUtility.sendMessage(admin, "Du hast die Zeit auf " + params[0].toString() + "veraendert");
	}

	@Override
	public void onFail(Player player, String message) {
		String syntax = "Syntax: //time < dawn | day | dusk | night | Gewuenschte Zeit (stunde) / (stunde) (minute) >";
		PacketSendUtility.sendMessage(player, syntax);
	}

}
