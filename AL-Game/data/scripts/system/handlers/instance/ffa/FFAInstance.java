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
package instance.ffa;

import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.gameobjects.LetterType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.mail.SystemMailService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;


/**
 * @author Maestross
 *
 */
@InstanceID(300350000)
public class FFAInstance extends GeneralInstanceHandler {

	private Future<?> instanceTimer;
	private long remainingTime;
	private LetterType mail;
	
	@Override
  public void onInstanceCreate(WorldMapInstance instance) {
      super.onInstanceCreate(instance);
  }
	
	@Override
  public void onEnterInstance(Player player) {
      player.getController().cancelCurrentSkill();
      cancelAvatar(player);
      player.getCommonData().setDp(4000);    
      if (instanceTimer == null) {
      	  setRemainingTime(System.currentTimeMillis() + 15000 * 1000);
          instanceTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

              @Override
              public void run() {
              	checkRemainingTime();
              }
          }, 500 * 1000 /*
                   * 30 * 1000
                   */);
      }
      PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(4, 0, getRemainingTime()));
  }
	
	private int getRemainingTime() {
    int result;
    result = (int) (remainingTime - System.currentTimeMillis()) / 1000;
    if (result < 0) {
        result = 0;
    }
    return result;
  }
	
	public void cancelAvatar(Player player)
  {
      for (com.aionemu.gameserver.skillengine.model.Effect ef : player.getEffectController().getAbnormalEffects()) {
			if (ef.isDeityAvatar()) {
				ef.endEffect();
				player.getEffectController().clearEffect(ef);
			}
		}
  }
	
	public final void setRemainingTime(long time) {
    remainingTime = time;
  }
	
	public void checkRemainingTime() {
		int result;
		result = (int) getRemainingTime();
		if (result < 0 || result == 0) 
			setRemainingTime(System.currentTimeMillis() + 15000 * 1000);
	}
	
	 @Override
   public void onInstanceDestroy() {
       if (instanceTimer != null) {
           instanceTimer.cancel(false);
       }
   }
	 
	 @Override
   public void onLeaveInstance(Player player) {
		 leaveMail(player);
		 if (player.getSpecialKills() > 100)
		     monsterMail(player);
		 player.setSpecialKills(0);
   }
	 
	 private void leaveMail(Player player) {
     String senderName = "[FFA]HQ";
     String title = "FFA Participant";
     String message = "Hello young Daeva! We hope you proved you in the FFA. We want to give you some reward for your participation.";
     String service = "[FFA]";
     int rewardItem = 186000223;
     mail = LetterType.NORMAL;
     SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, rewardItem, 5, 1000000, mail);
 }
	 
	 private void monsterMail(Player player) {
     String senderName = "[FFA]HQ";
     String title = "FFA Monster";
     String message = "Hello Daeva! You have proved your abilities in the FFA. We want to give you some reward for your participation.";
     String service = "[FFA]";
     int rewardItem = 186000223;
     mail = LetterType.NORMAL;
     SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, rewardItem, 50, 1000000, mail);
 }
	 
	 @Override
		public boolean onReviveEvent(Player player) {
			PlayerReviveService.revive(player, 25, 25, false, 0);
			player.getGameStats().updateStatsAndSpeedVisually();
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
			TeleportService2.moveToBindLocation(player, true);
			return true;
		}
}
