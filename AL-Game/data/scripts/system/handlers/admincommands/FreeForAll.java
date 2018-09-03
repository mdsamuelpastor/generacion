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
package admincommands;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.global.FFAService;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;

/**
 * @author Maestross
 */
public class FreeForAll extends ChatCommand{
                  
    public FreeForAll(){
        super("ffa");
    }

    @Override
    public void execute(final Player player, String... params) {
        if(params.length < 1 || params == null){
            onFail(player, null);
            return;
        }
                                    
        if(params[0].equals("enter")){
            if(player.isInTeam()){
                PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.FFA_IS_ALREADY_IN_TEAM));
                return;
            }
                                                      
            if(player.isInFFAPVP()){
                PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.FFA_IS_ALREADY_IN));
                return;
            }
                                                      
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.FFA_FROZEN_MESSAGE));
            player.getEffectController().setAbnormal(AbnormalState.SLEEP.getId());
            player.getEffectController().updatePlayerEffectIcons();
            player.getEffectController().broadCastEffects();
            ThreadPoolManager.getInstance().schedule(new Runnable(){

            @Override
            public void run() {
                player.getEffectController().unsetAbnormal(AbnormalState.SLEEP.getId());
                player.getEffectController().updatePlayerEffectIcons();
                player.getEffectController().broadCastEffects();
                                                                                          
                FFAService.getInstance().TeleIn(player);
            }
            }, 10000);
            return;
        }
                                    
        if(params[0].equals("leave")){
        	  if (player.getAggroList() != null)
        	  	return;
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.FFA_FROZEN_MESSAGE));
            player.getEffectController().setAbnormal(AbnormalState.SLEEP.getId());
            player.getEffectController().updatePlayerEffectIcons();
            player.getEffectController().broadCastEffects();
                                                      
            ThreadPoolManager.getInstance().schedule(new Runnable(){

            @Override
            public void run() {
                player.getEffectController().unsetAbnormal(AbnormalState.SLEEP.getId());
                player.getEffectController().updatePlayerEffectIcons();
                player.getEffectController().broadCastEffects();
                                                                                          
                FFAService.getInstance().TeleOut(player);
            }
            }, 10000);
            return;
        }
                                    
        if(params[0].equals("info")){
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.FFA_CURRENT_PLAYERS) + FFAService.getInstance().getPlayerCount());
        }
    }

    @Override
    public void onFail(Player player, String message) {
        PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.FFA_USAGE));
    }
}