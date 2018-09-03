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
package com.aionemu.gameserver.services.siegeservice;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.callbacks.util.GlobalCallbackHelper;
import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.dao.SiegeDAO;
import com.aionemu.gameserver.model.gameobjects.LetterType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.siege.SiegeModType;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.siege.SourceLocation;
import com.aionemu.gameserver.model.templates.siegelocation.SiegeReward;
import com.aionemu.gameserver.services.mail.SystemMailService;
import com.aionemu.gameserver.services.player.PlayerService;
import com.google.common.collect.Lists;

public class SourceSiege extends Siege<SourceLocation> {

	private static final Logger log = LoggerFactory.getLogger("SIEGE_LOG");

	private final AbyssPointsListener addAPListener = new AbyssPointsListener(this);

	public SourceSiege(SourceLocation siegeLocation) {
		super(siegeLocation);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onSiegeStart() {
		getSiegeLocation().setPreparation(false);
		getSiegeLocation().setVulnerable(true);
		getSiegeLocation().setUnderShield(true);
		broadcastState(getSiegeLocation());
		GlobalCallbackHelper.addCallback(addAPListener);
		deSpawnNpcs(getSiegeLocationId());
		spawnNpcs(getSiegeLocationId(), getSiegeLocation().getRace(), SiegeModType.SIEGE);
		initSiegeBoss();
	}

	@Override
	protected void onSiegeFinish() {
		GlobalCallbackHelper.removeCallback(addAPListener);
		unregisterSiegeBossListeners();
		deSpawnNpcs(getSiegeLocationId());
		getSiegeLocation().setVulnerable(false);
		getSiegeLocation().setUnderShield(false);
		if (isBossKilled()) {
			onCapture();
			broadcastUpdate(getSiegeLocation(), getSiegeLocation().getTemplate().getNameId());
		}
		else {
			broadcastState(getSiegeLocation());
		}
		spawnNpcs(getSiegeLocationId(), getSiegeLocation().getRace(), SiegeModType.PEACE);
		if (SiegeRace.BALAUR != getSiegeLocation().getRace())
			giveRewardsToPlayers(getSiegeCounter().getRaceCounter(getSiegeLocation().getRace()));
		DAOManager.getDAO(SiegeDAO.class).updateSiegeLocation(getSiegeLocation());
		updateTiamarantaRiftsStatus(false, false);
	}

	@Override
	public boolean isEndless() {
		return false;
	}

	@Override
	public void addAbyssPoints(Player player, int abysPoints) {
		getSiegeCounter().addAbyssPoints(player, abysPoints);
	}

	public void onCapture() {
		SiegeRaceCounter winner = getSiegeCounter().getWinnerRaceCounter();
		getSiegeLocation().setRace(winner.getSiegeRace());

		if (SiegeRace.BALAUR == winner.getSiegeRace()) {
			getSiegeLocation().setLegionId(0);
		}
		else {
			Integer topLegionId = winner.getWinnerLegionId();
			getSiegeLocation().setLegionId(topLegionId != null ? topLegionId : 0);
		}
	}

	protected void giveRewardsToPlayers(SiegeRaceCounter winnerDamage) {
		Map<Integer, Long> playerAbyssPoints = winnerDamage.getPlayerAbyssPoints();
		List<Integer> topPlayersIds = Lists.newArrayList(playerAbyssPoints.keySet());
		Map<Integer, String> playerNames = PlayerService.getPlayerNames(playerAbyssPoints.keySet());
		List<SiegeReward> playerRewards = getSiegeLocation().getReward();
		int i = 0;
		for (SiegeReward topGrade : playerRewards) {
			for (int rewardedPC = 0; i < topPlayersIds.size() && rewardedPC < topGrade.getTop(); ++i) {
				Integer playerId = topPlayersIds.get(i);
				++rewardedPC;
				if (LoggingConfig.LOG_SIEGE) {
					log.info("[SIEGE]  > [FORTRESS:" + getSiegeLocationId() + "] [RACE: " + getSiegeLocation().getRace() + "] Player Reward to: " + playerNames.get(playerId) + "] ITEM RETURN "
						+ topGrade.getItemId() + " ITEM COUNT " + topGrade.getCount() * SiegeConfig.SIEGE_MEDAL_RATE);
				}

				SystemMailService.getInstance().sendMail("SiegeService", playerNames.get(playerId), "SiegeReward", "Medal", topGrade.getItemId(), topGrade.getCount() * SiegeConfig.SIEGE_MEDAL_RATE, 0,
					LetterType.NORMAL);
			}
		}
	}
}
