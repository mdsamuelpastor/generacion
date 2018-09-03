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
package ai. instance.lowerUdasTemple;

import ai.AggressiveNpcAI2;
import com.aionemu.gameserver.ai2.*;
import com.aionemu.gameserver.ai2.event.*;
import com.aionemu.gameserver.ai2.manager.*;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.skill.NpcSkillEntry;

@AIName("churatwinblade")
public class ChuraTwinbladeAI2 extends AggressiveNpcAI2
{
	@Override
    protected void handleAttack(Creature creature) {
        super.handleAttack(creature);
        checkPercentage(getLifeStats().getHpPercentage());
    }
	
	private void checkPercentage(int hpPercentage) {
        if (hpPercentage <=33) {
        	AI2Actions.useSkill(this, 18624); //Stigma Burst.
        }
    }
	
	@Override
    protected void handleBackHome() {
        super.handleBackHome();
    }
    
    @Override
    public AttackIntention chooseAttackIntention() {
        VisibleObject currentTarget = getTarget();
        Creature mostHated = getAggroList().getMostHated();
        if (mostHated == null || mostHated.getLifeStats().isAlreadyDead()) {
            return AttackIntention.FINISH_ATTACK;
        }
        if (currentTarget == null || !currentTarget.getObjectId().equals(mostHated.getObjectId())) {
            onCreatureEvent(AIEventType.TARGET_CHANGED, mostHated);
            return AttackIntention.SWITCH_TARGET;
        }
        NpcSkillEntry skill = SkillAttackManager.chooseNextSkill(this);
        if (skill != null) {
            skillId = skill.getSkillId();
            skillLevel = skill.getSkillLevel();
            return AttackIntention.SKILL_ATTACK;
        }
        return AttackIntention.SIMPLE_ATTACK;
    }
}