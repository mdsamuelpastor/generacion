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

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import javolution.util.FastMap;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.AdvCustomConfig;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.configs.shedule.SiegeSchedule;
import com.aionemu.gameserver.configs.shedule.SiegeSchedule.Fortress;
import com.aionemu.gameserver.configs.shedule.SiegeSchedule.Source;
import com.aionemu.gameserver.dao.SiegeDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.PlayerInitialData;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.BindPointPosition;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.siege.SiegeNpc;
import com.aionemu.gameserver.model.siege.ArtifactLocation;
import com.aionemu.gameserver.model.siege.FortressLocation;
import com.aionemu.gameserver.model.siege.Influence;
import com.aionemu.gameserver.model.siege.OutpostLocation;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.model.siege.SiegeModType;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.siege.SourceLocation;
import com.aionemu.gameserver.model.siege.SiegeType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.siegespawns.SiegeSpawnTemplate;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_ARTIFACT_INFO3;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FORTRESS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FORTRESS_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INFLUENCE_RATIO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RIFT_ANNOUNCE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SHIELD_EFFECT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SIEGE_LOCATION_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SIEGE_LOCATION_STATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.siegeservice.ArtifactSiege;
import com.aionemu.gameserver.services.siegeservice.FortressSiege;
import com.aionemu.gameserver.services.siegeservice.OutpostSiege;
import com.aionemu.gameserver.services.siegeservice.Siege;
import com.aionemu.gameserver.services.siegeservice.SiegeAutoRace;
import com.aionemu.gameserver.services.siegeservice.SiegeException;
import com.aionemu.gameserver.services.siegeservice.SiegeStartRunnable;
import com.aionemu.gameserver.services.siegeservice.SourceSiege;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.WorldType;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author SoulKeeper, Maestross
 */
public class SiegeService {

	/**
	 * Just a logger
	 */
	private static final Logger log = LoggerFactory.getLogger("SIEGE_LOG");
	/**
	 * Balaur protector spawn schedule.
	 */
	private static final String RACE_PROTECTOR_SPAWN_SCHEDULE = SiegeConfig.RACE_PROTECTOR_SPAWN_SCHEDULE;

	private static final String BERSERKER_SUNAYAKA_SPAWN_SCHEDULE = SiegeConfig.BERSERKER_SUNAYAKA_SPAWN_SCHEDULE;

	private static final String SIEGE_LOCATION_STATUS_BROADCAST_SCHEDULE = "0 0 * ? * *";
	/**
	 * Singleton that is loaded on the class initialization. Guys, we really do not SingletonHolder classes
	 */
	private static final SiegeService instance = new SiegeService();
	/**
	 * Map that holds fortressId to Siege. We can easily know what fortresses is under siege ATM :)
	 */
	private final Map<Integer, Siege<?>> activeSieges = new FastMap<Integer, Siege<?>>().shared();
	/**
	 * Object that holds siege schedule.<br>
	 * And maybe other useful information (in future).
	 */
	private SiegeSchedule siegeSchedule;
	private boolean cl;
	private boolean cr;
	private boolean tl;
	private boolean tr;
	private FastMap<Integer, VisibleObject> tiamarantaPortals = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> tiamarantaEyeBoss = new FastMap<Integer, VisibleObject>();
	private Map<Integer, ArtifactLocation> artifacts;
	private Map<Integer, FortressLocation> fortresses;
	private Map<Integer, OutpostLocation> outposts;
	private Map<Integer, SourceLocation> sources;
	private Map<Integer, SiegeLocation> locations;

	public static SiegeService getInstance() {
		return instance;
	}

	public void initSiegeLocations() {
		if (SiegeConfig.SIEGE_ENABLED) {
			log.info("Initializing sieges...");

			if (siegeSchedule != null) {
				log.error("SiegeService should not be initialized two times!");
				return;
			}

			// initialize current siege locations
			artifacts = DataManager.SIEGE_LOCATION_DATA.getArtifacts();
			fortresses = DataManager.SIEGE_LOCATION_DATA.getFortress();
			outposts = DataManager.SIEGE_LOCATION_DATA.getOutpost();
			sources = DataManager.SIEGE_LOCATION_DATA.getSource();
			locations = DataManager.SIEGE_LOCATION_DATA.getSiegeLocations();
			DAOManager.getDAO(SiegeDAO.class).loadSiegeLocations(locations);
		}
		else {
			artifacts = Collections.emptyMap();
			fortresses = Collections.emptyMap();
			outposts = Collections.emptyMap();
			sources = Collections.emptyMap();
			locations = Collections.emptyMap();
			log.info("Sieges are disabled in config.");
		}
	}

	@SuppressWarnings("deprecation")
	public void initSieges() {
		if (!SiegeConfig.SIEGE_ENABLED) {
			return;
		}

		// despawn all NPCs spawned by spawn engine.
		// Siege spawns should be controlled by siege service
		for (Integer i : getSiegeLocations().keySet()) {
			deSpawnNpcs(i);
		}

		// spawn fortress common npcs
		for (FortressLocation f : getFortresses().values()) {
			spawnNpcs(f.getLocationId(), f.getRace(), SiegeModType.PEACE);
		}

		for (SourceLocation s : getSources().values()) {
			spawnNpcs(s.getLocationId(), s.getRace(), SiegeModType.PEACE);
		}
		for (OutpostLocation o : getOutposts().values()) {
			if (SiegeRace.BALAUR != o.getRace() && o.getLocationRace() != o.getRace()) {
				spawnNpcs(o.getLocationId(), o.getRace(), SiegeModType.PEACE);
			}
		}

		// spawn artifacts
		for (ArtifactLocation a : getStandaloneArtifacts().values()) {
			spawnNpcs(a.getLocationId(), a.getRace(), SiegeModType.PEACE);
		}

		// initialize siege schedule
		siegeSchedule = SiegeSchedule.load();

		// Schedule fortresses sieges protector spawn
		for (Fortress f : siegeSchedule.getFortressesList()) {
			for (String siegeTime : f.getSiegeTimes()) {
				CronService.getInstance().schedule(new SiegeStartRunnable(f.getId()), siegeTime);
				log.debug("Scheduled siege of fortressID " + f.getId() + " based on cron expression: " + siegeTime);
			}
		}
		for (Source s : siegeSchedule.getSourcesList()) {
			for (String siegeTime : s.getSiegeTimes()) {
				CronService.getInstance().schedule(new SiegeStartRunnable(s.getId()), siegeTime);
				log.debug("Scheduled siege of sourceID " + s.getId() + " based on cron expression: " + siegeTime);
			}
		}
		updateTiamarantaRiftsStatus(false, true);

		// Outpost siege start... Why it's called balaur?
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				Calendar calendar = Calendar.getInstance();
				int bossId = calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY ? 218553 : 219359;
				if ((cl) && (cr)) {
					if (tiamarantaEyeBoss.containsKey(bossId) && tiamarantaEyeBoss.get(bossId).isSpawned()) {
						SiegeService.log.warn("Sunayaka was already spawned...");
					}
					else {
						tiamarantaEyeBoss.put(bossId, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(600040000, bossId, 759.09583F, 765.68158F, 1226.5004F, (byte) 0), 1));

						tiamarantaEyeBoss.put(283074, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(600040000, 283074, 754.84625F, 819.98828F, 1223.957F, (byte) 0), 1));

						tiamarantaEyeBoss.put(283076, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(600040000, 283076, 754.90234F, 712.99042F, 1223.957F, (byte) 0), 1));
					}

					if (bossId == 219359)
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								for (VisibleObject vo : tiamarantaEyeBoss.values()) {
									if (vo != null) {
										Npc npc = (Npc) vo;
										if (!npc.getLifeStats().isAlreadyDead()) {
											npc.getController().onDelete();
										}
									}
									tiamarantaEyeBoss.clear();
								}
							}
						}, 3600000);
				}
			}
		}, BERSERKER_SUNAYAKA_SPAWN_SCHEDULE);
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				for (OutpostLocation o : getOutposts().values())
					if (o.isSiegeAllowed())
						startSiege(o.getLocationId());
			}
		}, RACE_PROTECTOR_SPAWN_SCHEDULE);

		// Start siege of artifacts
		for (ArtifactLocation artifact : artifacts.values()) {
			if (artifact.isStandAlone()) {
				log.debug("Starting siege of artifact #" + artifact.getLocationId());
				startSiege(artifact.getLocationId());
			}
			else {
				log.debug("Artifact #" + artifact.getLocationId() + " siege was not started, it belongs to fortress");
			}
		}

		// We should set valid next state for fortress on startup
		// no need to broadcast state here, no players @ server ATM
		updateFortressNextState();

		// Schedule siege status broadcast (every hour)
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				updateFortressNextState();
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						for (FortressLocation fortress : getFortresses().values()) {
							PacketSendUtility.sendPacket(player, new SM_FORTRESS_INFO(fortress.getLocationId(), false));
						}
						PacketSendUtility.sendPacket(player, new SM_FORTRESS_STATUS());

						for (FortressLocation fortress : getFortresses().values())
							PacketSendUtility.sendPacket(player, new SM_FORTRESS_INFO(fortress.getLocationId(), true));
					}
				});
			}
		}, SIEGE_LOCATION_STATUS_BROADCAST_SCHEDULE);

		log.debug("Broadcasting Siege Location status based on expression: 0 0 * ? * *");
	}

	public void checkSiegeStart(int locationId) {
		if (AdvCustomConfig.SIEGE_AUTO_RACE && SiegeAutoRace.isAutoSiege(locationId)) {
			SiegeAutoRace.AutoSiegeRace(locationId);
		}
		else if (getSource(locationId) == null) {
			startSiege(locationId);
		}
		else if (locationId == 4011)
			startPreparations();
	}

	public void startPreparations() {
		log.debug("Starting preparations of all source locations");

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				World.getInstance().getWorldMap(600040000).getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						TeleportService2.moveToBindLocation(player, true);
					}
				});
				for (SourceLocation source : getSources().values())
					startSiege(source.getLocationId());
			}
		}, 300000);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				for (SourceLocation source : getSources().values()) {
					source.clearLocation();
				}
				World.getInstance().getWorldMap(600030000).getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						for (SourceLocation source : getSources().values()) {
							PacketSendUtility.sendPacket(player, new SM_SHIELD_EFFECT(source.getLocationId()));
						}
						for (SourceLocation source : getSources().values())
							PacketSendUtility.sendPacket(player, new SM_SIEGE_LOCATION_STATE(source.getLocationId(), 2));
					}
				});
			}
		}, 310000);

		for (final SourceLocation source : getSources().values()) {
			source.setPreparation(true);

			if (AdvCustomConfig.AUTO_SOURCE_RACE) {
				SiegeAutoRace.AutoSourceRace();
			}
			else if (!source.getRace().equals(SiegeRace.BALAUR)) {
				deSpawnNpcs(source.getLocationId());

				final int oldOwnerRaceId = source.getRace().getRaceId();
				final int legionId = source.getLegionId();
				final String legionName = legionId != 0 ? LegionService.getInstance().getLegion(legionId).getLegionName() : "";
				final DescriptionId sourceNameId = new DescriptionId(source.getTemplate().getNameId());

				source.setRace(SiegeRace.BALAUR);
				source.setLegionId(0);

				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						if ((legionId != 0) && (player.getRace().getRaceId() == oldOwnerRaceId)) {
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1301037, legionName, sourceNameId));
						}

						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1301039, source.getRace().getDescriptionId(), sourceNameId));

						PacketSendUtility.sendPacket(player, new SM_SIEGE_LOCATION_INFO(source));
					}
				});
				spawnNpcs(source.getLocationId(), SiegeRace.BALAUR, SiegeModType.PEACE);

				DAOManager.getDAO(SiegeDAO.class).updateSiegeLocation(source);
			}
		}

		updateTiamarantaRiftsStatus(true, false);
	}

	public void startSiege(final int siegeLocationId) {
		log.debug("Starting siege of siege location: " + siegeLocationId);

		// Siege should not be started two times. Never.
		Siege<?> siege;
		synchronized (this) {
			if (activeSieges.containsKey(siegeLocationId)) {
				log.error("Attempt to start siege twice for siege location: " + siegeLocationId);
				return;
			}
			siege = newSiege(siegeLocationId);
			activeSieges.put(siegeLocationId, siege);
		}

		siege.startSiege();

		// certain sieges are endless
		// should end only manually on siege boss death
		if (siege.isEndless()) {
			return;
		}

		// schedule siege end
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				stopSiege(siegeLocationId);
			}
		}, siege.getSiegeLocation().getSiegeDuration() * 1000);
	}

	public void stopSiege(int siegeLocationId) {

		log.debug("Stopping siege of siege location: " + siegeLocationId);

		// Just a check here...
		// If fortresses was captured in 99% the siege timer will return here
		// without concurrent race
		if (!isSiegeInProgress(siegeLocationId)) {
			log.debug("Siege of siege location " + siegeLocationId + " is not in progress, it was captured earlier?");
			return;
		}

		// We need synchronization here for that 1% of cases :)
		// It may happen that fortresses siege is stopping in the same time by 2 different threads
		// 1 is for killing the boss
		// 2 is for the schedule
		// it might happen that siege will be stopping by other thread, but in such case siege object will be null
		Siege<?> siege;
		synchronized (this) {
			siege = activeSieges.remove(siegeLocationId);
		}
		if (siege == null || siege.isFinished()) {
			return;
		}

		siege.stopSiege();
	}

	/**
	 * Updates next state for fortresses
	 */
	protected void updateFortressNextState() {

		// get current hour and add 1 hour
		Calendar currentHourPlus1 = Calendar.getInstance();
		currentHourPlus1.set(Calendar.MINUTE, 0);
		currentHourPlus1.set(Calendar.SECOND, 0);
		currentHourPlus1.set(Calendar.MILLISECOND, 0);
		currentHourPlus1.add(Calendar.HOUR, 1);

		// filter fortress siege start runnables
		Map<Runnable, JobDetail> siegeStartRunables = CronService.getInstance().getRunnables();
		siegeStartRunables = Maps.filterKeys(siegeStartRunables, new Predicate<Runnable>() {

			@Override
			public boolean apply(@Nullable Runnable runnable) {
				return (runnable instanceof SiegeStartRunnable);
			}
		});

		// Create map FortressId-To-AllTriggers
		Map<Integer, List<Trigger>> siegeIdToStartTriggers = Maps.newHashMap();
		for (Map.Entry<Runnable, JobDetail> entry : siegeStartRunables.entrySet()) {
			SiegeStartRunnable fssr = (SiegeStartRunnable) entry.getKey();

			List<Trigger> storage = siegeIdToStartTriggers.get(fssr.getLocationId());
			if (storage == null) {
				storage = Lists.newArrayList();
				siegeIdToStartTriggers.put(fssr.getLocationId(), storage);
			}
			storage.addAll(CronService.getInstance().getJobTriggers(entry.getValue()));
		}

		// update each fortress next state
		for (Map.Entry<Integer, List<Trigger>> entry : siegeIdToStartTriggers.entrySet()) {

			List<Date> nextFireDates = Lists.newArrayListWithCapacity(entry.getValue().size());
			for (Trigger trigger : entry.getValue()) {
				nextFireDates.add(trigger.getNextFireTime());
			}
			Collections.sort(nextFireDates);

			// clear non-required times
			Date nextSiegeDate = nextFireDates.get(0);
			Calendar siegeStartHour = Calendar.getInstance();
			siegeStartHour.setTime(nextSiegeDate);
			siegeStartHour.set(Calendar.MINUTE, 0);
			siegeStartHour.set(Calendar.SECOND, 0);
			siegeStartHour.set(Calendar.MILLISECOND, 0);

			// update fortress state that will be valid in 1 h
			SiegeLocation fortress = getSiegeLocation(entry.getKey());

			Calendar siegeCalendar = Calendar.getInstance();
			siegeCalendar.set(Calendar.MINUTE, 0);
			siegeCalendar.set(Calendar.SECOND, 0);
			siegeCalendar.set(Calendar.MILLISECOND, 0);
			siegeCalendar.add(Calendar.HOUR, 0);
			siegeCalendar.add(Calendar.SECOND, getRemainingSiegeTimeInSeconds(fortress.getLocationId()));

			if (fortress instanceof SourceLocation || AdvCustomConfig.SIEGE_AUTO_RACE && SiegeAutoRace.isAutoSiege(fortress.getLocationId())) {
				siegeStartHour.add(Calendar.HOUR, 1);
			}
			if (currentHourPlus1.getTimeInMillis() == siegeStartHour.getTimeInMillis() || siegeCalendar.getTimeInMillis() > currentHourPlus1.getTimeInMillis()) {
				fortress.setNextState(1);
			}
			else
				fortress.setNextState(0);
		}
	}

	/**
	 * TODO: WTF is it?
	 * 
	 * @return seconds before hour end
	 */
	public int getSecondsBeforeHourEnd() {
		Calendar c = Calendar.getInstance();
		int minutesAsSeconds = c.get(Calendar.MINUTE) * 60;
		int seconds = c.get(Calendar.SECOND);
		return 3600 - (minutesAsSeconds + seconds);
	}

	/**
	 * TODO: Check if it's valid
	 * <p/>
	 * If siege duration is endless - will return -1
	 * 
	 * @param siegeLocationId
	 *          Scheduled siege end time
	 * @return remaining seconds in current hour
	 */
	public int getRemainingSiegeTimeInSeconds(int siegeLocationId) {

		Siege<?> siege = getSiege(siegeLocationId);
		if (siege == null || siege.isFinished()) {
			return 0;
		}

		if (!siege.isStarted()) {
			return siege.getSiegeLocation().getSiegeDuration();
		}

		// endless siege
		if (siege.getSiegeLocation().getSiegeDuration() == -1) {
			return -1;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, siege.getSiegeLocation().getSiegeDuration());

		int result = (int) ((calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000);
		return result > 0 ? result : 0;
	}

	public Siege<?> getSiege(SiegeLocation loc) {
		return activeSieges.get(loc.getLocationId());
	}

	public Siege<?> getSiege(Integer siegeLocationId) {
		return activeSieges.get(siegeLocationId);
	}

	public boolean isSiegeInProgress(int fortressId) {
		return activeSieges.containsKey(fortressId);
	}

	public Map<Integer, OutpostLocation> getOutposts() {
		return outposts;
	}

	public OutpostLocation getOutpost(int id) {
		return outposts.get(id);
	}

	public Map<Integer, SourceLocation> getSources() {
		return sources;
	}

	public SourceLocation getSource(int id) {
		return sources.get(id);
	}

	public Map<Integer, FortressLocation> getFortresses() {
		return fortresses;
	}

	public FortressLocation getFortress(int fortressId) {
		return fortresses.get(fortressId);
	}

	public Map<Integer, ArtifactLocation> getArtifacts() {
		return artifacts;
	}

	public ArtifactLocation getArtifact(int id) {
		return getArtifacts().get(id);
	}

	public Map<Integer, ArtifactLocation> getStandaloneArtifacts() {
		return Maps.filterValues(artifacts, new Predicate<ArtifactLocation>() {

			@Override
			public boolean apply(@Nullable ArtifactLocation input) {
				return input != null && input.isStandAlone();
			}

		});
	}

	public Map<Integer, ArtifactLocation> getFortressArtifacts() {
		return Maps.filterValues(artifacts, new Predicate<ArtifactLocation>() {

			@Override
			public boolean apply(@Nullable ArtifactLocation input) {
				return input != null && input.getOwningFortress() != null;
			}

		});
	}

	public Map<Integer, SiegeLocation> getSiegeLocations() {
		return locations;
	}

	public SiegeLocation getSiegeLocation(int locationId) {
		return locations.get(locationId);
	}

	public Map<Integer, SiegeLocation> getSiegeLocations(int worldId) {
		Map<Integer, SiegeLocation> mapLocations = new FastMap<Integer, SiegeLocation>();
		for (SiegeLocation location : getSiegeLocations().values()) {
			if (location.getWorldId() == worldId)
				mapLocations.put(location.getLocationId(), location);
		}
		return mapLocations;
	}

	protected Siege<?> newSiege(int siegeLocationId) {
		if (fortresses.containsKey(siegeLocationId))
			return new FortressSiege(fortresses.get(siegeLocationId));
		if (sources.containsKey(siegeLocationId))
			return new SourceSiege(sources.get(siegeLocationId));
		if (outposts.containsKey(siegeLocationId))
			return new OutpostSiege(outposts.get(siegeLocationId));
		if (artifacts.containsKey(siegeLocationId)) {
			return new ArtifactSiege(artifacts.get(siegeLocationId));
		}
		throw new SiegeException("Unknown siege handler for siege location: " + siegeLocationId);
	}

	public void cleanLegionId(int legionId) {
		for (SiegeLocation loc : this.getSiegeLocations().values()) {
			if (loc.getLegionId() == legionId) {
				loc.setLegionId(0);
				break;
			}
		}
	}

	public void updateOutpostStatusByFortress(FortressLocation fortress) {
		for (OutpostLocation outpost : getOutposts().values()) {
			if (outpost.getFortressDependency().contains(fortress.getLocationId())) {
				SiegeRace fortressRace = fortress.getRace();
				for (Integer fortressId : outpost.getFortressDependency()) {
					SiegeRace sr = getFortresses().get(fortressId).getRace();
					if (fortressRace != sr) {
						fortressRace = SiegeRace.BALAUR;
						break;
					}

				}

				boolean isSpawned = outpost.isSilentraAllowed();
				SiegeRace newOwnerRace;
				if (SiegeRace.BALAUR == fortressRace) {
					newOwnerRace = SiegeRace.BALAUR;
				}
				else {
					newOwnerRace = fortressRace == SiegeRace.ELYOS ? SiegeRace.ASMODIANS : SiegeRace.ELYOS;
				}

				if (outpost.getRace() != newOwnerRace) {
					stopSiege(outpost.getLocationId());
					deSpawnNpcs(outpost.getLocationId());

					outpost.setRace(newOwnerRace);
					DAOManager.getDAO(SiegeDAO.class).updateSiegeLocation(outpost);
					broadcastStatusAndUpdate(outpost, isSpawned);

					if (SiegeRace.BALAUR != outpost.getRace())
						if (outpost.isSiegeAllowed())
							startSiege(outpost.getLocationId());
						else
							spawnNpcs(outpost.getLocationId(), outpost.getRace(), SiegeModType.PEACE);
				}
			}
		}
	}

	public void updateTiamarantaRiftsStatus(boolean isPreparation, boolean isSync) {
		int sourceState = 0;
		int aSources = 0;
		int eSources = 0;

		if (isPreparation) {
			broadcastStatusAndUpdate(aSources, eSources, isPreparation, isSync);
		}
		else {
			for (SourceLocation source : getSources().values()) {
				sourceState += (source.isVulnerable() ? 0 : 1);
				if (source.getRace().equals(SiegeRace.ASMODIANS))
					aSources++;
				else if (source.getRace().equals(SiegeRace.ELYOS)) {
					eSources++;
				}
			}

			if (sourceState == 4)
				broadcastStatusAndUpdate(aSources, eSources, isPreparation, isSync);
		}
	}

	private void spawnTiamarantaPortels(boolean cl, boolean cr, boolean tl, boolean tr) {
		if (cl) {
			SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(600030000, 701286, 1524.45F, 1250.425F, 247.048F, (byte) 60);
			template.setStaticId(1594);
			tiamarantaPortals.put(701286, SpawnEngine.spawnObject(template, 1));
		}
		if (cr) {
			SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(600030000, 701287, 1526.465F, 1784.999F, 250.436F, (byte) 60);
			template.setStaticId(2282);
			tiamarantaPortals.put(701287, SpawnEngine.spawnObject(template, 1));
		}
		if (tl) {
			SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(600030000, 701288, 116.665F, 1543.754F, 295.99701F, (byte) 0);
			template.setStaticId(681);
			tiamarantaPortals.put(701288, SpawnEngine.spawnObject(template, 1));
		}
		if (tr) {
			SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(600030000, 701289, 117.26F, 1929.155F, 295.69101F, (byte) 0);
			template.setStaticId(680);
			tiamarantaPortals.put(701289, SpawnEngine.spawnObject(template, 1));
		}
	}

	private void deSpawnTiamarantaPortals() {
		for (VisibleObject portal : tiamarantaPortals.values())
			portal.getController().onDelete();
		this.cl = (this.cr = this.tl = this.tr = false);

		for (VisibleObject boss : tiamarantaEyeBoss.values()) {
			boss.getController().onDelete();
		}
		tiamarantaPortals.clear();
		tiamarantaEyeBoss.clear();
	}

	public void spawnNpcs(int siegeLocationId, SiegeRace race, SiegeModType type) {
		List<SpawnGroup2> siegeSpawns = DataManager.SPAWNS_DATA2.getSiegeSpawnsByLocId(siegeLocationId);
		for (SpawnGroup2 group : siegeSpawns)
			for (SpawnTemplate template : group.getSpawnTemplates()) {
				SiegeSpawnTemplate siegetemplate = (SiegeSpawnTemplate) template;
				if (siegetemplate.getSiegeRace().equals(race) && siegetemplate.getSiegeModType().equals(type))
					SpawnEngine.spawnObject(siegetemplate, 1);
			}
	}

	public void deSpawnNpcs(int siegeLocationId) {
		Collection<SiegeNpc> siegeNpcs = World.getInstance().getLocalSiegeNpcs(siegeLocationId);
		for (SiegeNpc npc : siegeNpcs)
			npc.getController().onDelete();
	}

	public boolean isSiegeNpcInActiveSiege(Npc npc) {
		if ((npc instanceof SiegeNpc)) {
			FortressLocation fort = getFortress(((SiegeNpc) npc).getSiegeId());
			if (fort != null) {
				if (fort.isVulnerable())
					return true;
				if (fort.getNextState() == 1)
					return npc.getSpawn().getRespawnTime() >= getSecondsBeforeHourEnd();
			}
		}
		return false;
	}

	public void broadcastUpdate() {
		broadcast(new SM_SIEGE_LOCATION_INFO(), null);
	}

	public void broadcastUpdate(SiegeLocation loc) {
		Influence.getInstance().recalculateInfluence();
		broadcast(new SM_SIEGE_LOCATION_INFO(loc), new SM_INFLUENCE_RATIO());
	}

	public void broadcast(final AionServerPacket pkt1, final AionServerPacket pkt2) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (pkt1 != null)
					PacketSendUtility.sendPacket(player, pkt1);
				if (pkt2 != null)
					PacketSendUtility.sendPacket(player, pkt2);
			}
		});
	}

	public void broadcastUpdate(SiegeLocation loc, DescriptionId nameId) {
		SM_SIEGE_LOCATION_INFO pkt = new SM_SIEGE_LOCATION_INFO(loc);
		SM_SYSTEM_MESSAGE info = loc.getLegionId() == 0 ? new SM_SYSTEM_MESSAGE(1301039, loc.getRace().getDescriptionId(), nameId) : new SM_SYSTEM_MESSAGE(1301038, LegionService.getInstance()
			.getLegion(loc.getLegionId()).getLegionName(), nameId);

		broadcast(pkt, info, loc.getRace());
	}

	private void broadcast(final AionServerPacket pkt, final AionServerPacket info, final SiegeRace race) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (player.getRace().getRaceId() == race.getRaceId())
					PacketSendUtility.sendPacket(player, info);
				PacketSendUtility.sendPacket(player, pkt);
			}
		});
	}

	public void broadcastStatusAndUpdate(OutpostLocation outpost, boolean oldSilentraState) {
		SM_SYSTEM_MESSAGE info = null;
		if (oldSilentraState != outpost.isSilentraAllowed()) {
			if (outpost.isSilentraAllowed()) {
				info = outpost.getLocationId() == 2111 ? SM_SYSTEM_MESSAGE.STR_FIELDABYSS_LIGHTUNDERPASS_SPAWN : SM_SYSTEM_MESSAGE.STR_FIELDABYSS_DARKUNDERPASS_SPAWN;
			}
			else {
				info = outpost.getLocationId() == 2111 ? SM_SYSTEM_MESSAGE.STR_FIELDABYSS_LIGHTUNDERPASS_DESPAWN : SM_SYSTEM_MESSAGE.STR_FIELDABYSS_DARKUNDERPASS_DESPAWN;
			}
		}

		broadcast(new SM_RIFT_ANNOUNCE(getOutpost(3111).isSilentraAllowed(), getOutpost(2111).isSilentraAllowed()), info);
	}

	public void broadcastStatusAndUpdate(int aSources, int eSources, boolean isPreparation, boolean isSync) {
		deSpawnTiamarantaPortals();
		cl = eSources > 1;
		cr = aSources > 1;

		if (isSync) {
			spawnTiamarantaPortels(cl, cr, this.tl = cl, this.tr = cr);
		}
		else if ((!isPreparation) && ((cl) || (cr))) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if ((!tl) || (!tr)) {
						spawnTiamarantaPortels(false, false, tl = cl, tr = cr);
						broadcast(new SM_RIFT_ANNOUNCE(cl, cr, tl, tr), null);
					}
				}
			}, 5400000);

			spawnTiamarantaPortels(cl, cr, false, false);
		}

		broadcast(new SM_RIFT_ANNOUNCE(cl, cr, tl, tr), null);
	}

	private void broadcast(final SM_RIFT_ANNOUNCE rift, final SM_SYSTEM_MESSAGE info) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, rift);
				if (info != null && player.getWorldType().equals(WorldType.BALAUREA))
					PacketSendUtility.sendPacket(player, info);
			}
		});
	}

	public void validateLoginZone(Player player) {
		BindPointPosition bind = player.getBindPoint();
		int mapId;
		float x;
		float y;
		float z;
		byte h;
		if (bind != null) {
			mapId = bind.getMapId();
			x = bind.getX();
			y = bind.getY();
			z = bind.getZ();
			h = bind.getHeading();
		}
		else {
			PlayerInitialData.LocationData start = DataManager.PLAYER_INITIAL_DATA.getSpawnLocation(player.getRace());

			mapId = start.getMapId();
			x = start.getX();
			y = start.getY();
			z = start.getZ();
			h = start.getHeading();
		}

		if (player.getWorldId() == 600040000) {
			if (player.getRace() == Race.ELYOS ? !cl : !cr && !getSource(4011).isPreparations())
				World.getInstance().setPosition(player, mapId, x, y, z, h);
			return;
		}

		for (FortressLocation fortress : getFortresses().values()) {
			if (fortress.isInActiveSiegeZone(player) && fortress.isEnemy(player)) {
				World.getInstance().setPosition(player, mapId, x, y, z, h);
				return;
			}
		}
		for (SourceLocation source : getSources().values())
			if (source.isInActiveSiegeZone(player)) {
				WorldPosition pos = source.getEntryPosition();
				World.getInstance().setPosition(player, pos.getMapId(), pos.getX(), pos.getY(), pos.getZ(), pos.getHeading());
				return;
			}
	}
	
	public void alertVulnerable(Player p) {
        for (SiegeLocation loc : locations.values()) {
		final DescriptionId fortressNameId = new DescriptionId(loc.getTemplate().getNameId());
            if (loc.getType() == SiegeType.FORTRESS && loc.isVulnerable()) {
                PacketSendUtility.sendPacket(p, new SM_SYSTEM_MESSAGE(1301040, fortressNameId));
            }
        }
    }

	public void onPlayerLogin(Player player) {
		if (SiegeConfig.SIEGE_ENABLED) {
		    alertVulnerable(player);
			PacketSendUtility.sendPacket(player, new SM_INFLUENCE_RATIO());
			PacketSendUtility.sendPacket(player, new SM_SIEGE_LOCATION_INFO());
			PacketSendUtility.sendPacket(player, new SM_RIFT_ANNOUNCE(getOutpost(3111).isSilentraAllowed(), getOutpost(2111).isSilentraAllowed()));

			PacketSendUtility.sendPacket(player, new SM_RIFT_ANNOUNCE(cl, cr, tl, tr));
		}
	}

	public void onEnterSiegeWorld(Player player) {
		FastMap<Integer, SiegeLocation> worldLocations = new FastMap<Integer, SiegeLocation>();
		FastMap<Integer, ArtifactLocation> worldArtifacts = new FastMap<Integer, ArtifactLocation>();

		for (SiegeLocation location : getSiegeLocations().values()) {
			if (location.getWorldId() == player.getWorldId())
				worldLocations.put(location.getLocationId(), location);
		}
		for (ArtifactLocation artifact : getArtifacts().values()) {
			if (artifact.getWorldId() == player.getWorldId())
				worldArtifacts.put(artifact.getLocationId(), artifact);
		}
		PacketSendUtility.sendPacket(player, new SM_SHIELD_EFFECT(worldLocations.values()));
		PacketSendUtility.sendPacket(player, new SM_ABYSS_ARTIFACT_INFO3(worldArtifacts.values()));
		PacketSendUtility.sendPacket(player, new SM_RIFT_ANNOUNCE(player.getRace()));
	}

	public int getFortressId(int locId) {
		switch (locId) {
			case 49:
			case 61:
				return 1011;
			case 36:
			case 54:
				return 1131;
			case 37:
			case 55:
				return 1132;
			case 39:
			case 56:
				return 1141;
			case 44:
			case 62:
				return 1211;
			case 45:
			case 57:
			case 72:
			case 75:
				return 1221;
			case 46:
			case 58:
			case 73:
			case 76:
				return 1231;
			case 47:
			case 59:
			case 74:
			case 77:
				return 1241;
			case 48:
			case 60:
				return 1251;
			case 90:
				return 2011;
			case 91:
				return 2021;
			case 93:
				return 3011;
			case 94:
				return 3021;
			/* 4.0 fortress
			case XX:
				return 5011;
			case XX:
				return 6011;
			case XX:
				return 6021; */
		}
		return 0;
	}
}
