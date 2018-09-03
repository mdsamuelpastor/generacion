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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.QuestTemplate;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CM_QUEST_SHARE extends AionClientPacket {

	public int questId;

	public CM_QUEST_SHARE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	protected void readImpl() {
		questId = readD();
	}

	protected void runImpl() {
		Player player = getConnection().getActivePlayer();

		if (player == null) {
			return;
		}
		if (!player.isInGroup2()) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1100000));
			return;
		}

		QuestTemplate questTemplate = DataManager.QUEST_DATA.getQuestById(questId);

		if ((questTemplate == null) || (questTemplate.isCannotShare())) {
			return;
		}
		QuestState questState = player.getQuestStateList().getQuestState(questId);

		if ((questState == null) || (questState.getStatus() == QuestStatus.COMPLETE)) {
			return;
		}
		for (Player member : player.getPlayerGroup2().getOnlineMembers())
			if ((player != member) && (MathUtil.isIn3dRange(member, player, GroupConfig.GROUP_MAX_DISTANCE))
				&& (!member.getQuestStateList().hasQuest(questId))) {
				if (!QuestService.checkLevelRequirement(questId, member.getLevel())) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1100003, member.getName()));
					PacketSendUtility.sendPacket(member, new SM_SYSTEM_MESSAGE(1100003, player.getName()));
				}
				else {
					PacketSendUtility.sendPacket(member, new SM_QUEST_ACTION(questId, member.getObjectId(), true));
				}
			}
	}
}
