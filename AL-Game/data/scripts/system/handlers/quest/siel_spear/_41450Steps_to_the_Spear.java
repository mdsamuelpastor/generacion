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

public class _41450Steps_to_the_Spear extends QuestHandler {

    private final static int questId = 41450;

    public _41450Steps_to_the_Spear() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(205579).addOnQuestStart(questId);
        qe.registerQuestNpc(205579).addOnTalkEvent(questId);
        qe.registerQuestNpc(205585).addOnTalkEvent(questId);
        qe.registerQuestNpc(205798).addOnTalkEvent(questId);
        qe.registerQuestNpc(205799).addOnTalkEvent(questId);
        qe.registerQuestNpc(205800).addOnTalkEvent(questId);
        qe.registerQuestNpc(205801).addOnTalkEvent(questId);
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
                            return sendQuestDialog(env, 4762);
                        case ACCEPT_QUEST_SIMPLE:
                            return sendQuestStartDialog(env);
                        case REFUSE_QUEST_SIMPLE:
                            return closeDialogWindow(env);
                    }
                }
            } else if (qs.getStatus() == QuestStatus.START) {
                if (targetId == 205798) { //Emita.
                    switch (dialog) {
                        case START_DIALOG:
                            return sendQuestDialog(env, 1011);
                        case STEP_TO_1:
                            return defaultCloseDialog(env, 0, 1);
                    }
                } else if (targetId == 205799) { //Ampria.
                    switch (dialog) {
                        case START_DIALOG:
                            return sendQuestDialog(env, 1352);
                        case STEP_TO_2:
                            return defaultCloseDialog(env, 1, 2);
                    }
                } else if (targetId == 205800) { //Draitia.
                    switch (dialog) {
                        case START_DIALOG:
                            return sendQuestDialog(env, 1693);
                        case STEP_TO_3:
                            return defaultCloseDialog(env, 2, 3);
                    }
                } else if (targetId == 205801) { //Tinotia.
                    switch (dialog) {
                        case START_DIALOG:
                            return sendQuestDialog(env, 2034);
                        case STEP_TO_4:
                            return defaultCloseDialog(env, 3, 4);
                    }
                } else if (targetId == 205579) { //Kahrun.
                    switch (dialog) {
                        case START_DIALOG:
                            return sendQuestDialog(env, 2375);
                        case STEP_TO_5:
                            return sendQuestDialog(env, 2716);
                        case CHECK_COLLECTED_ITEMS_SIMPLE:
                            if (QuestService.collectItemCheck(env, true)) {
                                player.getInventory().decreaseByItemId(186000160, 1);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 10002);
                            } else {
                                return sendQuestDialog(env, 10001);
                            }
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