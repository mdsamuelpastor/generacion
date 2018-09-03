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
package com.aionemu.gameserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.FastTrackConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SERVER_IDS;
import com.aionemu.gameserver.services.transfers.FastTrack;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * 
 * @author -Enomine-
 *
 */

public class FastTrackService {
	
    private static final FastTrackService instance = new FastTrackService();
    
    private Logger log = LoggerFactory.getLogger(FastTrackService.class);
    public static FastTrackService getInstance(){
    	
                return instance;
    }
    
    /**
     * 
     * @param player 
     */
    public void checkAuthorizationRequest(Player player){
                int upto = FastTrackConfig.FAST_TRACK_UPTO;
                
                if(player.getLevel() > upto){
                            return;
                }
                PacketSendUtility.sendPacket(player, new SM_SERVER_IDS(new FastTrack(FastTrackConfig.FAST_TRACK_SERVERID, true, 1, upto)));
    }
    
    public void handleMoveThere(Player player){
                log.info("Fast Track Service : Move Player " + player.getName());
    }
}
