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
package ai.instance.kromedesTrial;

import ai.HomingNpcAI2;

import com.aionemu.gameserver.ai2.manager.SkillAttackManager;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AttackIntention;
import com.aionemu.gameserver.model.skill.NpcSkillEntry;

/**
 * @author Rolandas, Eloann
 */
@AIName("rotan")
public class SilverBladeRotanAI2 extends HomingNpcAI2 {

    @Override
    public void think() {
    }

    @Override
    public AttackIntention chooseAttackIntention() {
        NpcSkillEntry skill = SkillAttackManager.chooseNextSkill(this);
        if (skill != null) {
            skillId = skill.getSkillId();
            skillLevel = skill.getSkillLevel();
            return AttackIntention.SKILL_ATTACK;
        }
        return AttackIntention.SIMPLE_ATTACK;
    }
}
