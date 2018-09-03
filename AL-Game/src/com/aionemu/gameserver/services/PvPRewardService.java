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
package com.aionemu.gameserver.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.PvPConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;

public class PvPRewardService {

	private static final Logger log = LoggerFactory.getLogger("PVP_LOG");
	private static final String templar = "115001381,100901028,100101022,100001339,112601240,110601295,111601260,113601249,114601244,121001151,125002883,123001205,120001233,122001392";
	private static final String gladiator = "101300978,100901028,100001339,112601240,110601295,111601260,113601249,114601244,121001151,125002883,123001205,120001233,122001392";
	private static final String cleric = "115001381,101501050,100101022,112501222,114501300,113501294,111501279,110501318,121001152,125002882,123001206,120001234,122001393";
	private static final String chanter = "115001381,101501050,100101022,112501222,114501300,113501294,111501279,110501318,121001151,125002882,123001205,120001233,122001392";
	private static final String assassin = "100201184,100001339,111301285,114301342,113301309,110301340,112301229,121001151,125002881,123001205,120001233,122001392";
	private static final String ranger = "101701060,111301285,114301342,113301309,110301340,112301229,121001151,125002881,123001205,120001233,122001392";
	private static final String sorcerer = "100501035,100601088,110101413,111101290,113101307,112101245,114101336,121001152,125002880,123001206,120001234,122001393";
	private static final String sm = "100501035,100601088,110101413,111101290,113101307,112101245,114101336,121001152,125002880,123001206,120001234,122001393";

	private static List<Integer> getRewardList(PlayerClass pc) {
		List<Integer> rewardList = new ArrayList<Integer>();
		String rewardString = "";
		switch (pc) {
			case TEMPLAR:
				rewardString = templar;
				break;
			case GLADIATOR:
				rewardString = gladiator;
				break;
			case CLERIC:
				rewardString = cleric;
				break;
			case CHANTER:
				rewardString = chanter;
				break;
			case ASSASSIN:
				rewardString = assassin;
				break;
			case RANGER:
				rewardString = ranger;
				break;
			case SORCERER:
				rewardString = sorcerer;
				break;
			case SPIRIT_MASTER:
				rewardString = sm;
				break;
			default:
				rewardString = null;
		}

		if (rewardString != null) {
			String[] parts = rewardString.split(",");
			for (int i = 0; i < parts.length; i++)
				rewardList.add(Integer.valueOf(Integer.parseInt(parts[i])));
		}
		else {
			log.warn("[PvP][Reward] There is no reward list for the {PlayerClass: " + pc.toString() + "}");
		}
		return rewardList;
	}

	public static int getRewardId(Player winner, Player victim, boolean isAdvanced) {
		int itemId = 0;
		if (victim.getSpreeLevel() > 2)
			isAdvanced = true;
		if (!isAdvanced) {
			int lvl = victim.getLevel();
			if (lvl <= 45)
				itemId = 186000031;
			if ((lvl > 45) && (lvl <= 50))
				itemId = 186000030;
			if ((lvl > 50) && (lvl <= 55))
				itemId = 186000096;
			if (lvl > 55)
				itemId = 186000147;
		}
		else {
			List<Integer> abyssItemsList = getAdvancedReward(winner);
			itemId = abyssItemsList.get(Rnd.get(abyssItemsList.size())).intValue();
		}
		return itemId;
	}

	public static float getMedalRewardChance(Player winner, Player victim) {
		float chance = PvPConfig.MEDAL_REWARD_CHANCE;
		chance += 1.5F * winner.getRawKillCount();
		int diff = victim.getLevel() - winner.getLevel();
		if (diff * diff > 100) {
			if (diff < 0)
				diff = -10;
			else
				diff = 10;
		}
		chance += 2.0F * diff;

		if ((victim.getSpreeLevel() > 0) || (chance > 100.0F)) {
			chance = 100.0F;
		}

		return chance;
	}

	public static int getRewardQuantity(Player winner, Player victim) {
		int rewardQuantity = winner.getSpreeLevel() + 1;
		switch (victim.getSpreeLevel()) {
			case 1:
				rewardQuantity += 5;
				break;
			case 2:
				rewardQuantity += 10;
				break;
			case 3:
				rewardQuantity += 15;
		}

		return rewardQuantity;
	}

	public static float getTollRewardChance(Player winner, Player victim) {
		float chance = PvPConfig.TOLL_REWARD_CHANCE;
		chance += 1.5F * winner.getRawKillCount();
		int diff = victim.getLevel() - winner.getLevel();
		if (diff * diff > 100) {
			if (diff < 0)
				diff = -10;
			else
				diff = 10;
		}
		chance += 2.0F * diff;

		if ((victim.getSpreeLevel() > 0) || (chance > 100.0F)) {
			chance = 100.0F;
		}
		return chance;
	}

	public static int getTollQuantity(Player winner, Player victim) {
		int tollQuantity = winner.getSpreeLevel() + 1;
		switch (victim.getSpreeLevel()) {
			case 1:
				tollQuantity += 5;
				break;
			case 2:
				tollQuantity += 10;
				break;
			case 3:
				tollQuantity += 15;
		}

		return tollQuantity;
	}

	private static List<Integer> getAdvancedReward(Player winner) {
		int lvl = winner.getLevel();
		PlayerClass pc = winner.getPlayerClass();
		List<Integer> rewardList = new ArrayList<Integer>();

		if ((lvl >= 10) && (lvl < 70)) {
			rewardList.addAll(getFilteredRewardList(pc, 10, 70));
		}
		return rewardList;
	}

	private static List<Integer> getFilteredRewardList(PlayerClass pc, int minLevel, int maxLevel) {
		List<Integer> filteredRewardList = new ArrayList<Integer>();
		List<Integer> rewardList = getRewardList(pc);

		for (Iterator<Integer> i = rewardList.iterator(); i.hasNext();) {
			int id = i.next();
			ItemTemplate itemTemp = DataManager.ITEM_DATA.getItemTemplate(id);
			if (itemTemp == null) {
				log.warn("[PvP][Reward] Incorrect {Item ID: " + id + "} reward for {PlayerClass: " + pc.toString() + "}");
			}
			int itemLevel = itemTemp.getLevel();

			if (itemLevel >= minLevel && itemLevel < maxLevel)
				filteredRewardList.add(id);
		}
		return filteredRewardList.size() > 0 ? filteredRewardList : new ArrayList<Integer>();
	}
}
