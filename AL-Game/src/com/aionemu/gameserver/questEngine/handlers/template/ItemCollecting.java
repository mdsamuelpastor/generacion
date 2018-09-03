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
package com.aionemu.gameserver.questEngine.handlers.template;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author MrPoke
 * @reworked vlog
 */
public class ItemCollecting extends QuestHandler {

	private final Set<Integer> startNpcs = new HashSet<Integer>();
	private final Set<Integer> actionItems = new HashSet<Integer>();
	private final Set<Integer> endNpcs = new HashSet<Integer>();
	private final int questMovie;

	public ItemCollecting(int questId, List<Integer> startNpcIds, List<Integer> actionItemIds, List<Integer> endNpcIds, int questMovie) {
		super(questId);
		startNpcs.addAll(startNpcIds);
		startNpcs.remove(0);
		if (actionItemIds != null) {
			actionItems.addAll(actionItemIds);
			actionItems.remove(0);
		}
		if (endNpcIds == null) {
			endNpcs.addAll(startNpcs);
		}
		else {
			endNpcs.addAll(endNpcIds);
			endNpcs.remove(0);
		}
		this.questMovie = questMovie;
	}

	@Override
	public void register() {
		Iterator<Integer> iterator = startNpcs.iterator();
		while (iterator.hasNext()) {
			int startNpc = iterator.next();
			qe.registerQuestNpc(startNpc).addOnQuestStart(getQuestId());
			qe.registerQuestNpc(startNpc).addOnTalkEvent(getQuestId());
		}

		iterator = actionItems.iterator();
		while (iterator.hasNext()) {
			int actionItem = iterator.next();
			qe.registerQuestNpc(actionItem).addOnTalkEvent(getQuestId());
			qe.registerCanAct(getQuestId(), actionItem);
		}

		iterator = endNpcs.iterator();
		while (iterator.hasNext()) {
			int endNpc = iterator.next();
			qe.registerQuestNpc(endNpc).addOnTalkEvent(getQuestId());
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(getQuestId());
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if ((startNpcs.isEmpty()) || (startNpcs.contains(targetId))) {
				switch (dialog) {
					case START_DIALOG: {
						if (!QuestService.inventoryItemCheck(env, true)) {
							return true;
						}
						return sendQuestDialog(env, 1011);
					}
					case SELECT_ACTION_1012: {
						if (questMovie != 0) {
							playQuestMovie(env, questMovie);
						}
						return sendQuestDialog(env, 1012);
					}
					default: {
						return sendQuestStartDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (endNpcs.contains(targetId)) {
				switch (dialog) {
					case START_DIALOG: {
						return sendQuestDialog(env, 2375);
					}
					case CHECK_COLLECTED_ITEMS: {
						return checkQuestItems(env, var, var, true, 5, 2716); // reward
					}
					case CHECK_COLLECTED_ITEMS_SIMPLE:
						return checkQuestItemsSimple(env, var, var, true, 5, 0, 0);
					case FINISH_DIALOG: {
						return sendQuestSelectionDialog(env);
					}
					default:
						break;
				}
			}
			else if ((targetId != 0) && (actionItems.contains(targetId))) {
				return true; // looting
			}
		}
		else if ((qs.getStatus() == QuestStatus.REWARD) && (endNpcs.contains(targetId))) {
			return sendQuestEndDialog(env);
		}

		return false;
	}
}
