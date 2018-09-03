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

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureVisualState;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import com.aionemu.gameserver.services.HTMLService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
import br.focus.battleground.BattleGroundManager;
import br.focus.factories.SurveyFactory;

/**
 * @author Maestross
 *
 */
public class BattleGround extends ChatCommand
{
	public BattleGround ()
	{
		super("bg");
	}

	@Override
	public void execute(Player player, String... params)
	{
		if(!BattleGroundManager.INITIALIZED)
		{
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_DISABLED));
			return;
		}

		if(player.isInPrison())
		{
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE0));
			//PacketSendUtility.sendMessage(player, "You cannot register for battlegrounds while you are in prison.");
			return;
		}

		if(params.equals(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE35))
		{
			if(player.getBattleGround() != null)
			{
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE1));
				//PacketSendUtility.sendMessage(player, "You are already in a battleground.");
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE2));
				//PacketSendUtility.sendMessage(player, "Use your spell Return to leave the battleground.");
				return;
			}
			else if(player.battlegroundWaiting)
			{
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE3));
				//PacketSendUtility.sendMessage(player, "You are already registered in a battleground.");
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE4));
				//PacketSendUtility.sendMessage(player, "Use the command .bg unregister to cancel your registration.");
				return;
			}
			else
			{
				BattleGroundManager.sendRegistrationForm(player);
			}
		}
		else if(params.equals(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE36))
		{
			if(player.getBattleGround() != null)
			{
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE9));
				//PacketSendUtility.sendMessage(player, "You are already in a battleground.");
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE10));
				//PacketSendUtility.sendMessage(player, "Use your spell Return to leave the battleground.");
				return;
			}
			else if(player.battlegroundWaiting)
			{
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE11));
				//PacketSendUtility.sendMessage(player, "You are already registered in a battleground.");
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE12));
				//PacketSendUtility.sendMessage(player, "Use the command .bg unregister to cancel your registration.");
				return;
			}
			else
			{
				BattleGroundManager.sendRegistrationFormObs(player);
			}
		}
		else if(params.equals(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE37))
		{
			if(player.getBattleGround() != null && player.battlegroundObserve == 0)
			{
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE13));
				//PacketSendUtility.sendMessage(player, "You are playing in a battleground, not an observer.");
				return;
			}
			else if(player.getBattleGround() != null && player.battlegroundObserve > 0)
			{
				player.unsetVisualState(CreatureVisualState.HIDE20);
				PacketSendUtility.broadcastPacket(player, new SM_PLAYER_STATE(player), true);
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE14));
				//PacketSendUtility.sendMessage(player, "You are now visible.");
				player.setInvul(false);
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE15));
				//PacketSendUtility.sendMessage(player, "You are now mortal.");

				if(player.battlegroundBetE > 0 || player.battlegroundBetA > 0)
				{
					//PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE16));
					PacketSendUtility.sendMessage(player, "You have lost your bet of " + (player.battlegroundBetE + player.battlegroundBetA) + "kinah.");
					player.battlegroundBetE = 0;
					player.battlegroundBetA = 0;
				}

				if(player.getCommonData().getRace() == Race.ELYOS)
					TeleportService2.teleportTo(player, 110010000, 1374f, 1399f, 573f, (byte) 0);
				else
					TeleportService2.teleportTo(player, 120010000, 1324f, 1550f, 210f, (byte) 0);

				return;
			}
			else
			{
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE17));
				//PacketSendUtility.sendMessage(player, "You are not observing any battleground.");
			}
		}
		else if(params.equals(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE38))
		{
			if(player.getBattleGround() == null || (!player.getBattleGround().running && !player.battlegroundWaiting))
			{
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE18));
				//PacketSendUtility.sendMessage(player, "You are not registered in any battleground or the battleground is over.");
				return;
			}
			else
			{
				player.battlegroundRequestedRank = true;
				HTMLService.showHTML(player, SurveyFactory.getBattleGroundRanking(player.getBattleGround()), 151000001);
			}
		}
		else if(params.equals("stat") && player.getAccessLevel() > 0)
		{
			PacketSendUtility.sendMessage(player, BattleGroundManager.getElyosWaitList().get(1).size() + " Elyos " + BattleGroundManager.getAsmodiansWaitList().get(1).size() + " Asmodians for Triniel");
			PacketSendUtility.sendMessage(player, BattleGroundManager.getElyosWaitList().get(2).size() + " Elyos " + BattleGroundManager.getAsmodiansWaitList().get(2).size() + " Asmodians for Sanctum");
			PacketSendUtility.sendMessage(player, BattleGroundManager.getElyosWaitList().get(3).size() + " Elyos " + BattleGroundManager.getAsmodiansWaitList().get(3).size() + " Asmodians for Haramel");
		}
		else if(params.equals("exchange"))
		{
			if(player.getAccessLevel() < 1)
			{
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE19));
				//PacketSendUtility.sendMessage(player, "The exchange tool is not available.");
				return;
			}

			if(params.equals(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE40))
			{
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE20));
				//PacketSendUtility.sendMessage(player, "The exchange rate is 1 BG point for 3 Abyss points.");
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE21));
				//PacketSendUtility.sendMessage(player, "To exchange some points, write .bg exchange <bg_points_number>");
			}
			else
			{
				try
				{
					int bgPts = Integer.parseInt(params[1]);
					if(player.getCommonData().getBattleGroundPoints() < bgPts)
					{
						PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE22));
						//PacketSendUtility.sendMessage(player, "You don't have enough BG points.");
						return;
					}
					player.getCommonData().setBattleGroundPoints(player.getCommonData().getBattleGroundPoints() - bgPts);
					PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE23, bgPts));
					//PacketSendUtility.sendMessage(player, "You have lost " + bgPts + " BG points.");
					AbyssPointsService.addAp(player, bgPts * 3);
				}
				catch(Exception e)
				{
					PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE24));
					//PacketSendUtility.sendMessage(player, "Syntax error. Use .bg exchange <bg_points_number>.");
				}
			}
		}
		else if(params.equals("end") && player.getAccessLevel() > 0)
		{
			player.getBattleGround().end();
		}
		else if(params.equals(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE41))
		{
			if(!player.battlegroundWaiting)
			{
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE25));
				//PacketSendUtility.sendMessage(player, "You are not registered in any battleground.");
				return;
			}
			if(player.battlegroundObserve == 0)
				BattleGroundManager.unregisterPlayer(player);
			else if(player.battlegroundObserve > 0)
				BattleGroundManager.unregisterPlayerObs(player);
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE26));
			//PacketSendUtility.sendMessage(player, "Registration canceled.");
		}
		else if(params.equals(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE42))
		{
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE27));
			//PacketSendUtility.sendMessage(player, ".bg register : register in a BG");
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE28));
			//PacketSendUtility.sendMessage(player, ".bg observe : observe a battleground");
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE29));
			//PacketSendUtility.sendMessage(player, ".bg unregister : unregister from the BG (before starting)");
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE30));
			//PacketSendUtility.sendMessage(player, ".bg stop : stop observe and back to home");
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE31));
			//PacketSendUtility.sendMessage(player, ".bg rank : see your rank during a BG");
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE32));
			//PacketSendUtility.sendMessage(player, ".bg points : : to see your points");
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE33));
			//PacketSendUtility.sendMessage(player, ".bet : bet on a faction during observe mode");
		}
		else if(params.equals(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE43))
		{
			PacketSendUtility.sendMessage(player, "You have actually " + (player.getCommonData().getBattleGroundPoints() + player.battlegroundSessionPoints) + " BG points" + (player.battlegroundSessionPoints > 0 ? ", including " + player.battlegroundSessionPoints + " in the current BG " : "") + ".");
		}
		else
		{
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE34));
			//PacketSendUtility.sendMessage(player, "This command doesn't exist, use .bg help");
		}
	}

}
