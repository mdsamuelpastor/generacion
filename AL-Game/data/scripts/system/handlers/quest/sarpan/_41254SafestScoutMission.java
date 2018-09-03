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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

public class _41254SafestScoutMission extends QuestHandler {

    private final static int questId = 41254;

    public _41254SafestScoutMission() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(205756).addOnQuestStart(questId);
        qe.registerQuestNpc(205756).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestDialog dialog = env.getDialog();
        QuestState qs = player.getQuestStateList().getQuestState(getQuestId());

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 205756) {
                switch (dialog) {
                    case START_DIALOG: {
                        return sendQuestDialog(env, 4762);
                    }
                    case ASK_ACCEPTION: {
                        return sendQuestDialog(env, 4);
                    }
                    case ACCEPT_QUEST_SIMPLE: {
                        return sendQuestStartDialog(env);
                    }
                    case REFUSE_QUEST: {
                        return sendQuestDialog(env, 1004);
                    }
                    case FINISH_DIALOG:
                        return closeDialogWindow(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 205756) {
                switch (dialog) {
                    case START_DIALOG: {
                        return sendQuestDialog(env, 1011);
                    }
                    case SET_REWARD:
                        playQuestMovie(env, 711);
                        changeQuestStep(env, 0, 1, true);
                        return closeDialogWindow(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 205756) {
                return sendQuestEndDialog(env);
            }
        }
        return false;
    }
}