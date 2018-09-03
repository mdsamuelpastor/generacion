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
package ai.instance.unstable_abyssal_splinter;

import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author Eloann
 */
@AIName("unstable_kaluvaspawn")
public class UnstableKaluvaSpawnAI2 extends NpcAI2 {

    private Future<?> task;

    @Override
    protected void handleDied() {
        super.handleDied();
        if (task != null && !task.isDone()) {
            task.cancel(true);
        }
        checkKaluva();
    }

    @Override
    protected void handleSpawned() {
        super.handleSpawned();
        scheduleHatch();
    }

    private void checkKaluva() {
        Npc kaluva = getPosition().getWorldMapInstance().getNpc(219941);
        if (kaluva != null && !kaluva.getLifeStats().isAlreadyDead()) {
            kaluva.getEffectController().removeEffect(19152);
        }
        AI2Actions.deleteOwner(this);
    }

    private void scheduleHatch() {
        task = ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!isAlreadyDead()) {
                    hatchAdds();
                    checkKaluva();
                }
            }
        }, 22000); // schedule hatch when debuff ends(20s)
    }

    private void hatchAdds() { // 4 different spawn-formations;
        WorldPosition p = getPosition();
        switch (Rnd.get(1, 4)) {
            case 1:
                spawn(219960, p.getX(), p.getY(), p.getZ(), p.getHeading()); // kaluva's unstable limbs
                spawn(219960, p.getX(), p.getY(), p.getZ(), p.getHeading()); // kaluva's unstable limbs
                break;
            case 2:
                for (int i = 0; i < 12; i++) {
                    spawn(219960, p.getX(), p.getY(), p.getZ(), p.getHeading()); // kaluva's unstable debris
                }
                break;
            case 3:
                spawn(219972, p.getX(), p.getY(), p.getZ(), p.getHeading()); // kaluva's unstable underling
                break;
            case 4:
                spawn(219960, p.getX(), p.getY(), p.getZ(), p.getHeading()); // kaluva's unstable limbs
                spawn(219961, p.getX(), p.getY(), p.getZ(), p.getHeading()); // kaluva's unstable debris
                spawn(219960, p.getX(), p.getY(), p.getZ(), p.getHeading()); // kaluva's unstable debris
                spawn(219960, p.getX(), p.getY(), p.getZ(), p.getHeading()); // kaluva's unstable debris
                break;
        }
    }
}
