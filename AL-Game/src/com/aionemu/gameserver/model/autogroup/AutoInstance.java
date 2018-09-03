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
package com.aionemu.gameserver.model.autogroup;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import javolution.util.FastList;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author xTz
 */
public class AutoInstance {

	private FastList<Player> players = new FastList<Player>();
	private FastList<Player> playersInside = new FastList<Player>();
	private Race race;
	private byte instanceMaskId;
	private WorldMapInstance worldMapInstance;
	private long startInstanceTime;
	private AutoGroupsType agt;
	private EntryRequestType registredErt;

	public AutoInstance(Race race, byte instanceMaskId, WorldMapInstance worldMapInstance, EntryRequestType registredErt) {
		this.race = race;
		this.instanceMaskId = instanceMaskId;
		this.worldMapInstance = worldMapInstance;
		startInstanceTime = System.currentTimeMillis();
		agt = AutoGroupsType.getAutoGroupByInstanceMaskId(instanceMaskId);
		this.registredErt = registredErt;
	}

	public int getPlayerSize() {
		return players.size();
	}

	public Race getRace() {
		return race;
	}

	public byte getInstanceMaskId() {
		return instanceMaskId;
	}

	public void setWorldMapInstance(WorldMapInstance worldMapInstance) {
		this.worldMapInstance = worldMapInstance;
	}

	public synchronized void enterToGroup(Player player) {

		List<Player> playersByRace = getPlayersInsideByRace(player.getRace());
		if (playersByRace.size() == 1 && !playersByRace.get(0).isInGroup2()) {
			PlayerGroup newGroup = PlayerGroupService.createGroup(playersByRace.get(0), player);
			newGroup.setGroupType(0x02);
			int groupId = newGroup.getObjectId();
			if (!worldMapInstance.isRegistered(groupId)) {
				worldMapInstance.register(groupId);
			}
		}
		else if (!playersByRace.isEmpty() && playersByRace.get(0).isInGroup2()) {
			PlayerGroupService.addPlayer(playersByRace.get(0).getPlayerGroup2(), player);
		}
		Integer object = player.getObjectId();
		if (!worldMapInstance.isRegistered(object)) {
			worldMapInstance.register(object);
		}
		playersInside.add(player);
	}

	private List<Player> getPlayersInsideByRace(Race race) {
		return select(playersInside, having(on(Player.class).getRace(), equalTo(race)));
	}

	private List<Player> getPlayersByRace(Race race) {
		return select(players, having(on(Player.class).getRace(), equalTo(race)));
	}

	public FastList<Player> getPlayersInside() {
		return playersInside;
	}

	private synchronized int getDmgPlayerCount(Player player) {
		int dmgPlayers = 0;
		for (Player playerInside : players) {
			if (!player.getRace().equals(playerInside.getRace())) {
				continue;
			}
			switch (playerInside.getPlayerClass()) {
				case GLADIATOR:
				case ASSASSIN:
				case RANGER:
				case SORCERER:
				case SPIRIT_MASTER:
				case CHANTER:
					dmgPlayers++;
					break;
				default:
					break;
			}
		}
		if (dmgPlayers < 4) {
			players.add(player);
		}
		return dmgPlayers;
	}

	private synchronized boolean canEnterSpecialPlayer(Player player) {
		for (Player playerInside : players) {
			if (!player.getRace().equals(playerInside.getRace())) {
				continue;
			}
			switch (playerInside.getPlayerClass()) {
				case CLERIC:
					if (player.getPlayerClass() == PlayerClass.CLERIC) {
						return false;
					}
					break;
				case TEMPLAR:
					if (player.getPlayerClass() == PlayerClass.TEMPLAR) {
						return false;
					}
					break;
				default:
					break;
			}
		}
		players.add(player);
		return true;
	}

	public boolean canAddPlayer(Player player) {
		if (agt.isDredgion()) {
			if (getPlayersByRace(player.getRace()).size() >= 6) {
				return false;
			}
			addPlayer(player);
			return true;
		}
		else if (agt.isPvPSoloArena() || agt.isTrainigPvPSoloArena()) {
			if (getPlayerSize() >= 2) {
				return false;
			}
			addPlayer(player);
			return true;
		}
		else if (agt.isPvPFFAArena() || agt.isTrainigPvPFFAArena()) {
			if (getPlayerSize() >= 10) {
				return false;
			}
			addPlayer(player);
			return true;
		}
		else if (agt.isPvPTeamArena() || agt.isTrainigPvPTeamArena()) {
			if (getPlayerSize() >= 6) {
				return false;
			}
			addPlayer(player);
			return true;
		}

		switch (player.getPlayerClass()) {
			case GLADIATOR:
			case ASSASSIN:
			case RANGER:
			case SORCERER:
			case SPIRIT_MASTER:
			case CHANTER:
				return getDmgPlayerCount(player) < 4;
			case CLERIC:
			case TEMPLAR:
				return canEnterSpecialPlayer(player);
			default:
				break;
		}
		return false;
	}

	public FastList<Player> getPlayers() {
		return players;
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

	public boolean containPlayer(Player player) {
		return players.contains(player);
	}

	public int getInstanceId() {
		return worldMapInstance.getInstanceId();
	}

	public WorldMapInstance getWorldMapInstance() {
		return worldMapInstance;
	}

	public long getStartInstanceTime() {
		return startInstanceTime;
	}

	public boolean satisfyTime(EntryRequestType ert) {
		InstanceReward<?> instanceReward = worldMapInstance.getInstanceHandler().getInstanceReward();
		if (instanceReward != null && instanceReward.getInstanceScoreType().isEndProgress()) {
			return false;
		}
		if (registredErt.isGroupEntry() && (System.currentTimeMillis() - startInstanceTime < 121000)) {
			return false;
		}
		if (!ert.isQuickGroupEntry()) {
			return false;
		}
		int time = agt.getTime();
		if (time == 0) {
			return true;
		}
		return System.currentTimeMillis() - startInstanceTime < time;
	}

	public void unregisterPlayer(Player player) {
		players.remove(player);
		playersInside.remove(player);
	}

	public boolean hasRacePermit(Race race) {
		if (this.race == Race.PC_ALL) {
			return true;
		}
		return this.race.equals(race);
	}

	public boolean hasSizePermit() {
		return agt.getPlayerSize() == getPlayerSize();
	}

	public boolean hasInstanceMask(byte instanceMaskId) {
		return this.instanceMaskId == instanceMaskId;
	}
}
