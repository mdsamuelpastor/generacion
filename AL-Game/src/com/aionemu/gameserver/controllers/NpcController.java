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
package com.aionemu.gameserver.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.controllers.attack.AggroInfo;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.gameobjects.siege.SiegeNpc;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.common.service.PlayerTeamDistributionService;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.LOG;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.CustomBosses.BossSpawnService;
import com.aionemu.gameserver.services.CustomBosses.CustomBossSpawn;
import com.aionemu.gameserver.services.DialogService;
import com.aionemu.gameserver.services.RespawnService;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.drop.DropService;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.stats.StatFunctions;
import com.aionemu.gameserver.world.zone.ZoneInstance;

/**
 * This class is for controlling Npc's
 * 
 * @author -Nemesiss-, ATracer (2009-09-29), Sarynth modified by Wakizashi, Maestross
 */

public class NpcController extends CreatureController<Npc> {

	private static final Logger log = LoggerFactory.getLogger(NpcController.class);

	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange) {
		super.notSee(object, isOutOfRange);
		if (object instanceof Creature) {
			getOwner().getAi2().onCreatureEvent(AIEventType.CREATURE_NOT_SEE, (Creature) object);
			getOwner().getAggroList().remove((Creature) object);
		}
	}

	@Override
	public void see(VisibleObject object) {
		super.see(object);
		Npc owner = getOwner();
		if (object instanceof Creature) {
			owner.getAi2().onCreatureEvent(AIEventType.CREATURE_SEE, (Creature) object);
		}
		if (object instanceof Player) {
			if (owner.hasWalkRoutes()) {
				PacketSendUtility.sendPacket((Player) object, new SM_EMOTION(owner, EmotionType.START_EMOTE2));
			}
			if (owner.getLifeStats().isAlreadyDead())
				DropService.getInstance().see((Player) object, owner);
		}
		else if (object instanceof Summon) {
			// TODO see summon ai event
		}
	}

	@Override
	public void onBeforeSpawn() {
		super.onBeforeSpawn();
		Npc owner = getOwner();

		// set state from npc templates
		if (owner.getObjectTemplate().getState() != 0)
			owner.setState(owner.getObjectTemplate().getState());
		else
			owner.setState(CreatureState.NPC_IDLE);

		owner.getLifeStats().setCurrentHpPercent(100);
		owner.getAi2().onGeneralEvent(AIEventType.RESPAWNED);

		if (owner.getSpawn().canFly()) {
			owner.setState(CreatureState.FLYING);
		}
		if (owner.getSpawn().getState() != 0)
			owner.setState(owner.getSpawn().getState());
	}

	@Override
	public void onAfterSpawn() {
		super.onAfterSpawn();
		getOwner().getAi2().onGeneralEvent(AIEventType.SPAWNED);
	}

	@Override
	public void onDespawn() {
		Npc owner = getOwner();
		DropService.getInstance().unregisterDrop(getOwner());
		owner.getAi2().onGeneralEvent(AIEventType.DESPAWNED);
		super.onDespawn();
	}

	@Override
	public void onDie(Creature lastAttacker) {
	     CustomBossSpawn sp;

    if((sp = BossSpawnService.getInstance().getCustomBoss(this.getOwner())) != null && lastAttacker instanceof Player)

    {

        sp.onDie((Player) lastAttacker);

    }
		super.onDie(lastAttacker);
		Npc owner = getOwner();
		if (owner.getSpawn().hasPool()) {
			owner.getSpawn().setUse(false);
		}
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.DIE, 0, owner.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()));

		if (owner.getAi2().poll(AIQuestion.SHOULD_REWARD)) {
			this.doReward();
		}

		owner.getPosition().getWorldMapInstance().getInstanceHandler().onDie(owner);
		owner.getAi2().onGeneralEvent(AIEventType.DIED);

		if (owner.getAi2().poll(AIQuestion.SHOULD_DECAY)) {
			addTask(TaskId.DECAY, RespawnService.scheduleDecayTask(owner));
		}
		if (owner.getAi2().poll(AIQuestion.SHOULD_RESPAWN) && !SiegeService.getInstance().isSiegeNpcInActiveSiege(owner)) {
			scheduleRespawn();
		}
		else {
			if (!hasScheduledTask(TaskId.DECAY)) {
				onDelete();
			}
		}

	}

	@Override
	public void doReward() {
		if (getOwner() instanceof SiegeNpc)
			rewardSiegeNpc();
		AionObject winner = getOwner().getAggroList().getMostDamage();

		if (winner == null)
			return;
		if (winner instanceof TemporaryPlayerTeam) {
			PlayerTeamDistributionService.doReward((TemporaryPlayerTeam) winner, getOwner());
		}
		else if (((Player) winner).isInGroup2()) {
			PlayerTeamDistributionService.doReward(((Player) winner).getPlayerGroup2(), getOwner());
		}
		else {
			super.doReward();

			Player player = (Player) ((Creature) winner).getActingCreature();

			long expReward = StatFunctions.calculateSoloExperienceReward(player, getOwner());
			player.getCommonData().addExp(expReward, RewardType.HUNTING, this.getOwner().getObjectTemplate().getNameId());

			int currentDp = player.getCommonData().getDp();
			int dpReward = StatFunctions.calculateSoloDPReward(player, getOwner());
			player.getCommonData().setDp(dpReward + currentDp);

			if (getOwner().isRewardAP())
				AbyssPointsService.addAp(player, getOwner(), StatFunctions.calculatePvEApGained(player, getOwner()));

			QuestEngine.getInstance().onKill(new QuestEnv(getOwner(), player, 0, 0));
			DropRegistrationService.getInstance().registerDrop(getOwner(), player, player.getLevel(), null);
			// notify instance script
		}
	}

	@Override
	public Npc getOwner() {
		return (Npc) super.getOwner();
	}

	@Override
	public void onDialogRequest(Player player) {
        if (player.isTrading()) {
            log.warn("[AUDIT] Trying to use trade exploit: " + player.getName());
            return;
        }
		// notify npc dialog request observer
		if (!getOwner().getObjectTemplate().canInteract())
			return;
		player.getObserveController().notifyRequestDialogObservers(getOwner());

		getOwner().getAi2().onCreatureEvent(AIEventType.DIALOG_START, player);
	}

	@Override
	public void onDialogSelect(int dialogId, final Player player, int questId, int extendedRewardIndex) {
		if (!MathUtil.isInRange(getOwner(), player, getOwner().getObjectTemplate().getTalkDistance() + 2)) {
			return;
		}
		if (!getOwner().getAi2().onDialogSelect(player, dialogId, questId, extendedRewardIndex)) {
			DialogService.onDialogSelect(dialogId, player, getOwner(), questId, extendedRewardIndex);
		}
	}

	@Override
	public void onAttack(Creature creature, int skillId, TYPE type, int damage, boolean notifyAttack, LOG log) {
		if (getOwner().getLifeStats().isAlreadyDead())
			return;
		final Creature actingCreature;

		// summon should gain its own aggro
		if (creature instanceof Summon)
			actingCreature = creature;
		else
			actingCreature = creature.getActingCreature();

		super.onAttack(actingCreature, skillId, type, damage, notifyAttack, log);

		Npc npc = getOwner();

		if (actingCreature instanceof Player) {
			QuestEngine.getInstance().onAttack(new QuestEnv(npc, (Player) actingCreature, 0, 0));
		}

		PacketSendUtility.broadcastPacket(npc, new SM_ATTACK_STATUS(npc, type, skillId, damage, log));
	}

	@Override
	public void onStopMove() {
		getOwner().getMoveController().setInMove(false);
		super.onStopMove();
	}

	@Override
	public void onStartMove() {
		getOwner().getMoveController().setInMove(true);
		super.onStartMove();
	}

	@Override
	public void onEnterZone(ZoneInstance zoneInstance) {
		if (zoneInstance.getAreaTemplate().getZoneName() == null)
			log.error("No name found for a Zone in the map " + zoneInstance.getAreaTemplate().getWorldId());
	}

	private void rewardSiegeNpc() {
		int totalDamage = getOwner().getAggroList().getTotalDamage();
		for (AggroInfo aggro : getOwner().getAggroList().getFinalDamageList(true)) {
			float percentage = aggro.getDamage() / totalDamage;
			List<Player> players = new ArrayList<Player>();
			if (aggro.getAttacker() instanceof Player) {
				Player player = (Player) aggro.getAttacker();
				if (MathUtil.isIn3dRange(player, getOwner(), GroupConfig.GROUP_MAX_DISTANCE) && !player.getLifeStats().isAlreadyDead()) {
					int apPlayerReward = Math.round(StatFunctions.calculatePvEApGained(player, getOwner()) * percentage);
					AbyssPointsService.addAp(player, getOwner(), apPlayerReward);
				}

			}
			else if (aggro.getAttacker() instanceof PlayerGroup) {
				PlayerGroup group = (PlayerGroup) aggro.getAttacker();

				for (Player member : group.getMembers()) {
					if (MathUtil.isIn3dRange(member, getOwner(), GroupConfig.GROUP_MAX_DISTANCE) && !member.getLifeStats().isAlreadyDead()) {
						players.add(member);
					}
				}

				if (!players.isEmpty()) {
					for (Player member : players) {
						int baseApReward = StatFunctions.calculatePvEApGained(member, getOwner());
						int apRewardPerMember = Math.round(baseApReward * percentage / players.size());
						if (apRewardPerMember > 0)
							AbyssPointsService.addAp(member, getOwner(), apRewardPerMember);
					}
				}
			}
			else if ((aggro.getAttacker() instanceof PlayerAlliance)) {
				PlayerAlliance alliance = (PlayerAlliance) aggro.getAttacker();
				players = new ArrayList<Player>();

				for (Player member : alliance.getMembers()) {
					if (MathUtil.isIn3dRange(member, getOwner(), GroupConfig.GROUP_MAX_DISTANCE) && !member.getLifeStats().isAlreadyDead()) {
						players.add(member);
					}
				}

				if (!players.isEmpty()) {
					for (Player member : players) {
						int baseApReward = StatFunctions.calculatePvEApGained(member, getOwner());
						int apRewardPerMember = Math.round(baseApReward * percentage / players.size());
						if (apRewardPerMember > 0)
							AbyssPointsService.addAp(member, getOwner(), apRewardPerMember);
					}
				}
			}
		}
	}

	public Future<?> scheduleRespawn() {
		if (!getOwner().getSpawn().isNoRespawn()) {
			return RespawnService.scheduleRespawnTask(getOwner());
		}
		return null;
	}

	public final float getAttackDistanceToTarget() {
		return getOwner().getGameStats().getAttackRange().getCurrent() / 1000f;
	}

	@Override
	public boolean useSkill(int skillId, int skillLevel) {
		SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(skillId);
		if (!getOwner().isSkillDisabled(skillTemplate)) {
			getOwner().getGameStats().renewLastSkillTime();
			return super.useSkill(skillId, skillLevel);
		}
		return false;
	}

}
