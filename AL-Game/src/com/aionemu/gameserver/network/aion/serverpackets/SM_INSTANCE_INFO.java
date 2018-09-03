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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PortalCooldownList;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import javolution.util.FastMap.Entry;

/**
 * @author xavier
 */
public class SM_INSTANCE_INFO extends AionServerPacket {

	private Player player;
	private boolean isAnswer;
	private int cooldownId;
	private int worldId;
	private TemporaryPlayerTeam<?> playerTeam;

	public SM_INSTANCE_INFO(Player player, boolean isAnswer, TemporaryPlayerTeam<?> playerTeam) {
		this.player = player;
		this.isAnswer = isAnswer;
		this.playerTeam = playerTeam;
		worldId = 0;
		cooldownId = 0;
	}

	public SM_INSTANCE_INFO(Player player, int instanceId) {
		this.player = player;
		isAnswer = false;
		playerTeam = null;
		worldId = instanceId;
		cooldownId = (DataManager.INSTANCE_COOLTIME_DATA.getInstanceCooltimeByWorldId(instanceId) != null ? DataManager.INSTANCE_COOLTIME_DATA
			.getInstanceCooltimeByWorldId(instanceId).getId() : 0);
	}

	@Override
	protected void writeImpl(AionConnection con) {
		boolean hasTeam = playerTeam != null;
		writeC(!isAnswer ? 2 : hasTeam ? 1 : 0);
		writeC(cooldownId);
		writeD(0);
		if (isAnswer) {
			if (hasTeam) {
				writeH(playerTeam.getMembers().size());
				for (Player p : playerTeam.getMembers()) {
					PortalCooldownList cooldownList = p.getPortalCooldownList();
					writeD(p.getObjectId());
					writeH(cooldownList.size());
					Entry<Integer, Long> e = cooldownList.getPortalCoolDowns().head();
					for (Entry<Integer, Long> end = cooldownList.getPortalCoolDowns().tail(); (e = e.getNext()) != end;) {
						writeD(DataManager.INSTANCE_COOLTIME_DATA.getInstanceCooltimeByWorldId(e.getKey()).getId());
						writeD(0);
						writeD((int) (e.getValue() - System.currentTimeMillis()) / 1000);
					}
					writeS(p.getName());
				}
			}
			else {
				writeH(1);
				PortalCooldownList cooldownList = player.getPortalCooldownList();
				writeD(player.getObjectId());
				writeH(cooldownList.size());
				Entry<Integer, Long> e = cooldownList.getPortalCoolDowns().head();
				for (Entry<Integer, Long> end = cooldownList.getPortalCoolDowns().tail(); (e = e.getNext()) != end;) {
					writeD(DataManager.INSTANCE_COOLTIME_DATA.getInstanceCooltimeByWorldId(e.getKey()).getId());
					writeD(0);
					writeD((int) ((long) (e.getValue() - System.currentTimeMillis())) / 1000);
				}
				writeS(player.getName());
			}

		}
		else if (cooldownId == 0) {
			writeH(1);
			PortalCooldownList cooldownList = player.getPortalCooldownList();
			writeD(player.getObjectId());
			writeH(cooldownList.size());
			Entry<Integer, Long> e = cooldownList.getPortalCoolDowns().head();
			for (Entry<Integer, Long> end = cooldownList.getPortalCoolDowns().tail(); (e = e.getNext()) != end;) {
				writeD(DataManager.INSTANCE_COOLTIME_DATA.getInstanceCooltimeByWorldId(e.getKey()).getId());
				writeD(0);
				writeD((int) ((long) (e.getValue() - System.currentTimeMillis())) / 1000);
			}
			writeS(player.getName());
		}
		else {
			writeH(1);
			writeD(player.getObjectId());
			writeH(1);
			writeD(cooldownId);
			writeD(0);
			writeD((int) (player.getPortalCooldownList().getPortalCooldown(worldId) - System.currentTimeMillis()) / 1000);
			writeS(player.getName());
		}
	}
}
