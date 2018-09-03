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
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/****/
/** Author Rinzler Modifed Da08 
/****/

public class _41520Bringing_The_Mountain_To_Kiriyu extends QuestHandler
{
    private final static int questId = 41520;
	
    public _41520Bringing_The_Mountain_To_Kiriyu() {
        super(questId);
    }
	
	@Override
    public void register() {
        qe.registerQuestNpc(205948).addOnQuestStart(questId); //Kiriyu.
        qe.registerQuestNpc(205948).addOnTalkEvent(questId); //Kiriyu.
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
        targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
	        if (targetId == 205948) { //Kiriyu.
		       if (env.getDialogId() == 26) {
			      return sendQuestDialog(env, 1011);
		       }
		       else
			     return sendQuestStartDialog(env);
	        }
        }
        if (qs == null)
            return false;
        if (qs == null || qs.getStatus() == QuestStatus.COMPLETE)  {
	        if (targetId == 205948) { //Kiriyu.
		       if (env.getDialogId() == 26) {
			     return sendQuestDialog(env, 1011);
		       }
		       else
			     return sendQuestStartDialog(env);
	        }
        }
        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 205948: //Kiriyu.
                    switch (env.getDialogId()) {
                        case 26:
                            return sendQuestDialog(env, 2375);
                        case 2034:
                            return sendQuestDialog(env, 2034);
                        case 34:
                        //Collect Transfiguration Fragments (x7)
                        if (QuestService.collectItemCheck(env, true)) {                 
			                player.getInventory().decreaseByItemId(182212524, 7); //Transfiguration Fragments.
                            qs.setStatus(QuestStatus.REWARD);
                            updateQuestStatus(env);
                            return sendQuestDialog(env, 5);
                        } else {
                            return sendQuestDialog(env, 2716);
                        }
                    }
                    break;
                default:
                    return sendQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 205948) //Kiriyu.
                return sendQuestEndDialog(env);
        }
        return false;
    }
}