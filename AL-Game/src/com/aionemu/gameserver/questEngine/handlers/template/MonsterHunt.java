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

import javolution.util.FastMap;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.handlers.models.Monster;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author MrPoke
 * @reworked vlog
 */
public class MonsterHunt extends QuestHandler {

	private final int questId;
	private final Set<Integer> startNpcs = new HashSet<Integer>();
	private final Set<Integer> endNpcs = new HashSet<Integer>();
	private final FastMap<List<Integer>, Monster> monsters;

	public MonsterHunt(int questId, List<Integer> startNpcIds, List<Integer> endNpcIds, FastMap<List<Integer>, Monster> monsters) {
		super(questId);
		this.questId = questId;
		startNpcs.addAll(startNpcIds);
		startNpcs.remove(0);
		if (endNpcIds == null) {
			endNpcs.addAll(startNpcs);
		}
		else {
			endNpcs.addAll(endNpcIds);
			endNpcs.remove(0);
		}
		this.monsters = monsters;
	}

	@Override
	public void register() {
		Iterator<Integer> iterator = startNpcs.iterator();
		while (iterator.hasNext()) {
			int startNpc = iterator.next();
			qe.registerQuestNpc(startNpc).addOnQuestStart(getQuestId());
			qe.registerQuestNpc(startNpc).addOnTalkEvent(getQuestId());
		}
		for (List<Integer> monsterIds : monsters.keySet()) {
			iterator = monsterIds.iterator();
			while (iterator.hasNext()) {
				int monsterId = iterator.next();
				qe.registerQuestNpc(monsterId).addOnKillEvent(questId);
			}
		}
		iterator = endNpcs.iterator();
		while (iterator.hasNext()) {
			int endNpc = iterator.next();
			qe.registerQuestNpc(endNpc).addOnTalkEvent(getQuestId());
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if ((startNpcs.isEmpty()) || (startNpcs.contains(targetId))) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			for (Monster mi : monsters.values()) {
				if (mi.getEndVar() > qs.getQuestVarById(mi.getVar())) {
					return false;
				}
			}
			if (endNpcs.contains(targetId)) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1352);
				}
				else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestDialog(env, 5);
				}
			}
		}
		else if ((qs.getStatus() == QuestStatus.REWARD) && (endNpcs.contains(targetId))) {
			return sendQuestEndDialog(env);
		}

		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			for (Monster m : monsters.values()) {
				if (m.getNpcIds().contains(env.getTargetId()) && qs.getQuestVarById(m.getVar()) < m.getEndVar()) {
					qs.setQuestVarById(m.getVar(), qs.getQuestVarById(m.getVar()) + 1);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
}
