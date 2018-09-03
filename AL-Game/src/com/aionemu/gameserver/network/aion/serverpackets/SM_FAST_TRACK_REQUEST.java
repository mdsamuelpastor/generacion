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

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.transfers.FastTrack;


/**
 * @author Maestross
 *
 */
public class SM_FAST_TRACK_REQUEST extends AionServerPacket {

	private FastTrack settings;
  public SM_FAST_TRACK_REQUEST(FastTrack settings){
              this.settings = settings;
  }
  
  @Override
  public void writeImpl(AionConnection con){
              writeH(settings.getServerId()); //Server Id
              writeH(0x00); //unk
              writeH(settings.getIconSet()); //257 is Icon - Other Without Icon.
              writeD(settings.getMinLevel()); //Min Level 
              writeD(settings.getMaxLevel()); //Max Level 
              writeD(1); //unk
              writeC(0); //unk
  }
}
