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
package com.aionemu.gameserver.model.team2.alliance;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;
import com.aionemu.gameserver.model.team2.league.League;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.google.common.base.Preconditions;

/**
 * @author ATracer, Maestross
 */
public class PlayerAlliance extends TemporaryPlayerTeam<PlayerAllianceMember> {

	private final Map<Integer, PlayerAllianceGroup> groups = new HashMap<Integer, PlayerAllianceGroup>();
	private final Collection<Integer> viceCaptainIds = new CopyOnWriteArrayList<Integer>();
	private int allianceReadyStatus;
	private int groupType = 63;

	private League league;

	public PlayerAlliance(PlayerAllianceMember leader) {
		super(IDFactory.getInstance().nextId());
		initializeTeam(leader);
		for (int groupId = 1000; groupId <= 1003; groupId++) {
			groups.put(groupId, new PlayerAllianceGroup(this, groupId));
		}
	}

	@Override
	public void addMember(PlayerAllianceMember member) {
		super.addMember(member);
		PlayerAllianceGroup openAllianceGroup = getOpenAllianceGroup();
		openAllianceGroup.addMember(member);
	}

	@Override
	public void removeMember(PlayerAllianceMember member) {
		super.removeMember(member);
		member.getPlayerAllianceGroup().removeMember(member);
	}

	@Override
	public boolean isFull() {
		return size() == 24;
	}

	@Override
	public int getMinExpPlayerLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxExpPlayerLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	public PlayerAllianceGroup getOpenAllianceGroup() {
		lock();
		try {
			for (int groupId = 1000; groupId <= 1003; groupId++) {
				PlayerAllianceGroup playerAllianceGroup = groups.get(groupId);
				if (!playerAllianceGroup.isFull()) {
					return playerAllianceGroup;
				}
			}
		}
		finally {
			unlock();
		}
		throw new IllegalStateException("All alliance groups are full.");
	}

	public PlayerAllianceGroup getAllianceGroup(Integer allianceGroupId) {
		PlayerAllianceGroup allianceGroup = groups.get(allianceGroupId);
		Preconditions.checkNotNull(allianceGroup, "No such alliance group " + allianceGroupId);
		return allianceGroup;
	}

	public final Collection<Integer> getViceCaptainIds() {
		return viceCaptainIds;
	}

	public final boolean isViceCaptain(Player player) {
		return viceCaptainIds.contains(player.getObjectId());
	}

	public final boolean isSomeCaptain(Player player) {
		return isLeader(player) || isViceCaptain(player);
	}

	public int getAllianceReadyStatus() {
		return allianceReadyStatus;
	}

	public void setAllianceReadyStatus(int allianceReadyStatus) {
		this.allianceReadyStatus = allianceReadyStatus;
	}

	public final League getLeague() {
		return league;
	}

	public final void setLeague(League league) {
		this.league = league;
	}

	public final boolean isInLeague() {
		return this.league != null;
	}

	public final int groupSize() {
		return groups.size();
	}

	public final Collection<PlayerAllianceGroup> getGroups() {
		return groups.values();
	}
	
	public final int getGroupType() {
        return groupType;
    }

    public void setGroupType(int paramInt) {
        groupType = paramInt;
    }

}
