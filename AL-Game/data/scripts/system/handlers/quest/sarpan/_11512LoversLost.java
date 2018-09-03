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
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

public class _11512LoversLost extends QuestHandler {

    private final static int questId = 11512;

    public _11512LoversLost() {
        super(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 11511);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(205989).addOnQuestStart(questId); //owllaet
        qe.registerQuestNpc(205989).addOnTalkEvent(questId);
        qe.registerQuestNpc(730467).addOnTalkEvent(questId); //fancy box
        qe.registerQuestNpc(205746).addOnTalkEvent(questId); //molius
        qe.registerQuestNpc(218650).addOnKillEvent(questId); //aurio
    }

    @Override
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = 0;
        Npc npc = null;
        if (env.getVisibleObject() instanceof Npc) {
            npc = (Npc) env.getVisibleObject();
            targetId = npc.getNpcId();
        }
        switch (targetId) {
            case 218650:
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
                return true;
        }
        return false;
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        final QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        }
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 205989) {
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
            return false;
        }
        if (qs == null) {
            return false;
        }
        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 205989:
                    switch (env.getDialogId()) {
                        case 26:
                            return sendQuestDialog(env, 1352);
                        case 10001:
                            return defaultCloseDialog(env, 0, 1);
                    }
                    break;
                case 730467:
                    switch (env.getDialogId()) {
                        case -1: {
                            Npc npc = (Npc) player.getTarget();
                            QuestService.addNewSpawn(npc.getWorldId(), npc.getInstanceId(), 218650, npc.getX(), npc.getY(), npc.getZ(), (byte) 0);
                            qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                    }
                    break;
                default:
                    return sendQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 205746) {
                return sendQuestEndDialog(env);
            }
        }
        return false;
    }
}