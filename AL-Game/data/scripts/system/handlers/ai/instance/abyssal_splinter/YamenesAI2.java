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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.knownlist.Visitor;

import ai.AggressiveNpcAI2;

/**
 * @author Eloann
 */
@AIName("yamennes")
public class YamenesAI2 extends AggressiveNpcAI2 {

    @Override
    protected void handleAttack(Creature creature) {

        super.handleAttack(creature);

        if (Rnd.get(1, 100) < 3) {
            spawnPortal();
        }
    }

    private void spawnPortal() {
        Npc portalA = getPosition().getWorldMapInstance().getNpc(282014);
        Npc portalB = getPosition().getWorldMapInstance().getNpc(282015);
        Npc portalC = getPosition().getWorldMapInstance().getNpc(282131);
        if (portalA == null && portalB == null && portalC == null) {
            int random = Rnd.get(1, 2);
            if (random == 1) {
                sendMessage();
                spawn(282014, 288.10f, 741.95f, 216.81f, (byte) 3);
                spawn(282015, 375.05f, 750.67f, 216.82f, (byte) 59);
                spawn(282131, 341.33f, 699.38f, 216.86f, (byte) 59);
            } else {
                sendMessage();
                spawn(282014, 303.69f, 736.35f, 198.7f, (byte) 0);
                spawn(282015, 335.19f, 708.92f, 198.9f, (byte) 35);
                spawn(282131, 360.23f, 741.07f, 198.7f, (byte) 0);
            }
        }
    }

    private void sendMessage() {
        getKnownList().doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
                if (player.isOnline()) {
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400637));
                }
            }
        });

    }
}
