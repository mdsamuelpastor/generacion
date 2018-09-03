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

import org.apache.commons.lang.ArrayUtils;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;

/**
 * @author ATracer, Maestros
 */
 
public class cmd_questauto extends ChatCommand {

	/**
	 * put quests for automation here (new int[]{1245,1345,7895})
	 */
	private final int[] questIds = new int[] {};

	public cmd_questauto() {
		super("questauto");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params == null || params.length < 1) {
			PacketSendUtility.sendMessage(player, "syntax .questauto <questid>");
			return;
		}

		int questId = 0;
		try {
			questId = Integer.parseInt(params[0]);
		}
		catch (Exception ex) {
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.WRONGQID));
			return;
		}
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.NOTSTARTED));
			return;
		}

		if (!ArrayUtils.contains(questIds, questId)) {
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.NOTSUPPORT));
			return;
		}

		qs.setStatus(QuestStatus.REWARD);
		PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId, qs.getStatus(), qs.getQuestVars().getQuestVars()));
	}

	@Override
	public void onFail(Player player, String message) {
		// TODO Auto-generated method stub
	}
}
