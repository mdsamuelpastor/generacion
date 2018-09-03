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

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.drop.Drop;
import com.aionemu.gameserver.model.drop.DropGroup;
import com.aionemu.gameserver.model.drop.NpcDrop;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;

/**
 * @author Oliver
 */
public class DropInfo extends ChatCommand {

	public DropInfo() {
		super("dropinfo");
	}
	
	@Override
	public void execute(Player player, String... params) {
		NpcDrop npcDrop = null;
		if (params.length > 0) {
			int npcId = Integer.parseInt(params[0]);
			NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(npcId);
			if (npcTemplate == null) {
				PacketSendUtility.sendMessage(player, "Incorrect npcId: " + npcId);
				return;
			}
			npcDrop = npcTemplate.getNpcDrop();
		}
		else {
			VisibleObject visibleObject = player.getTarget();

			if (visibleObject == null) {
				PacketSendUtility.sendMessage(player, "Du musst dabei ein Mob im Target haben !");
				return;
			}

			if (visibleObject instanceof Npc) {
				npcDrop = ((Npc) visibleObject).getNpcDrop();
			}
		}
		if (npcDrop == null) {
			PacketSendUtility.sendMessage(player, "==========================");
			Npc npc = (Npc) player.getTarget();
			PacketSendUtility.sendMessage(player, "Der Mob mit der ID :" + " "  + npc.getNpcId() + " hat keine Drops");
			PacketSendUtility.sendMessage(player, "Bitte melde dies bei uns im Forum");
			PacketSendUtility.sendMessage(player, "www.celestialaion.com");
			PacketSendUtility.sendMessage(player, "==========================");
			return;
		}

		int count = 0;
		PacketSendUtility.sendMessage(player, "[Mob Drops]\n");
		for (DropGroup dropGroup : npcDrop.getDropGroup()) {
			PacketSendUtility.sendMessage(player, "DropGroup: " + dropGroup.getGroupName());
			for (Drop drop : dropGroup.getDrop()) {
				PacketSendUtility.sendMessage(player, "[item:" + drop.getItemId() + "]" + "	Rate: " + drop.getChance());
				count++;
			}
		}

		PacketSendUtility.sendMessage(player, "Es sind " + count + " Drops in diesem Mob vorhanden.");
		Npc npc = (Npc) player.getTarget();		
		PacketSendUtility.sendMessage(player, "NpcId :" + " "  + npc.getNpcId());
	}
		
	@Override
	public void onFail(Player player, String message) {
		// TODO Auto-generated method stub
	}
}
