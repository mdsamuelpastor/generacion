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
package quest.crafting;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Ritsu
 */
public class _19053ExpertExpertofConstruction extends QuestHandler {

	private final static int questId = 19053;

	public _19053ExpertExpertofConstruction() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(798450).addOnQuestStart(questId);
		qe.registerQuestNpc(798450).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();

		QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 798450) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else
					return sendQuestStartDialog(env);
			}
		}

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 798450: {
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 2375);
						}
						case CHECK_COLLECTED_ITEMS: {
							if (QuestService.collectItemCheck(env, true)) {
								changeQuestStep(env, 0, 0, true);
								return sendQuestDialog(env, 5);
							}
							else
								return sendQuestDialog(env, 2716);
						}
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798450) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
