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
package instance.steelrake;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author xTz
 */
@InstanceID(300100000)
public class SteelRakeInstance extends GeneralInstanceHandler {

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		if (Rnd.get(1, 2) == 1) { // Collector memekin
			spawn(215064, 747.065f, 458.293f, 942.354f, (byte) 60);
		}
		else { // Discerner werikiki
			spawn(215065, 728.008f, 541.524f, 942.354f, (byte) 59);
		}
		if (Rnd.get(1, 100) > 25) { // Pegureronerk
			spawn(798376, 516.198364f, 489.708008f, 885.760315f, (byte) 60);
		}
		if (Rnd.get(1, 2) == 1) { // Madame Bovariki
			spawn(215078, 460.904999f, 512.684998f, 952.549011f, (byte) 0);
		}
		else {
			spawn(215078, 477.534210f, 478.140991f, 951.703674f, (byte) 0);
		}
		int npcId = 0;
		switch (Rnd.get(1, 6)) { // Special Delivery
			case 1:
				npcId = 215074;
				break;
			case 2:
				npcId = 215075;
				break;
			case 3:
				npcId = 215076;
				break;
			case 4:
				npcId = 215077;
				break;
			case 5:
				npcId = 215054;
				break;
			case 6:
				npcId = 215055;
				break;
		}
		spawn(npcId, 461.933350f, 510.545654f, 877.618103f, (byte) 90);
	}
	
	@Override
	public boolean onReviveEvent(Player player) {
		PlayerReviveService.revive(player, 25, 25, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		TeleportService2.teleportTo(player, mapId, instanceId, 236.70343f, 505.17365f, 948.67365f, (byte) 119);
		return true;
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);

		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
