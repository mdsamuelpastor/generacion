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
package ai;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;


/**
 * @author Maestros
 */

@AIName("coinfountain4")
public class CoinFountain4AI2 extends NpcAI2 {

    @Override
    protected void handleDialogStart(Player player) {
        if (player.getCommonData().getLevel() >= 58) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
        } else {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
        }
    }



    public boolean onDialogSelect(Player player, int dialogId, int questId) {
        switch (dialogId) {
            case 10000:
                if (hasItem(player, 186000096) && hasItem(player, 186000174) && (player.getRace() == Race.ASMODIANS)) {
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 5, 41598));
                } else {
                	PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 5, 41598));
                }

                break;

            case 26:
		Item item1 = player.getInventory().getFirstItemByItemId(186000174);
                Item item = player.getInventory().getFirstItemByItemId(186000096);
                player.getInventory().decreaseByItemId(item.getObjectId(), 1);
		player.getInventory().decreaseByItemId(item1.getObjectId(), 1);
                giveItem(player);
                player.getCommonData().addExp(1043900, RewardType.QUEST);
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1008, 41598));
		        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1008, 41598));
                break;
        }
        return true;
    }

    private boolean hasItem(Player player, int itemId) {
        return player.getInventory().getItemCountByItemId(itemId) > 0;
    }

    private void giveItem(Player player) {
        int rnd = Rnd.get(0, 100);
        if (rnd < 10) {
            ItemService.addItem(player, 186000147, 1);
		}else if (rnd < 2) {
		    ItemService.addItem(player, 186000096, 1);

        }
    }
}
