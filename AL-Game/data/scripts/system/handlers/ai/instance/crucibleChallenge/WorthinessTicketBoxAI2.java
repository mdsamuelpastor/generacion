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
package ai.instance.crucibleChallenge;

import java.util.Set;

import ai.ChestAI2;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.drop.DropRegistrationService;

/**
 * @author xTz
 */
@AIName("worthinessticketbox")
public class WorthinessTicketBoxAI2 extends ChestAI2 {

	@Override
	protected void handleUseItemFinish(Player player) {
		super.handleUseItemFinish(player);
		spawn(205674, 345.52954f, 1662.6697f, 95.25f, (byte) 0);
	}

	@Override
	public void handleDropRegistered() {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().geCurrentDropMap().get(getObjectId());
		dropItems.clear();
		dropItems.add(DropRegistrationService.getInstance().regDropItem(1, getPosition().getWorldMapInstance().getSoloPlayerObj(), getNpcId(), 186000134, 1));
	}
}
