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

import java.util.HashMap;
import java.util.Map;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Seraphin, fixed by Zulu & Reso, Maestross
 */

public class _41598_Making_Balaur_Luck extends QuestHandler {

	private final static int questId = 41598;
	private final Map<Integer, Integer> rewards = new HashMap<Integer, Integer>();

	public _41598_Making_Balaur_Luck() {
		super(questId);
		rewards.put(186000096, 2);
		rewards.put(186000147, 1);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(730555).addOnQuestStart(questId);
		qe.registerQuestNpc(730555).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();

		// check to avoid players from powerleveling with l2ph
		if (player.getLevel() < 58 || player.getWorldId() != 600030000)
			return false;

		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		PacketSendUtility.sendMessage(player,"Questdialog: " +dialog);

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 730555) { // tiamaranta coin fountain
				switch (dialog) {
					case START_DIALOG: {
						if (player.getCommonData().getLevel() >= 58)
							return sendQuestDialog(env, 1011);
					}
					case ACCEPT_QUEST_SIMPLE: {
						PacketSendUtility.sendMessage(player, "Quest accepted !");
 						return sendQuestDialog(env,2375);
						}

					case CHECK_COLLECTED_ITEMS_SIMPLE: {
						PacketSendUtility.sendMessage(player, "Checking..");
						long platinMedals = player.getInventory().getItemCountByItemId(186000096);
						long balaurSerum = player.getInventory().getItemCountByItemId(186000174);
						if (platinMedals > 0 && balaurSerum > 0) {
							if (!player.getInventory().isFull()) {
								if (QuestService.startQuest(env)) {
									changeQuestStep(env, 0, 0, true);
									return sendQuestDialog(env, 5);
								}
							}
							else {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_FULL_INVENTORY);
								return sendQuestSelectionDialog(env);
							}
						}
						else {
							return sendQuestSelectionDialog(env);
						}
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 730555) { // tiamaranta coin fountain
				if (dialog == QuestDialog.SELECT_NO_REWARD)
				{
					if (QuestService.finishQuest(env))
					{
						if (removeQuestItem(env, 186000096, 1) && (removeQuestItem(env, 186000174, 1)))
						{
							if (isRewardSuccessful())
							{
								ItemService.addItem(player, 186000147, rewards.get(186000147));
								return sendQuestDialog(env, 1008);
							}
							else
							{
								ItemService.addItem(player, 182005206, 1);
								return sendQuestDialog(env, 1008);
							}
						}
					}
				}
				else {
					return QuestService.abandonQuest(player, questId);
				}
			}
		}
		return false;
	}

	private boolean isRewardSuccessful()
	{
		return Rnd.get(1, 100) > 40; // 25% success
	}


}
