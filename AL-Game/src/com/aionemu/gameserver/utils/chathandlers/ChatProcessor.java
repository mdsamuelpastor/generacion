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
package com.aionemu.gameserver.utils.chathandlers;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import javolution.util.FastMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.scripting.classlistener.AggregatedClassListener;
import com.aionemu.commons.scripting.classlistener.OnClassLoadUnloadListener;
import com.aionemu.commons.scripting.classlistener.ScheduledTaskClassListener;
import com.aionemu.commons.scripting.scriptmanager.ScriptManager;
import com.aionemu.commons.utils.PropertiesUtils;
import com.aionemu.gameserver.GameServerError;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.configs.main.WeddingsConfig;
import com.aionemu.gameserver.model.GameEngine;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author KID
 * @Modified Rolandas
 */
public class ChatProcessor implements GameEngine {

	private static final Logger log = LoggerFactory.getLogger("ADMINAUDIT_LOG");
	private static ChatProcessor instance = new ChatProcessor();
	private Map<String, ChatCommand> commands = new FastMap<String, ChatCommand>();
	private Map<String, Byte> accessLevel = new FastMap<String, Byte>();
	private ScriptManager sm = new ScriptManager();
	private Exception loadException = null;

	private static final String[] EMPTY_PARAMS = new String[] {};

	public static ChatProcessor getInstance() {
		return instance;
	}

	@Override
	public void load(CountDownLatch progressLatch) {
		try {
			log.info("Chat processor load started");
			init(sm, this);
		}
		finally {
			if (progressLatch != null)
				progressLatch.countDown();
		}
	}

	@Override
	public void shutdown() {
	}

	private ChatProcessor() {
	}

	private ChatProcessor(ScriptManager scriptManager) {
		init(scriptManager, this);
	}

	private void init(final ScriptManager scriptManager, ChatProcessor processor) {
		loadLevels();

		AggregatedClassListener acl = new AggregatedClassListener();
		acl.addClassListener(new OnClassLoadUnloadListener());
		acl.addClassListener(new ScheduledTaskClassListener());
		acl.addClassListener(new ChatCommandsLoader(processor));
		scriptManager.setGlobalClassListener(acl);

		final File[] files = { new File("./data/scripts/system/adminhandlers.xml"), new File("./data/scripts/system/playerhandlers.xml"), new File("./data/scripts/system/weddinghandlers.xml") };

		final CountDownLatch loadLatch = new CountDownLatch(files.length);

		for (int i = 0; i < files.length; i++) {
			final int index = i;
			ThreadPoolManager.getInstance().execute(new Runnable() {

				@Override
				public void run() {
					try {
						scriptManager.load(files[index]);
					}
					catch (Exception e) {
						loadException = e;
					}
					finally {
						loadLatch.countDown();
					}
				}
			});
		}
		try {
			loadLatch.await();
		}
		catch (InterruptedException e1) {
		}
		if (loadException != null)
			throw new GameServerError("Can't initialize chat handlers.", loadException);
	}

	public void registerCommand(ChatCommand cmd) {
		if (commands.containsKey(cmd.getAlias())) {
			log.warn("Command " + cmd.getAlias() + " is already registered. Fail");
			return;
		}

		if (!accessLevel.containsKey(cmd.getAlias())) {
			log.warn("Command " + cmd.getAlias() + " do not have access level. Fail");
			return;
		}

		cmd.setAccessLevel(accessLevel.get(cmd.getAlias()));
		commands.put(cmd.getAlias(), cmd);
	}

	public void reload() {
		ScriptManager tmpSM;
		final ChatProcessor adminCP;
		Map<String, ChatCommand> backupCommands = new FastMap<String, ChatCommand>(commands);
		commands.clear();
		loadException = null;
		try {
			tmpSM = new ScriptManager();
			adminCP = new ChatProcessor(tmpSM);
		}
		catch (Throwable e) {
			commands = backupCommands;
			throw new GameServerError("Can't reload chat handlers.", e);
		}

		if (tmpSM != null && adminCP != null) {
			backupCommands.clear();
			sm.shutdown();
			sm = null;
			sm = tmpSM;
			instance = adminCP;
		}
	}

	private void loadLevels() {
		accessLevel.clear();
		try {
			Properties props = PropertiesUtils.load("config/administration/commands.properties");

			for (Object key : props.keySet()) {
				String str = (String) key;
				accessLevel.put(str, Byte.valueOf(props.getProperty(str).trim()));
			}
		}
		catch (IOException e) {
			log.error("Can't read commands.properties", e);
		}
	}

	public boolean handleChatCommand(Player player, String text) {
		if (text.startsWith("//") && player.getAccessLevel() > 0) {
			return processCommand(player, text.substring(2), true);
		}
		else if (text.startsWith("..") && (player.havePermission(WeddingsConfig.WEDDINGS_COMMAND_MEMBERSHIP)) && player.isMarried()) {
			return processCommand(player, text.substring(2), false);
		}
		else if (text.startsWith(".")) {
			return processCommand(player, text.substring(1), false);
		}
		else
			return false;
	}

	private boolean processCommand(Player player, String text, boolean admin) {
		if (text.split(" ").length == 0) {
			return true;
		}
		String alias = text.split(" ")[0];

		ChatCommand cmd = this.commands.get(alias);
		if (cmd == null) {
			if (admin && player.getAccessLevel() > 0)
				PacketSendUtility.sendMessage(player, "[WARN] There is no such command as //" + alias);
			return true;
		}

		if (!admin && cmd.getLevel() != 0)
			return false;
		else {
			if (player.getAccessLevel() < cmd.getLevel()) {
				if (LoggingConfig.LOG_GMAUDIT)
					log.info("[ADMIN COMMAND] > [Player: " + player.getName() + "] has tried to use the command //" + alias + " without having the rights");
				if (player.getAccessLevel() > 0)
					PacketSendUtility.sendMessage(player, "[WARN] You need to have access level " + cmd.getLevel() + " or more to use //" + alias);
				return true;
			}
		}

		boolean success = false;
		if (text.length() == alias.length())
			success = cmd.run(player, EMPTY_PARAMS);
		else
			success = cmd.run(player, text.substring(alias.length() + 1).split(" "));

		if (LoggingConfig.LOG_GMAUDIT && admin) {
			if (player.getTarget() != null && player.getTarget() instanceof Creature) {
				Creature target = (Creature) player.getTarget();
				log.info("[ADMIN COMMAND] > [Name: " + player.getName() + "][Target : " + target.getName() + "]: " + text);
			}
			else
				log.info("[ADMIN COMMAND] > [Name: " + player.getName() + "]: " + text);
		}

		if (!success && admin && player.getAccessLevel() > 0) { // An admin fails an Admin command
			PacketSendUtility.sendMessage(player, "<You have failed to execute //" + text + ">");
			return true;
		}
		else
			return success;
	}

	public void onCompileDone() {
		log.info("Loaded " + commands.size() + " commands.");
	}
}
