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
import com.aionemu.gameserver.configs.main.AdvCustomConfig;
import com.aionemu.gameserver.dao.SiegeDAO;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.siege.FortressLocation;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.model.siege.SiegeModType;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.siege.SourceLocation;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SIEGE_LOCATION_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

public class SiegeAutoRace {

	private static final Logger log = LoggerFactory.getLogger("SIEGE_LOG");
	private static String[] siegeIds = AdvCustomConfig.SIEGE_AUTO_LOCID.split(";");
	private static String[] sourceIds = AdvCustomConfig.SIEGE_AUTO_LOCID.split(";");

	public static void AutoSourceRace() {

		log.debug("Starting preparations of all source locations");

		for (final SourceLocation source : SiegeService.getInstance().getSources().values()) {
			if (!source.getRace().equals(SiegeRace.ASMODIANS) || !source.getRace().equals(SiegeRace.ELYOS)) {
				SiegeService.getInstance().deSpawnNpcs(source.getLocationId());
				final int oldOwnerRaceId = source.getRace().getRaceId();
				final int legionId = source.getLegionId();
				final String legionName = legionId != 0 ? LegionService.getInstance().getLegion(legionId).getLegionName() : "";
				final DescriptionId sourceNameId = new DescriptionId(source.getTemplate().getNameId());

				if (ElyAutoSource(source.getLocationId()))
					source.setRace(SiegeRace.ELYOS);
				if (AmoAutoSource(source.getLocationId()))
					source.setRace(SiegeRace.ASMODIANS);
				source.setLegionId(0);

				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						if (legionId != 0 && player.getRace().getRaceId() == oldOwnerRaceId) {
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1301037, legionName, sourceNameId));
						}
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1301039, source.getRace().getDescriptionId(), sourceNameId));
						PacketSendUtility.sendPacket(player, new SM_SIEGE_LOCATION_INFO(source));
					}
				});
				if (ElyAutoSource(source.getLocationId()))
					SiegeService.getInstance().spawnNpcs(source.getLocationId(), SiegeRace.ELYOS, SiegeModType.PEACE);
				else if (AmoAutoSource(source.getLocationId()))
					SiegeService.getInstance().spawnNpcs(source.getLocationId(), SiegeRace.ASMODIANS, SiegeModType.PEACE);
				DAOManager.getDAO(SiegeDAO.class).updateSiegeLocation(source);
			}
		}
		SiegeService.getInstance().updateTiamarantaRiftsStatus(false, true);
	}

	public static void AutoSiegeRace(final int locid) {
		final SiegeLocation loc = SiegeService.getInstance().getSiegeLocation(locid);
		if (!loc.getRace().equals(SiegeRace.ASMODIANS) || !loc.getRace().equals(SiegeRace.ELYOS)) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					SiegeService.getInstance().startSiege(locid);
				}
			}, 300000);

			SiegeService.getInstance().deSpawnNpcs(locid);

			final int oldOwnerRaceId = loc.getRace().getRaceId();
			final int legionId = loc.getLegionId();
			final String legionName = legionId != 0 ? LegionService.getInstance().getLegion(legionId).getLegionName() : "";
			final DescriptionId NameId = new DescriptionId(loc.getTemplate().getNameId());
			if (ElyAutoSiege(locid))
				loc.setRace(SiegeRace.ELYOS);
			if (AmoAutoSiege(locid))
				loc.setRace(SiegeRace.ASMODIANS);
			loc.setLegionId(0);
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(Player player) {
					if (legionId != 0 && player.getRace().getRaceId() == oldOwnerRaceId) {
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1301037, legionName, NameId));
					}
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1301039, loc.getRace().getDescriptionId(), NameId));
					PacketSendUtility.sendPacket(player, new SM_SIEGE_LOCATION_INFO(loc));
				}
			});
			if (ElyAutoSiege(locid))
				SiegeService.getInstance().spawnNpcs(locid, SiegeRace.ELYOS, SiegeModType.PEACE);
			else if (AmoAutoSiege(locid))
				SiegeService.getInstance().spawnNpcs(locid, SiegeRace.ASMODIANS, SiegeModType.PEACE);
			DAOManager.getDAO(SiegeDAO.class).updateSiegeLocation(loc);
			SiegeService.getInstance().updateOutpostStatusByFortress((FortressLocation) loc);
		}
		SiegeService.getInstance().broadcastUpdate(loc);
	}

	public static boolean isAutoSiege(int locId) {
		return ElyAutoSiege(locId) || AmoAutoSiege(locId);
	}

	public static boolean ElyAutoSiege(int locId) {
		for (String id : siegeIds[0].split(",")) {
			if (locId == Integer.parseInt(id))
				return true;
		}
		return false;
	}

	public static boolean AmoAutoSiege(int locId) {
		for (String id : siegeIds[1].split(",")) {
			if (locId == Integer.parseInt(id))
				return true;
		}
		return false;
	}

	public static boolean isAutoSource(int locId) {
		return ElyAutoSource(locId) || AmoAutoSource(locId);
	}

	public static boolean ElyAutoSource(int locId) {
		for (String id : sourceIds[0].split(",")) {
			if (locId == Integer.parseInt(id))
				return true;
		}
		return false;
	}

	public static boolean AmoAutoSource(int locId) {
		for (String id : sourceIds[1].split(",")) {
			if (locId == Integer.parseInt(id))
				return true;
		}
		return false;
	}
}
