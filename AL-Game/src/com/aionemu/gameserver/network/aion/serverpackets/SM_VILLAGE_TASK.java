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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.ChallengeTaskService;


/**
 * @author Maestross
 * 
 * doneCount = how much people has completed it
 * repCount = how often it is possible repeatable???
 * maxCount = how much people can complete it
 */
public class SM_VILLAGE_TASK extends AionServerPacket {

	private int type;
	private int type2;
	private int villageId;
	private int playerObjectId;
	private int timestamp;
	private int taskId;
	
	public SM_VILLAGE_TASK(int type, int villageId, int type2, int playerObjectId, int timestamp) {
		this.type = type;
		this.villageId = villageId;
		this.type2 = type2;
		this.playerObjectId = playerObjectId;
		this.timestamp = timestamp;
	}

	/**
	 * @param type3
	 * @param taskId
	 * @param villageId2
	 * @param type22
	 * @param playerObjectId2
	 * @param timestamp2
	 */
	public SM_VILLAGE_TASK(int type, int taskId, int villageId, int type2, int playerObjectId, int timestamp) {
		this.type = type;
		this.taskId = taskId;
		this.villageId = villageId;
		this.type2 = type2;
		this.playerObjectId = playerObjectId;
		this.timestamp = timestamp;
	}

	protected void writeImpl(AionConnection con) {
		Player player = con.getActivePlayer();
		Legion legion = con.getActivePlayer().getLegion();
		writeC(type);//type
    switch(type) {
    	case 2:
    	  writeD(villageId);
    	  writeC(type2);
    		switch (type2) {
    			case 1://legionTasks
    				writeD(playerObjectId);
    				writeD(timestamp);
    				writeH(3);//taskSize
    			  switch(player.getLegion().getLegionLevel()) {
    			  	case 5:
    			  		switch (player.getRace()) {
    			  			case ELYOS:
    			  				writeD(0);//unk
            				writeD(300);
            				writeC(0);
            				writeH(0);
            				writeD(timestamp);
            				break;
    			  			case ASMODIANS:
    			  				writeD(0);//unk
            				writeD(400);
            				writeC(0);
            				writeH(0);
            				writeD(timestamp);
            				break;
									default:
										break;
    			  		}
        				break;
    			  	case 6:
    			  		switch (player.getRace()) {
    			  			case ELYOS:
    			  				writeD(0);//unk
            				writeD(301);
            				writeC(0);
            				writeH(0);
            				writeD(timestamp);
            				break;
    			  			case ASMODIANS:
    			  				writeD(0);//unk
            				writeD(401);
            				writeC(0);
            				writeH(0);
            				writeD(timestamp);
            				break;
									default:
										break;
    			  		}
        				break;
    			  	case 7:
    			  		switch (player.getRace()) {
    			  			case ELYOS:
    			  				writeD(0);//unk
            				writeD(302);
            				writeC(0);
            				writeH(0);
            				writeD(timestamp);
            				break;
    			  			case ASMODIANS:
    			  				writeD(0);//unk
            				writeD(402);
            				writeC(0);
            				writeH(0);
            				writeD(timestamp);
            				break;
									default:
										break;
    			  		}
        				break;
    			  	case 8:
    			  		switch (player.getRace()) {
    			  			case ELYOS:
    			  				writeD(0);//unk
            				writeD(303);
            				writeC(0);
            				writeH(0);
            				writeD(timestamp);
            				writeD(0);//unk
            				writeD(304);
            				writeC(0);
            				writeH(0);
            				writeD(timestamp);
            				writeD(0);//unk
            				writeD(305);
            				writeC(0);
            				writeH(0);
            				writeD(timestamp);
            				break;
    			  			case ASMODIANS:
    			  				writeD(0);//unk
            				writeD(403);
            				writeC(0);
            				writeH(0);
            				writeD(timestamp);
            				writeD(0);//unk
            				writeD(404);
            				writeC(0);
            				writeH(0);
            				writeD(timestamp);
            				writeD(0);//unk
            				writeD(405);
            				writeC(0);
            				writeH(0);
            				writeD(timestamp);
            				break;
									default:
										break;
    			  		}
        			default:
        				writeD(0);//unk
        				writeD(0);
        				writeC(0);
        				writeH(0);
        				writeD(timestamp);
        				break;
    			  }
    				break;
    			case 2://villageTasks
    				writeD(playerObjectId);
    				writeD(timestamp);
    				writeH(0);//taskSize
    				break;
    		}
    		break;
    	case 7:
    		writeD(villageId);
    		writeC(type2);
    		writeD(playerObjectId);
    		writeC(0);//unk
    		writeH(0);//unk 0x00
    		writeC(0);//unk 0x00
    		writeD(taskId);//taskId
    		writeH(3);//questCount
    		switch(player.getLegion().getLegionLevel()) {
			  	case 5:
			  		switch (taskId) {
			  			case 300:
			  				writeD(17000);//questId
        				writeH(ChallengeTaskService.maxCountQuest1);//maxCount
        				writeD(6);//repCount
        				writeH(legion.getDoneCountQuest1());//doneCount
        				writeD(17001);//questId
        				writeH(ChallengeTaskService.maxCountQuest2);//maxCount
        				writeD(12);//repCount
        				writeH(legion.getDoneCountQuest2());//doneCount
        				writeD(17002);//questId
        				writeH(ChallengeTaskService.maxCountQuest3);//maxCount
        				writeD(42);//repCount
        				writeH(legion.getDoneCountQuest3());//doneCount
        				break;
			  			case 400:
			  				writeD(27000);//questId
			  				writeH(ChallengeTaskService.maxCountQuest1);//maxCount
        				writeD(6);//repCount
        				writeH(legion.getDoneCountQuest1());//doneCount
        				writeD(27001);//questId
        				writeH(ChallengeTaskService.maxCountQuest2);//maxCount
        				writeD(12);//repCount
        				writeH(legion.getDoneCountQuest2());//doneCount
        				writeD(27002);//questId
        				writeH(ChallengeTaskService.maxCountQuest3);//maxCount
        				writeD(42);//repCount
        				writeH(legion.getDoneCountQuest3());//doneCount
        				break;
							default:
								break;
			  		}
    				break;
			  	case 6:
			  		switch (taskId) {
			  			case 301:
			  				writeD(17003);//questId
			  				writeH(ChallengeTaskService.maxCountQuest4);//maxCount
        				writeD(12);//repCount
        				writeH(legion.getDoneCountQuest4());//doneCount
        				writeD(17004);//questId
        				writeH(ChallengeTaskService.maxCountQuest5);//maxCount
        				writeD(24);//repCount
        				writeH(legion.getDoneCountQuest5());//doneCount
        				writeD(17005);//questId
        				writeH(ChallengeTaskService.maxCountQuest6);//maxCount
        				writeD(84);//repCount
        				writeH(legion.getDoneCountQuest6());//doneCount
        				break;
			  			case 401:
			  				writeD(27003);//questId
			  				writeH(ChallengeTaskService.maxCountQuest4);//maxCount
        				writeD(12);//repCount
        				writeH(legion.getDoneCountQuest4());//doneCount
        				writeD(27004);//questId
        				writeH(ChallengeTaskService.maxCountQuest5);//maxCount
        				writeD(24);//repCount
        				writeH(legion.getDoneCountQuest5());//doneCount
        				writeD(27005);//questId
        				writeH(ChallengeTaskService.maxCountQuest6);//maxCount
        				writeD(84);//repCount
        				writeH(legion.getDoneCountQuest6());//doneCount
        				break;
							default:
								break;
			  		}
    				break;
			  	case 7:
			  		switch (taskId) {
			  			case 302:
			  				writeD(17006);//questId
			  				writeH(ChallengeTaskService.maxCountQuest7);//maxCount
        				writeD(24);//repCount
        				writeH(legion.getDoneCountQuest7());//doneCount
        				writeD(17007);//questId
        				writeH(ChallengeTaskService.maxCountQuest8);//maxCount
        				writeD(48);//repCount
        				writeH(legion.getDoneCountQuest8());//doneCount
        				writeD(17008);//questId
        				writeH(ChallengeTaskService.maxCountQuest9);//maxCount
        				writeD(168);//repCount
        				writeH(legion.getDoneCountQuest9());//doneCount
        				break;
			  			case 402:
			  				writeD(27006);//questId
			  				writeH(ChallengeTaskService.maxCountQuest7);//maxCount
        				writeD(24);//repCount
        				writeH(legion.getDoneCountQuest7());//doneCount
        				writeD(27007);//questId
        				writeH(ChallengeTaskService.maxCountQuest8);//maxCount
        				writeD(48);//repCount
        				writeH(legion.getDoneCountQuest8());//doneCount
        				writeD(27008);//questId
        				writeH(ChallengeTaskService.maxCountQuest9);//maxCount
        				writeD(168);//repCount
        				writeH(legion.getDoneCountQuest9());//doneCount
        				break;
							default:
								break;
			  		}
    				break;
			  	case 8:
			  		switch (taskId) {
			  			case 303:
			  				writeD(17009);//questId
			  				writeH(ChallengeTaskService.maxCountQuest10);//maxCount
        				writeD(12);//repCount
        				writeH(legion.getDoneCountQuest10());//doneCount
        				writeD(17010);//questId
        				writeH(ChallengeTaskService.maxCountQuest11);//maxCount
        				writeD(48);//repCount
        				writeH(legion.getDoneCountQuest11());//doneCount
        				writeD(17011);//questId
        				writeH(ChallengeTaskService.maxCountQuest12);//maxCount
        				writeD(168);//repCount
        				writeH(legion.getDoneCountQuest12());//doneCount
        				break;
			  			case 304:
			  				writeD(17012);//questId
			  				writeH(ChallengeTaskService.maxCountQuest13);//maxCount
        				writeD(84);//repCount
        				writeH(legion.getDoneCountQuest13());//doneCount
        				writeD(17013);//questId
        				writeH(ChallengeTaskService.maxCountQuest14);//maxCount
        				writeD(84);//repCount
        				writeH(legion.getDoneCountQuest14());//doneCount
        				writeD(17014);//questId
        				writeH(ChallengeTaskService.maxCountQuest15);//maxCount
        				writeD(84);//repCount
        				writeH(legion.getDoneCountQuest15());//doneCount
        				break;
			  			case 305:
			  				writeD(17015);//questId
			  				writeH(ChallengeTaskService.maxCountQuest16);//maxCount
        				writeD(24);//repCount
        				writeH(legion.getDoneCountQuest16());//doneCount
        				writeD(17016);//questId
        				writeH(ChallengeTaskService.maxCountQuest17);//maxCount
        				writeD(24);//repCount
        				writeH(legion.getDoneCountQuest17());//doneCount
        				writeD(17017);//questId
        				writeH(ChallengeTaskService.maxCountQuest18);//maxCount
        				writeD(24);//repCount
        				writeH(legion.getDoneCountQuest18());//doneCount
			  			case 403:
			  				writeD(27009);//questId
			  				writeH(ChallengeTaskService.maxCountQuest10);//maxCount
        				writeD(12);//repCount
        				writeH(legion.getDoneCountQuest10());//doneCount
        				writeD(27010);//questId
        				writeH(ChallengeTaskService.maxCountQuest11);//maxCount
        				writeD(48);//repCount
        				writeH(legion.getDoneCountQuest11());//doneCount
        				writeD(27011);//questId
        				writeH(ChallengeTaskService.maxCountQuest12);//maxCount
        				writeD(168);//repCount
        				writeH(legion.getDoneCountQuest12());//doneCount
			  			case 404:
			  				writeD(27012);//questId
			  				writeH(ChallengeTaskService.maxCountQuest13);//maxCount
        				writeD(84);//repCount
        				writeH(legion.getDoneCountQuest13());//doneCount
        				writeD(27013);//questId
        				writeH(ChallengeTaskService.maxCountQuest14);//maxCount
        				writeD(84);//repCount
        				writeH(legion.getDoneCountQuest14());//doneCount
        				writeD(27014);//questId
        				writeH(ChallengeTaskService.maxCountQuest15);//maxCount
        				writeD(84);//repCount
        				writeH(legion.getDoneCountQuest15());//doneCount
			  			case 405:
			  				writeD(27015);//questId
			  				writeH(ChallengeTaskService.maxCountQuest16);//maxCount
        				writeD(24);//repCount
        				writeH(legion.getDoneCountQuest16());//doneCount
        				writeD(27016);//questId
        				writeH(ChallengeTaskService.maxCountQuest17);//maxCount
        				writeD(24);//repCount
        				writeH(legion.getDoneCountQuest17());//doneCount
        				writeD(27017);//questId
        				writeH(ChallengeTaskService.maxCountQuest18);//maxCount
        				writeD(24);//repCount
        				writeH(legion.getDoneCountQuest18());//doneCount
							default:
								break;
			  		}
    			default:
    				writeD(0);//questId
    				writeH(0);//maxCount
    				writeD(0);//repCount
    				writeH(0);//doneCount
    				break;
			  }
    		break;
    	default:
    		break;
    }
    
  }
}
