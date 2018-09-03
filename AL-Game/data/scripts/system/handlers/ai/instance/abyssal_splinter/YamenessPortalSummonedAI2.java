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

import ai.AggressiveNpcAI2;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Eloann
 */
@AIName("yameness_portal")
public class YamenessPortalSummonedAI2 extends AggressiveNpcAI2 {

    @Override
    protected void handleSpawned() {

        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                spawnSummons();
            }
        }, 10000);
    }

    @Override
    protected void handleDied() {
        super.handleDied();
        getOwner().getController().onDespawn();
    }

    private void spawnSummons() {
        Npc portalA = getPosition().getWorldMapInstance().getNpc(282014);
        Npc portalB = getPosition().getWorldMapInstance().getNpc(282015);
        Npc portalC = getPosition().getWorldMapInstance().getNpc(282131);
        Npc orkanimum = getPosition().getWorldMapInstance().getNpc(281903);
        Npc lapilima = getPosition().getWorldMapInstance().getNpc(281904);
        if (orkanimum == null && lapilima == null) {
            if (portalA != null && !portalA.getLifeStats().isAlreadyDead() && portalB != null
                    && !portalB.getLifeStats().isAlreadyDead() && portalC != null && !portalC.getLifeStats().isAlreadyDead()) {
                spawn(281903, portalA.getX() + 3, portalA.getY() - 3, portalA.getZ() + 2, (byte) 0);
                spawn(281904, portalA.getX() - 3, portalA.getY() + 3, portalA.getZ() + 2, (byte) 0);
                spawn(281903, portalB.getX() + 3, portalB.getY() - 3, portalB.getZ() + 2, (byte) 0);
                spawn(281904, portalB.getX() - 3, portalB.getY() + 3, portalB.getZ() + 2, (byte) 0);
                spawn(281903, portalC.getX() + 3, portalC.getY() - 3, portalC.getZ() + 2, (byte) 0);
                spawn(281904, portalC.getX() - 3, portalC.getY() + 3, portalC.getZ() + 2, (byte) 0);
            }
        }
        scheduleRespawn();
    }

    private void scheduleRespawn() {
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                spawnSummons();
            }
        }, 60000);
    }
}
