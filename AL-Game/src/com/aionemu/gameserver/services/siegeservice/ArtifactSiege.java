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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.SiegeDAO;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.siege.ArtifactLocation;
import com.aionemu.gameserver.model.siege.SiegeModType;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.services.player.PlayerService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author SoulKeeper
 */
public class ArtifactSiege extends Siege<ArtifactLocation> {

	private static final Logger log = LoggerFactory.getLogger(ArtifactSiege.class.getName());

	public ArtifactSiege(ArtifactLocation siegeLocation) {
		super(siegeLocation);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onSiegeStart() {
		initSiegeBoss();
	}

	@Override
	protected void onSiegeFinish() {

		// cleanup
		unregisterSiegeBossListeners();

		deSpawnNpcs(getSiegeLocationId());
		if (isBossKilled())
			onCapture();
		else {
			log.error("Artifact siege (artifactId:" + getSiegeLocationId() + ") ended without killing a boss.");
		}

		// despawn npcs
		spawnNpcs(getSiegeLocationId(), getSiegeLocation().getRace(), SiegeModType.PEACE);

		DAOManager.getDAO(SiegeDAO.class).updateLocation(getSiegeLocation());
		broadcastUpdate(getSiegeLocation());
		startSiege(getSiegeLocationId());
	}

	protected void onCapture() {
		SiegeRaceCounter wRaceCounter = getSiegeCounter().getWinnerRaceCounter();
		getSiegeLocation().setRace(wRaceCounter.getSiegeRace());

		// update legion
		Integer wLegionId = wRaceCounter.getWinnerLegionId();
		getSiegeLocation().setLegionId(wLegionId != null ? wLegionId : 0);

		// misc stuff to send player system message
		if (getSiegeLocation().getRace() == SiegeRace.BALAUR) {
			// TODO: Fix message for Balaur Description id
			final AionServerPacket lRacePacket = new SM_SYSTEM_MESSAGE(1320004, getSiegeLocation().getNameAsDescriptionId(), getSiegeLocation().getRace().getDescriptionId());
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(Player object) {
					PacketSendUtility.sendPacket(object, lRacePacket);
				}
			});

		}
		else {
			// Prepare packet data
			String wPlayerName = "";
			final Race wRace = wRaceCounter.getSiegeRace() == SiegeRace.ELYOS ? Race.ELYOS : Race.ASMODIANS;
			Legion wLegion = wLegionId != null ? LegionService.getInstance().getLegion(wLegionId) : null;
			if (!wRaceCounter.getPlayerDamageCounter().isEmpty()) {
				Integer wPlayerId = wRaceCounter.getPlayerDamageCounter().keySet().iterator().next();
				wPlayerName = PlayerService.getPlayerName(wPlayerId);
			}
			final String winnerName = wLegion != null ? wLegion.getLegionName() : wPlayerName;

			// prepare packets, we can use single packet instance
			final AionServerPacket wRacePacket = new SM_SYSTEM_MESSAGE(1320002, wRace.getRaceDescriptionId(), winnerName, getSiegeLocation().getNameAsDescriptionId());
			final AionServerPacket lRacePacket = new SM_SYSTEM_MESSAGE(1320004, getSiegeLocation().getNameAsDescriptionId(), wRace.getRaceDescriptionId());

			// send update to players
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(Player player) {

					PacketSendUtility.sendPacket(player, player.getRace().equals(wRace) ? wRacePacket : lRacePacket);
				}
			});
		}
	}

	@Override
	public boolean isEndless() {
		return true;
	}

	@Override
	public void addAbyssPoints(Player player, int abysPoints) {
	}
}
