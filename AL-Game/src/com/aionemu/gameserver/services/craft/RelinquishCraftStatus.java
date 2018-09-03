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
package com.aionemu.gameserver.services.craft;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.craft.ExpertQuestsList;
import com.aionemu.gameserver.model.craft.MasterQuestsList;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.skill.PlayerSkillEntry;
import com.aionemu.gameserver.model.templates.CraftLearnTemplate;
import com.aionemu.gameserver.model.templates.recipe.RecipeTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_COMPLETED_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.services.trade.PricesService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class RelinquishCraftStatus {

	private static final int expertMinValue = 400;
	private static final int expertMaxValue = 499;
	private static final int masterMinValue = 500;
	private static final int masterMaxValue = 549;
	private static final int expertPrice = 120000;
	private static final int masterPrice = 3500000;
	private static final int skillMessageId = 1401127;

	public static final RelinquishCraftStatus getInstance() {
		return SingletonHolder.instance;
	}

	public static void relinquishExpertStatus(Player player, Npc npc) {
		CraftLearnTemplate craftLearnTemplate = CraftSkillUpdateService.npcBySkill.get(npc.getNpcId());
		int skillId = craftLearnTemplate.getSkillId();
		PlayerSkillEntry skill = player.getSkillList().getSkillEntry(skillId);
		if (!canRelinquishCraftStatus(player, skill, craftLearnTemplate, expertMinValue, expertMaxValue)) {
			return;
		}
		if (!successDecreaseKinah(player, expertPrice)) {
			return;
		}
		skill.setSkillLvl(399);
		PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(skill, skillMessageId, false));
		removeRecipesAbove(player, skillId, expertMinValue);
		deleteCraftStatusQuests(skillId, player, false);
	}

	public static void relinquishMasterStatus(Player player, Npc npc) {
		CraftLearnTemplate craftLearnTemplate = CraftSkillUpdateService.npcBySkill.get(npc.getNpcId());
		int skillId = craftLearnTemplate.getSkillId();
		PlayerSkillEntry skill = player.getSkillList().getSkillEntry(skillId);
		if (!canRelinquishCraftStatus(player, skill, craftLearnTemplate, masterMinValue, masterMaxValue)) {
			return;
		}
		if (!successDecreaseKinah(player, masterPrice)) {
			return;
		}
		skill.setSkillLvl(expertMaxValue);
		PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(skill, skillMessageId, false));
		removeRecipesAbove(player, skillId, masterMinValue);
		deleteCraftStatusQuests(skillId, player, false);
	}

	private static boolean canRelinquishCraftStatus(Player player, PlayerSkillEntry skill, CraftLearnTemplate craftLearnTemplate, int minValue, int maxValue) {
		if ((craftLearnTemplate == null) || (!craftLearnTemplate.isCraftSkill())) {
			return false;
		}
		if ((skill == null) || (skill.getSkillLevel() < minValue) || (skill.getSkillLevel() > maxValue)) {
			return false;
		}
		return true;
	}

	private static boolean successDecreaseKinah(Player player, int basePrice) {
		if (!player.getInventory().tryDecreaseKinah(PricesService.getPriceForService(basePrice, player.getRace()))) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300388));
			return false;
		}
		return true;
	}

	private static void removeRecipesAbove(Player player, int skillId, int level) {
		for (RecipeTemplate recipe : DataManager.RECIPE_DATA.getRecipeTemplates().valueCollection())
			if ((recipe.getSkillid().intValue() == skillId) && (recipe.getSkillpoint().intValue() >= level)) {
				player.getRecipeList().deleteRecipe(player, recipe.getId().intValue());
			}
	}

	private static void deleteCraftStatusQuests(int skillId, Player player, boolean isExpert) {
		for (int questId : MasterQuestsList.getSkillsIds(skillId, player.getRace())) {
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			if (qs != null) {
				qs.setQuestVar(0);
				qs.setCompleteCount(0);
				qs.setStatus(null);
				qs.setPersistentState(PersistentState.DELETED);
			}
		}
		if (isExpert) {
			for (int questId : ExpertQuestsList.getSkillsIds(skillId, player.getRace())) {
				QuestState qs = player.getQuestStateList().getQuestState(questId);
				if (qs != null) {
					qs.setQuestVar(0);
					qs.setCompleteCount(0);
					qs.setStatus(null);
					qs.setPersistentState(PersistentState.DELETED);
				}
			}
		}
		PacketSendUtility.sendPacket(player, new SM_QUEST_COMPLETED_LIST(player.getQuestStateList().getAllFinishedQuests()));
		player.getController().updateNearbyQuests();
	}

	private static class SingletonHolder {

		protected static final RelinquishCraftStatus instance = new RelinquishCraftStatus();
	}
}
