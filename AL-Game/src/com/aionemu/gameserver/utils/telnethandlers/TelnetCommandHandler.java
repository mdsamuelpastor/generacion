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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.aionemu.gameserver.model.TelnetUser;

/**
 * @author Divinity
 */
public class TelnetCommandHandler {

	private static final Logger log = Logger.getLogger(TelnetCommandHandler.class);

	private Map<String, TelnetCommand> commands = new HashMap<String, TelnetCommand>();

	TelnetCommandHandler() {
	}

	void registerTelnetCommand(TelnetCommand command) {
		if (command == null)
			throw new NullPointerException("Telnet commands can not be an empty value.");

		String commandName = command.getCommandName();
		TelnetCommand old = commands.put(commandName, command);

		if (old != null)
			log.warn(command.getClass().getName() + "... " + old.getClass().getName() + "... " + commandName + " ...");
	}

	public boolean handleTelnetMessage(String message, TelnetUser sender) {
		if (!message.startsWith("//"))
			return false;
		else {
			String[] commandAndParams = message.split(" ", 2);
			String command = commandAndParams[0].substring(2);
			TelnetCommand telnetCommand = commands.get(command);

			if (telnetCommand == null)
				return false;

			String[] params = new String[] {};
			if (commandAndParams.length > 1)
				params = commandAndParams[1].split(" ", telnetCommand.getSplitSize());

			telnetCommand.executeCommand(sender, params);
			return true;
		}
	}

	/**
	 * Clear all registered handlers (before reload).
	 */
	void clearHandlers() {
		this.commands.clear();
	}

	/**
	 * Returns count of available telnet command handlers.
	 * 
	 * @return count of available telnet command handlers.
	 */
	public int getSize() {
		return this.commands.size();
	}

	public ArrayList<String> getCommandsSyntax() {
		ArrayList<String> syntaxCommands = new ArrayList<String>();

		for (Entry<String, TelnetCommand> entry : commands.entrySet())
			syntaxCommands.add(entry.getValue().getCommandSyntax());

		return syntaxCommands;
	}
}
