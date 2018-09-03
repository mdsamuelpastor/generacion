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
package com.aionemu.gameserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.focus.battleground.BattleGroundManager;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.network.NioServer;
import com.aionemu.commons.network.ServerCfg;
import com.aionemu.commons.services.CronService;
import com.aionemu.commons.utils.AEInfos;
import com.aionemu.gameserver.ai2.AI2Engine;
import com.aionemu.gameserver.cache.HTMLCache;
import com.aionemu.gameserver.configs.Config;
import com.aionemu.gameserver.configs.main.AIConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.DredgionConfig;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.configs.main.TelnetConfig;
import com.aionemu.gameserver.configs.main.ThreadConfig;
import com.aionemu.gameserver.configs.main.WeddingsConfig;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.eventEngine.EventEngine;
import com.aionemu.gameserver.instance.InstanceEngine;
import com.aionemu.gameserver.model.GameEngine;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.house.MaintenanceTask;
import com.aionemu.gameserver.model.siege.Influence;
import com.aionemu.gameserver.network.BannedMacManager;
import com.aionemu.gameserver.network.aion.GameConnectionFactoryImpl;
import com.aionemu.gameserver.network.chatserver.ChatServer;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.services.AdminService;
import com.aionemu.gameserver.services.AnnouncementService;
import com.aionemu.gameserver.services.BrokerService;
import com.aionemu.gameserver.services.CuringZoneService;
import com.aionemu.gameserver.services.DatabaseCleaningService;
import com.aionemu.gameserver.services.DebugService;
import com.aionemu.gameserver.services.EventService;
import com.aionemu.gameserver.services.ExchangeService;
import com.aionemu.gameserver.services.FlyRingService;
import com.aionemu.gameserver.services.GameTimeService;
import com.aionemu.gameserver.services.HousingBidService;
import com.aionemu.gameserver.services.InvasionRiftService;
import com.aionemu.gameserver.services.LegionHistoryCleaningService;
import com.aionemu.gameserver.services.LimitedItemTradeService;
import com.aionemu.gameserver.services.MoltenusSpawnService;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.services.PeriodicSaveService;
import com.aionemu.gameserver.services.PetitionService;
import com.aionemu.gameserver.services.RoadService;
import com.aionemu.gameserver.services.ShieldService;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.VillageService;
import com.aionemu.gameserver.services.WeatherService;
import com.aionemu.gameserver.services.WeddingService;
import com.aionemu.gameserver.services.CustomBosses.BossSpawnService;
import com.aionemu.gameserver.services.abyss.AbyssRankCleaningService;
import com.aionemu.gameserver.services.abyss.AbyssRankUpdateService;
import com.aionemu.gameserver.services.craft.AdvCraftSpawn;
import com.aionemu.gameserver.services.crazy_daeva.CrazyDaevaService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.global.FFAService;
import com.aionemu.gameserver.services.global.PvPGrind;
import com.aionemu.gameserver.services.instance.DredgionService2;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.player.PlayerEventService;
import com.aionemu.gameserver.services.player.PlayerLimitService;
import com.aionemu.gameserver.services.reward.OnlineBonus;
import com.aionemu.gameserver.services.reward.RewardService;
import com.aionemu.gameserver.services.transfers.PlayerTransferService;
import com.aionemu.gameserver.spawnengine.DayTimeSpawnEngine;
import com.aionemu.gameserver.spawnengine.DimensionalVortex;
import com.aionemu.gameserver.spawnengine.InstanceRiftSpawnManager;
import com.aionemu.gameserver.spawnengine.LegendaryRaidSpawn;
import com.aionemu.gameserver.spawnengine.RandomSpawnManager;
import com.aionemu.gameserver.spawnengine.RiftSpawnManager;
import com.aionemu.gameserver.spawnengine.SeasonTimeSpawnEngine;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.taskmanager.TaskManagerFromDB;
import com.aionemu.gameserver.taskmanager.tasks.PacketBroadcaster;
import com.aionemu.gameserver.telnet.TelnetServer;
import com.aionemu.gameserver.utils.AEVersions;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.ThreadUncaughtExceptionHandler;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.ChatProcessor;
import com.aionemu.gameserver.utils.cron.ThreadPoolManagerRunnableRunner;
import com.aionemu.gameserver.utils.gametime.DateTimeUtil;
import com.aionemu.gameserver.utils.gametime.GameTimeManager;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.utils.javaagent.JavaAgentUtils;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.geo.GeoService;
import com.aionemu.gameserver.world.zone.ZoneService;

/**
 * <tt>GameServer </tt> is the main class of the application and represents the whole game server.<br>
 * This class is also an entry point with main() method.
 *
 * @author -Nemesiss-
 * @author SoulKeeper
 * @author cura
 * @author Maestros
 */
public class GameServer {

	private static final Logger log = LoggerFactory.getLogger(GameServer.class);

	//TODO remove all this shit
	private static int ELYOS_COUNT = 0;
	private static int ASMOS_COUNT = 0;
	private static double ELYOS_RATIO = 0.0;
	private static double ASMOS_RATIO = 0.0;
	private static final ReentrantLock lock = new ReentrantLock();

	private static Set<StartupHook> startUpHooks = new HashSet<StartupHook>();

	private static void initalizeLoggger() {
		new File("./log/backup/").mkdirs();
		File[] files = new File("log").listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".log");
			}
		});

		if (files != null && files.length > 0) {
			byte[] buf = new byte[1024];
			try {
				String outFilename = "./log/backup/" + new SimpleDateFormat("yyyy-MM-dd HHmmss").format(new Date()) + ".zip";
				ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
				out.setMethod(ZipOutputStream.DEFLATED);
				out.setLevel(Deflater.BEST_COMPRESSION);

				for (File logFile : files) {
					FileInputStream in = new FileInputStream(logFile);
					out.putNextEntry(new ZipEntry(logFile.getName()));
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					out.closeEntry();
					in.close();
					logFile.delete();
				}
				out.close();
			}
			catch (IOException e) {
			}
		}
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(lc);
			lc.reset();
			configurator.doConfigure("config/slf4j-logback.xml");
		}
		catch (JoranException je) {
			throw new RuntimeException("Failed to configure loggers, shutting down...", je);
		}
	}

	/**
	 * Launching method for GameServer
	 *
	 * @param args arguments, not used
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();

		final GameEngine[] parallelEngines = { QuestEngine.getInstance(), InstanceEngine.getInstance(), EventEngine.getInstance(), AI2Engine.getInstance(), ChatProcessor.getInstance() };

		final CountDownLatch progressLatch = new CountDownLatch(parallelEngines.length);
		initalizeLoggger();
		initUtilityServicesAndConfig();
		DataManager.getInstance();
		Util.printSection("IDFactory");
		IDFactory.getInstance();

		Util.printSection("Zone");
		ZoneService.getInstance().load(null);
		Util.printSection("World");
		World.getInstance();

		Util.printSection("Drops");
		DropRegistrationService.getInstance();

		GameServer gs = new GameServer();
		// Set all players is offline
		DAOManager.getDAO(PlayerDAO.class).setPlayersOffline(false);

		Util.printSection("Cleaning");
		DatabaseCleaningService.getInstance();
		AbyssRankCleaningService.getInstance();
		LegionHistoryCleaningService.getInstance();

		BannedMacManager.getInstance();

		Util.printSection("Geodata");
		GeoService.getInstance().initializeGeo();

		System.gc();
		for (int i = 0; i < parallelEngines.length; i++) {
			final int index = i;
			ThreadPoolManager.getInstance().execute(new Runnable() {

				public void run() {
					parallelEngines[index].load(progressLatch);
				}
			});
		}
		try {
			progressLatch.await();
		}
		catch (InterruptedException e1) {
		}

		// This is loading only siege location data
		// No Siege schedule or spawns
		Util.printSection("Siege Location Data");
		SiegeService.getInstance().initSiegeLocations();
		VillageService.getInstance().initVillageLocations();

		Util.printSection("Spawns");
		SpawnEngine.spawnAll();
		RiftSpawnManager.spawnAll();
		InstanceRiftSpawnManager.spawnAll();
		DayTimeSpawnEngine.spawnAll();
		SeasonTimeSpawnEngine.spawnAll();
		RandomSpawnManager.getInstance().scheduleUpdate();
		InvasionRiftService.getInstance();

		Util.printSection("Limits");
		LimitedItemTradeService.getInstance().start();
		if (CustomConfig.LIMITS_ENABLED)
			PlayerLimitService.getInstance().scheduleUpdate();

		GameTimeManager.startClock();

		Util.printSection("Siege Schedule initialization");
		SiegeService.getInstance().initSieges();

		Util.printSection("TaskManagers");
		PacketBroadcaster.getInstance();

		GameTimeService.getInstance();
		AnnouncementService.getInstance();
		DebugService.getInstance();
		WeatherService.getInstance();
		BrokerService.getInstance();
		BossSpawnService.getInstance();
		Influence.getInstance();
		ExchangeService.getInstance();
		PeriodicSaveService.getInstance();
		PetitionService.getInstance();

		if (AIConfig.SHOUTS_ENABLE)
			NpcShoutsService.getInstance();
		InstanceService.load();

		FlyRingService.getInstance();
		LanguageHandler.getInstance();
		CuringZoneService.getInstance();
		RoadService.getInstance();
		HTMLCache.getInstance();
		AbyssRankUpdateService.getInstance().scheduleUpdate();
		MoltenusSpawnService.getInstance().scheduleUpdate();
		DimensionalVortex.getInstance().scheduleUpdate();

		if (EventsConfig.LEGENDARY_RAID_EVENT_ENABLE) {
			LegendaryRaidSpawn.getInstance().scheduleUpdateAsmodians();
			LegendaryRaidSpawn.getInstance().scheduleUpdateElyos();
		}

		if (EventsConfig.ENABLE_CRAZY) {
			Util.printSection("CRAZY DAEVA [EVENT]");
			CrazyDaevaService.getInstance().startTimer();
		}
		PvPGrind.getInstance().scheduleUpdate();
		AdvCraftSpawn.getInstance().scheduleUpdate();
		TaskManagerFromDB.getInstance();
		if (SiegeConfig.SIEGE_SHIELD_ENABLED)
			ShieldService.getInstance();
		if (DredgionConfig.DREDGION2_ENABLE) {
			Util.printSection("Dredgion");
			DredgionService2.getInstance().start();
		}
		if (CustomConfig.ENABLE_REWARD_SERVICE)
			RewardService.getInstance();
		if (EventsConfig.EVENT_ENABLED)
			PlayerEventService.getInstance();
		if (EventsConfig.ENABLE_EVENT_SERVICE)
			EventService.getInstance().start();
		if (WeddingsConfig.WEDDINGS_ENABLE)
			WeddingService.getInstance();

		AdminService.getInstance();
		PlayerTransferService.getInstance();
		MaintenanceTask.getInstance();
		HousingBidService.getInstance();

		if (MembershipConfig.ONLINE_BONUS_ENABLE) {
			OnlineBonus.getInstance();
		}

		Util.printSection("Telnet-server");
		Telnet();

		Util.printSection("System");
		AEVersions.printFullVersionInfo();
		System.gc();
		AEInfos.printAllInfos();

		Util.printSection("GameServerLog");
		log.info("NextGen Game Server started in " + (System.currentTimeMillis() - start) / 1000 + " seconds.");
		log.info("==================================================");
		log.info("==================>NextGenCore<===================");
		log.info("Thanks to all who helped this project!");
		log.info("Core Version: 3.9");
		log.info("Copyright 2013");
		log.info("==================================================");

		gs.startServers();

		Runtime.getRuntime().addShutdownHook(ShutdownHook.getInstance());

		if (GSConfig.ENABLE_RATIO_LIMITATION) {
			addStartupHook(new StartupHook() {

				@Override
				public void onStartup() {
					lock.lock();
					try {
						ASMOS_COUNT = DAOManager.getDAO(PlayerDAO.class).getCharacterCountForRace(Race.ASMODIANS);
						ELYOS_COUNT = DAOManager.getDAO(PlayerDAO.class).getCharacterCountForRace(Race.ELYOS);
						computeRatios();
					}
					catch (Exception e) {
					}
					finally {
						lock.unlock();
					}
					displayRatios(false);
				}
			});
		}

		onStartup();

		if (CustomConfig.BATTLEGROUNDS_ENABLED) {
			BattleGroundManager.initialize();
		}

		if (CustomConfig.FFA_ENABLED) {
			FFAService.getInstance().announceTask(30);
		}
	}

	/**
	 * Starts servers for connection with aion client and login\chat server.
	 */
	private void startServers() {
		Util.printSection("Starting Network");
		NioServer nioServer = new NioServer(NetworkConfig.NIO_READ_WRITE_THREADS, new ServerCfg(NetworkConfig.GAME_BIND_ADDRESS, NetworkConfig.GAME_PORT, "Game Connections",
				new GameConnectionFactoryImpl()));
		if (TelnetConfig.TELNET_IS_ENABLE)
		{
			log.info("Telnet-server is listening on port " + TelnetConfig.TELNET_PORT);
		}

		LoginServer ls = LoginServer.getInstance();
		ChatServer cs = ChatServer.getInstance();

		ls.setNioServer(nioServer);
		cs.setNioServer(nioServer);

		// Nio must go first
		nioServer.connect();
		ls.connect();

		if (GSConfig.ENABLE_CHAT_SERVER)
			cs.connect();

		HousingBidService.loadBidData();
	}

	/**
	 * Initialize all helper services, that are not directly related to aion gs, which includes:
	 * <ul>
	 * <li>Logging</li>
	 * <li>Database factory</li>
	 * <li>Thread pool</li>
	 * </ul>
	 * This method also initializes {@link Config}
	 */
	private static void initUtilityServicesAndConfig() {
		// Set default uncaught exception handler
		Thread.setDefaultUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());

		// make sure that callback code was initialized
		if (JavaAgentUtils.isConfigured())
			log.info("JavaAgent [Callback Support] is configured.");

		// Initialize cron service
		CronService.initSingleton(ThreadPoolManagerRunnableRunner.class);

		// init config
		Config.load();
		// DateTime zone override from configs
		DateTimeUtil.init();
		// Second should be database factory
		Util.printSection("DataBase");
		DatabaseFactory.init();
		// Initialize DAOs
		DAOManager.init();
		// Initialize thread pools
		Util.printSection("Threads");
		ThreadConfig.load();
		ThreadPoolManager.getInstance();
	}

	public static synchronized void addStartupHook(StartupHook hook) {
		if (startUpHooks != null)
			startUpHooks.add(hook);
		else
			hook.onStartup();
	}

	private static void Telnet() {
		if (TelnetConfig.TELNET_IS_ENABLE)
			new TelnetServer().start();
		else
			log.info("Telnet-server disabled.");
	}

	private synchronized static void onStartup() {
		final Set<StartupHook> startupHooks = startUpHooks;

		startUpHooks = null;

		for (StartupHook hook : startupHooks)
			hook.onStartup();
	}

	/**
	 * @param race
	 * @param i
	 */
	public static void updateRatio(Race race, int i) {
		lock.lock();
		try {
			switch (race) {
			case ASMODIANS:
				ASMOS_COUNT += i;
				break;
			case ELYOS:
				ELYOS_COUNT += i;
				break;
			default:
				break;
			}

			computeRatios();
		}
		catch (Exception e) {
		}
		finally {
			lock.unlock();
		}

		displayRatios(true);
	}

	private static void computeRatios() {
		if (ASMOS_COUNT <= GSConfig.RATIO_MIN_CHARACTERS_COUNT && ELYOS_COUNT <= GSConfig.RATIO_MIN_CHARACTERS_COUNT) {
			ASMOS_RATIO = GameServer.ELYOS_RATIO = 50.0;
		}
		else {
			ASMOS_RATIO = ASMOS_COUNT * 100.0 / (ASMOS_COUNT + ELYOS_COUNT);
			ELYOS_RATIO = ELYOS_COUNT * 100.0 / (ASMOS_COUNT + ELYOS_COUNT);
		}
	}

	private static void displayRatios(boolean updated) {
		log.info("FACTIONS RATIO " + (updated ? "UPDATED " : "") + ": E " + String.format("%.1f", GameServer.ELYOS_RATIO) + " % / A " + String.format("%.1f", GameServer.ASMOS_RATIO)
				+ " %");
	}

	public static double getRatiosFor(Race race) {
		switch (race) {
		case ASMODIANS:
			return ASMOS_RATIO;
		case ELYOS:
			return ELYOS_RATIO;
		default:
			return 0.0;
		}
	}

	public static int getCountFor(Race race) {
		switch (race) {
		case ASMODIANS:
			return ASMOS_COUNT;
		case ELYOS:
			return ELYOS_COUNT;
		default:
			return 0;
		}
	}

	public static abstract interface StartupHook {

		public abstract void onStartup();
	}
}
