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
package com.aionemu.gameserver.skillengine.model;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_STANCE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 *
 * @author GoodT
 */
public class SkillReplace {
                  
                  public static void checkThis(Player player, int skillId) {
                                    SkillTemplate base = DataManager.SKILL_DATA.getSkillTemplate(skillId);
                                    if(base.getProperties().getAbsorbId() != null) {
                                        checkSkillAbsorb(player, skillId);
                                    } 
                  }
                  
                  /**
                   * 
                   * @param player
                   * @param skillId
                   * @param targetSkillId 
                   */
                  private static void sendConflict(Player player, int skillId, int targetSkillId) {
                                    SkillTemplate base = DataManager.SKILL_DATA.getSkillTemplate(skillId);
                                    SkillTemplate target = DataManager.SKILL_DATA.getSkillTemplate(base.getProperties().getAbsorbId());
                                    if(player.getTarget().getObjectId() != player.getObjectId()) {
                                    	Player p = (Player) player.getTarget();
                                      PacketSendUtility.sendPacket(p, new SM_SYSTEM_MESSAGE(1201087, target.getNameId(), player.getName()));                                                      
                                    } 
                                    else {
                                      PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1201079, target.getNameId(), base.getNameId()));
                                    }
                  }
                  
                  /**
                   * 
                   * @param player
                   * @param skillId 
                   */
                  private static void checkSkillAbsorb(Player player, int skillId) {
                                    SkillTemplate baseTemplate = DataManager.SKILL_DATA.getSkillTemplate(skillId);
                                    SkillTemplate absorbTemplate = DataManager.SKILL_DATA.getSkillTemplate(baseTemplate.getProperties().getAbsorbId());
                                    if(player.getTarget().getObjectId() != player.getObjectId() && !absorbTemplate.isToggle()) {
                                        player = (Player) player.getTarget();
                                    }                                    
                                    if(player.getEffectController().hasAbnormalEffect(absorbTemplate.getProperties().getAbsorbId())) {
                                    	boolean absorbIsHigherThanBase = baseTemplate.getSkillId() < absorbTemplate.getSkillId();
                                      if(absorbIsHigherThanBase){                                                                        
                                        sendConflict(player, skillId, absorbTemplate.getSkillId());
                                      } 
                                      else {
                                      	int desc = baseTemplate.getNameId();
                                        int desc2 = absorbTemplate.getNameId();                                                                        
                                        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_CONFLICT_WITH_OTHER_SKILL(desc2, desc));
                                        player.getEffectController().removeEffect(absorbTemplate.getSkillId());                                                                        
                                      }                                                      
                                    } 
                                    else if(player.getEffectController().isNoshowPresentBySkillId(absorbTemplate.getSkillId())) {
                                    	player.getEffectController().removeNoshowEffect(absorbTemplate.getSkillId());
                                      if (player.getController().getStanceSkillId() == absorbTemplate.getSkillId()) {
                                                        PacketSendUtility.sendPacket(player, new SM_PLAYER_STANCE(player, 0));
                                                        player.getController().startStance(0);
                                      }
                                      PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1201078, absorbTemplate.getNameId(), baseTemplate.getNameId()));
                                    }
                  }                  
}