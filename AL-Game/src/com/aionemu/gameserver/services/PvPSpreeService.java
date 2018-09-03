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
package com.aionemu.gameserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Iterator;

import com.aionemu.gameserver.configs.main.PvPConfig;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;

/**
 * @author Maestros
 */

public class PvPSpreeService {

	private static final Logger log = LoggerFactory.getLogger("PVP_LOG");
	private static final String STRING_SPREE1 = LanguageHandler.translate(CustomMessageId.SPREE1);
	private static final String STRING_SPREE2 = LanguageHandler.translate(CustomMessageId.SPREE2);
	private static final String STRING_SPREE3 = LanguageHandler.translate(CustomMessageId.SPREE3);

	public static void increaseRawKillCount(Player winner) {
		int currentRawKillCount = winner.getRawKillCount();
		winner.setRawKillCount(currentRawKillCount + 1);
		int newRawKillCount = currentRawKillCount + 1;
		PacketSendUtility.sendWhiteMessageOnCenter(winner, LanguageHandler.translate(CustomMessageId.KILL_COUNT) + newRawKillCount);

		if ((newRawKillCount == PvPConfig.SPREE_KILL_COUNT) || (newRawKillCount == PvPConfig.RAMPAGE_KILL_COUNT) || (newRawKillCount == PvPConfig.GENOCIDE_KILL_COUNT)) {
			if (newRawKillCount == PvPConfig.SPREE_KILL_COUNT)
				updateSpreeLevel(winner, 1);
			if (newRawKillCount == PvPConfig.RAMPAGE_KILL_COUNT)
				updateSpreeLevel(winner, 2);
			if (newRawKillCount == PvPConfig.GENOCIDE_KILL_COUNT)
				updateSpreeLevel(winner, 3);
		}
	}

	private static void updateSpreeLevel(Player winner, int level) {
		winner.setSpreeLevel(level);
		sendUpdateSpreeMessage(winner, level);
	}

	private static void sendUpdateSpreeMessage(Player winner, int level) {
		Iterator<Player> iter = World.getInstance().getPlayersIterator();
		for (Player p : World.getInstance().getAllPlayers()) {
			if (level == 1)
				PacketSendUtility.sendBrightYellowMessageOnCenter(p, winner.getName() + LanguageHandler.translate(CustomMessageId.CUSTOM_MSG1) + winner.getCommonData().getRace().toString().toLowerCase() + LanguageHandler.translate(CustomMessageId.MSG_SPREE1) + STRING_SPREE1 + LanguageHandler.translate(CustomMessageId.MSG_SPREE1_1));
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), winner.getName() + LanguageHandler.translate(CustomMessageId.CUSTOM_MSG1) + winner.getCommonData().getRace().toString().toLowerCase() + LanguageHandler.translate(CustomMessageId.MSG_SPREE1) + STRING_SPREE1 + LanguageHandler.translate(CustomMessageId.MSG_SPREE1_1));
					}
					ItemService.addItem(winner, 186000147, 5);
			if (level == 2)
				PacketSendUtility.sendBrightYellowMessageOnCenter(p, winner.getName() + LanguageHandler.translate(CustomMessageId.CUSTOM_MSG1) + winner.getCommonData().getRace().toString().toLowerCase() + LanguageHandler.translate(CustomMessageId.MSG_SPREE2) + STRING_SPREE2 + LanguageHandler.translate(CustomMessageId.MSG_SPREE2_1));
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), winner.getName() + LanguageHandler.translate(CustomMessageId.CUSTOM_MSG1) + winner.getCommonData().getRace().toString().toLowerCase() + LanguageHandler.translate(CustomMessageId.MSG_SPREE2) + STRING_SPREE2 + LanguageHandler.translate(CustomMessageId.MSG_SPREE2_1));
					}
					ItemService.addItem(winner, 186000147, 5);
			if (level == 3)
				PacketSendUtility.sendBrightYellowMessageOnCenter(p, winner.getName() + LanguageHandler.translate(CustomMessageId.CUSTOM_MSG1) + winner.getCommonData().getRace().toString().toLowerCase() + LanguageHandler.translate(CustomMessageId.MSG_SPREE3) + STRING_SPREE3
					+ LanguageHandler.translate(CustomMessageId.MSG_SPREE3_1));
					while (iter.hasNext()) {
						PacketSendUtility.sendBrightYellowMessage(iter.next(), winner.getName() + LanguageHandler.translate(CustomMessageId.CUSTOM_MSG1) + winner.getCommonData().getRace().toString().toLowerCase() + LanguageHandler.translate(CustomMessageId.MSG_SPREE3) + STRING_SPREE3
					+ LanguageHandler.translate(CustomMessageId.MSG_SPREE3_1));
					}
					ItemService.addItem(winner, 186000147, 5);
		}
		log.info("[PvP][Spree] {Player : " + winner.getName() + "} have now " + level + " Killing Spree Level");
	}

	public static void cancelSpree(Player victim, Creature killer, boolean isPvPDeath) {
		int killsBeforeDeath = victim.getRawKillCount();
		victim.setRawKillCount(0);
		if (victim.getSpreeLevel() > 0) {
			victim.setSpreeLevel(0);
			sendEndSpreeMessage(victim, killer, isPvPDeath, killsBeforeDeath);
		}
	}

	private static void sendEndSpreeMessage(Player victim, Creature killer, boolean isPvPDeath, int killsBeforeDeath) {
		String spreeEnder = isPvPDeath ? ((Player) killer).getName() : LanguageHandler.translate(CustomMessageId.SPREE_MONSTER_MSG);
		for (Player p : World.getInstance().getAllPlayers()) {
			PacketSendUtility.sendWhiteMessageOnCenter(p, LanguageHandler.translate(CustomMessageId.SPREE_END_MSG1) + victim.getName() + LanguageHandler.translate(CustomMessageId.SPREE_END_MSG2) + spreeEnder + LanguageHandler.translate(CustomMessageId.SPREE_END_MSG3) + killsBeforeDeath + LanguageHandler.translate(CustomMessageId.SPREE_END_MSG4));
		}
		log.info("[PvP][Spree] {The Spree from " + victim.getName() + "} was ended by " + spreeEnder + "}");
	}
}
