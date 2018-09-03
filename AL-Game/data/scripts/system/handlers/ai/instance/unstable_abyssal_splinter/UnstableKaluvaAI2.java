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

import ai.SummonerAI2;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.manager.EmoteManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.ai.Percentage;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Eloann
 */
@AIName("unstable_kaluva")
public class UnstableKaluvaAI2 extends SummonerAI2 {

    private boolean canThink = true;

    @Override
    protected void handleIndividualSpawnedSummons(Percentage percent) {
        spawn();
        canThink = false;
        EmoteManager.emoteStopAttacking(getOwner());
        setStateIfNot(AIState.FOLLOWING);
        getOwner().setState(1);
        PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
        AI2Actions.targetCreature(this, getPosition().getWorldMapInstance().getNpc(219952));
        getMoveController().moveToTargetObject();
    }

    @Override
    protected void handleMoveArrived() {
        if (canThink == false) {
            Npc egg = getPosition().getWorldMapInstance().getNpc(219952);
            if (egg != null) {
                SkillEngine.getInstance().getSkill(getOwner(), 19223, 60, egg).useNoAnimationSkill();
            }

            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    canThink = true;
                    Creature creature = getAggroList().getMostHated();
                    if (creature == null || !getOwner().canSee(creature) || CreatureActions.isAlreadyDead(creature)) {
                        setStateIfNot(AIState.FIGHT);
                        think();
                    } else {
                        getOwner().setTarget(creature);
                        getOwner().getGameStats().renewLastAttackTime();
                        getOwner().getGameStats().renewLastAttackedTime();
                        getOwner().getGameStats().renewLastChangeTargetTime();
                        getOwner().getGameStats().renewLastSkillTime();
                        setStateIfNot(AIState.FIGHT);
                        think();
                    }
                }
            }, 2000);
        }
        super.handleMoveArrived();
    }

    private void spawn() {
        switch (Rnd.get(1, 4)) {
            case 1:
                spawn(219952, 611f, 539f, 425f, (byte) 0); // unstable kaluva's spawner
                break;
            case 2:
                spawn(219969, 628f, 585f, 425f, (byte) 0); // unstable kaluva's spawner
                break;
            case 3:
                spawn(219970, 663f, 556f, 425f, (byte) 0); // unstable kaluva's spawner
                break;
            case 4:
                spawn(219971, 644f, 524f, 425f, (byte) 0); // unstable kaluva's spawner
                break;
        }
    }

    @Override
    public boolean canThink() {
        return canThink;
    }
}
