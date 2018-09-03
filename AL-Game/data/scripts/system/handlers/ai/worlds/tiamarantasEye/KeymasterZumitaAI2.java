/*
 * This file is part of NextGenCore <Ver:3.9>.
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
package ai.worlds.tiamarantasEye;

import ai.AggressiveNpcAI2;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;

/**
 * @author Maestross
 */
@AIName("keyzumita") //219192
public class KeymasterZumitaAI2 extends AggressiveNpcAI2 {

    @Override
    public void handleSpawned() {
        super.handleSpawned();
        if (!isAlreadyDead()) {
            AI2Actions.useSkill(KeymasterZumitaAI2.this, 19493);
        }
    }

    @Override
    protected void handleAttack(Creature creature) {
        super.handleAttack(creature);
        getEffectController().removeEffect(19493);
    }
}
