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
package admincommands;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ItemId;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
import com.aionemu.gameserver.world.World;

public class LevelUp extends ChatCommand {

	public LevelUp() {
		super("levelup");
	}

	@SuppressWarnings("unused")
	@Override
	public void execute(Player admin, String... params) {
		if (admin.getAccessLevel() < AdminConfig.COMMAND_LEVEL_UP) {
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
			return;
		}

		if (params.length == 0 || params.length > 1) {
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADD_SYNTAX));
			return;
		}

		Player receiver = null;

		receiver = World.getInstance().findPlayer(Util.convertName(params[0]));
		PlayerClass playerClass = receiver.getPlayerClass();

		PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.SOMETHING_WRONG_HAPPENED));

		if (receiver == null) {
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.PLAYER_NOT_ONLINE, Util.convertName(params[0])));
			return;
		}

		receiver.getCommonData().setLevel(50);
		ItemService.addItem(receiver, ItemId.KINAH.value(), 5000000);
		
		
		if(playerClass == PlayerClass.GLADIATOR) {
			// Ruestung
			ItemService.addItem(receiver, 110600871, 1);
			ItemService.addItem(receiver, 113600836, 1);
			ItemService.addItem(receiver, 112600821, 1);
			ItemService.addItem(receiver, 111600847, 1);
			ItemService.addItem(receiver, 114600830, 1);
			ItemService.addItem(receiver, 100900812, 1); // Gro?schwert
			ItemService.addItem(receiver, 101300567, 1); // speer
			ItemService.addItem(receiver, 101700854, 1); // Bogen
			ItemService.addItem(receiver, 123000751, 1);
			ItemService.addItem(receiver, 121000636, 1);
			ItemService.addItem(receiver, 120000699, 2);
			ItemService.addItem(receiver, 122000748, 2);
			ItemService.addItem(receiver, 125001487, 1);
			ItemService.addItem(receiver, 187000025, 1); // Fluegel
		}
		else if (playerClass == PlayerClass.TEMPLAR) {
			ItemService.addItem(receiver, 110600871, 1);
			ItemService.addItem(receiver, 113600836, 1);
			ItemService.addItem(receiver, 112600821, 1);
			ItemService.addItem(receiver, 111600847, 1);
			ItemService.addItem(receiver, 114600830, 1);
			ItemService.addItem(receiver, 100900812, 1); // Gro?schwert
			ItemService.addItem(receiver, 100100812, 1); // Kriegshammer
			ItemService.addItem(receiver, 115000719, 1); // Schild
			ItemService.addItem(receiver, 123000751, 1);
			ItemService.addItem(receiver, 121000636, 1);
			ItemService.addItem(receiver, 120000699, 2);
			ItemService.addItem(receiver, 122000748, 2);
			ItemService.addItem(receiver, 125001487, 1);
			ItemService.addItem(receiver, 187000025, 1); // Fluegel
		}
		else if (playerClass == PlayerClass.ASSASSIN) {
			ItemService.addItem(receiver, 110300919, 1);
			ItemService.addItem(receiver, 113300895, 1);
			ItemService.addItem(receiver, 112300819, 1);
			ItemService.addItem(receiver, 111300869, 1);
			ItemService.addItem(receiver, 114300928, 1);
			ItemService.addItem(receiver, 100001072, 1); // Schwert
			ItemService.addItem(receiver, 100200711, 1); // dolch
			ItemService.addItem(receiver, 101700854, 1); // Bogen
			ItemService.addItem(receiver, 123000751, 1);
			ItemService.addItem(receiver, 121000636, 1);
			ItemService.addItem(receiver, 120000699, 2);
			ItemService.addItem(receiver, 122000748, 2);
			ItemService.addItem(receiver, 125001485, 1);
			ItemService.addItem(receiver, 187000025, 1); // Fluegel
		}
		else if (playerClass == PlayerClass.RANGER) {
			ItemService.addItem(receiver, 110300919, 1);
			ItemService.addItem(receiver, 113300895, 1);
			ItemService.addItem(receiver, 112300819, 1);
			ItemService.addItem(receiver, 111300869, 1);
			ItemService.addItem(receiver, 114300928, 1);
			ItemService.addItem(receiver, 100001072, 1); // Schwert
			ItemService.addItem(receiver, 100200711, 1); // dolch
			ItemService.addItem(receiver, 101700854, 1); // Bogen
			ItemService.addItem(receiver, 123000751, 1);
			ItemService.addItem(receiver, 121000636, 1);
			ItemService.addItem(receiver, 120000699, 2);
			ItemService.addItem(receiver, 122000748, 2);
			ItemService.addItem(receiver, 125001485, 1);
			ItemService.addItem(receiver, 187000025, 1); // Fluegel
		}
		else if (playerClass == PlayerClass.SORCERER) {
			ItemService.addItem(receiver, 110100974, 1);
			ItemService.addItem(receiver, 113100878, 1);
			ItemService.addItem(receiver, 112100825, 1);
			ItemService.addItem(receiver, 111100868, 1);
			ItemService.addItem(receiver, 114100903, 1);
			ItemService.addItem(receiver, 100600641, 1); // Buch2
			ItemService.addItem(receiver, 121000635, 1);
			ItemService.addItem(receiver, 122000747, 2);
			ItemService.addItem(receiver, 120000698, 2);
			ItemService.addItem(receiver, 123000752, 1); // Gurtel der Leere
			ItemService.addItem(receiver, 125001484, 1);
			ItemService.addItem(receiver, 187000025, 1); // Fluegel
		}
		else if (playerClass == PlayerClass.SPIRIT_MASTER) {
			ItemService.addItem(receiver, 110100974, 1);
			ItemService.addItem(receiver, 113100878, 1);
			ItemService.addItem(receiver, 112100825, 1);
			ItemService.addItem(receiver, 111100868, 1);
			ItemService.addItem(receiver, 114100903, 1);
			ItemService.addItem(receiver, 100600641, 1); // Buch2
			ItemService.addItem(receiver, 121000635, 1);
			ItemService.addItem(receiver, 122000747, 2);
			ItemService.addItem(receiver, 120000698, 2);
			ItemService.addItem(receiver, 123000752, 1); // Gurtel der Leere
			ItemService.addItem(receiver, 125001484, 1);
			ItemService.addItem(receiver, 187000025, 1); // Fluegel
		}
		else if (playerClass == PlayerClass.CLERIC) {
			ItemService.addItem(receiver, 110500887, 1);
			ItemService.addItem(receiver, 113500861, 1);
			ItemService.addItem(receiver, 112500810, 1);
			ItemService.addItem(receiver, 111500859, 1);
			ItemService.addItem(receiver, 114500871, 1);
			ItemService.addItem(receiver, 100100812, 1); // Kriegshammer
			ItemService.addItem(receiver, 115000719, 1); // Schild
			ItemService.addItem(receiver, 101500608, 1); // Stab
			ItemService.addItem(receiver, 121000635, 1);
			ItemService.addItem(receiver, 122000747, 2);
			ItemService.addItem(receiver, 120000698, 2);
			ItemService.addItem(receiver, 123000752, 1); // Gurtel der Leere
			ItemService.addItem(receiver, 125001486, 1);
			ItemService.addItem(receiver, 187000025, 1); // Fluegel
		}
		else if (playerClass == PlayerClass.CHANTER) {
			ItemService.addItem(receiver, 110500887, 1);
			ItemService.addItem(receiver, 113500861, 1);
			ItemService.addItem(receiver, 112500810, 1);
			ItemService.addItem(receiver, 111500859, 1);
			ItemService.addItem(receiver, 114500871, 1);
			ItemService.addItem(receiver, 100100812, 1); // Kriegshammer
			ItemService.addItem(receiver, 115000719, 1); // Schild
			ItemService.addItem(receiver, 101500608, 1); // Stab
			ItemService.addItem(receiver, 123000751, 1);
			ItemService.addItem(receiver, 121000636, 1);
			ItemService.addItem(receiver, 120000699, 2);
			ItemService.addItem(receiver, 122000748, 2);
			ItemService.addItem(receiver, 125001486, 1);
			ItemService.addItem(receiver, 187000025, 1);
		}
		if (receiver.getRace() == Race.ELYOS) {
			ItemService.addItem(receiver, 190070004, 1); // Kim-Suro-Beschworungslampe (30 Tage)>
			ItemService.addItem(receiver, 110900343, 1); // Gewand des stilbewussten Singles
		}
		 PacketSendUtility.sendMessage(admin, "[Event-Winterzauber] wurde erfolgreich an den Spieler : " + receiver.getName() + " " + "geaddet");
		 PacketSendUtility.sendMessage(admin, "//givemissingskills muss noch auf den Spieler [Target] : " + receiver.getName() + " " + "gemacht werden :)");
		 PacketSendUtility.sendMessage(receiver, "[Event-Winterzauber] erhalten , Have Fun");
	}
}
