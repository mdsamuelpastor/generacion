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
package ai.instance.elementisForest;

import java.util.concurrent.atomic.AtomicBoolean;

import ai.FollowingNpcAI2;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIAnswers;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author Luzien
 */
@AIName("terma")
public class TrappedTermaAI2 extends FollowingNpcAI2 {

	private AtomicBoolean spawned = new AtomicBoolean(false);

	@Override
	public void handleAttack(Creature creature) {
		if (getOwner().getTarget() != creature && getState() != AIState.RETURNING) {
			handleFollowMe(creature);
			getOwner().setTarget(creature);
			getMoveController().moveToTargetObject();
		}
		super.handleAttack(creature);
	}

	@Override
	public void handleMoveArrived() {
		super.handleMoveArrived();
		if (getState().equals(AIState.RETURNING))
			return;
		WorldPosition p = getPosition();
		if (p.getWorldMapInstance() != null) {
			if (MathUtil.isIn3dRange(p.getX(), p.getY(), p.getZ(), 453.774f, 536.489f, 131.693f, 8)) {
				if (spawned.compareAndSet(false, true)) {
					AI2Actions.dieSilently(this, getOwner());
					Npc freeTerma = (Npc) spawn(205495, 453.774f, 536.489f, 131.693f, (byte) 0);
					spawn(701009, 451.706f, 534.313f, 131.979f, (byte) 0);
					NpcShoutsService.getInstance().sendMsg(freeTerma, 1500444, freeTerma.getObjectId(), 0, 3000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							AI2Actions.deleteOwner(TrappedTermaAI2.this);
						}

					}, 3000);

				}
			}
			else if (!MathUtil.isIn3dRange(p.getX(), p.getY(), p.getZ(), getOwner().getSpawn().getX(), getOwner().getSpawn().getY(), getOwner().getSpawn().getZ(), 150)) {
				setStateIfNot(AIState.RETURNING);
				getOwner().setTarget(null);
				onGeneralEvent(AIEventType.NOT_AT_HOME);
			}
			else if (Rnd.get(100) < 5) {
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1500441 + Rnd.get(3), getOwner().getObjectId(), 0, 0);
			}
		}
	}

	@Override
	public boolean canThink() {
		return false;
	}

	@Override
	public int modifyDamage(int damage) {
		return 1;
	}

	@Override
	protected AIAnswer pollInstance(AIQuestion question) {
		switch (question) {
			case SHOULD_DECAY:
				return AIAnswers.NEGATIVE;
			case SHOULD_RESPAWN:
				return AIAnswers.NEGATIVE;
			case SHOULD_REWARD:
				return AIAnswers.NEGATIVE;
			default:
				return null;
		}
	}

}
