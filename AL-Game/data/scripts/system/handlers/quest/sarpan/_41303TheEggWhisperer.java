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
package quest.sarpan;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import java.util.Collections;

public class _41303TheEggWhisperer extends QuestHandler {

    private final static int questId = 41303;
    private final static int[] mob_ids = {218156, 218157};

    public _41303TheEggWhisperer() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(205794).addOnQuestStart(questId);
        qe.registerQuestNpc(205794).addOnTalkEvent(questId);
        for (int mob_id : mob_ids) {
            qe.registerQuestNpc(mob_id).addOnKillEvent(questId);
        }
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        }
        if (targetId == 205794) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                switch (env.getDialogId()) {
                    case 26:
                        return sendQuestDialog(env, 1011);
                    case 1007:
                        return sendQuestDialog(env, 1003);
                    case 20000:
                        if (QuestService.startQuest(env, QuestStatus.START)) {
                            giveQuestItem(env, 182213110, 1);
                        }
                        return sendQuestDialog(env, 1008);
                }
            } else if (qs.getStatus() == QuestStatus.START) {
                switch (env.getDialogId()) {
                    case 26:
                        return sendQuestDialog(env, 1352);
                    case 1009:
                        qs.setQuestVarById(0, qs.getQuestVarById(0) + 11);
                        removeQuestItem(env, 182213110, 1);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestEndDialog(env);
                }
            } else if (qs.getStatus() == QuestStatus.REWARD) {
                if (targetId == 205794) {
                    if (env.getDialogId() == 1009) {
                        return sendQuestDialog(env, 5);
                    } else {
                        return sendQuestEndDialog(env);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestEnv env) {
        if (defaultOnKillEvent(env, mob_ids, 0, 11)) {
            return true;
        } else {
            return false;
        }
    }
}