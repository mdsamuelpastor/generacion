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
package com.aionemu.gameserver.telnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.aionemu.gameserver.configs.main.TelnetConfig;
import com.aionemu.gameserver.model.TelnetUser;

/**
 * Telnet Client
 * 
 * @author Divinity
 */
public class TelnetClient extends Thread {

	/**
	 * Logger for TelnetClient
	 */
	private static final Logger log = Logger.getLogger(TelnetClient.class);

	private PrintStream out;
	private BufferedReader in;
	private Socket client;
	private String password;
	private String line;
	private String gmName;
	private boolean response;
	private int accesslevel;

	/**
	 * Constructor
	 * 
	 * @param client
	 *          : Socket - the client
	 * @param password
	 *          : String - the server password
	 * @throws IOException
	 */
	public TelnetClient(Socket client, String password) throws IOException {
		this.client = client;
		this.password = password;
		this.gmName = "";
		this.out = new PrintStream(client.getOutputStream());
		this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		this.response = false;
	}

	@Override
	public void run() {
		try {
			out.println("###############################################################################");
			out.println(TelnetExecutor.lang("TELNET_HEADER"));
			out.println("###############################################################################\n");

			String password = "";

			// GM Auth is enabled ?
			/**
			 * if (TelnetConfig.TELNET_USE_GM_AUTHENTIFICATION) { out.println(TelnetExecutor.lang("TELNET_PLAYER_NAME"));
			 * out.flush(); gmName = readLine(true); out.println(TelnetExecutor.lang("TELNET_ACCOUNT_PASSWORD")); out.flush();
			 * password = readLine(true); } else { out.println(TelnetExecutor.lang("TELNET_SERVER_PASSWORD")); out.flush();
			 * password = readLine(true); }
			 */

			out.println(TelnetExecutor.lang("TELNET_SERVER_PASSWORD"));
			out.flush();
			password = readLine(true);

			// Don't allow empty password
			while (password == null || password.isEmpty()) {
				out.println(TelnetExecutor.lang("TELNET_PASSWORD_IS_EMPTY",
					(TelnetConfig.TELNET_USE_GM_AUTHENTIFICATION ? TelnetExecutor.lang("TELNET_ACCOUNT_PASSWORD_TINY") : TelnetExecutor.lang("TELNET_SERVER_PASSWORD_TINY"))));
				out.flush();
				password = readLine(true);
			}

			// Banishment system is enabled ?
			if (TelnetConfig.TELNET_BANISHMENT_SYSTEM_IS_ENABLE) {
				// Adding try to the banishment system
				TelnetExecutor.addTry(client.getInetAddress().getHostAddress());
			}

			String clientIP = client.getInetAddress().getHostAddress();

			// GM Auth is ensabled ?
			if (TelnetConfig.TELNET_USE_GM_AUTHENTIFICATION) {
				// Requesting LS
				TelnetExecutor.requestConnexion(gmName.toLowerCase(), password, this);

				// Waiting for the LS response (15 sec max)
				int i = 0;
				while (!response && i < 30) {
					if (i == 0)
						out.print(TelnetExecutor.lang("TELNET_CHECKING_LS"));
					else
						out.print(".");

					try {
						Thread.sleep(500);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
					i++;
				}

				out.println();

				// Response here ?
				if (!response) {
					// No response, bye
					out.println(TelnetExecutor.lang("TELNET_UNABLE_CHECKING_LS"));
					close();
					return;
				}

				// Unknow player
				if (accesslevel == -1) {
					log.info(TelnetExecutor.lang("TELNET_WRONG_LOGIN_PASSWORD", gmName, clientIP));
					out.println(TelnetExecutor.lang("TELNET_LOGIN_PASSWORD_INCORRECT"));

					// Banishment system is enabled ?
					if (TelnetConfig.TELNET_BANISHMENT_SYSTEM_IS_ENABLE) {
						int currentTries = TelnetExecutor.getTry(clientIP);
						log.info(TelnetExecutor.lang("TELNET_CURRENT_TRIES", clientIP, currentTries, TelnetConfig.TELNET_BANISHMENT_SYSTEM_MAX_CONNEXION));

						// The player's ip is banned ?
						if (currentTries >= TelnetConfig.TELNET_BANISHMENT_SYSTEM_MAX_CONNEXION) {
							log.warn(TelnetExecutor.lang("TELNET_MAX_CONNECTION", clientIP, TelnetConfig.TELNET_BANISHMENT_SYSTEM_TIME_TO_BAN));
							out.println(TelnetExecutor.lang("TELNET_YOUR_MAX_CONNECTION"));
						}
					}

					close();
					return;
				}
				else if (accesslevel > 0) {
					// Good, welcome home
					if (TelnetConfig.TELNET_BANISHMENT_SYSTEM_IS_ENABLE)
						TelnetExecutor.deleteTries(clientIP);
					log.info(TelnetExecutor.lang("TELNET_SUCCESS_GM_AUTH", clientIP, gmName));
					out.println(TelnetExecutor.lang("TELNET_WELCOME", gmName));
				}
				else {
					// Not enough rights to connect to the server, bye
					log.info(TelnetExecutor.lang("TELNET_ENOUGH_RIGHT_GM_AUTH", clientIP, gmName, accesslevel));
					log.info(TelnetExecutor.lang("TELNET_CONNECTION_REJECTED"));
					out.println(TelnetExecutor.lang("TELNET_ENOUGH_RIGHT"));
					close();
					return;
				}
			}
			else {
				// Password is good ?
				if (password.equals(this.password)) {
					// Good
					if (TelnetConfig.TELNET_BANISHMENT_SYSTEM_IS_ENABLE)
						TelnetExecutor.deleteTries(clientIP);

					out.println(TelnetExecutor.lang("TELNET_NICKNAME"));
					out.flush();
					gmName = readLine(true);

					// Don't allow empty nickname
					while (gmName == null || gmName.isEmpty()) {
						out.println(TelnetExecutor.lang("TELNET_EMPTY_NICKNAME"));
						out.flush();
						gmName = readLine(true);
					}

					accesslevel = TelnetConfig.TELNET_ACCESSLEVEL;
					log.info(TelnetExecutor.lang("TELNET_SUCCES_PASSWORD_AUTH", clientIP, gmName));
					out.println(TelnetExecutor.lang("TELNET_WELCOME", gmName));
				}
				else {
					log.info(TelnetExecutor.lang("TELNET_WRONG_PASSWORD", clientIP));
					out.println(TelnetExecutor.lang("TELNET_PASSWORD_INCORRECT"));

					if (TelnetConfig.TELNET_BANISHMENT_SYSTEM_IS_ENABLE) {
						int currentTries = TelnetExecutor.getTry(clientIP);
						log.info(TelnetExecutor.lang("TELNET_CURRENT_TRIES", clientIP, currentTries, TelnetConfig.TELNET_BANISHMENT_SYSTEM_MAX_CONNEXION));

						// The player's ip is banned ?
						if (currentTries >= TelnetConfig.TELNET_BANISHMENT_SYSTEM_MAX_CONNEXION) {
							log.warn(TelnetExecutor.lang("TELNET_CURRENT_TRIES", clientIP, currentTries, TelnetConfig.TELNET_BANISHMENT_SYSTEM_MAX_CONNEXION));
							out.println(TelnetExecutor.lang("TELNET_YOUR_MAX_CONNECTION"));
						}
					}

					close();
					return;
				}
			}

			// Starting telnet commands
			executeQuery();

			// Bye
			close();
			return;
		}
		catch (IOException e) {
			log.error("Error while conecting [" + client.getInetAddress().getHostAddress() + "] to the TELNET server!!!");
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Telnet Commands
	 * 
	 * @param query
	 *          : String - the player's input
	 * @throws IOException
	 */
	private void executeQuery() throws IOException {
		out.println("###############################################################################");
		out.println(TelnetExecutor.lang("TELNET_AVAILABLE_COMMANDS"));
		out.println("###############################################################################");
		out.println("#");

		ArrayList<String> commands = TelnetExecutor.getCommandsSyntax();

		for (String command : commands)
			out.println("# " + command);

		out.println("#");
		out.println("# " + TelnetExecutor.lang("TELNET_HELP_COMMAND"));
		out.println("# " + TelnetExecutor.lang("TELNET_QUIT_COMMAND"));
		out.println("#");
		out.println("###############################################################################\n");
		out.println(TelnetExecutor.lang("TELNET_TYPE_YOUR_COMMAND", gmName));

		while ((line = readLine()) != null && !line.isEmpty() && !line.equals("quit") && !line.equals("help")) {
			out.flush();

			TelnetUser sender = new TelnetUser(client, out, in, gmName, client.getInetAddress().getHostAddress(), accesslevel);

			if (!TelnetExecutor.executeQuery(line, sender)) {
				log.info(TelnetExecutor.lang("TELNET_DONT_UNDERSTAND_COMMAND", client.getInetAddress().getHostAddress(), gmName, line));
				out.println(TelnetExecutor.lang("TELNET_DONT_UNDERSTAND_YOU"));
			}

			out.println();
			out.println(TelnetExecutor.lang("TELNET_TYPE_YOUR_COMMAND", gmName));
		}

		if (line == null || line.isEmpty()) {
			executeQuery();
			return;
		}

		out.println();
		if (line.equals("help"))
			executeQuery();
	}

	/**
	 * Replace all non alphanum char by an empty char Needed on some soft, like putty : the first input is wrong
	 * 
	 * @return String : cleared string
	 * @throws IOException
	 */
	private String readLine(boolean word) throws IOException {
		String line = in.readLine();

		if (line == null || line.isEmpty())
			return null;

		// Non alphanum
		if (word)
			line = line.replaceAll("[^a-zA-Z0-9\\uAC00-\\uD7A3/]", "");

		return line;
	}

	/**
	 * Cf. readLine(boolean)
	 * 
	 * @return
	 * @throws IOException
	 */
	private String readLine() throws IOException {
		return readLine(false);
	}

	/**
	 * Close client connection
	 * 
	 * @throws IOException
	 */
	private void close() throws IOException {
		out.println(TelnetExecutor.lang("TELNET_GOODBYE"));
		out.flush();

		// Deleting user from java memory
		if (gmName != null || !gmName.isEmpty())
			TelnetExecutor.deleteUser(gmName);
		client.close();

		log.info(TelnetExecutor.lang("TELNET_DISCONNECT", client.getInetAddress().getHostAddress()));
	}

	/**
	 * Set the current player's accesslevel
	 * 
	 * @param accesslevel
	 *          : int - the accesslevel
	 */
	public void setAccessLevel(int accesslevel) {
		this.accesslevel = accesslevel;
	}

	/**
	 * Get the current player's name
	 * 
	 * @return String : the player's name
	 */
	public String getPlayerName() {
		return gmName;
	}

	/**
	 * Set if LS gives a response
	 * 
	 * @param response
	 *          : boolean - the response
	 */
	public void setResponse(boolean response) {
		this.response = response;
	}
}
