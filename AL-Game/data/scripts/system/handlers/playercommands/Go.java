/*
 * This file is part of NextGenCore <Ver:3.9>.
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
package playercommands;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.global.FFAStruct;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldMapType;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;

/**
 * @rework Imaginary, Maestros
 */
 
public class Go extends ChatCommand {

	public Go() {
		super("go");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params.length != 0) {
			onFail(player, null);
			return;
		}
		if (player.isAttackMode()) {
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.NOT_USE_WHILE_FIGHT));
			return;
		}
		if (player.getPosition().getMapId() == WorldMapType.TIAMARANTA_EYE_2.getId()) {
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.NOT_USE_ON_PVP_MAP));
			return;
		}
		if (player.getPosition().getMapId() == WorldMapType.INGGISON.getId()) {
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.NOT_USE_ON_PVP_MAP));
			return;
		}
		if (player.getPosition().getMapId() == WorldMapType.GELKMAROS.getId()) {
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.NOT_USE_ON_PVP_MAP));
			return;
		}
		if (player.getPosition().getMapId() == WorldMapType.SILENTERA_CANYON.getId()) {
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.NOT_USE_ON_PVP_MAP));
			return;
		}
		if (player.getLevel() > 1 && player.getLevel() < 55) {
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.LEVEL_TOO_LOW));
			return;
		}
		if (player.isInFFAPVP() && player.getWorldId() == FFAStruct.worldId) {
			return;
		}

		if (player.getRace() == Race.ELYOS && !player.isInPrison()) {
			goTo(player, WorldMapType.TIAMARANTA_EYE_2.getId(), 753, 134, 1196);
		}
		else if (player.getRace() == Race.ASMODIANS && !player.isInPrison()) {
			goTo(player, WorldMapType.TIAMARANTA_EYE_2.getId(), 754, 1459, 1196);
		} else {
			return;
		}
	}

	private static void goTo(final Player player, int worldId, float x, float y, float z) {
		WorldMap destinationMap = World.getInstance().getWorldMap(worldId);
		if (destinationMap.isInstanceType())
			TeleportService2.teleportTo(player, worldId, getInstanceId(worldId, player), x, y, z);
		else
			TeleportService2.teleportTo(player, worldId, x, y, z);
	}

	private static int getInstanceId(int worldId, Player player) {
		if (player.getWorldId() == worldId) {
			WorldMapInstance registeredInstance = InstanceService.getRegisteredInstance(worldId, player.getObjectId());
			if (registeredInstance != null)
				return registeredInstance.getInstanceId();
		}
		WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(worldId);
		InstanceService.registerPlayerWithInstance(newInstance, player);
		return newInstance.getInstanceId();
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax: .go");
	}
}
