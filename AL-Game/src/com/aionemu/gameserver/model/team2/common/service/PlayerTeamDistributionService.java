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
package com.aionemu.gameserver.model.team2.common.service;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.gameobjects.player.XPCape;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.stats.StatFunctions;
import com.google.common.base.Predicate;

/**
 * @author ATracer
 */
public class PlayerTeamDistributionService {

	/**
	 * This method will send a reward if a player is in a group
	 *
	 * @param player
	 */
	public static final void doReward(TemporaryPlayerTeam<?> team, Npc owner) {
		if (team == null || owner == null) {
			return;
		}
		// Find Group Members and Determine Highest Level

		PlayerTeamRewardStats filteredStats = new PlayerTeamRewardStats(owner);
		team.applyOnMembers(filteredStats);

		// All are dead or not nearby.
		if (filteredStats.players.size() == 0 || !filteredStats.hasLivingPlayer) {
			return;
		}

		// Rewarding...
		long expReward = 0;
		if (filteredStats.players.size() + filteredStats.mentorCount == 1)
			expReward = StatFunctions.calculateSoloExperienceReward(filteredStats.players.get(0), owner);
		else
			expReward = StatFunctions.calculateGroupExperienceReward(filteredStats.highestLevel, owner);

		// Party Bonus 2 members 10%, 3 members 20% ... 6 members 50%
		int size = filteredStats.players.size();
		int bonus = 100;
		if (size > 1)
			bonus = 150 + ((size - 2) * 10);

		for (Player member : filteredStats.players) {
			if (!member.isMentor() && !member.getLifeStats().isAlreadyDead()) {
				// Exp reward
				long reward = (expReward * bonus * member.getLevel()) / (filteredStats.partyLvlSum * 100);

			// Players 10 levels below highest member get 0 exp.
			if (filteredStats.highestLevel - member.getLevel() >= 10)
				reward = 0;
			else if (filteredStats.mentorCount > 0) {
				int cape = XPCape.values()[(int) member.getLevel()].value();
				if (cape < reward)
					reward = cape;
			}

			member.getCommonData().addExp(reward, RewardType.GROUP_HUNTING, owner.getObjectTemplate().getNameId());

				// DP reward
				member.getCommonData().addDp(StatFunctions.calculateGroupDPReward(member, owner));

				// AP reward
				if (owner.isRewardAP() && (filteredStats.mentorCount <= 0 || !CustomConfig.MENTOR_GROUP_AP)) {
					int ap = StatFunctions.calculatePvEApGained(member, owner) / filteredStats.players.size();
					AbyssPointsService.addAp(member, owner, ap);
				}
			}
		}

		Player mostDamagePlayer = owner.getAggroList().getMostPlayerDamageOfMembers(team.getMembers(), filteredStats.highestLevel);
		if (mostDamagePlayer == null) {
			return;
		}
		if ((!owner.getAi2().getName().equals("chest")) || (filteredStats.mentorCount == 0))
			DropRegistrationService.getInstance().registerDrop(owner, mostDamagePlayer, filteredStats.highestLevel, filteredStats.players);
	}

	private static class PlayerTeamRewardStats implements Predicate<Player> {

		final List<Player> players = new ArrayList<Player>();
		int partyLvlSum = 0;
		int highestLevel = 0;
		int mentorCount = 0;
		boolean hasLivingPlayer = false;
		Npc owner;

		public PlayerTeamRewardStats(Npc owner) {
			this.owner = owner;
		}

		@Override
		public boolean apply(Player member) {
			if (member.isOnline()) {
				if (MathUtil.isIn3dRange(member, owner, GroupConfig.GROUP_MAX_DISTANCE)) {
					QuestEngine.getInstance().onKill(new QuestEnv(owner, member, 0, 0));
					if (member.isMentor()) {
						mentorCount++;
						return true;
					}
					if (!hasLivingPlayer && !member.getLifeStats().isAlreadyDead()) {
						hasLivingPlayer = true;
					}
					players.add(member);
					partyLvlSum += member.getLevel();
					if (member.getLevel() > highestLevel)
						highestLevel = member.getLevel();
				}
			}
			return true;
		}
	}
}
