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
package ai.instance.dragonLordRefuge;

import com.aionemu.gameserver.ai2.*;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.teleport.TeleportService2;

import ai.ActionItemNpcAI2;

/**
 * @author Maestross
 */

@AIName("internalpassagein2")
public class InternalPassageIn2AsmodiansAI2 extends ActionItemNpcAI2
{
	@Override
	protected void handleUseItemFinish(Player player) {
		switch (getNpcId()) {
		    case 730674: //Dragon Lord Refuge (Internal Passage In 2).
				TeleportService2.teleportTo(player, 300520000, 785.866f, 197.713f, 246.071f, (byte) 0);
			break;
		}
	}
}