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
package com.aionemu.gameserver.model.team2.alliance.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.group.PlayerFilters.ExcludePlayerFilter;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.google.common.base.Preconditions;

/**
 * @author ATracer, Maestross
 */
public class PlayerAllianceInvite extends RequestResponseHandler {

	private final Player inviter;
	private final Player invited;

	public PlayerAllianceInvite(Player inviter, Player invited) {
		super(inviter);
		this.inviter = inviter;
		this.invited = invited;
	}

	@Override
	public void acceptRequest(Creature requester, Player responder) {
		if (PlayerAllianceService.canInvite(inviter, invited)) {

			PlayerAlliance alliance = inviter.getPlayerAlliance2();

			if (alliance != null) {
				if (alliance.size() == 24) {
					PacketSendUtility.sendPacket(invited, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_CANT_ADD_NEW_MEMBER);
					PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_CANT_ADD_NEW_MEMBER);
					return;
				}
				else if (invited.isInGroup2() && invited.getPlayerGroup2().size() + alliance.size() > 24) {
					PacketSendUtility.sendPacket(invited, SM_SYSTEM_MESSAGE.STR_FORCE_INVITE_FAILED_NOT_ENOUGH_SLOT);
					PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_FORCE_INVITE_FAILED_NOT_ENOUGH_SLOT);
					return;
				}
			}

			List<Player> playersToAdd = new ArrayList<Player>();
			collectPlayersToAdd(playersToAdd, alliance);

			if (alliance == null) {
				alliance = PlayerAllianceService.createAlliance(inviter, invited);
			}

			for (Player member : playersToAdd) {
				PlayerAllianceService.addPlayer(alliance, member);
			}
		}
	}

	private final void collectPlayersToAdd(List<Player> playersToAdd, PlayerAlliance alliance) {
		// Collect Inviter Group without leader
		if (inviter.isInGroup2()) {
			Preconditions.checkState(alliance == null, "If inviter is in group - alliance should be null");
			PlayerGroup group = inviter.getPlayerGroup2();
			playersToAdd.addAll(group.filterMembers(new ExcludePlayerFilter(inviter)));

			Iterator<Player> pIter = group.getMembers().iterator();
			while (pIter.hasNext()) {
				PlayerGroupService.removePlayer(pIter.next());
			}
		}

		// Collect full Invited Group
		if (invited.isInGroup2()) {
			PlayerGroup group = invited.getPlayerGroup2();
			playersToAdd.addAll(group.getMembers());
			Iterator<Player> pIter = group.getMembers().iterator();
			while (pIter.hasNext()) {
				PlayerGroupService.removePlayer(pIter.next());
			}
		}
		// or just single player
		else {
			playersToAdd.add(invited);
		}
	}

	@Override
	public void denyRequest(Creature requester, Player responder) {
		PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_HE_REJECT_INVITATION(responder.getName()));
	}

}
