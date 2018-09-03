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

public class _41253Remembrance extends QuestHandler {

    private final static int questId = 41253;
    private final static int[] npcs = {205787, 205572, 730487};

    public _41253Remembrance() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(205787).addOnQuestStart(questId);
        for (int npc : npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = 0;
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        }
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 205787) {
                switch (env.getDialogId()) {
                    case 26:
                        return sendQuestDialog(env, 1011);
                    case 1007:
                        return sendQuestDialog(env, 1003);
                    case 10000:
                        if (QuestService.startQuest(env, QuestStatus.START)) {
                            return sendQuestDialog(env, 1008);
                        }
                }
            }
        }
        if (qs == null) {
            return false;
        }

        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            switch (env.getTargetId()) {
                case 205572:
                    switch (env.getDialogId()) {
                        case 26:
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            }
                        case 10000:
                            return defaultCloseDialog(env, 0, 1);
                        case 20002:
                            removeQuestItem(env, 182213213, 1);
                            giveQuestItem(env, 182213107, 1);
                            return defaultCloseDialog(env, 1, 2);
                    }
                    break;
                case 730487:
                    switch (env.getDialogId()) {
                        case -1:
                            if (var == 2) {
                                return sendQuestDialog(env, 1693);
                            }
                        case 10255: {
                            removeQuestItem(env, 182213107, 1);
                            qs.setQuestVar(2);
                            qs.setStatus(QuestStatus.REWARD);
                            updateQuestStatus(env);
                            return true;
                        }
                    }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 205787) {
                return sendQuestEndDialog(env);
            }
        }
        return false;
    }
}
