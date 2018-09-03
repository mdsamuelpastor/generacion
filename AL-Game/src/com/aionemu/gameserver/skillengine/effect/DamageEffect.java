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
package com.aionemu.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.controllers.attack.AttackUtil;
import com.aionemu.gameserver.skillengine.action.DamageType;
import com.aionemu.gameserver.skillengine.change.Func;
import com.aionemu.gameserver.skillengine.effect.modifier.ActionModifier;
import com.aionemu.gameserver.skillengine.model.Effect;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DamageEffect")
public abstract class DamageEffect extends EffectTemplate {

    @XmlAttribute
    protected Func mode = Func.ADD;

    @Override
    public void applyEffect(Effect effect) {

        effect.getEffected().getController().onAttack(effect.getEffector(), effect.getSkillId(), effect.getReserved1(), true);
        effect.getEffector().getObserveController().notifyAttackObservers(effect.getEffected());
    }

    public boolean calculate(Effect effect, DamageType damageType) {
        if (!super.calculate(effect, null, null))
            return false;

        int skillLvl = effect.getSkillLevel();
        int valueWithDelta = value + delta * skillLvl;
        ActionModifier modifier = getActionModifiers(effect);
        int accMod = this.accMod2 + this.accMod1 * skillLvl;
        int critAddDmg = critAddDmg2 + critAddDmg1 * skillLvl;

        switch (damageType) {
            case PHYSICAL:
                int rndDmg = (this instanceof SkillAttackInstantEffect ? ((SkillAttackInstantEffect) this).getRnddmg() : 0);
                AttackUtil.calculateSkillResult(effect, valueWithDelta, modifier, getMode(), rndDmg, accMod, critProbMod2, critAddDmg);
                break;
            case MAGICAL:
                boolean useKnowledge = true;
                if (this instanceof ProcAtkInstantEffect) {
                    useKnowledge = false;
                }
                AttackUtil.calculateMagicalSkillResult(effect, valueWithDelta, modifier, getElement(), true, useKnowledge, false, getMode(), critProbMod2, critAddDmg);
                break;
            default:
                AttackUtil.calculateSkillResult(effect, 0, null, getMode(), 0, accMod, 100, 0);
        }

        return true;
    }

    public Func getMode() {
        return mode;
    }

}