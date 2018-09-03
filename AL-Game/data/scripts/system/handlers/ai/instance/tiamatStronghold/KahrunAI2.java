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
package ai.instance.tiamatStronghold;

import com.aionemu.gameserver.ai2.*;
import com.aionemu.gameserver.model.*;
import com.aionemu.gameserver.model.gameobjects.player.*;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.utils.*;

/**
 * @author Madmax/Mogli, Maestross
 */
@AIName("kahrun")
public class KahrunAI2 extends NpcAI2 {

    @Override
    protected void handleDialogStart(Player player) {
        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
    }

    @Override
    public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
        if (dialogId == 10000) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
            switch (getNpcId()) {
                case 800456: //Kahrun (Reian Leader).
                    PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "<PREPARE YOU FIGHT>", ChatType.BRIGHT_YELLOW_CENTER), true);
                    spawn(219417, 1560.08f, 1061.26f, 492.16f, (byte) 58); //Protectorate Elite Fighter.
                    spawn(219417, 1559.88f, 1075.19f, 492.16f, (byte) 60); //Protectorate Elite Fighter.
                    spawn(219417, 1554.85f, 1076.33f, 492.16f, (byte) 98); //Protectorate Elite Fighter.
                    spawn(219417, 1553.87f, 1062.09f, 492.16f, (byte) 113); //Protectorate Elite Fighter.
                    spawn(800335, 1556.74f, 1069.26f, 492.16f, (byte) 0); //Kahrun (Reian Leader).
                    break;
            }
            AI2Actions.deleteOwner(this);
        }
        return true;
    }
}