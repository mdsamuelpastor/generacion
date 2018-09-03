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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Eloann
 */
@AIName("summoned_lapilima")
public class LapilimaAI2 extends NpcAI2 {

    @Override
    protected void handleSpawned() {
        super.handleSpawned();
        Npc yameness = getPosition().getWorldMapInstance().getNpc(216952);
        Npc yamennesHard = getPosition().getWorldMapInstance().getNpc(216960);
        final Npc lapilima = getPosition().getWorldMapInstance().getNpc(281904);

        if (lapilima != null && !lapilima.getLifeStats().isAlreadyDead()) {
            if (yameness != null) {
                AI2Actions.targetCreature(LapilimaAI2.this, getPosition().getWorldMapInstance().getNpc(216952));
                getMoveController().moveToTargetObject();
            }
            if (yamennesHard != null) {
                AI2Actions.targetCreature(LapilimaAI2.this, getPosition().getWorldMapInstance().getNpc(216960));
                getMoveController().moveToTargetObject();
            }
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    applyEffect(19257);
                    applyEffect(19281);
                }
            }, 30000);

        }
    }

    private void sendMessage() {
        getKnownList().doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
                if (player.isOnline()) {
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400730));
                }
            }
        });

    }

    private void scheduleHeal() {
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                applyEffect(19257);
                applyEffect(19281);
            }
        }, 30000);
    }

    @Override
    protected void handleDied() {
        super.handleDied();
        getOwner().getController().onDespawn();
    }

    private void applyEffect(int skillId) {
        final Npc lapilima = getPosition().getWorldMapInstance().getNpc(281904);
        if (lapilima.getLifeStats().isAlreadyDead() || lapilima == null) {
            return;
        }
        Npc yameness = getPosition().getWorldMapInstance().getNpc(216952);
        Npc yamennesHard = getPosition().getWorldMapInstance().getNpc(216960);
        if (yameness != null || yamennesHard != null) {
            SkillTemplate st = DataManager.SKILL_DATA.getSkillTemplate(skillId);
            Effect e = new Effect(yameness, yameness, st, 1, 0);
            e.initialize();
            e.applyEffect();
            sendMessage();
            getOwner().getController().onStopMove();

        }
        scheduleHeal();
    }
}
