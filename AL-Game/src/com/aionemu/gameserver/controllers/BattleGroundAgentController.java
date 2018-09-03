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
package com.aionemu.gameserver.controllers;

import com.aionemu.gameserver.model.gameobjects.BattleGroundAgent;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.utils.PacketSendUtility;
import br.focus.battleground.BattleGroundManager;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
/**
 * @author Maestross
 */
public class BattleGroundAgentController extends NpcController
{
	@Override
	public void onDialogRequest(final Player player)
	{
		if(player.getCommonData().getRace() != getOwner().getObjectTemplate().getRace())
			return;
		if(player.battlegroundWaiting)
		{
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.BATTLEGROUNDAGENTCONTROLLER_1));
		}
		else
		{
			String message = LanguageHandler.translate(CustomMessageId.BATTLEGROUNDFLAGCONTROLLER_3);
			RequestResponseHandler responseHandler = new RequestResponseHandler(player){

				public void acceptRequest(Creature requester, Player responder)
				{
					BattleGroundManager.sendRegistrationForm(player);
					return;
				}

				public void denyRequest(Creature requester, Player responder)
				{
					return;
				}
			};
			boolean requested = player.getResponseRequester().putRequest(902247, responseHandler);
			if(requested)
			{
				PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(902247, 1, 1, message));
				return;
			}
		}
	}

	@Override
	public BattleGroundAgent getOwner()
	{
		return (BattleGroundAgent) super.getOwner();
	}
}
