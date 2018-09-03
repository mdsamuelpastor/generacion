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

import java.util.List;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.stats.container.PlayerLifeStats;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceMember;
import com.aionemu.gameserver.model.team2.common.legacy.PlayerAllianceEvent;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author Sarynth (Thx Rhys2002 for Packets), Maestross
 * @reworked  -Enomine-
 */
public class SM_ALLIANCE_MEMBER_INFO extends AionServerPacket {

	private Player player;
	private PlayerAllianceEvent event;
	private final int allianceId;
	public SM_ALLIANCE_MEMBER_INFO(PlayerAllianceMember member, PlayerAllianceEvent event) {
		this.player = member.getObject();
		this.event = event;
		this.allianceId = member.getAllianceId();
		member.getObjectId();
	}

	@Override
	protected void writeImpl(AionConnection con) {
	    PlayerLifeStats pls = player.getLifeStats();
		PlayerCommonData pcd = player.getCommonData();
		WorldPosition wp = pcd.getPosition();

		/**
		 * Required so that when member is disconnected, and his playerAllianceGroup slot is changed, he will continue to
		 * appear as disconnected to the alliance.
		 */
		if (event == PlayerAllianceEvent.ENTER && !player.isOnline())
			event = PlayerAllianceEvent.ENTER_OFFLINE;
		if (event == PlayerAllianceEvent.UPDATE && !player.isOnline())
		    event = PlayerAllianceEvent.DISCONNECTED;

		writeD(allianceId);
		writeD(player.getObjectId());
		if (player.isOnline()) {
			writeD(pls.getMaxHp());
			writeD(pls.getCurrentHp());
			writeD(pls.getMaxMp());
			writeD(pls.getCurrentMp());
			writeD(pls.getMaxFp());
			writeD(pls.getCurrentFp());
			writeD(0);
			writeD(wp.getMapId());
		    writeD(wp.getMapId());
		    writeF(wp.getX());
		    writeF(wp.getY());
		    writeF(wp.getZ());
		}
		else {
			writeD(0);
            writeD(0);
            writeD(0);
            writeD(0);
            writeD(0);
            writeD(0);
            writeD(0);
            writeD(0);
            writeD(0);
            writeF(0.0F);
            writeF(0.0F);
            writeF(0.0F);
		}	
		writeC(pcd.getPlayerClass().getClassId());
		writeC(pcd.getGender().getGenderId());
		writeC(pcd.getLevel());
		writeC(event.getId());
		writeH(0);//new
		writeC(0);//new
		//writeH(player.isOnline() ? 1 : 0); // TODO channel? C !!-Enomine-
		//writeC(player.isMentor() ? 0x01 : 0x00); //Mentor not available in Alliance
		switch (event.ordinal()) {
		    case 1:
            case 2:
            case 3:
            case 4:
			    break;
            case 5:
            case 6:
			    writeS(pcd.getName()); // name
				break;
            case 7:
            case 8:
            	break;
            case 13:
            	writeD(0);//new 
            	writeD(0);//new 
            	if(player.isOnline()){
            		List<Effect> abnormalEffects1 = player.getEffectController().getAbnormalEffects();
            		writeH(abnormalEffects1.size());//new buff count
            		for(Effect effect : abnormalEffects1){
            			writeD(effect.getEffectorId());//playerId
            			writeH(effect.getSkillId());//skillID?
            			writeC(effect.getSkillLevel());//Skilllevel?
            			writeC(effect.getTargetSlot());//TargetSlot?
            			writeD(effect.getRemainingTime());//remainingTime
            		}
            		}
            	else{
            		writeH(0);
            	}
            	break;
            case 10:
            case 11:
            case 12:
			case 9:
			    writeS(pcd.getName()); // name
				writeD(0x00); // unk
				writeD(0x00); // unk
				if (player.isOnline()) {
					List<Effect> abnormalEffects = player.getEffectController().getAbnormalEffects();
					writeH(abnormalEffects.size());
					for (Effect effect : abnormalEffects) {
						writeD(effect.getEffectorId());
						writeH(effect.getSkillId());
						writeC(effect.getSkillLevel());
						writeC(effect.getTargetSlot());
						writeD(effect.getRemainingTime());
					}
				}
				else {
					writeH(0);
				}
				break;
			case 14:
			    writeS(pcd.getName());
				break;
		}
	}

}