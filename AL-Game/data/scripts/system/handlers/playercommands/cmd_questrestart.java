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
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;

/**
 * @author ginho1, Maestros
 */
 
public class cmd_questrestart extends ChatCommand {

	public cmd_questrestart() {
		super("questrestart");
	}

	@Override
	public void execute(Player player, String... params) {

		if (params == null || params.length < 1) {
			PacketSendUtility.sendMessage(player, "syntax .questrestart <quest id>");
			return;
		}

		int id;
		try {
			id = Integer.valueOf(params[0]);
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(player, "syntax .questrestart <quest id>");
			return;
		}

		QuestState qs = player.getQuestStateList().getQuestState(id);

		if (qs == null || id == 1006 || id == 2008 || id == 2009 || id == 10021 || id == 20021 || id == 18602 || id == 28602) {
			PacketSendUtility.sendMessage(player, "Quest [quest: " + id + LanguageHandler.translate(CustomMessageId.CANNOTRESTART));
			return;
		}

		if (qs.getStatus() == QuestStatus.START || qs.getStatus() == QuestStatus.REWARD) {
			if (qs.getQuestVarById(0) != 0) {
				qs.setStatus(QuestStatus.START);
				qs.setQuestVar(0);
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(id, qs.getStatus(), qs.getQuestVars().getQuestVars()));
				PacketSendUtility.sendMessage(player, "Quest [quest: " + id + LanguageHandler.translate(CustomMessageId.CANNOTRESTART));
			}
			else
				PacketSendUtility.sendMessage(player, "Quest [quest: " + id + LanguageHandler.translate(CustomMessageId.CANNOTRESTART));
		}
		else {
			PacketSendUtility.sendMessage(player, "Quest [quest: " + id + LanguageHandler.translate(CustomMessageId.CANNOTRESTART));
		}
	}

	@Override
	public void onFail(Player player, String message) {
		// TODO Auto-generated method stub
	}
}
