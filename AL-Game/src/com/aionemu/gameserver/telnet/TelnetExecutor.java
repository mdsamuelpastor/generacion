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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.aionemu.commons.utils.Base64;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.TelnetConfig;
import com.aionemu.gameserver.model.TelnetUser;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.utils.telnethandlers.TelnetCommandHandler;
import com.aionemu.gameserver.utils.telnethandlers.TelnetHandler;

/**
 * @author Divinity
 */
public class TelnetExecutor {

	/**
	 * Logger for TelnetServer
	 */
	private static final Logger log = Logger.getLogger(TelnetServer.class);

	private static ArrayList<TelnetClient> telnetClients = new ArrayList<TelnetClient>();
	private static HashMap<String, Integer> banishmentSystem = new HashMap<String, Integer>();
	private static HashMap<String, Long> bannedUsers = new HashMap<String, Long>();
	private static HashMap<String, String> languageString = new HashMap<String, String>();
	private static TelnetCommandHandler telnetCommandHandler;

	/**
	 * Initialize the TelnetExecutor
	 */
	public static void init() {
		initLanguage();
		TelnetHandler.getInstance();
		telnetCommandHandler = TelnetHandler.getInstance().getHandler();
	}

	/**
	 * Request a connexion with a GM Auth
	 * 
	 * @param playerName
	 *          : String - the player's name
	 * @param password
	 *          : String - the account's password
	 * @param telnetClient
	 *          : TelnetClient - the current connection
	 */
	public static void requestConnexion(String playerName, String password, TelnetClient telnetClient) {
		String hashPass = "";

		// Encoding password
		try {
			MessageDigest messageDiegest = MessageDigest.getInstance("SHA-1");
			messageDiegest.update(password.getBytes("UTF-8"));
			hashPass = Base64.encodeToString(messageDiegest.digest(), false);
		}
		catch (NoSuchAlgorithmException e) {
			log.error("Password encryption error!");
			throw new Error(e);
		}
		catch (UnsupportedEncodingException e) {
			log.error("Password encryption error!");
			throw new Error(e);
		}

		telnetClients.add(telnetClient);
		LoginServer.getInstance().requestTelnetConnection(playerName, hashPass);
	}

	/**
	 * LS response for the GM Auth
	 * 
	 * @param playerName
	 *          : String - the player's name
	 * @param accesslevel
	 *          : int - the player's accesslevel
	 */
	public static void receiveAccesslevel(String playerName, int accesslevel) {
		boolean found = false;
		int i = 0;
		int clients = telnetClients.size();

		while (!found && i < clients) {
			if (playerName.equals(telnetClients.get(i).getPlayerName().toLowerCase())) {
				found = true;
				telnetClients.get(i).setAccessLevel(accesslevel);
				// Warn the TelnetClient : user found
				telnetClients.get(i).setResponse(true);
			}
			i++;
		}
	}

	/**
	 * Delete user from TelnetExecutor
	 * 
	 * @param playerName
	 *          : String - the player's name
	 */
	public static void deleteUser(String playerName) {
		boolean found = false;
		int i = 0;
		int clients = telnetClients.size();

		while (!found && i < clients) {
			if (playerName.equals(telnetClients.get(i).getPlayerName()))
				telnetClients.remove(i);
			i++;
		}
	}

	/**
	 * Add try for the player's ip
	 * 
	 * @param ip
	 *          : String - the player's ip
	 */
	public static void addTry(String ip) {
		if (banishmentSystem.get(ip) != null)
			banishmentSystem.put(ip, banishmentSystem.get(ip) + 1);
		else
			banishmentSystem.put(ip, 1);
	}

	/**
	 * Get all tries of the player's ip
	 * 
	 * @param ip
	 *          : String - the player's ip
	 * @return int : the tries
	 */
	public static int getTry(String ip) {
		return banishmentSystem.get(ip);
	}

	/**
	 * Delete all tries of the player's ip
	 * 
	 * @param ip
	 *          : String - the player's ip
	 */
	public static void deleteTries(String ip) {
		banishmentSystem.remove(ip);
	}

	/**
	 * Check if the player's is is banned
	 * 
	 * @param ip
	 *          : String - the player's ip
	 * @return boolean : true if banned, false otherwise
	 */
	public static boolean isBanned(String ip) {
		int timeToBan = TelnetConfig.TELNET_BANISHMENT_SYSTEM_TIME_TO_BAN * 60000; // minutes

		if (banishmentSystem.get(ip) != null && banishmentSystem.get(ip) >= TelnetConfig.TELNET_BANISHMENT_SYSTEM_MAX_CONNEXION) {
			if (bannedUsers.get(ip) != null) {
				if (bannedUsers.get(ip) < System.currentTimeMillis()) {
					bannedUsers.remove(ip);
					return false;
				}
				else
					return true;
			}
			else {
				bannedUsers.put(ip, System.currentTimeMillis() + timeToBan);
				return true;
			}
		}
		else
			return false;
	}

	/**
	 * Initialize different languages
	 */
	public static void initLanguage() {
		if (TelnetConfig.TELNET_LANGUAGE.equals("en")) // English
		{
			// TelnetServer
			languageString.put("TELNET_INIT", "Telnet Server initialized !");
			languageString.put("TELNET_EXECUTOR_INIT", "Telnet Server Executor initialized !");
			languageString.put("TELNET_PASSWORD_AUTH_EMPTY", "Telnet Server use the password authenficiation, and the password is empty !");
			languageString.put("SERVER_PASSWORD_IS", "Telnet Server generating password...\nTelnet Server session password is : {1}");
			languageString.put("TELNET_PASSWORD_AUTH_NO_EMPTY", "Telnet Server use the password authentification, and the password isn't empty, no password generated");
			languageString.put("TELNET_USE_GM_AUTH", "Telnet Server use the GM Authentification, no password generated");
			languageString.put("TELNET_LANGUAGE", "Telnet Server selected language : {1}");
			languageString.put("TELNET_STARTED", "Telnet Server started successfully with the listening port : {1}");
			languageString.put("TELNET_HALTED", "Telnet Server can't start !!!");
			languageString.put("TELNET_IP_ACCEPTED", "[TELNET IP RESTRICTION]: Client IP: [{1}] accepted");
			languageString.put("TELNET_IP_REJECTED", "[TELNET IP RESTRICTION]: Client IP: [{1}] rejected !");
			languageString.put("TELNET_BAN_REJECTED", "[TELNET BANISHMENT SYSTEM]: Attempt to connect with a banned IP: [{1}]. Connection rejected !");
			languageString.put("TELNET_BAN_ACCEPTED", "[TELNET BANISHMENT SYSTEM]: Client IP: [{1}] accepted");
			languageString.put("TELNET_IP_ENABLED", "Telnet Server IP Restriction is enabled !");
			languageString.put("TELNET_BAN_ENABLED", "Telnet Server Banishment System is enabled !");

			// TelnetClient
			languageString.put("TELNET_HEADER", "#		WELCOME TO THE " + GSConfig.SERVER_NAME + " TELNET SERVER	   #");
			languageString.put("TELNET_PLAYER_NAME", "[@SERVER]: Your player's name (this name will appear in the server) ? : ");
			languageString.put("TELNET_ACCOUNT_PASSWORD", "[@SERVER]: Your account's password ? : ");
			languageString.put("TELNET_SERVER_PASSWORD", "[@SERVER]: The server's password ? : ");
			languageString.put("TELNET_PASSWORD_IS_EMPTY", "[@SERVER]: The password can't be empty. {1} ? : ");
			languageString.put("TELNET_ACCOUNT_PASSWORD_TINY", "Your account's password");
			languageString.put("TELNET_SERVER_PASSWORD_TINY", "The server's password");
			languageString.put("TELNET_CHECKING_LS", "[@SERVER]: Checking LoginServer, please wait...");
			languageString.put("TELNET_UNABLE_CHECKING_LS", "[@SERVER]: Unable to checking the LoginServer. Connection lost.");
			languageString.put("TELNET_WRONG_LOGIN_PASSWORD", "[TELNET]: Wrong login ({1}) or password for the IP: {2} ! Connection rejected !");
			languageString.put("TELNET_LOGIN_PASSWORD_INCORRECT", "[@SERVER]: Login or password incorrect. Please try again.");
			languageString.put("TELNET_CURRENT_TRIES", "[TELNET BANISHMENT SYSTEM]: Current tries for the IP: {1} > {2} / {3}");
			languageString.put("TELNET_MAX_CONNECTION", "[TELNET BANISHMENT SYSTEM]: The maximum connections has been reached for the IP: {1}. Client banned for {2} minutes and connection rejected !");
			languageString.put("TELNET_YOUR_MAX_CONNECTION", "[@SERVER]: Your maximum connections has been reached. Please try again in the few minutes.");
			languageString.put("TELNET_SUCCESS_GM_AUTH", "[TELNET]: Connection [{1}][{2}] login successfully with the GM Authentification !");
			languageString.put("TELNET_WELCOME", "[@SERVER]: Welcome {1} !\n");
			languageString.put("TELNET_ENOUGH_RIGHT_GM_AUTH", "[TELNET]: Connection from [{1}] with login [{2}] and access level [{3}] hasn't enough rights to connect to this server.");
			languageString.put("TELNET_CONNECTION_REJECTED", "[TELNET]: Connection rejected !");
			languageString.put("TELNET_ENOUGH_RIGHT", "[@SERVER]: You don't have enough rights to connect to this server.");
			languageString.put("TELNET_NICKNAME", "[@SERVER]: Your nickname (this name will appear in the server) ? : ");
			languageString.put("TELNET_EMPTY_NICKNAME", "[@SERVER]: The nickname can't be empty. Your nickname ? : ");
			languageString.put("TELNET_SUCCES_PASSWORD_AUTH", "[TELNET]: Connection [{1}][{2}] login successfully with the password authentification !");
			languageString.put("TELNET_WRONG_PASSWORD", "[TELNET]: Wrong password for the IP: {1} ! Connection rejected !");
			languageString.put("TELNET_PASSWORD_INCORRECT", "[@SERVER]: Password incorrect. Please try again.");
			languageString.put("TELNET_DISCONNECT", "[TELNET]: User [{1}] closed connection");
			languageString.put("TELNET_DONT_UNDERSTAND_YOU", "[@SERVER]: I don't understand you, sorry. Please try again.");
			languageString.put("TELNET_DONT_UNDERSTAND_COMMAND", "[TELNET]: Client [{1}][{2}] type an unknow telnet command : {3}");
			languageString.put("TELNET_AVAILABLE_COMMANDS", "#                       THE LIST OF AVAILABLE COMMANDS                        #");
			languageString.put("TELNET_TYPE_YOUR_COMMAND", "{1}, please type the command you want : \n");
			languageString.put("TELNET_LOADED_COMMANDS", "Telnet Server loaded {1} telnet commands");
			languageString.put("TELNET_GOODBYE", "[@SERVER]: Goodbye !");
			languageString.put("TELNET_HELP_COMMAND", "help - to see the commands list");
			languageString.put("TELNET_QUIT_COMMAND", "quit - to quit");

			// TelnetUser
			languageString.put("TELNET_SERVER", "[@SERVER]:");
		}
		else
		// german BY DEFAULT
		{
			// TelnetServer
			languageString.put("TELNET_INIT", "Telnetserver initialisiert!");
			languageString.put("TELNET_EXECUTOR_INIT", "TelnerserverExecutor initialisiert!");
			languageString.put("TELNET_PASSWORD_AUTH_EMPTY", "Der Telnetserver benoetigt ein Passwort!");
			languageString.put("SERVER_PASSWORD_IS", "Password wird generiert...\nDas Passwort lautet: {1}");
			languageString.put("TELNET_PASSWORD_AUTH_NO_EMPTY", "Der Telnetserver benoetigt ein Passwort! Autogenerierung nicht moeglich!");
			languageString.put("TELNET_USE_GM_AUTH", "Zur Authentifizierung wird ein GM-Account benoetigt!");
			languageString.put("TELNET_LANGUAGE", "Ausgewaehlte Sprache: {1}");
			languageString.put("TELNET_STARTED", "Telnetserver gestartet! Horche auf Port: {1}");
			languageString.put("TELNET_HALTED", "Starten nicht moeglich!!!");
			languageString.put("TELNET_IP_ACCEPTED", "[TELNET IP-Einschraenkung]: Zugriff von IP: [{1}] akzeptiert");
			languageString.put("TELNET_IP_REJECTED", "[TELNET IP-Einschraenkung]: Zugriff von IP: [{1}] verweigert!");
			languageString.put("TELNET_BAN_REJECTED", "[TELNET BAN-System]: Das Einloggen mit der IP [{1}] ist nicht moeglich, da die IP gebannt ist!");
			languageString.put("TELNET_BAN_ACCEPTED", "[TELNET BAN-System]: Zugriff von IP: [{1}] akzeptiert");
			languageString.put("TELNET_IP_ENABLED", "TELNET IP-Einschraenkung aktiviert!");
			languageString.put("TELNET_BAN_ENABLED", "TELNET BAN-System aktiviert!");

			// TelnetClient
			languageString.put("TELNET_HEADER", "#		Willkommen auf dem Telnetserver von " + GSConfig.SERVER_NAME + "!           #");
			languageString.put("TELNET_PLAYER_NAME", "[@SERVER]: Dein Name? : ");
			languageString.put("TELNET_ACCOUNT_PASSWORD", "[@SERVER]: Dein Accountpasswort? : ");
			languageString.put("TELNET_SERVER_PASSWORD", "[@SERVER]: Das Telnetpasswort? : ");
			languageString.put("TELNET_PASSWORD_IS_EMPTY", "[@SERVER]: Das Passwort darf nicht leer sein! {1} ? : ");
			languageString.put("TELNET_ACCOUNT_PASSWORD_TINY", "Dein Accountpasswort");
			languageString.put("TELNET_SERVER_PASSWORD_TINY", "Das Telnetpasswort");
			languageString.put("TELNET_CHECKING_LS", "[@SERVER]: Verbindung zum Login-Server wird ueberprueft. Bitte warten...");
			languageString.put("TELNET_UNABLE_CHECKING_LS", "[@SERVER]: Eine Verbindung zum Login-Server war nicht moeglich!");
			languageString.put("TELNET_WRONG_LOGIN_PASSWORD", "[TELNET]: Passwort oder Login ({1}) fuer die IP: {2} sind falsch! Verbindung verweigert!!");
			languageString.put("TELNET_LOGIN_PASSWORD_INCORRECT", "[@SERVER]: Passwort oder Login falsch. Bitte erneut eingeben!");
			languageString.put("TELNET_CURRENT_TRIES", "[TELNET BANISHMENT SYSTEM]: Aktuelle fehlerhafte Versuche: IP: {1} > {2} / {3}");
			languageString.put("TELNET_MAX_CONNECTION",
				"[TELNET BANISHMENT SYSTEM]: Die maximale Anzahl der Fehlversuche fuer die IP: {1} wurde erreicht. Du wurdest fuer {2} Minuten gebannt und die Verbindung wird verweigert!");
			languageString.put("TELNET_YOUR_MAX_CONNECTION", "[@SERVER]: Deine maximale Verbindungsbandbreite wurde erreicht. Bitte versuche es spaeter erneut.");
			languageString.put("TELNET_SUCCESS_GM_AUTH", "[TELNET]: [{1}][{2}] ist erfolgreich als GM/Admin verifiziert worden!");
			languageString.put("TELNET_WELCOME", "[@SERVER]: {1} Willkommen!\n");
			languageString.put("TELNET_ENOUGH_RIGHT_GM_AUTH", "[TELNET]: Der Zugriff auf den Account [{2}] von der IP [{1}] wurde auf grund des GM-Levels [{3}] verweigert!");
			languageString.put("TELNET_CONNECTION_REJECTED", "[TELNET]: Zugriff verweigert!");
			languageString.put("TELNET_ENOUGH_RIGHT", "[@SERVER]: Dein GM-Level erlaubt keinen Zugriff auf den Server.");
			languageString.put("TELNET_NICKNAME", "[@SERVER]: Dein Name? : ");
			languageString.put("TELNET_EMPTY_NICKNAME", "[@SERVER]: Dein Name darf nicht leer sein. Bitte gib deinen Namen ein: ");
			languageString.put("TELNET_SUCCES_PASSWORD_AUTH", "[TELNET]: [{1}][{2}] ist erfolgreich verifiziert worden");
			languageString.put("TELNET_WRONG_PASSWORD", "[TELNET]: Falsches Passwort! IP {1}. Zugriff verweigert!");
			languageString.put("TELNET_PASSWORD_INCORRECT", "[@SERVER]: Passwort falsch. Bitte erneut eingeben!");
			languageString.put("TELNET_DISCONNECT", "[TELNET]: Benutzer [{1}] hat die Verbindung getrennt.");
			languageString.put("TELNET_DONT_UNDERSTAND_YOU", "[@SERVER]: Deine Eingabe war unverstaendlich. Bitte erneut eingeben.");
			languageString.put("TELNET_DONT_UNDERSTAND_COMMAND", "[TELNET]: Benutzer [{1}][{2}] hat ein ungueltiges Kommando eingegeben: {3}");
			languageString.put("TELNET_AVAILABLE_COMMANDS", "#				Verfuegbare Befehle:			#");
			languageString.put("TELNET_TYPE_YOUR_COMMAND", "{1}, bitte gib einen Befehl ein: \n");
			languageString.put("TELNET_LOADED_COMMANDS", "Es wurden {1} Kommandos geladen.");
			languageString.put("TELNET_GOODBYE", "[@SERVER]: Verbindung auf Wunsch beendet! Auf Wiedersehen!");
			languageString.put("TELNET_HELP_COMMAND", "help - zeigt die verfuegbaren Befehle an");
			languageString.put("TELNET_QUIT_COMMAND", "quit - beendet die Verbindung");

			// TelnetUser
			languageString.put("TELNET_SERVER", "[@SERVER]:");
		}
	}

	/**
	 * Get the language
	 * 
	 * @param id
	 *          : String - ID of the string
	 * @return String
	 */
	public static String lang(String id) {
		Object params[] = null;
		return lang(id, params);
	}

	/**
	 * /** Get the language
	 * 
	 * @param id
	 *          : String - ID of the string
	 * @param params
	 *          : String[unkown] - Params
	 * @return String
	 */
	public static String lang(String id, Object... params) {
		if (languageString.get(id) == null)
			return "Incorrect language ID: " + id;

		if (params == null)
			return languageString.get(id);
		else {
			String lang = languageString.get(id);
			int paramsSize = params.length;

			for (int i = 1; i <= paramsSize; i++)
				lang = lang.replace("{" + i + "}", params[i - 1].toString());

			return lang;
		}
	}

	/**
	 * Execute a command
	 * 
	 * @param query
	 *          : String - the command
	 * @param sender
	 *          : TelnetUser - current connexion
	 * @return boolean : true if the command successful, flase otherwise
	 */
	public static boolean executeQuery(String query, TelnetUser sender) {
		return telnetCommandHandler.handleTelnetMessage(query, sender);
	}

	/**
	 * Get all commands syntaxes
	 * 
	 * @return ArrayList<String> : all commands syntaxes
	 */
	public static ArrayList<String> getCommandsSyntax() {
		return telnetCommandHandler.getCommandsSyntax();
	}
}
