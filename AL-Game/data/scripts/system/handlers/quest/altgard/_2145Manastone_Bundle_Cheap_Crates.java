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
package quest.altgard;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

public class _2145Manastone_Bundle_Cheap_Crates extends QuestHandler
{
	private final static int questId = 2145;

	public _2145Manastone_Bundle_Cheap_Crates() {
		super(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203690) {
				if (env.getDialogId() == 26) {
					return sendQuestDialog(env, 1011);
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		if (qs == null)
			return false;
		if (qs == null || qs.getStatus() == QuestStatus.COMPLETE) {
			if (targetId == 203690) {
				if (env.getDialogId() == 26) {
					return sendQuestDialog(env, 1011);
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
			case 203690:
				switch (env.getDialogId()) {
				case 26:
					return sendQuestDialog(env, 2375);
				case 2034:
					return sendQuestDialog(env, 2034);
				case 34:
					//Collect Manastone: MP +30 (x10)
					//Collect Manastone: Evasion +6 (x10)
					//Collect Manastone: Parry +14 (x10)
					if (QuestService.collectItemCheck(env, true)) {                 
						player.getInventory().decreaseByItemId(167000259, 10); //Manastone: MP +30.
						player.getInventory().decreaseByItemId(167000261, 10); //Manastone: Evasion +6.
						player.getInventory().decreaseByItemId(167000264, 10); //Manastone: Manastone: Parry +14.
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 5);
					} else {
						return sendQuestDialog(env, 2716);
					}
				}
				break;
			default:
				return sendQuestStartDialog(env);
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203690)
				return sendQuestEndDialog(env);
		}
		return false;
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203690).addOnQuestStart(questId);
		qe.registerQuestNpc(203690).addOnTalkEvent(questId);
	}
}