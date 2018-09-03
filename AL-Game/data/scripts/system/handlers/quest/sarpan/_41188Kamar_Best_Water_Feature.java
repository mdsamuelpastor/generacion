/*
 * This file is part of NextGenCore <Ver:3.1>.
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
 * @author Seraphin
 * @author Maestross
 */

public class _41188Kamar_Best_Water_Feature extends QuestHandler {
	
	private final static int questId = 41188;
	private final Map<Integer, Integer> rewards = new HashMap<Integer, Integer>();
	
	public _41188Kamar_Best_Water_Feature() {
		super(questId);
		rewards.put(186000030, 2);
		rewards.put(186000096, 1);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(730545).addOnQuestStart(questId);
		qe.registerQuestNpc(730545).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		
		//check to avoid players from powerleveling with l2ph
		if (player.getLevel() < 50 )
			return false;
				
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 730545 || targetId == 730556 || targetId == 730557) { // Sarpan/Elian/Pernon Coin's Fountain.
				switch (dialog) {
					case START_DIALOG: {
						if(player.getCommonData().getLevel() >= 50)
							return sendQuestDialog(env, 1011);
					}
					case STEP_TO_1: {
						long goldMedals = player.getInventory().getItemCountByItemId(186000096);
						if (goldMedals > 0) {
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
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 730545 || targetId == 730556 || targetId == 730557) { // Sarpan/Elian/Pernon Coin's Fountain.
				if (dialog == QuestDialog.SELECT_NO_REWARD) {
					if (QuestService.finishQuest(env)) {
						if(removeQuestItem(env, 186000030, 1)) {
							if (isRewardSuccessful()) {
								switch (determineReward()) {
									case 0: {
										ItemService.addItem(player, 186000030, rewards.get(186000030));
										break;
									}
									case 1: {
										ItemService.addItem(player, 186000096, rewards.get(186000096));
										break;
									}
								}
								return sendQuestDialog(env, 1008);
							}
							else {
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
	
	/** Based on retail tests. Tester: Tibald */
	private boolean isRewardSuccessful() {
		// 303 total
		// 174 rusty
		return Rnd.get(1, 100) > 40; // 43% success
	}
	
	/** Based on retail tests. Tester: Tibald */
	private int determineReward() {
		// 129 total
		// 56 platine 43%
		// 67 gold 52%
		// 6 quartz 5%
		int random = Rnd.get(1, 100);
		if (random <= 10) {
			return 1;
		}
		else if (random > 20 && random < 96) {
			return 0;
		}
		else {
			return 2;
		}
	}
}
