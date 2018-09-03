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
package com.aionemu.gameserver.configs.main;

import org.apache.log4j.Logger;

import com.aionemu.commons.configuration.Property;

/**
 * @author Divinity
 */
public class TelnetConfig {

	/**
	 * Logger for this class.
	 */
	protected static final Logger log = Logger.getLogger(TelnetConfig.class);

	/*
	 * Enable/disable Telnet Server
	 */
	@Property(key = "gameserver.telnet.enable", defaultValue = "false")
	public static boolean TELNET_IS_ENABLE;

	/**
	 * Your language en : English de : Deutsch (german)
	 */
	@Property(key = "gameserver.telnet.language", defaultValue = "de")
	public static String TELNET_LANGUAGE;

	/**
	 * The port listening by the Telnet Server
	 */
	@Property(key = "gameserver.telnet.port", defaultValue = "4444")
	public static int TELNET_PORT;

	/**
	 * Enable/disable Telnet Server IP restriction
	 */
	@Property(key = "gameserver.telnet.iprestriction.enable", defaultValue = "true")
	public static boolean TELNET_IP_RESTRICTION_IS_ENABLE;

	/**
	 * The list of IP which will be able to connect to the telnet server Separated by commas, no space
	 */
	@Property(key = "gameserver.telnet.iprestriction.list", defaultValue = "127.0.0.1,localhost")
	public static String TELNET_IP_RESTRICTION_LIST;

	/**
	 * Enable/disable GM Authentification If enabled : Login : Your GM's name Password : Your account's password Your GM
	 * name will be use for the announcements on the server Your accesslevel will be use for the commands If disabled :
	 * Login : None Password : The telnet server's password. So it's the same for everyone You will be able to choose the
	 * name (when you will be connected) for the announcements on the server The accesslevel will be the same for everyone
	 * (telnet config)
	 */
	@Property(key = "gameserver.telnet.gmauth.enable", defaultValue = "false")
	public static boolean TELNET_USE_GM_AUTHENTIFICATION;

	/**
	 * ONLY IF THE GM AUTHENTIFICATION IS DISABLED ! The password of the Telnet Server If the password is empty, it will
	 * be generated everytime the server (the GameServer) will start It will be show on the GameServer's console to know
	 * what is the current password
	 */
	@Property(key = "gameserver.telnet.password", defaultValue = "")
	public static String TELNET_AUTHENTIFICATION_PASSWORD;

	/**
	 * ONLY IF THE GM AUTHENTIFICATION IS DISABLED ! All telnet admin commands are based on in game admin command
	 * accesslevel This is the accesslevel to the connected player
	 */
	@Property(key = "gameserver.telnet.accesslevel", defaultValue = "3")
	public static int TELNET_ACCESSLEVEL;

	/**
	 * Enable/disable Telnet Server Banishment System
	 */
	@Property(key = "gameserver.telnet.banishment.system.enable", defaultValue = "true")
	public static boolean TELNET_BANISHMENT_SYSTEM_IS_ENABLE;

	/**
	 * The number of maximum connection of banishment system If the player reached this number, he won't be able to
	 * connect to the telnet server
	 */
	@Property(key = "gameserver.telnet.banishment.system.maxconnection", defaultValue = "3")
	public static int TELNET_BANISHMENT_SYSTEM_MAX_CONNEXION;

	/**
	 * Time to ban a player's IP Unit : minute
	 */
	@Property(key = "gameserver.telnet.banishment.system.timetoban", defaultValue = "30")
	public static int TELNET_BANISHMENT_SYSTEM_TIME_TO_BAN;
}
