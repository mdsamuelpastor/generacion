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
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import java.util.Collections;

public class _11507MerchantNeedsFoodBadly extends QuestHandler {

    private final static int questId = 11507;

    public _11507MerchantNeedsFoodBadly() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(205695).addOnQuestStart(questId);
        qe.registerQuestNpc(205695).addOnTalkEvent(questId);
        qe.registerQuestNpc(205697).addOnTalkEvent(questId);
        qe.registerOnQuestTimerEnd(questId);
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        }
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 205695) {
                switch (env.getDialogId()) {
                    case 26:
                        return sendQuestDialog(env, 1011);
                    case 1007:
                        return sendQuestDialog(env, 1003);
                    case 20000:
                        QuestService.questTimerStart(env, 300);
                        giveQuestItem(env, 182213101, 1);
                        if (QuestService.startQuest(env, QuestStatus.START)) {
                            return sendQuestDialog(env, 1008);
                        } else {
                            return false;
                        }
                }
            }
        } else if (targetId == 205697) {
            if (qs != null) {
                if (env.getDialogId() == 26 && qs.getStatus() == QuestStatus.START) {
                    removeQuestItem(env, 182213101, 1);
                    QuestService.questTimerEnd(env);
                    return sendQuestDialog(env, 2375);
                } else if (env.getDialogId() == 1009) {
                    qs.setQuestVar(1);
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    return sendQuestEndDialog(env);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }

    @Override
    public boolean onQuestTimerEndEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        }
        PacketSendUtility.sendMessage(player, "ToDo: Set what happens when timer ends..... And remove temp from QuestService");
        return true;
    }
}