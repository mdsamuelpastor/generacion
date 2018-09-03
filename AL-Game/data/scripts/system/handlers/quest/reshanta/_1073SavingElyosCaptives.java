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
package quest.reshanta;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * Talk with Sakmis (278502). Talk with Nereus (278517). Talk with Dactyl (278590). Locate the Captured Elyos Prisoner
 * (253623). Escort Captured Elyos Prisoners to the Magic Ward (1273, 1494, 1538). Report back to Nereus.
 * 
 * @author MetaWind, kale
 * @reworked vlog
 */
public class _1073SavingElyosCaptives extends QuestHandler {

	private final static int questId = 1073;
	private final static int[] npcs = { 278502, 278517, 278590, 253623 };

	public _1073SavingElyosCaptives() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerQuestNpc(253623).addOnReachTargetEvent(questId);
		qe.registerQuestNpc(253623).addOnLostTargetEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 278502: { // Sakmis
					switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 0)
								return sendQuestDialog(env, 1011);
						case STEP_TO_1:
							return defaultCloseDialog(env, 0, 1); // 1
						default:
							break;
					}
					break;
				}
				case 278517: { // Nereus
					switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 1)
								return sendQuestDialog(env, 1352);
						case STEP_TO_2:
							return defaultCloseDialog(env, 1, 2); // 2
						default:
							break;
					}
					break;
				}
				case 278590: { // Dactyl
					switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 2)
								return sendQuestDialog(env, 1693);
						case STEP_TO_3:
							return defaultCloseDialog(env, 2, 3); // 3
						default:
							break;
					}
					break;
				}
				case 253623: { // Captured Elyos Prisoner
					switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
						case SELECT_ACTION_2035: {
							playQuestMovie(env, 269);
							return sendQuestDialog(env, 2035);
						}
						case STEP_TO_4: {
							return defaultStartFollowEvent(env, 1295.1139f, 1498.6543f, 1571.1763f, 3, 4); // 4
						}
						default:
							break;
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 278517) { // Nereus
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 10002);
				else if (env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onNpcReachTargetEvent(QuestEnv env) {
		playQuestMovie(env, 270);
		return defaultFollowEndEvent(env, 4, 4, true); // reward
	}

	@Override
	public boolean onNpcLostTargetEvent(QuestEnv env) {
		return defaultFollowEndEvent(env, 4, 3, false); // 3
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 1701, true);
	}
}
