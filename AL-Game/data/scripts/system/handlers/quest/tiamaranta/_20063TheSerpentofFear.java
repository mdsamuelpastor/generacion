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
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Pralinka
 */
public class _20063TheSerpentofFear extends QuestHandler {

    private final static int questId = 20063;

    public _20063TheSerpentofFear() {
        super(questId);
    }

    @Override
    public void register() {

        qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerOnLevelUp(questId);
        qe.registerOnEnterWorld(questId);
        qe.registerQuestNpc(800018).addOnTalkEvent(questId); //garnon
        qe.registerQuestNpc(800069).addOnTalkEvent(questId); //benjal
        qe.registerQuestNpc(205886).addOnTalkEvent(questId); //adella
        qe.registerOnEnterZone(ZoneName.get("HEART_OF_PETRIFICATION_600030000"), questId); //("DUEL_3_600030000", "LDF4B_CASTLE_AREA_4041", "PVP_600030000", "PVP_5_600030000",
        //																						"LDF4B_ITEMUSEAREA_Q12066A", "LDF4B_T4_PVP_LIMITEDAREA_NOBIND", "LDF4B_T4_LIMITEDAREA_NORIDING",
        //																						"LDF4B_ARTIFACT_RESULT_AREA_4041", "LDF4B_ITEMUSEAREA_Q12066A", "HEART_OF_PETRIFICATION_600030000")
    }

    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        int[] quests = {10062};
        return defaultOnLvlUpEvent(env, quests, true);
    }

    @Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            return QuestService.startQuest(env);
        }
        return false;
    }

    @Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        if (zoneName == ZoneName.get("HEART_OF_PETRIFICATION_600030000")) {
            Player player = env.getPlayer();
            if (player == null) {
                return false;
            }
            QuestState qs = player.getQuestStateList().getQuestState(questId);
            if (qs != null && qs.getStatus() == QuestStatus.START) {
                int var = qs.getQuestVarById(0);
                if (var == 1) {
                    QuestService.addNewSpawn(600030000, player.getInstanceId(), 800069, 2726f, 2514f, 349f, (byte) 85);
                    changeQuestStep(env, 1, 2, false);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }

        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        }
        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 800018: {
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            } else if (var == 3) {
                                return sendQuestDialog(env, 2034);
                            }
                        case STEP_TO_1:
                            if (var == 0) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                            }
                        case CHECK_COLLECTED_ITEMS:
                            if (var == 3) {
                                if (QuestService.collectItemCheck(env, true)) {
                                    qs.setStatus(QuestStatus.REWARD);
                                    updateQuestStatus(env);
                                    return sendQuestDialog(env, 10000);
                                } else {
                                    return sendQuestDialog(env, 10001);
                                }
                            }
                    }
                }
                break;
                case 800065: {
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            if (var == 2) {
                                env.getVisibleObject().getController().delete();
                            }
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            return sendQuestDialog(env, 1693);
                    }
                }
                break;
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 205886) {
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 1693);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
}