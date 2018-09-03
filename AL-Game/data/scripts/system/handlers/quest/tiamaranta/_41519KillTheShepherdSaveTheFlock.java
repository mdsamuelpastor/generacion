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
package quest.tiamaranta;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Maxin
 */
public class _41519KillTheShepherdSaveTheFlock extends QuestHandler {

    private final static int questId = 41519;

    public _41519KillTheShepherdSaveTheFlock() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(205938).addOnQuestStart(questId);
        qe.registerQuestNpc(205938).addOnTalkEvent(questId);
        qe.registerQuestNpc(701316).addOnTalkEvent(questId);
        qe.registerQuestNpc(205914).addOnTalkEvent(questId);
        qe.registerQuestNpc(218333).addOnKillEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        int targetId = env.getTargetId();

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 205938) {
                switch (dialog) {
                    case START_DIALOG:
                        return sendQuestDialog(env, 4762);
                    case ACCEPT_QUEST_SIMPLE:
                        return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 701316) {
                switch (dialog) {
                    case USE_OBJECT:
                        return useQuestObject(env, 0, 1, false, false);
                }
            } else if (targetId == 205914) {
                switch (dialog) {
                    case START_DIALOG:
                        return sendQuestDialog(env, 2375);
                    case SELECT_REWARD:
                        changeQuestStep(env, 2, 2, true);
                        return sendQuestDialog(env, 5);
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 205914) {
                return sendQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }

        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        }

        if (qs.getStatus() != QuestStatus.START) {
            return false;
        }
        if (targetId == 218333) {
            if (var == 1) {
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                return true;
            }
        }
        return false;
    }
}