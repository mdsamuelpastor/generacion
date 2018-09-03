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
package com.aionemu.gameserver.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import com.aionemu.gameserver.telnet.TelnetExecutor;

/**
 * @author Divinity
 */
public class TelnetUser {

	private String name;
	private String ip;
	private int accesslevel;
	@SuppressWarnings("unused")
	private Socket connection;
	private PrintStream out;
	private BufferedReader in;

	/**
	 * Constructor
	 * 
	 * @param connection
	 *          : Socket - the user's connection
	 * @param out
	 *          : PrintStream - the current user's printer
	 * @param in
	 *          : BufferedReader - the current user's reader
	 * @param name
	 *          : String - the user's name
	 * @param ip
	 *          : String - the user's ip
	 * @param accesslevel
	 *          : int - the user's accesslevel
	 */
	public TelnetUser(Socket connection, PrintStream out, BufferedReader in, String name, String ip, int accesslevel) {
		this.connection = connection;
		this.out = out;
		this.in = in;
		this.name = name;
		this.ip = ip;
		this.accesslevel = accesslevel;
	}

	/**
	 * Write in the telnet's console
	 * 
	 * @param msg
	 *          : String - the message
	 */
	public void say(String msg) {
		out.println(TelnetExecutor.lang("TELNET_SERVER") + " " + msg);
	}

	/**
	 * User input
	 * 
	 * @return String : the input line
	 */
	public String write() {
		String line = null;

		try {
			line = in.readLine();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		if (line == null || line.isEmpty())
			return null;

		// Non alphanum
		line = line.replaceAll("[^a-zA-Z\\uAC00-\\uD7A30-9/ ]", "");

		return line;
	}

	/**
	 * Get the user's name
	 * 
	 * @return String : user's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the user's ip
	 * 
	 * @return : String : user's ip
	 */
	public String getIP() {
		return ip;
	}

	/**
	 * Get the user's accesslevel
	 * 
	 * @return int : user's accesslevel
	 */
	public int getAccessLevel() {
		return accesslevel;
	}
}
