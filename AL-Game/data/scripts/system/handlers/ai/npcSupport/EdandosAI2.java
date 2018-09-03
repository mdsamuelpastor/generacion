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
package ai.npcSupport;

import com.aionemu.gameserver.ai2.*;
import com.aionemu.gameserver.model.gameobjects.player.*;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.utils.*;
import com.aionemu.gameserver.skillengine.SkillEngine;

/**
 * @author Madmax_Mogli
 */

@AIName("edandos")
public class EdandosAI2 extends NpcAI2
{
	 @Override
    	 protected void handleDialogStart(Player player) {
  	 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
    }

 	@Override
	 public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
  	 if (dialogId == 10000) {
		SkillEngine.getInstance().getSkill(player, 955, 1, player).useSkill();
		SkillEngine.getInstance().getSkill(player, 951, 1, player).useSkill();
		player.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.HP, 2500);
		player.getLifeStats().increaseMp(SM_ATTACK_STATUS.TYPE.MP, 2500);
		}
  		return true;
            	}

}