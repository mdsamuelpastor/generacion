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
package ai.worlds.theobomos;

import ai.ActionItemNpcAI2;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.Skill;

/**
 * @author Maestross
 * @rework Eloann - retail
 */
@AIName("siegeweaponelyos")
public class SiegeWeaponElyosAI2 extends ActionItemNpcAI2 {

    private int skillid = 20364;
    private int skilllevel = 60;

    @Override
    protected void handleDialogStart(Player player) {
        super.handleUseItemStart(player);
    }

    private void transform(Player player) {
        Skill skill = SkillEngine.getInstance().getSkill(player, skillid, skilllevel, player);
        skill.setTargetType(1, player.getX(), player.getY(), player.getZ());
        skill.useNoAnimationSkill();
    }

    @Override
    protected void handleUseItemFinish(Player player) {
        transform(player); // skill 20364
        AI2Actions.deleteOwner(this);
        AI2Actions.scheduleRespawn(this); // 30 minutes
    }
}