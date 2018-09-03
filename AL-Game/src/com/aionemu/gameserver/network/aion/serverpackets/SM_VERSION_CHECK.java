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

import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.WorldConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.configs.network.IPConfig;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.network.NetworkController;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.ChatService;

/**
 * @author -Nemesiss- CC fix, Maestross
 * @reworked -Enomine-
 */
	// Default constructor * 12102012_D7APLJiZi35sZRmZ *.
 
public class SM_VERSION_CHECK extends AionServerPacket {

	private int version;
	private int characterLimitCount;

	/**
	 * Related to the character creation mode
	 */
	private final int characterFactionsMode;
	private final int characterCreateMode;

	/**
	 * @param chatService
	 */
	public SM_VERSION_CHECK(int version) {
		this.version = version;
		if (MembershipConfig.CHARACTER_ADDITIONAL_ENABLE != 10
			&& MembershipConfig.CHARACTER_ADDITIONAL_COUNT > GSConfig.CHARACTER_LIMIT_COUNT) {
			characterLimitCount = MembershipConfig.CHARACTER_ADDITIONAL_COUNT;
		}
		else {
			characterLimitCount = GSConfig.CHARACTER_LIMIT_COUNT;
		}

		characterLimitCount *= NetworkController.getInstance().getServerCount();

		if (GSConfig.CHARACTER_CREATION_MODE < 0 || GSConfig.CHARACTER_CREATION_MODE > 2)
			characterFactionsMode = 0;
		else
			characterFactionsMode = GSConfig.CHARACTER_CREATION_MODE;

		if (GSConfig.CHARACTER_FACTION_LIMITATION_MODE < 0 || GSConfig.CHARACTER_FACTION_LIMITATION_MODE > 3)
			characterCreateMode = 0;
		else
			characterCreateMode = GSConfig.CHARACTER_FACTION_LIMITATION_MODE * 0x04;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		if (version < 0xC2) {
			writeC(0x02);
			return;
		}
		writeC(0x00); //checked Gameforge
		writeC(NetworkConfig.GAMESERVER_ID);//checked Gameforge
		writeD(0x0001FCA6);//checked Gameforge
		writeD(0x0001D985);//checked Gameforge
		writeD(0);// spacing
		writeD(0x0001D985);//checked Gameforge
		writeD(0x515BFBE9);//checked Gameforge
		writeC(0x00);// unk
		writeC(GSConfig.SERVER_COUNTRY_CODE);// country code;
		writeC(0x00);// unk

		int serverMode = (characterLimitCount * 0x10) | characterFactionsMode;

		if (GSConfig.ENABLE_RATIO_LIMITATION) {
			if (GameServer.getCountFor(Race.ELYOS) + GameServer.getCountFor(Race.ASMODIANS) > GSConfig.RATIO_HIGH_PLAYER_COUNT_DISABLING)
				writeC(serverMode | 0x0C);
			else if (GameServer.getRatiosFor(Race.ELYOS) > GSConfig.RATIO_MIN_VALUE)
				writeC(serverMode | 0x04);
			else if (GameServer.getRatiosFor(Race.ASMODIANS) > GSConfig.RATIO_MIN_VALUE)
				writeC(serverMode | 0x08);
			else
				writeC(serverMode);
		}
		else {
			writeC(serverMode | characterCreateMode);
		}

		writeD((int) (System.currentTimeMillis() / 1000));
		writeD(0x0501015E);//checked Gameforge
		writeD(0x3D010A0F);//checked Gameforge
		writeH(2);
		writeC(GSConfig.CHARACTER_REENTRY_TIME);
		if (WorldConfig.ENABLE_DECORATIONS_CHRISTMAS) {
            writeC(0x01);
        } else if (WorldConfig.ENABLE_DECORATIONS_HALLOWEEN) {
            writeC(0x02);
        } else if (WorldConfig.ENABLE_DECORATIONS_VALENTINE) {
            writeC(0x04);
        } else {
            writeC(0x00);
        }
		writeD(0x00); // 2.5//checked Gameforge
		writeD(0xFFFFF1F0);//checked Gameforge
		writeD(4);//checked Gameforge
		writeD(0x00010000);//checked Gameforge
		writeC(0);// its loop size//checked Gameforge


		writeB(IPConfig.getDefaultAddress());//checked Gameforge
		writeH(ChatService.getPort());//checked Gameforge
	}
}