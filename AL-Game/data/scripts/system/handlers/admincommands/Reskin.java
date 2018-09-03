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

import java.util.Iterator;
import java.util.List;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;

/**
 * @author Wakizashi, Imaginary
 */
public class Reskin extends ChatCommand {

	public Reskin() {
		super("reskin");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length != 2) {
			onFail(admin, null);
			return;
		}

		Player target = admin;
		VisibleObject creature = admin.getTarget();
		if (admin.getTarget() instanceof Player) {
			target = (Player) creature;
		}

		int oldItemId = 0;
		int newItemId = 0;
		try {
			oldItemId = Integer.parseInt(params[0]);
			newItemId = Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(admin, "<old item ID> & <new item ID> must be an integer.");
			return;
		}

		List<Item> items = target.getInventory().getItemsByItemId(oldItemId);
		if (items.size() == 0) {
			PacketSendUtility.sendMessage(admin, "Tu n'as pas cet objet dans ton inventaire.");
			return;
		}

		Iterator<Item> iter = items.iterator();
		Item item = iter.next();
		item.setItemSkinTemplate(DataManager.ITEM_DATA.getItemTemplate(newItemId));

		PacketSendUtility.sendMessage(admin, "Reskin Successfull.");
	}

	@Override
	public void onFail(Player admin, String message) {
		PacketSendUtility.sendMessage(admin, "syntax //reskin <Old Item ID> <New Item ID>");
	}
}
