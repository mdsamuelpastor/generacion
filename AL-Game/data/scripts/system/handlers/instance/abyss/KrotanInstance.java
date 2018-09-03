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
package instance.abyss;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author keqi, xTz
 * @reworked Luzien
 */
@InstanceID(300140000)
public class KrotanInstance extends GeneralInstanceHandler {

	private boolean rewarded = false;

	@Override
	public void onDie(Npc npc) {
		switch (npc.getNpcId()) {
			case 215136: // bosses
			case 215135:
				spawnChests(npc);
				break;
			case 215413: // artifact spawns weak boss
				Npc boss = getNpc(215136);
				if (boss != null && !boss.getLifeStats().isAlreadyDead()) {
					spawn(215135, boss.getX(), boss.getY(), boss.getZ(), boss.getHeading());
					boss.getController().onDelete();
				}
		}
	}

	private void spawnChests(Npc npc) {
		if (!rewarded) {
			rewarded = true; // safety mechanism
			if (npc.getAi2().getRemainigTime() != 0) {
				long rtime = (600000 - npc.getAi2().getRemainigTime()) / 30000;
				spawn(700539, 471.05634f, 834.5538f, 199.70894f, (byte) 63);
				if (rtime > 1)
					spawn(700539, 490f, 889f, 199f, (byte) 43);
				if (rtime > 2)
					spawn(700539, 528, 903, 199.7f, (byte) 32);
				if (rtime > 3)
					spawn(700539, 578, 874, 199.7f, (byte) 10);
				if (rtime > 4)
					spawn(700539, 477, 814, 199.7f, (byte) 8);
				if (rtime > 5)
					spawn(700539, 470, 854, 199.7f, (byte) 115);
				if (rtime > 6)
					spawn(700539, 478, 873, 199.7f, (byte) 110);
				if (rtime > 7)
					spawn(700539, 507, 898, 199.7f, (byte) 96);
				if (rtime > 8)
					spawn(700539, 547, 899, 199.7f, (byte) 85);
				if (rtime > 9)
					spawn(700539, 564, 889, 199.7f, (byte) 78);
				if (rtime > 10)
					spawn(700559, 584, 855, 199.7f, (byte) 85);
				if (rtime > 11 && npc.getNpcId() == 215136)
					spawn(700540, 576.4634f, 837.3374f, 199.7f, (byte) 99);
			}
		}
	}
	
	@Override
	public boolean onReviveEvent(Player player) {
		PlayerReviveService.revive(player, 25, 25, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		TeleportService2.teleportTo(player, mapId, instanceId, 527.61273f, 112.81209f, 175.92393f, (byte) 31);
		return true;
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);

		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
