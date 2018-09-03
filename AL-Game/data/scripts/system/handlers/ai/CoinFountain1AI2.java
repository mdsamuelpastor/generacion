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

@AIName("coinfountain1")
public class CoinFountain1AI2 extends NpcAI2 {

    @Override
    protected void handleDialogStart(Player player) {
        if (player.getCommonData().getLevel() >= 50 && (player.getRace() == Race.ASMODIANS)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011, 41188));
        } else {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011, 41188));
        }
    }

    public boolean onDialogSelect(Player player, int dialogId, int questId) {
        switch (dialogId) {
            case 10000:
                if (hasItem(player, 186000030) && (player.getRace() == Race.ASMODIANS)) {
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 5, 41188));
                } else {
                	PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 5, 41188));
                }
                
                break;
            case 18:
                Item item = player.getInventory().getFirstItemByItemId(186000030);
                player.getInventory().decreaseByItemId(item.getObjectId(), 1);
                giveItem(player);
                player.getCommonData().addExp(1043900, RewardType.QUEST);
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1008, 41188));
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1008, 41188));
                break;
        }
        return true;
    }

    private boolean hasItem(Player player, int itemId) {
        return player.getInventory().getItemCountByItemId(itemId) > 0;
    }

    private void giveItem(Player player) {
        int rnd = Rnd.get(0, 100);
        if (rnd < 5) {
            ItemService.addItem(player, 186000096, 1);
        } else {
            ItemService.addItem(player, 182005205, 1);
        }
    }
}
