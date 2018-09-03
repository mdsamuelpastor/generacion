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

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class _41258BasicRestoration extends QuestHandler {

    private final static int questId = 41258;

    public _41258BasicRestoration() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(205756).addOnQuestStart(questId);
        qe.registerQuestNpc(205756).addOnTalkEvent(questId);
        qe.registerQuestNpc(730477).addOnTalkEvent(questId);
        qe.registerQuestNpc(218661).addOnKillEvent(questId);
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
            if (targetId == 205756) {
                switch (env.getDialogId()) {
                    case 26:
                        return sendQuestDialog(env, 4762);
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

        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            switch (env.getTargetId()) {
                case 730477:
                    switch (env.getDialogId()) {
                        case -1: {
                            Npc npc = (Npc) player.getTarget();
                            QuestService.addNewSpawn(npc.getWorldId(), npc.getInstanceId(), 701165, npc.getX(), npc.getY(), npc.getZ(), (byte) 0);
                            qs.setStatus(QuestStatus.REWARD);
                            updateQuestStatus(env);
                            return true;
                        }
                    }
                    break;
                case 205756:
                    switch (env.getDialogId()) {
                        case 26:
                            if (var == 2) {
                                return sendQuestDialog(env, 1693);
                            }
                        case 10255: {
                            qs.setQuestVar(2);
                            qs.setStatus(QuestStatus.REWARD);
                            updateQuestStatus(env);
                            return sendQuestEndDialog(env);
                        }
                    }
            }
        }
        return sendQuestEndDialog(env);
    }

    @Override
    public boolean onKillEvent(QuestEnv env) {
        if (defaultOnKillEvent(env, 218661, 0, 1)) {
            return true;
        } else {
            return false;
        }
    }
}
