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

import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.IStorage;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
/**
 * @author Maestross
 *
 */
public class Bet extends ChatCommand
{
	public Bet ()
	{
		super("bet");
	}

	@Override
	public void execute(Player player, String... params)
	{
		IStorage inventory =  player.getStorage(StorageType.CUBE.getId());
		long PlayerKinah = inventory.getKinah();

		int amount;

		if(params.equals("") || params.equals(" ") || params.length != 2)
		{
			PacketSendUtility.sendMessage(player, "Use : .bet <e|a> <amount>");
			return;
		}

		try
		{
			amount = Integer.parseInt(params[1]);
		}
		catch(NumberFormatException e)
		{
			PacketSendUtility.sendMessage(player, "Use : .bet <e | a> <amount>");
			return;
		}

		if (player.battlegroundObserve == 1 || player.battlegroundObserve == 2)
		{
			if(PlayerKinah < amount)
				PacketSendUtility.sendMessage(player, "You don't have enough kinah !");

			int maxBetAmount = 5000000;
			if(player.battlegroundBetE + player.battlegroundBetA + amount > maxBetAmount)
				PacketSendUtility.sendMessage(player, "You can't bet more than " + maxBetAmount + " !");

			else if(player.battlegroundBetE == 0 && player.battlegroundBetA == 0)
			{
				if(params[0].equals("e"))
				{
					player.battlegroundBetE = amount;
					inventory.decreaseKinah(amount);
					LoggerFactory.getLogger(ChatCommand.class).info(String.format("[BET] - Player : " + player.getName() + " | Bet : " + params[1] + " | Faction : e"));
					PacketSendUtility.sendMessage(player, "You have bet : " + player.battlegroundBetE + " for the elyos");
				}
				else if(params[0].equals("a"))
				{
					player.battlegroundBetA = amount;
					inventory.decreaseKinah(amount);
					LoggerFactory.getLogger(ChatCommand.class).info(String.format("[BET] - Player : " + player.getName() + " | Bet : " + params[1] + " | Faction : a"));
					PacketSendUtility.sendMessage(player, "You have bet : " + player.battlegroundBetA + " for the asmodians");
				}
				else
					PacketSendUtility.sendMessage(player, "Use : .bet <e | a> <amount>");        
			}
			else if(player.battlegroundBetE > 0)
			{
				if(params[0].equals("e"))
				{
					player.battlegroundBetE += amount;
					inventory.decreaseKinah(amount);
					LoggerFactory.getLogger(ChatCommand.class).info(String.format("[BET] - Player : " + player.getName() + " | Bet : " + params[1] + " | Faction : e"));
					PacketSendUtility.sendMessage(player, "You have bet : " + player.battlegroundBetE + " for the elyos");
				}
				else if(params[0].equals("a"))
					PacketSendUtility.sendMessage(player, "You have already bet for the elyos");
				else
					PacketSendUtility.sendMessage(player, "Use : .bet <e | a> <amount>");        
			}
			else if(player.battlegroundBetA > 0)
			{
				if(params[0].equals("a"))
				{
					player.battlegroundBetA += amount;
					inventory.decreaseKinah(amount);
					LoggerFactory.getLogger(ChatCommand.class).info(String.format("[BET] - Player : " + player.getName() + " | Bet : " + params[1] + " | Faction : a"));
					PacketSendUtility.sendMessage(player, "You have bet : " + player.battlegroundBetA + " for the asmodians");
				}
				else if(params[0].equals("e"))
					PacketSendUtility.sendMessage(player, "You have already bet for the asmodians");
				else
					PacketSendUtility.sendMessage(player, "Use : .bet <e | a> <amount>");        
			}
			else
				PacketSendUtility.sendMessage(player, "Use : .bet <e | a> <amount>");
		}

		else
			PacketSendUtility.sendMessage(player, "You can't bet for the moment");
	}
}
