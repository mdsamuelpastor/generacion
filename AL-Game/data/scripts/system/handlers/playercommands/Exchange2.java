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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;

/**
 * @author Maestross
 */
public class Exchange2 extends ChatCommand {

    public Exchange2() {
        super("ex");

    }

    @Override
    public void execute(Player player, String... params) {
        int ap = 15000;
		int derived = 186000147;
		int derived_q = 5;
        if (player.getAbyssRank().getAp() < ap) {
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.NOT_ENOUGH_AP) + ap);
            return;
        }
        ItemService.addItem(player, derived, derived_q);
        AbyssPointsService.addAp(player, -ap);

    }
}
