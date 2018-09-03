
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.FastTrackService;

/**
 * 
 * @author -Enomine
 *
 */

public class CM_FAST_TRACK extends AionClientPacket{

            public CM_FAST_TRACK(int opcode, State state, State... states){
                        super(opcode, state, states);
            }
            
            @Override
            protected void readImpl() {
            }

            @Override
            protected void runImpl() {
                        Player requested = getConnection().getActivePlayer();
                        FastTrackService.getInstance().handleMoveThere(requested);
            }
            
}
