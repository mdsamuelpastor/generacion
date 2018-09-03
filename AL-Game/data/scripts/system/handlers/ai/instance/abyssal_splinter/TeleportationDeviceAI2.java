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
package ai.instance.abyssal_splinter;

import ai.ActionItemNpcAI2;


import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.teleport.TeleportService2;

/**
 * @author Eloann
 */
@AIName("teleportation_device")
public class TeleportationDeviceAI2 extends ActionItemNpcAI2 {

    @Override
    protected void handleUseItemFinish(Player player) {
        switch (getNpcId()) {
            case 281905:
                TeleportService2.teleportTo(player, 300220000, 288.10f, 741.95f, 215.81f, (byte) 3);
                break;
            case 282038:
                TeleportService2.teleportTo(player, 300220000, 341.33f, 699.38f, 215.86f, (byte) 59);
                break;
            case 282039:
                TeleportService2.teleportTo(player, 300220000, 375.05f, 750.67f, 215.82f, (byte) 59);
                break;
        }
    }
}
