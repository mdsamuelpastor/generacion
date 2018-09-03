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
package quest.siel_spear;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.questEngine.model.QuestDialog;

public class _41469The_Real_Siels_Spears extends QuestHandler {

    private final static int questId = 41469;

    public _41469The_Real_Siels_Spears() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(205579).addOnQuestStart(questId);
        qe.registerQuestNpc(205579).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        {
            Player player = env.getPlayer();
            QuestState qs = player.getQuestStateList().getQuestState(questId);

            QuestDialog dialog = env.getDialog();

            int targetId = env.getTargetId();

            if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {

                if (targetId == 205579) //Kahrun.
                {
                    switch (dialog) {
                        case START_DIALOG:
                            return sendQuestDialog(env, 1011);
                        case ACCEPT_QUEST_SIMPLE:
                            return sendQuestStartDialog(env);
                        case REFUSE_QUEST_SIMPLE:
                            return closeDialogWindow(env);
                    }
                }
            } else if (qs.getStatus() == QuestStatus.START) {
                if (targetId == 205579) { //Kahrun.
                    switch (dialog) {
                        case START_DIALOG:
                            return sendQuestDialog(env, 2375);
                        case SELECT_REWARD:
                            qs.setStatus(QuestStatus.REWARD);
                            updateQuestStatus(env);
                            return defaultCloseDialog(env, 0, 1);
                    }
                }
            } else if (qs.getStatus() == QuestStatus.REWARD) {
                if (targetId == 205579) {
                    return sendQuestEndDialog(env);
                }
            }
            return false;

        }
    }
}