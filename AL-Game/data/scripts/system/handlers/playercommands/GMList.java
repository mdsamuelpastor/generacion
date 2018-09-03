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

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.FriendList;
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
 
public class GMList extends ChatCommand {

	public GMList() {
		super("gmlist");
	}

	@Override
	public void execute(Player player, String... params) {
		final List<Player> admins = new ArrayList<Player>();
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player object) {
				if (object.getAccessLevel() > 0 && object.getFriendList().getStatus() != FriendList.Status.OFFLINE) {
					admins.add(object);
				}
			}
		});

		if (admins.size() > 0) {
			PacketSendUtility.sendMessage(player, "====================");
			if (admins.size() == 1)
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.ONE_GM_ONLINE));
			else
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.MORE_GMS_ONLINE));

			for (Player admin : admins) {

				if (AdminConfig.ADMIN_TAG_ENABLE)

				{
					String gmTag = null;
					if (admin.getAccessLevel() == 1) {
						gmTag = AdminConfig.ADMIN_TAG_1.trim();
					}
					else if (admin.getAccessLevel() == 2) {
						gmTag = AdminConfig.ADMIN_TAG_2.trim();
					}
					else if (admin.getAccessLevel() == 3) {
						gmTag = AdminConfig.ADMIN_TAG_3.trim();
					}
					else if (admin.getAccessLevel() == 4) {
						gmTag = AdminConfig.ADMIN_TAG_4.trim();
					}
					else if (admin.getAccessLevel() == 5) {
						gmTag = AdminConfig.ADMIN_TAG_5.trim();
					}
					else if (admin.getAccessLevel() == 6) {
						gmTag = AdminConfig.ADMIN_TAG_6.trim();
					}
					else if (admin.getAccessLevel() == 7) {
						gmTag = AdminConfig.ADMIN_TAG_7.trim();
					}
					else if (admin.getAccessLevel() == 8) {
						gmTag = AdminConfig.ADMIN_TAG_8.trim();
					}
					else if (admin.getAccessLevel() == 9) {
						gmTag = AdminConfig.ADMIN_TAG_9.trim();
					}
					else if (admin.getAccessLevel() == 10) {
						gmTag = AdminConfig.ADMIN_TAG_10.trim();
					}
					PacketSendUtility.sendMessage(player, String.format(gmTag + "" + admin.getName()));
				}
			}
			PacketSendUtility.sendMessage(player, "====================");

		}
		else {
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.NO_GM_ONLINE));
		}

	}
}
