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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.aionemu.gameserver.configs.main.TelnetConfig;

/**
 * Telnet Server - Init server - Start connection with client
 * 
 * @author Divinity
 */
public class TelnetServer extends Thread {

	/**
	 * Logger for TelnetServer
	 */
	private static final Logger log = Logger.getLogger(TelnetServer.class);

	private int port;
	private String password;
	private ServerSocket server;

	/**
	 * Constructor
	 */
	public TelnetServer() {
		// Init
		TelnetExecutor.init();
		log.info(TelnetExecutor.lang("TELNET_INIT"));
		log.info(TelnetExecutor.lang("TELNET_EXECUTOR_INIT"));

		// Checking configs
		this.port = TelnetConfig.TELNET_PORT;
		this.password = TelnetConfig.TELNET_AUTHENTIFICATION_PASSWORD;

		// GM Auth enabled ?
		/**
		 * if (!TelnetConfig.TELNET_USE_GM_AUTHENTIFICATION) { // Password exist ? if (this.password == null ||
		 * this.password.isEmpty()) { // Generating password with length 10 password = generatePassword(10);
		 * log.info(TelnetExecutor.lang("TELNET_PASSWORD_AUTH_EMPTY")); log.info(TelnetExecutor.lang("SERVER_PASSWORD_IS",
		 * password)); } else log.info(TelnetExecutor.lang("TELNET_PASSWORD_AUTH_NO_EMPTY")); } else
		 * log.info(TelnetExecutor.lang("TELNET_USE_GM_AUTH"));
		 */

		// IP restriction or banishment system are enabled ?
		if (TelnetConfig.TELNET_IP_RESTRICTION_IS_ENABLE)
			log.info(TelnetExecutor.lang("TELNET_IP_ENABLED"));
		if (TelnetConfig.TELNET_BANISHMENT_SYSTEM_IS_ENABLE)
			log.info(TelnetExecutor.lang("TELNET_BAN_ENABLED"));

		log.info(TelnetExecutor.lang("TELNET_LANGUAGE", TelnetConfig.TELNET_LANGUAGE));

		try {
			// Connection...
			server = new ServerSocket(port);
			log.info(TelnetExecutor.lang("TELNET_STARTED", port));
		}
		catch (IOException e) {
			log.error(TelnetExecutor.lang("TELNET_HALTED"));
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void run() {
		// Thread priority max
		this.setPriority(MAX_PRIORITY);

		try {
			Socket connection;

			while (true) {
				connection = server.accept();

				// IP restriction is enabled ?
				if (TelnetConfig.TELNET_IP_RESTRICTION_IS_ENABLE) {
					String list = TelnetConfig.TELNET_IP_RESTRICTION_LIST;
					String ipList[] = list.split(",");
					int nbIP = ipList.length;
					int i = 0;
					boolean found = false;
					String clientIP = connection.getInetAddress().getHostAddress();

					while (!found && i < nbIP) {
						if (clientIP.equals(ipList[i]))
							found = true;
						i++;
					}

					// Found in the ip list ?
					if (found)
						log.info(TelnetExecutor.lang("TELNET_IP_ACCEPTED", clientIP));
					else {
						log.warn(TelnetExecutor.lang("TELNET_IP_REJECTED", clientIP));
						connection.close();
						continue;
					}
				}

				// Banishment System is enabled && IP is banned ?
				if (TelnetConfig.TELNET_BANISHMENT_SYSTEM_IS_ENABLE && TelnetExecutor.isBanned(connection.getInetAddress().getHostAddress())) {
					// Closing connection
					log.warn(TelnetExecutor.lang("TELNET_BAN_REJECTED", connection.getInetAddress().getHostAddress()));
					connection.close();
					continue;
				}
				else
					log.info(TelnetExecutor.lang("TELNET_BAN_ACCEPTED", connection.getInetAddress().getHostAddress()));

				// Starting client connection
				TelnetClient client = new TelnetClient(connection, password);
				client.start();
			}
		}
		catch (IOException e) {
			if (this.isInterrupted()) {
				try {
					server.close();
				}
				catch (IOException e1) {
					log.error(e1.getMessage(), e1);
				}
			}
			log.error(e.getMessage(), e);
		}
	}
}
