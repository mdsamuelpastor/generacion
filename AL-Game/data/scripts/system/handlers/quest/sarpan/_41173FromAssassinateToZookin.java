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
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

public class _41173FromAssassinateToZookin extends QuestHandler {

    private final static int questId = 41173;

    public _41173FromAssassinateToZookin() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(205778).addOnQuestStart(questId);
        qe.registerQuestNpc(205695).addOnTalkEvent(questId);
        qe.registerQuestNpc(218654).addOnKillEvent(questId);
    }

    @Override
    public boolean onKillEvent(QuestEnv env) {
        if (defaultOnKillEvent(env, 218654, 0, 1)) {
            return true;
        } else {
            return false;
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
        if (targetId == 205778) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                switch (env.getDialogId()) {
                    case 26:
                        return sendQuestDialog(env, 1011);
                    case 1007:
                        return sendQuestDialog(env, 1003);
                    case 20000:
                        if (QuestService.startQuest(env, QuestStatus.START)) {
                            return sendQuestDialog(env, 1008);
                        }
                }
            }
        }
        if (qs == null) {
            return false;
        }
        if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 205695) {
                switch (env.getDialogId()) {
                    case 26:
                        return sendQuestDialog(env, 1352);
                    case 1009:
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestEndDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 205695) {
                if (env.getDialogId() == 1009) {
                    return sendQuestDialog(env, 5);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
}