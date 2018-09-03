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
package playercommands;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;

/**
 * @author Maestros
 */
public class ElyChannel extends ChatCommand {

    public ElyChannel() {
        super("ely");
    }

    @Override
    public void execute(Player player, String... params) {
        if (player.getRace() == Race.ELYOS && !player.isInPrison()) {
            int i = 1;
            boolean check = true;
            Race adminRace = player.getRace();
            String adminname = "";

            if (params.length < 1) {
                PacketSendUtility.sendMessage(player, "syntax : .ely <message>");
                return;
            }

            if (AdminConfig.ADMIN_TAG_ENABLE) {
                if (player.getAccessLevel() == 1) {
                    adminname += "\uE042" + AdminConfig.ADMIN_TAG_1.trim() + "\uE043";
                } else if (player.getAccessLevel() == 2) {
                    adminname += "\uE042" + AdminConfig.ADMIN_TAG_2.trim() + "\uE043";
                } else if (player.getAccessLevel() == 3) {
                    adminname += "\uE042" + AdminConfig.ADMIN_TAG_3.trim() + "\uE043";
                } else if (player.getAccessLevel() == 4) {
                    adminname += "\uE042" + AdminConfig.ADMIN_TAG_4.trim() + "\uE043";
                } else if (player.getAccessLevel() == 5) {
                    adminname += "\uE042" + AdminConfig.ADMIN_TAG_5.trim() + "\uE043";
                } else if (player.getAccessLevel() == 6) {
                    adminname += "\uE042" + AdminConfig.ADMIN_TAG_6.trim() + "\uE043";
                } else if (player.getAccessLevel() == 7) {
                    adminname += "\uE042" + AdminConfig.ADMIN_TAG_7.trim() + "\uE043";
                } else if (player.getAccessLevel() == 8) {
                    adminname += "\uE042" + AdminConfig.ADMIN_TAG_8.trim() + "\uE043";
                } else if (player.getAccessLevel() == 9) {
                    adminname += "\uE042" + AdminConfig.ADMIN_TAG_9.trim() + "\uE043";
                } else if (player.getAccessLevel() == 10) {
                    adminname += "\uE042" + AdminConfig.ADMIN_TAG_10.trim() + "\uE043";
                }
            }

            adminname += player.getName() + ": ";

            StringBuilder sbMessage;
            if (player.isGM()) {
                sbMessage = new StringBuilder("[Elyos]" + " " + adminname);
            } else {
                sbMessage = new StringBuilder("[Elyos]" + " " + player.getName() + ": ");
            }
            adminRace = Race.ELYOS;

            for (String s : params) {
                if (i++ != 0 && (check)) {
                    sbMessage.append(s + " ");
                }
            }

            String message = sbMessage.toString().trim();
            int messageLenght = message.length();

            final String sMessage = message.substring(0, CustomConfig.MAX_CHAT_TEXT_LENGHT > messageLenght ? messageLenght : CustomConfig.MAX_CHAT_TEXT_LENGHT);
            final boolean toAll = params[0].equals("ALL");
            final Race race = adminRace;

            World.getInstance().doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player player) {
                    if (toAll || player.getRace() == race || (player.getAccessLevel() > 0)) {
                        PacketSendUtility.sendMessage(player, sMessage);
                    }
                }
            });
        } else {
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.ELY_FAIL));
        }
    }

    @Override
    public void onFail(Player player, String message) {
        PacketSendUtility.sendMessage(player, "syntax : .ely <message>");
    }
}
