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

public class _41536A_Message_In_Stone extends QuestHandler
{
	private final static int questId = 41536;
	
	public _41536A_Message_In_Stone() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(205944).addOnQuestStart(questId); //Taladin.
		qe.registerQuestNpc(205944).addOnTalkEvent(questId); //Taladin.
		qe.registerQuestNpc(701238).addOnTalkEvent(questId); //Monument Of Loyalty.
		qe.registerQuestNpc(701238).addOnTalkEvent(questId); //Glorious Monument.
		qe.registerQuestNpc(701240).addOnTalkEvent(questId); //Monument Of Victory.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (targetId == 205944) { //Taladin.
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				if (env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else
					return sendQuestStartDialog(env);
			}
			if (qs != null && qs.getStatus() == QuestStatus.START) {
				if (env.getDialogId() == 26)
					return sendQuestDialog(env, 2375);
				else if (env.getDialogId() == 34) {
					 if (QuestService.collectItemCheck(env, true)) {
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 5);
					 }
					 else
						return sendQuestDialog(env, 2716);
				}
				else
					return sendQuestEndDialog(env);
			}
			else if (qs != null && qs.getStatus() == QuestStatus.REWARD)
				return sendQuestEndDialog(env);
		}
		else if (qs != null && qs.getStatus() == QuestStatus.START) {
			switch(targetId) {
				case 701238: //Monument Of Loyalty.
				case 701239: //Glorious Monument.
				case 701240: { //Monument Of Victory.
					if (qs.getQuestVarById(0) == 0 && env.getDialogId() == -1)
						return true;
				}
			}
		}
		return false;
	}
}