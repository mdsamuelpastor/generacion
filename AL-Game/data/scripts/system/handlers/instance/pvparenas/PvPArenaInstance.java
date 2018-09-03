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
package instance.pvparenas;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.attack.AggroInfo;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.instancereward.PvPArenaReward;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.PvPArenaPlayerReward;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author xTz
 */
public class PvPArenaInstance extends GeneralInstanceHandler {

	private boolean isInstanceDestroyed;
	protected PvPArenaReward instanceReward;
	private boolean isInstanceStarted = false;
	protected int killBonus;
	protected int deathFine;

	@Override
	public boolean onDie(Player player, Creature lastAttacker) {
		PvPArenaPlayerReward ownerReward = getPlayerReward(player);
		ownerReward.endBoostMoraleEffect();
		ownerReward.applyBoostMoraleEffect();

		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);

		PacketSendUtility.sendPacket(player, new SM_DIE(false, false, 0, 8));

		if (lastAttacker != null && lastAttacker != player) {
			if (lastAttacker instanceof Player) {
				Player winner = (Player) lastAttacker;
				PvPArenaPlayerReward reward = getPlayerReward(winner);
				reward.addPvPKillToPlayer();

				// notify Kill-Quests
				int worldId = winner.getWorldId();
				QuestEngine.getInstance().onKillInWorld(new QuestEnv(player, winner, 0, 0), worldId);
			}
		}

		updatePoints(player);

		return true;
	}

	private void updatePoints(Creature victim) {

		if (!instanceReward.isStartProgress()) {
			return;
		}

		int bonus = 0;
		int rank = 0;

		// Decrease victim points
		if (victim instanceof Player) {
			PvPArenaPlayerReward victimFine = getPlayerReward((Player) victim);
			victimFine.addPoints(deathFine);
			bonus = killBonus;
			rank = instanceReward.getRank(victimFine.getPoints());
		}
		else
			bonus = getNpcBonus(((Npc) victim).getNpcId());

		if (bonus == 0) {
			return;
		}

		// Reward all damagers
		for (AggroInfo damager : victim.getAggroList().getList()) {
			if (!(damager.getAttacker() instanceof Creature)) {
				continue;
			}
			Creature master = ((Creature) damager.getAttacker()).getMaster();
			if (master == null) {
				continue;
			}
			if (master instanceof Player) {
				Player attaker = (Player) master;
				int rewardPoints = (victim instanceof Player && instanceReward.getRound() == 3 && rank == 0 ? bonus * 3 : bonus) * damager.getDamage() / victim.getAggroList().getTotalDamage();
				getPlayerReward(attaker).addPoints(rewardPoints);
				sendSystemMsg(attaker, victim, rewardPoints);
			}
		}
		if (instanceReward.hasCapPoints()) {
			instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
			reward();
		}
		sendPacket();
	}

	protected void sendSystemMsg(Player player, Creature creature, int rewardPoints) {
		int nameId = creature.getObjectTemplate().getNameId();
		DescriptionId name = new DescriptionId(nameId * 2 + 1);
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400237, nameId == 0 ? creature.getName() : name, rewardPoints));
	}

	@Override
	public void onDie(Npc npc) {
		if (npc.getAggroList().getMostPlayerDamage() == null) {
			return;
		}
		updatePoints(npc);
		final int npcId = npc.getNpcId();
		if (npcId == 701187 || npcId == 701188) {
			spawnRndRelics(30000);
		}
	}

	@Override
	public void onEnterInstance(Player player) {
		if (!isInstanceStarted) {
			isInstanceStarted = true;
			instanceReward.setInstanceStartTime();
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (!isInstanceDestroyed && !instanceReward.isRewarded()) {
						sendPacket(new SM_SYSTEM_MESSAGE(1401058));
					}
				}

			}, 111000);
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					// start round 1
					if (!isInstanceDestroyed && !instanceReward.isRewarded()) {
						openDoors();
						sendPacket(new SM_SYSTEM_MESSAGE(1401058));
						instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
						sendPacket();
					}
				}

			}, 120000);
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					// start round 2
					if (!isInstanceDestroyed && !instanceReward.isRewarded()) {
						instanceReward.setRound(2);
						instanceReward.setRndZone();
						sendPacket();
						changeZone();
					}
				}

			}, 300000);
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					// start round 3
					if (!isInstanceDestroyed && !instanceReward.isRewarded()) {
						instanceReward.setRound(3);
						instanceReward.setRndZone();
						sendPacket(new SM_SYSTEM_MESSAGE(1401203));
						sendPacket();
						changeZone();
					}
				}

			}, 480000);
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					// end
					if (!isInstanceDestroyed && !instanceReward.isRewarded()) {
						instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
						// to do reward
						reward();
						sendPacket();
					}
				}

			}, 660000);
		}
		if (!containPlayer(player.getObjectId())) {
			instanceReward.regPlayerReward(player);
			instanceReward.setRndPosition(player.getObjectId());
		}
		else {
			getPlayerReward(player).setPlayer(player);
		}
		sendPacket();
	}

	private void sendPacket(final AionServerPacket packet) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, packet);
			}

		});
	}

	private void spawnRndRelics(int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed && !instanceReward.isRewarded()) {
					spawn(Rnd.get(1, 2) == 1 ? 701187 : 701188, 1841.951f, 1733.968f, 300.242f, (byte) 0);
				}
			}

		}, time);
	}

	private int getNpcBonus(int npcId) {
		return instanceReward.getNpcBonus(npcId);
	}

	@Override
	public InstanceReward<?> getInstanceReward() {
		return instanceReward;
	}

	@Override
	public void onPlayerLogOut(Player player) {
		getPlayerReward(player).updateLogOutTime();
	}

	@Override
	public void onPlayerLogin(Player player) {
		// instanceReward.portToPosition(player, false); to do another
		getPlayerReward(player).updateBonusTime();
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		instanceReward = new PvPArenaReward(mapId, instanceId, instance);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		spawnRings();
		if (!instanceReward.isSoloArena()) {
			spawnRndRelics(0);
		}
	}

	@Override
	public void onExitInstance(Player player) {
		PvPArenaPlayerReward playerReward = getPlayerReward(player);
		playerReward.endBoostMoraleEffect();
		instanceReward.clearPosition(playerReward.getPosition(), Boolean.FALSE);
		playerReward.setPosition(0);
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}

	private void openDoors() {
		for (StaticDoor door : instance.getDoors().values()) {
			if (door != null) {
				door.setOpen(true);
			}
		}
	}

	private boolean containPlayer(Integer object) {
		return instanceReward.containPlayer(object);
	}

	protected PvPArenaPlayerReward getPlayerReward(Player player) {
		instanceReward.regPlayerReward(player);
		return instanceReward.getPlayerReward(player.getObjectId());
	}

	@Override
	public boolean onReviveEvent(Player player) {
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PlayerReviveService.revive(player, 100, 100, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		if (!isInstanceDestroyed) {
			instanceReward.portToPosition(player, false);
		}
		return true;
	}

	protected void sendPacket() {
		instanceReward.sendPacket();
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		instanceReward.clear();
	}

	private void changeZone() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				for (Player player : instance.getPlayersInside()) {
					instanceReward.portToPosition(player, true);
				}
			}

		}, 1000);
	}

	protected void reward() {
		if (instanceReward.canRewarded()) {
			for (InstancePlayerReward playerReward : instanceReward.getPlayersInside()) {
				PvPArenaPlayerReward reward = (PvPArenaPlayerReward) playerReward;
				if (!reward.isRewarded()) {
					reward.setRewarded();
					int points = reward.getPoints();
					int totalPoints = points + reward.getTimeBonus() + instanceReward.getRankBonus(instanceReward.getRank(points));
					float rate = instanceReward.isSoloArena() ? playerReward.getPlayer().getRates().getArenaSoloRewardRate() : playerReward.getPlayer().getRates().getArenaFFARewardRate();
					int abyssPoints = (int) ((0.1 * totalPoints + 800) * rate);
					int crucibleInsignia = (int) ((0.08 * totalPoints + 350) * rate);
					int courageInsignia = (int) ((0.05 * totalPoints + 5.5) * rate);
					reward.setAbyssPoints(abyssPoints);
					reward.setCrucibleInsignia(crucibleInsignia);
					reward.setCourageInsignia(courageInsignia);
					Player player = reward.getPlayer();
					AbyssPointsService.addAp(player, abyssPoints);

					ItemService.addItem(player, 186000137, courageInsignia);
					if (instanceReward.canRewardOpportunityToken(reward)) {
						ItemService.addItem(player, 186000165, instanceReward.isSoloArena() ? 4 : 5);
					}
					if(!instanceReward.isTeamArena()) {
						ItemService.addItem(player, 186000130, crucibleInsignia);
					}
					if (instanceReward.canRewardGloryTicket(reward)) {
						ItemService.addItem(player, 186000185, 1);
					}
				}
			}
		}
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					for (Player player : instance.getPlayersInside()) {
						if (!CreatureActions.isAlreadyDead(player)) {
							onExitInstance(player);
						}
					}
				}
			}

		}, 10000);
	}

	protected void spawnRings() {
	}

	protected Npc getNpc(float x, float y, float z) {
		if (!isInstanceDestroyed) {
			for (Npc npc : instance.getNpcs()) {
				SpawnTemplate st = npc.getSpawn();
				if (st.getX() == x && st.getY() == y && st.getZ() == z) {
					return npc;
				}
			}
		}
		return null;
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		if (!instanceReward.isStartProgress()) {
			return;
		}
		int rewardetPoints = instanceReward.getNpcBonus(npc.getNpcId());
		int skill = instanceReward.getNpcBonusSkill(npc.getNpcId());
		if (skill != 0) {
			useSkill(npc, player, skill >> 8, skill & 0xFF);
		}
		getPlayerReward(player).addPoints(rewardetPoints);
		sendSystemMsg(player, npc, rewardetPoints);
		sendPacket();
	}

	protected void useSkill(Npc npc, Player player, int skillId, int level) {
		SkillEngine.getInstance().getSkill(npc, skillId, level, player).useNoAnimationSkill();
	}
}
