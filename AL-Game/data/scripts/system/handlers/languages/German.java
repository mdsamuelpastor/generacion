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
package languages;

import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.Language;

/**
 * @author Maestross
 */
 
public class German extends Language {

	public German() {
		super("de");
		addSupportedLanguage("de_DE");
		addTranslatedMessage(CustomMessageId.SERVER_REVISION, "Version du serveur : %-6s");
		addTranslatedMessage(CustomMessageId.WELCOME_PREMIUM,
			"Bienvenue Membre Priviligié sur le serveur %s.\nDéveloppé par l'équipe Js-Encom.\nRates du serveur :\nExpérience : %d\nQuêtes : %d\nDrops : %d");
		addTranslatedMessage(CustomMessageId.WELCOME_REGULAR, "Bienvenue sur le serveur %s.\nDéveloppé par l'équipe Js-Encom.\nRates du serveur :\nExpérience : %d\nQuêtes : %d\nDrops : %d");
		addTranslatedMessage(CustomMessageId.WELCOME_BASIC, "Willkommen auf dem Server von : ");
		addTranslatedMessage(CustomMessageId.HOMEPAGE, "Homepage: www.celestialaion.com");
		addTranslatedMessage(CustomMessageId.TEAMSPEAK, "Teamspeak3: celestialaion.com");
		addTranslatedMessage(CustomMessageId.INFO1, "Achtung: Das werben fuer andere Server ist verboten und wird einen Ban zu folge haben!");
		addTranslatedMessage(CustomMessageId.INFO2, "Achtung: Das Team wird dich niemals nach deinem Passwort fragen!");
		addTranslatedMessage(CustomMessageId.INFO3, "Achtung: Das Hacken auf unserem Server hat ein sofortigen Ban zu folge.");
		addTranslatedMessage(CustomMessageId.INFO4, "Tipp: Benutze .ely/.asmo um den Fraktions Chat zu benutzen.");
		addTranslatedMessage(CustomMessageId.ENDMESSAGE, "Viel Spass auf: ");
		addTranslatedMessage(CustomMessageId.ANNOUNCE_GM_CONNECTION, " betrat gerade Atreia!");
		addTranslatedMessage(CustomMessageId.ANNOUNCE_MEMBER_CONNECTION, "%s betrat gerade Atreia.");
		addTranslatedMessage(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS, "Vous n'avez pas le droit d'exécuter cette commande.");
		addTranslatedMessage(CustomMessageId.PLAYER_NOT_ONLINE, "Le joueur %s n'est pas en ligne.");
		addTranslatedMessage(CustomMessageId.INTEGER_PARAMETER_REQUIRED, "Le paramètre doit être un nombre.");
		addTranslatedMessage(CustomMessageId.INTEGER_PARAMETERS_ONLY, "Les paramètres doivent être des nombres.");
		addTranslatedMessage(CustomMessageId.SOMETHING_WRONG_HAPPENED, "Quelque chose s'est mal passé.");
		addTranslatedMessage(CustomMessageId.COMMAND_DISABLED, "Cette commande est désactivée.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_SYNTAX, "Syntaxe: //add <nom du joueur> <item id> [<quantité>]");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_ADMIN_SUCCESS, "Objet(s) ajouté(s) à %s avec succès.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_PLAYER_SUCCESS, "%s vous a ajouté %d objet(s).");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_FAILURE, "L'objet %d n'existe pas et/ou ne peut pas être ajouté à %s.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDDROP_SYNTAX, "Syntaxe: //adddrop <mob id> <item id> <min> <max> <chance>");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_SYNTAX, "Syntaxe: <nom du joueur> <set id>");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_SET_DOES_NOT_EXISTS, "L'ensemble d'armures %d n'existe pas.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_NOT_ENOUGH_SLOTS, "L'inventaire requiert au moins %d emplacements libres.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_CANNOT_ADD_ITEM, "L'objet %d ne peut pas être ajouté à %s.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_ADMIN_SUCCESS, "L'ensemble d'armures %d a été ajouté à %s avec succès.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_PLAYER_SUCCESS, "%s vous a ajouté un ensemble d'armures.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_SYNTAX, "Syntaxe: //addskill <id de la compétence> <niveau de la compétence>");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_ADMIN_SUCCESS, "Vous avez ajouté la compétence %d à %s avec succès.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_PLAYER_SUCCESS, "%s vous a ajouté une nouvelle compétence.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_SYNTAX, "Syntaxe: //addtitle <id du titre> <nom du joueur> [special]");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_TITLE_INVALID, "Le titre %d est invalide, il doit être entre 1 et 50.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_ME, "Vous ne pouvez pas vous ajouter le titre %d.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_PLAYER, "Vous ne pouvez pas ajouter le titre %d à %s.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS_ME, "Vous vous êtes ajouté le titre %d avec succès.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS, "Vous avez ajouté le titre %d à %s avec succès.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_PLAYER_SUCCESS, "%s vous a donné le titre %d.");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_SYNTAX, "Syntaxe: //send <nom de fichier>");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_MAPPING_NOT_FOUND, "Fichier %s introuvable.");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_NO_PACKET, "Pas de paquet à envoyer.");
		addTranslatedMessage(CustomMessageId.CHANNEL_WORLD_DISABLED, "Le canal %s est désactivé, merci d'utiliser les canaux %s ou %s selon votre faction.");
		addTranslatedMessage(CustomMessageId.CHANNEL_ALL_DISABLED, "Les canaux sont désactivés.");
		addTranslatedMessage(CustomMessageId.CHANNEL_ALREADY_FIXED, "Vous êtes déjà fixé sur le canal %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED, "Votre chat est maintenant fixé sur le canal %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_NOT_ALLOWED, "Vous n'êtes pas autorisé à parler sur ce canal.");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED_BOTH, "Votre chat est maintenant fixé sur les canaux %s et %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_UNFIX_HELP, "Tapez %s unfix pour libérer votre chat.");
		addTranslatedMessage(CustomMessageId.CHANNEL_NOT_FIXED, "Votre chat n'est fixé sur aucun canal.");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED_OTHER, "Votre chat n'est pas fixé sur ce canal, mais sur %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_RELEASED, "Votre chat a été libéré du canal %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_RELEASED_BOTH, "Votre chat a été libéré des canaux %s et %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_BAN_ENDED, "Vous n'êtes plus banni des canaux de discussion.");
		addTranslatedMessage(CustomMessageId.CHANNEL_BAN_ENDED_FOR, "Le joueur %s n'est plus banni des canaux de discussion.");
		addTranslatedMessage(CustomMessageId.CHANNEL_BANNED, "Vous ne pouvez pas utiliser les canaux de discussion car %s vous a banni pour la raison suivante: %s, temps restant: %s.");
		addTranslatedMessage(CustomMessageId.COMMAND_MISSING_SKILLS_STIGMAS_ADDED, "%d compétences basiques et %d compétences stigmas vous ont été ajoutées.");
		addTranslatedMessage(CustomMessageId.COMMAND_MISSING_SKILLS_ADDED, "%d compétences basiques vous ont été ajoutées.");
		addTranslatedMessage(CustomMessageId.USER_COMMAND_DOES_NOT_EXIST, "Cette commande joueur n'existe pas.");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_DISABLED, "Votre gain d'XP a été désactivé. Tapez .xpon pour le réactiver.");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ALREADY_DISABLED, "Votre gain d'XP est déjà désactivé.");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ENABLED, "Votre gain d'XP a été ré-activé.");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ALREADY_ENABLED, "Votre gain d'XP est déjà activé.");
		addTranslatedMessage(CustomMessageId.DREDGION_LEVEL_TOO_LOW, "Votre niveau actuel est trop bas pour entrer dans le Dredgion.");
		addTranslatedMessage(CustomMessageId.DEFAULT_FINISH_MESSAGE, "Fertig!");
		
		/**
		 * PvP Spree Service
		 */
		addTranslatedMessage(CustomMessageId.SPREE1, "Blutiger Wahnsinn");
		addTranslatedMessage(CustomMessageId.SPREE2, "Blutbad");
		addTranslatedMessage(CustomMessageId.SPREE3, "Rassenmord");
		addTranslatedMessage(CustomMessageId.KILL_COUNT, "Kills in Folge: ");
		addTranslatedMessage(CustomMessageId.CUSTOM_MSG1, " von ");
		addTranslatedMessage(CustomMessageId.MSG_SPREE1, " ist ein ");
		addTranslatedMessage(CustomMessageId.MSG_SPREE1_1, " !");
		addTranslatedMessage(CustomMessageId.MSG_SPREE2, " verrichtet ein ");
		addTranslatedMessage(CustomMessageId.MSG_SPREE2_1, " ! Hoert auf !");
		addTranslatedMessage(CustomMessageId.MSG_SPREE3, " realisiert einen wahren ");
		addTranslatedMessage(CustomMessageId.MSG_SPREE3_1, " ! Flieht ihr Narren !");
		addTranslatedMessage(CustomMessageId.SPREE_END_MSG1, "Der Blutdurst von ");
		addTranslatedMessage(CustomMessageId.SPREE_END_MSG2, " wurde von ");
		addTranslatedMessage(CustomMessageId.SPREE_END_MSG3, " nach ");
		addTranslatedMessage(CustomMessageId.SPREE_END_MSG4, " Kills beendet!");
		addTranslatedMessage(CustomMessageId.SPREE_MONSTER_MSG, "ein Monster");
		
		/**
		 * PvP Service
		 */
		addTranslatedMessage(CustomMessageId.ADV_WINNER_MSG, "[PvP System] Spieler getoetet ");
		addTranslatedMessage(CustomMessageId.ADV_LOSER_MSG, "[PvP System] Du wurdest getoetet von ");
		addTranslatedMessage(CustomMessageId.PLAP_LOST1, "[PL-AP] Du verlierst ");
		addTranslatedMessage(CustomMessageId.PLAP_LOST2, "% deiner totalen AP");
		addTranslatedMessage(CustomMessageId.PVP_TOLL_REWARD1, "Du hast ");
		addTranslatedMessage(CustomMessageId.PVP_TOLL_REWARD2, " Toll erhalten.");
		addTranslatedMessage(CustomMessageId.PVP_NO_REWARD1, "Du bekommst nichts fuer das toeten von ");
		addTranslatedMessage(CustomMessageId.PVP_NO_REWARD2, " weil du ihn schon zu oft getoetet hast!");
		addTranslatedMessage(CustomMessageId.EASY_MITHRIL_MSG, "[Mithril System] du bekamst: ");
		
		/**
		 * Reward Service Login Messages
		 */
		addTranslatedMessage(CustomMessageId.REWARD10, "Du kannst .start benutzen um eine Level 10 Ausstattung zu bekommen!");
	    addTranslatedMessage(CustomMessageId.REWARD30, "Du kannst .start benutzen um eine Level 30 Ausstattung zu bekommen!");
	    addTranslatedMessage(CustomMessageId.REWARD40, "Du kannst .start benutzen um eine Level 40 Ausstattung zu bekommen!");
	    addTranslatedMessage(CustomMessageId.REWARD50, "Du kannst .start benutzen um eine Level 50 Ausstattung zu bekommen!");
	    addTranslatedMessage(CustomMessageId.REWARD60, "Du kannst .start benutzen um eine Level 60 Ausstattung zu bekommen!");
		
		/**
		 * Advanced PvP System
		 */
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE1, "Heutige PvP Map: Reshanta");
	    addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE2, "Heutige PvP Map: Tiamaranta");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE3, "Heutige PvP Map: Tiamaranta Auge");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE4, "Heutige PvP Map: Gelkmaros");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE5, "Heutige PvP Map: Inggison");
		
		/**
		 * Asmo, Ely and World Channel
		 */
		addTranslatedMessage(CustomMessageId.ASMO_FAIL, "Du bist Elyos! Du kannst diesen Chat nicht benutzen. Nutze .ely <Nachricht> um im Fraktions Chat zu schreiben!");
		addTranslatedMessage(CustomMessageId.ELY_FAIL, "Du bist Asmo! Du kannst diesen Chat nicht benutzen. Nutze .asmo <Nachricht> um im Fraktions Chat zu schreiben!");
		
		/**
		 * Wedding related
		 */
		addTranslatedMessage(CustomMessageId.WEDDINGNO1, "Du kannst dieses Kommando nicht waehrend des Kampfes nutzen!");
		addTranslatedMessage(CustomMessageId.WEDDINGNO2, "Hochzeit wurde nicht gestartet!");
		addTranslatedMessage(CustomMessageId.WEDDINGNO3, "Du hast die Heirat abgelehnt!");
		addTranslatedMessage(CustomMessageId.WEDDINGYES, "Du hast die Heirat akzeptiert!");
		
		/**
		 * Clean Command related
		 */
		addTranslatedMessage(CustomMessageId.CANNOTCLEAN, "Du musst eine Item Id eingeben oder einen Link posten!");
		addTranslatedMessage(CustomMessageId.CANNOTCLEAN2, "Du besitzt dieses Item nicht!");
		addTranslatedMessage(CustomMessageId.SUCCESSCLEAN, "Item wurde erfolgreich aus deinem Wuerfel entfernt!");
		
		/**
	     * Faction Chat (since 3.0 we don't use it more)
		 */
		addTranslatedMessage(CustomMessageId.INFOMESSAGE, "Wenn du den Fraktions Chat nutzen willst: ");
	    
	    /**
		 * Mission check command related
		 */
		addTranslatedMessage(CustomMessageId.SUCCESCHECKED, "Mission erfolgreich ueberprueft!");
	    /**
		 * No Exp Command
		 */
		addTranslatedMessage(CustomMessageId.EPACTIVATED, "Deine EP wurden wieder aktiviert!");
		addTranslatedMessage(CustomMessageId.ACTODE, "Um deine EP zu deaktivieren, nutze .noexp");
		addTranslatedMessage(CustomMessageId.EPDEACTIVATED, "Deine EP wurden deaktiviert!");
		addTranslatedMessage(CustomMessageId.DETOAC, "Um deine EP zu aktivieren, nutze .noexp");
		
		/**
	     * Auto Quest Command
		 */
		addTranslatedMessage(CustomMessageId.WRONGQID, "Bitte gebe eine richtige Quest Id an!");
		addTranslatedMessage(CustomMessageId.NOTSTARTED, "Quest konnte nicht gestartet werden!");
		addTranslatedMessage(CustomMessageId.NOTSUPPORT, "Diese Quest wird nicht von diesem Kommando unterstuetzt!");
		
		/**
		 * Quest Restart Command
		 */
		addTranslatedMessage(CustomMessageId.CANNOTRESTART, "] kann nicht neugestartet werden");
	    
	    /**
		 * Exchange Toll Command
		 */
		addTranslatedMessage(CustomMessageId.TOLLTOBIG, "Du hast zu viele Toll!");
		addTranslatedMessage(CustomMessageId.TOLOWAP, "Du hast nicht genug AP!");
		addTranslatedMessage(CustomMessageId.TOLOWTOLL, "Du hast nicht genug Toll!");
		addTranslatedMessage(CustomMessageId.WRONGTOLLNUM, "Irgendwas lief schief!");
		
		/**
	     * Cube Command
		 */
		addTranslatedMessage(CustomMessageId.CUBE_ALLREADY_EXPANDED, "Dein Wuerfel ist voll erweitert!");
		addTranslatedMessage(CustomMessageId.CUBE_SUCCESS_EXPAND, "Dein Wuerfel wurde erfolgreich erweitert!");
		
		/**
		 * GMList Command
		 */
		addTranslatedMessage(CustomMessageId.ONE_GM_ONLINE, "Ein Team Mitglied ist online: ");
		addTranslatedMessage(CustomMessageId.MORE_GMS_ONLINE, "Es sind folgende Team Mitglieder online: ");
		addTranslatedMessage(CustomMessageId.NO_GM_ONLINE, "Es ist kein Team Mitglied online!");
		
		/**
	     * Go Command (PvP Command)
		 */
		addTranslatedMessage(CustomMessageId.NOT_USE_WHILE_FIGHT, "Du kannst dieses Kommando nicht waehrend des Kampfes nutzen!");
		addTranslatedMessage(CustomMessageId.NOT_USE_ON_PVP_MAP, "Du kannst dieses Kommando nicht auf einer PvP Map benutzen!");
		addTranslatedMessage(CustomMessageId.LEVEL_TOO_LOW, "Du kannst dieses Kommando nur mit Level 55 oder hoeher nutzen!");
		
		/**
		 * Paint Command
		 */
		addTranslatedMessage(CustomMessageId.WRONG_TARGET, "Bitte benutze ein erlaubtes Ziel!");
	 
	    /**
		 * Shiva Command
		 */
		addTranslatedMessage(CustomMessageId.ENCHANT_SUCCES, "Alle deine Items wurden verzaubert auf: ");
		addTranslatedMessage(CustomMessageId.ENCHANT_INFO, "Info: Dieses Kommando verzaubert alle deine Items auf <value>!");
		addTranslatedMessage(CustomMessageId.ENCHANT_SAMPLE, "Beispiel: (.eq 15) wuerde alle deine Items auf 15 verzaubern");
		
		/**
		 * Userinfo Command
		 */
		addTranslatedMessage(CustomMessageId.CANNOT_SPY_PLAYER, "Du kannst keine Infos von anderen Spielern bekommen!");
		
		/**
		 * Custom BossSpawnService
		 */
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_SPAWNED, " wurde gesichtet in ");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_KILLED, " wurde getoetet von ");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_NAME1, "Erwachter Drache");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_NAME2, "Schattenschreiter");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_NAME3, "Flammensturm");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_NAME4, "Gewitter");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_NAME5, "Terra-Explosion");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_NAME6, "Die Schluchtwaechterin");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_NAME7, "Padmarashka");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_NAME8, "Sematariux");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_NAME9, "Cedella");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_NAME10, "Tarotran");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_NAME11, "Raksha Kochherz");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_NAME12, "Grosse Rote Kugel");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_UNK, "Unbekannter NPC");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_LOC1, "Beluslan");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_LOC2, "Morheim");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_LOC3, "Eltnen");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_LOC4, "Heiron");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_LOC5, "Inggison");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_LOC6, "Gelkmaros");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_LOC7, "Silentera Schlucht");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_LOC8, "Padmarashkas Hoehle");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_LOC9, "Sarpan");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_LOC10, "Tiamaranta");
		addTranslatedMessage(CustomMessageId.CUSTOM_BOSS_LOC_UNK, "");
		
		/**
		 * Battleground System
		 */
		addTranslatedMessage(CustomMessageId.BATTLEGROUND_MESSAGE1, "Du bekamst %d Battleground Punkte.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUND_MESSAGE2, "Du hast %d Battleground Punkte verloren.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUND_MESSAGE3, "Der Battleground: %s ist nun bereit. Du wirst in 30 Sekunden teleportiert.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUND_MESSAGE4, "Sekunden bis zum Start ...");
		addTranslatedMessage(CustomMessageId.BATTLEGROUND_MESSAGE5, "Du bist nun unsichtbar.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUND_MESSAGE6, "Du bist nun unsterblich.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUND_MESSAGE7, "Der Battleground ist nun verfuegbar!");
		addTranslatedMessage(CustomMessageId.BATTLEGROUND_MESSAGE8, "Der Battleground wird in 30 Sekunden beendet !");
		addTranslatedMessage(CustomMessageId.BATTLEGROUND_MESSAGE9, "Die Wettzeit ist nun vorbei.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUND_MESSAGE10, "Der Kampf ist vorbei! Klick auf die Umfrage um das Ranking zu sehen. Benutze deinen Bindepunkt Skill um weggeportet zu werden.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUND_MESSAGE11, "Du bist nun sichtbar.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUND_MESSAGE12, "Du bist nun sterblich.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUND_MESSAGE13, "Moechtest du erneut an einem Kampf teilnehmen?");
		addTranslatedMessage(CustomMessageId.BATTLEGROUND_MESSAGE14, "Du bist schon fuer einen Battleground registriert.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUND_MESSAGE15, "Nutze deinen Bindepunkt Skill um den Kampf zu verlassen.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUND_MESSAGE16, "Du bist schon fuer einen Battleground registriert.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUND_MESSAGE17, "Benutze das Kommando .bg unregister um die Registration zurueck zuziehen.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE1, "Nur der Gruppenfuehrer kann die Gruppe registrieren.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE2, "Du bist nun registriert fuer den Battleground: %s");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE3, "Bitte warte einen Moment...");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE4, "Alle Mitglieder koennen diesem Battleground nicht beitreten.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE5, "Ihr seit zu viele um euch fuer diesen Battleground zu registrieren.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE6, "Du bist nun registriert fuer den Battleground:%s");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE7, "Bitte warte einen Moment...");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE8, "Alle Mitglieder koennen diesem Battleground nicht beitreten.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE9, "Ihr seit zu viele um euch fuer diesen Battleground zu registrieren.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE10, "Ein paar Mitglieder sind schon fuer einen Battleground registriert");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE11, "Du bist nun registriert fuer den Battleground: %s");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE12, "Bitte warte einen Moment...");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE13, "Du bist nun registriert fuer den Battleground: %s");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE14, "Bitte warte einen Moment...");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE15, "Kein Battleground fuer dein Level und deine Battleground Punkte verfuegbar.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE16, "Fuer einen Battleground registrieren");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE016, "Du kannst dich fuer folgende Battlegrounds registrieren :");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE17, "Kein Battleground verfuegbar.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE18, "Fuer einen Battleground registrieren");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDMANAGER_MESSAGE018, "Du kannst dich fuer folgende Battlegrounds registrieren :");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDHEALERCONTROLLER_1, "Du warst alleine im Battleground, du wirst nun zurueckgeportet.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDHEALERCONTROLLER_2, "Du warst alleine im Battleground, du wirst nun zurueckgeportet.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDAGENTCONTROLLER_1, "Du bist schon fuer einen Battleground registriert");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDFLAGCONTROLLER_2, "Unbekannter Fehler.");
		addTranslatedMessage(CustomMessageId.BATTLEGROUNDFLAGCONTROLLER_3, "Moechtest du dich fuer einen Battleground registrieren ?");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE0, "Du kannst dich nicht fuer Battlegrounds registrieren solange du im Gefaengnis bist.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE1, "Du bist schon in einem Battleground.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE2, "Benutze deinen Bindepunkt Skill um den Kampf zu verlassen.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE3, "Du bist schon in einem Battleground.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE4, "Benutze das Kommando .bg unregister um deine Registration zurueck zuziehen.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE5, "Du bist bereits in einer Warteliste.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE6, "Benutze das Kommando .bg unregister um deine Registration zurueck zuziehen.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE7, "Du bist bereits in einer Warteliste.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE8, "Benutze deinen Bindepunkt Skill um den Kampf zu verlassen.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE9, "Du bist bereits in einem Battleground.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE10, "Benutze deinen Bindepunkt Skill um den Kampf zu verlassen.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE11, "Du bist schon fuer einen Battleground registriert");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE12, "Benutze das Kommando .bg unregister um deine Registration zurueck zuziehen.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE13, "Du nimmst am Battleground teil, du beobachtest nicht!");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE14, "Du bist nun sichtbar.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE15, "Du bist nun sterblich.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE16, "Du hast deinen Einsatz von: %d Kinah verloren.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE17, "Du beobachtest keinen Battleground.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE18, "Du bist in keinem Battleground registriert, oder er ist schon vorbei.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE19, "Das Umwandel Tool ist nicht verfuegbar.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE20, "Die Umwandel Rate ist 1 BG Punkt fuer 3 Abyss Punkte.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE21, "Zum umwandeln, schreibe .bg exchange <BG_PUNKTE_ANZAHL>");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE22, "Du hast nicht genug BG Punkte.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE23, "Du hast %d BG Punkte verloren.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE24, "Syntax Fehler. Nutze .bg exchange <BG_PUNKTE_ANZAHL>.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE25, "Du bist fuer keinen Battleground registriert.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE26, "Registration abgebrochen.");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE27, ".bg register : in einem Battleground registrieren");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE28, ".bg observe : einen Battleground beobachten");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE29, ".bg unregister : abmelden vom Battleground (vor dem Start)");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE30, ".bg stop : das beobachten beenden und heimkehren");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE31, ".bg rank : sehe deinen Rang waehrend eines Battlegrounds");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE32, ".bg points : : um deine BG Punkte zu sehen");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE33, ".bet : um zu bieten waehrend du beobachtest");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE34, "Dieses Kommando existiert nicht, nutze .bg help");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE35, "register");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE36, "observe");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE37, "stop");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE38, "rank");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE39, "stat");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE40, "exchange");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE41, "unregister");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE42, "help");
		addTranslatedMessage(CustomMessageId.COMMAND_BATTLEGROUND_MESSAGE43, "points");
		
		/**
		 * FFA System
		 */
		addTranslatedMessage(CustomMessageId.FFA_IS_ALREADY_IN_TEAM, "Bitte verlasse dein Team und probiere es erneut.");
		addTranslatedMessage(CustomMessageId.FFA_IS_ALREADY_IN, "Du bist schon im FFA");
		addTranslatedMessage(CustomMessageId.FFA_FROZEN_MESSAGE, "Bitte warte einen kurzen Moment ...");
		addTranslatedMessage(CustomMessageId.FFA_CURRENT_PLAYERS, "Aktuelle Spieler im FFA :");
		addTranslatedMessage(CustomMessageId.FFA_USAGE, "Gebrauch: .ffa enter :um die Arena zu betreten. .ffa leave :um die Arena zu verlassen.\n.ffa info :um die aktuelle Anzahl an Teilnehmern zu sehen ");
		addTranslatedMessage(CustomMessageId.FFA_YOU_KICKED_OUT, "Du bist nicht mehr im FFA!");
		addTranslatedMessage(CustomMessageId.FFA_YOUR_NOT_IN, "Du bist nicht im FFA.");
		addTranslatedMessage(CustomMessageId.FFA_ANNOUNCE_1, "Betrete unsere FFA Map mit .ffa enter ! ");
		addTranslatedMessage(CustomMessageId.FFA_ANNOUNCE_2, " taten es schon.");
		addTranslatedMessage(CustomMessageId.FFA_ANNOUNCE_3, "Betrete FFA jetzt!");
		addTranslatedMessage(CustomMessageId.FFA_GHOST_KILL, "Ein Geist vernichtete ");
		addTranslatedMessage(CustomMessageId.FFA_KILL_MESSAGE, " vernichtete ");
		addTranslatedMessage(CustomMessageId.FFA_KILL_NAME_1, " tut sein Bestes!");
		addTranslatedMessage(CustomMessageId.FFA_KILL_NAME_2, " ist aufm Trip!");
		addTranslatedMessage(CustomMessageId.FFA_KILL_NAME_3, " will mehr Blut!");
		addTranslatedMessage(CustomMessageId.FFA_KILL_NAME_4, " ist wie ein verruecktes Monster!");
		addTranslatedMessage(CustomMessageId.FFA_KILL_NAME_5, " alles in Ordnung? ");
		addTranslatedMessage(CustomMessageId.FFA_KILL_NAME_6, " verdammte Kills?");
		
		/**
		 * Exchange Command
		 */
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_ITEM, "Du besitzt nicht genug von dem item: ");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_AP, "Du hast nicht genug AP, du hast nur: ");
		
		/**
		 * Medal Command
		 */
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_SILVER, "Du hast nicht genug Silber Medaillen.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_GOLD, "Du hast nicht genug Gold Medaillen.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_PLATIN, "Du hast nicht genug Platin Medaillen.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_MITHRIL, "Du hast nicht genug Mithril Medaillen.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_AP2, "Du hast nicht genug AP, du brauchst: ");
		addTranslatedMessage(CustomMessageId.EXCHANGE_SILVER, "Du hast [item:186000031] zu [item:186000030] eingetauscht.");
		addTranslatedMessage(CustomMessageId.EXCHANGE_GOLD, "Du hast [item:186000030] zu [item:186000096] eingetauscht.");
		addTranslatedMessage(CustomMessageId.EXCHANGE_PLATIN, "Du hast [item:186000096] zu [item:186000147] eingetauscht.");
		addTranslatedMessage(CustomMessageId.EXCHANGE_MITHRIL, "Du hast [item:186000147] zu [item:186000223] eingetauscht.");
		addTranslatedMessage(CustomMessageId.EX_SILVER_INFO, "\nSyntax: .medal silver - Silber zu Gold eintauschen.");
		addTranslatedMessage(CustomMessageId.EX_GOLD_INFO, "\nSyntax: .medal gold - Gold zu Platin eintauschen.");
		addTranslatedMessage(CustomMessageId.EX_PLATIN_INFO, "\nSyntax: .medal platinum - Platin zu Mithril eintauschen.");
		addTranslatedMessage(CustomMessageId.EX_MITHRIL_INFO, "\nSyntax: .medal mithril - Mithril zu Heldenhafter Mithril eintauschen.");
		
		/**
		 * Dimensional Vortex
		 */
		addTranslatedMessage(CustomMessageId.DIM_VORTEX_SPAWNED_ELYOS, "Der Dimensions Riss wurde geoeffnet fur Elyos!");
		addTranslatedMessage(CustomMessageId.DIM_VORTEX_SPAWNED_ASMO, "Der Dimensions Riss wurde geoeffnet fur Asmo!");
		addTranslatedMessage(CustomMessageId.DIM_VORTEX_DESPAWNED, "Die Dimensionale Belagerung wurde beendet!");
		
		/**
		 * Invasion Rift
		 */
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_MIN_LEVEL, "Dein Level ist zu niedrig.");
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_ELYOS, "Ein Riss fuer Pandaemonium ist in Inggison erschienen");
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_ASMOS, "Ein Riss fuer Sanctum ist in Gelkmaros erschienen");
		
		/**
		 * Legendary Raid Spawn Events
		 */
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_SPAWNED_ASMO, "[Legendary Raid Spawn Event] Ragnarok wurde fuer die Asmodier in Beluslan gespawnt!");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_SPAWNED_ELYOS, "[Legendary Raid Spawn Event] Omega wurde fuer die Elyos in Heiron gespawnt!");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_DESPAWNED_ASMO, "[Legendary Raid Spawn Event] Ragnarok ist verschwunden, niemand hat ihn getoetet!");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_DESPAWNED_ELYOS, "[Legendary Raid Spawn Event] Omega ist verschwunden, niemand hat ihn getoetet!");
		
		/**
		 * HonorItems Command
		 */
		addTranslatedMessage(CustomMessageId.PLATE_ARMOR, "Platten Ruestung");
		addTranslatedMessage(CustomMessageId.LEATHER_ARMOR, "Leder Ruestung");
		addTranslatedMessage(CustomMessageId.CLOTH_ARMOR, "Stoff Ruestung");
		addTranslatedMessage(CustomMessageId.CHAIN_ARMOR, "Ketten Ruestung");
		addTranslatedMessage(CustomMessageId.WEAPONS, "Waffen");
		addTranslatedMessage(CustomMessageId.PLATE_ARMOR_PRICES, "Platten Ruestungs Preise");
		addTranslatedMessage(CustomMessageId.LEATHER_ARMOR_PRICES, "Leder Ruestungs Preise");
		addTranslatedMessage(CustomMessageId.CLOTH_ARMOR_PRICES, "Stoff Ruestungs Preise");
		addTranslatedMessage(CustomMessageId.CHAIN_ARMOR_PRICES, "Ketten Ruestungs Preise");
		addTranslatedMessage(CustomMessageId.WEAPONS_PRICES, "Waffen Preise");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_MEDALS, "Du hast nicht genug Medaillen, du brauchst: ");
		addTranslatedMessage(CustomMessageId.PLATE_ARMOR_USE_INFO, "Gebe nun .items und die entsprechende ID ein (Als Beispiel: .items 1");
		addTranslatedMessage(CustomMessageId.LEATHER_ARMOR_USE_INFO, "Gebe nun .items und die entsprechende ID ein (Als Beispiel: .items 6");
		addTranslatedMessage(CustomMessageId.CLOTH_ARMOR_USE_INFO, "Gebe nun .items und die entsprechende ID ein (Als Beispiel: .items 11");
		addTranslatedMessage(CustomMessageId.CHAIN_ARMOR_USE_INFO, "Gebe nun .items und die entsprechende ID ein (Als Beispiel: .items 16");
		addTranslatedMessage(CustomMessageId.WEAPONS_USE_INFO, "Gebe nun .items und die entsprechende ID ein (Als Beispiel: .items 21");
		addTranslatedMessage(CustomMessageId.SUCCESSFULLY_TRADED, "Du hast erfolgreich dein Item erhalten!");
	}
}
