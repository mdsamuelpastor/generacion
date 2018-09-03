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
package com.aionemu.gameserver.utils.telnethandlers;

import java.io.File;

import com.aionemu.commons.scripting.scriptmanager.ScriptManager;
import com.aionemu.gameserver.GameServerError;

/**
 * @author Divinity
 */
public class TelnetHandler {

	public static final File CHAT_DESCRIPTOR_FILE = new File("./data/scripts/system/handlers_telnet.xml");

	private ScriptManager sm;
	private TelnetCommandHandler telnetCCH;

	public static final TelnetHandler getInstance() {
		return SingletonHolder.instance;
	}

	private TelnetHandler() {
		telnetCCH = new TelnetCommandHandler();
		sm = new ScriptManager();

		createChatHandlers();
	}

	public TelnetCommandHandler getHandler() {
		return telnetCCH;
	}

	/**
	 * Creates and return object of {@link TelnetHandler} class
	 * 
	 * @return ChatHandlers
	 */
	private void createChatHandlers() {
		sm.setGlobalClassListener(new TelnetCommandLoader(telnetCCH));

		try {
			sm.load(CHAT_DESCRIPTOR_FILE);
		}
		catch (Exception e) {
			throw new GameServerError("Unable to initialize telnet chat handler.", e);
		}
	}

	/**
	 * Allow to reload the chat Handler that holds admin commands
	 * 
	 * @author Caz
	 */
	public void reloadChatHandlers() {
		try {
			sm.reload();
		}
		catch (Exception e) {
			throw new GameServerError("Telnet chat handler can not be restarted.", e);
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final TelnetHandler instance = new TelnetHandler();
	}
}
