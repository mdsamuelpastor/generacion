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
package com.aionemu.gameserver.services.global;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.utils3d.Point3D;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Maestross
 */
 
public class FFAService implements FFAStruct{

    WorldMapInstance instance = null;
    protected Map<Integer, WorldPosition> previousLocations = new HashMap<Integer, WorldPosition>();
    private static final FFAService service = new FFAService();
    String playerName = "";
                  
    public static FFAService getInstance(){
        return service;
    }
                  
    static Point3D[] positions = new Point3D[] {
        new Point3D(1932.19f, 1156.8029f, 280.75787f),
        new Point3D(1932.2992f, 1215.4747f, 280.71106f),
        new Point3D(1932.1354f, 1244.3516f, 279.94656f),
        new Point3D(1960.2673f, 1232.3081f, 270.39243f),
        new Point3D(1932.2255f, 1175.5641f, 272.16193f),
        new Point3D(1874.9468f, 1205.3832f, 270.33957f),
    };
    
    static Point3D[] positions2 = new Point3D[] {
      new Point3D(1927.4425f, 871.5636f, 231.37827f),
      new Point3D(1930.0623f, 946.61255f, 230.35043f),
      new Point3D(1892.2085f, 978.4068f, 230.54176f),
      new Point3D(1936.9454f, 946.82733f, 222.95479f),
      new Point3D(1956.6982f, 1004.1372f, 230.42795f),
      new Point3D(1976.7557f, 946.06354f, 230.35043f),
  };
                  
    public Point3D randomSpawn() {
        int pos = Rnd.get(positions.length - 1);
        return positions[pos];
    }
    
    public Point3D randomSpawn2() {
      int pos = Rnd.get(positions2.length - 1);
      return positions2[pos];
  }
                  
    @Override
    public void TeleIn(Player player) {
    	Calendar calendar = Calendar.getInstance();  
    	if(instance == null) {
            instance = createInstance();                                   
            previousLocations.put(player.getObjectId(), player.getPosition());                 
            Point3D loc = randomSpawn();
            Point3D loc2 = randomSpawn2();
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
              TeleportService2.teleportTo(player, FFAStruct.worldId, instance.getInstanceId(), (float)loc.x, (float)loc.y, (float)loc.z);
            else
            	TeleportService2.teleportTo(player, FFAStruct.worldId, instance.getInstanceId(), (float)loc2.x, (float)loc2.y, (float)loc2.z);
            resetStart(player);
		}
		else {
		    previousLocations.put(player.getObjectId(), player.getPosition());  
		    Point3D loc = randomSpawn();
		    Point3D loc2 = randomSpawn2();
		    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
          TeleportService2.teleportTo(player, FFAStruct.worldId, instance.getInstanceId(), (float)loc.x, (float)loc.y, (float)loc.z);
        else
        	TeleportService2.teleportTo(player, FFAStruct.worldId, instance.getInstanceId(), (float)loc2.x, (float)loc2.y, (float)loc2.z);
		    resetStart(player);
		}
    }

    @Override
    public void TeleOut(Player player) {
        if(player.isInFFAPVP()){
            WorldPosition pos = previousLocations.get(player.getObjectId());
            if (pos != null) {
                TeleportService2.teleportTo(player, pos.getMapId(), pos.getX(), pos.getY(), pos.getZ());
                previousLocations.remove(player.getObjectId());
            }
            else    
			TeleportService2.moveToBindLocation(player, true);
			resetEnd(player);
        }
        else {
            PacketSendUtility.sendBrightYellowMessageOnCenter(player, LanguageHandler.translate(CustomMessageId.FFA_YOUR_NOT_IN));
        }
    }

    @Override
    public ScheduledFuture<?> announceTask(int delayInMinutes) {
        return ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable(){

        @Override
        public void run() {
            if (instance != null) {
                World.getInstance().doOnAllPlayers(new Visitor<Player>()  {

                @Override
                public void visit(Player object) {
                    PacketSendUtility.sendBrightYellowMessageOnCenter(object, "FFA: " + LanguageHandler.translate(CustomMessageId.FFA_ANNOUNCE_1) + getPlayerCount() + LanguageHandler.translate(CustomMessageId.FFA_ANNOUNCE_2));	
                }
                });                
            } else {
                    World.getInstance().doOnAllPlayers(new Visitor<Player>() {

                    @Override
                    public void visit(Player object) {
                        PacketSendUtility.sendBrightYellowMessageOnCenter(object, LanguageHandler.translate(CustomMessageId.FFA_ANNOUNCE_3));
                    }
                    });
                }
        }
        }, delayInMinutes / 2 * 1000 * 60, delayInMinutes / 2 * 1000 * 60);
    }

    @Override
    public int getPlayerCount() {
        if(instance == null)
            return 0;
        else
            return instance.getPlayersInside().size();
    }

    @Override
    public boolean isEnemy(Player effector, Player effected) {
        if (effector.isInFFAPVP() && effected.isInFFAPVP() && effector.getObjectId() != effected.getObjectId() && effector.isCasting() && effected.isCasting())
            return true;
        else
            return false;
                                                      
    }

    @Override
    public boolean isInFFA(Player pl) {
        if(instance != null)
            return instance.isRegistered(pl.getObjectId());
        else if(pl.getWorldId() == worldId && pl.isInFFAPVP())
            return true;
        else
            return false;
    }

    @Override
    public boolean onDieDefault(Creature attacker, Creature attacked) {
        if (attacker instanceof Player && attacked instanceof Player) {
            Player winner = (Player) attacker;
            Player loser = (Player) attacked;
                                                      
            winner.setSpecialKills(+1);
            AbyssPointsService.addAp(winner, 20000);
			ItemService.addItem(winner, 186000147, 10);
            loser.setSpecialKills(0);
            throwKillMessage(winner, loser);
            throwStreakAnnouncement(winner);
            onDie(winner, loser);
            return true;
        }
        else if (attacked instanceof Player) {
            Player loser = (Player) attacked;
            Player winner = null;
            if (attacker instanceof Summon){
                winner = ((Summon) attacker).getMaster();
            }
        if (winner == null)
            winner = loser.getAggroList().getMostPlayerDamage();
                                                      
            winner.setSpecialKills(+1);
            AbyssPointsService.addAp(winner, 20000);
			ItemService.addItem(winner, 186000147, 10);
            loser.setSpecialKills(0);
            throwKillMessage(winner, loser);
            throwStreakAnnouncement(winner);
            onDie(winner, loser);
            return true;
        } else {
            playerAnnounce(LanguageHandler.translate(CustomMessageId.FFA_GHOST_KILL) + attacked.getName());
        }
        return false;
    }
    
    public boolean onDie(Player winner, final Player loser) {
        PacketSendUtility.broadcastPacket(loser, new SM_EMOTION(loser, EmotionType.DIE, 0, winner == null ? 0 : winner.getObjectId()), true);
        loser.setSpecialKills(0);
        PacketSendUtility.sendPacket(loser, new SM_DIE(loser.haveSelfRezEffect(), loser.haveSelfRezItem(), 0, 8));
        return true;
    }
                  
    private void throwKillMessage(Player p1, Player p2){
        playerAnnounce(p1.getName() + LanguageHandler.translate(CustomMessageId.FFA_KILL_MESSAGE) + p2.getName()+"!");
    }
                  
    private void throwStreakAnnouncement(Player winner){
        if(winner.getSpecialKills() == 5) {
            playerAnnounce(winner.getName() + LanguageHandler.translate(CustomMessageId.FFA_KILL_NAME_1));
            AbyssPointsService.addAp(winner, 50000);
			ItemService.addItem(winner, 186000147, 1);
        } else if (winner.getSpecialKills() == 10) {
            playerAnnounce(winner.getName() + LanguageHandler.translate(CustomMessageId.FFA_KILL_NAME_2));
            AbyssPointsService.addAp(winner, 60000);
			ItemService.addItem(winner, 186000147, 5);
        } else if (winner.getSpecialKills() == 15) {
            playerAnnounce(winner.getName() + LanguageHandler.translate(CustomMessageId.FFA_KILL_NAME_3));
            AbyssPointsService.addAp(winner, 70000);
			ItemService.addItem(winner, 186000147, 10);
        } else if (winner.getSpecialKills() == 30) {
            playerAnnounce(winner.getName() + LanguageHandler.translate(CustomMessageId.FFA_KILL_NAME_4));
            AbyssPointsService.addAp(winner, 80000);
			ItemService.addItem(winner, 186000147, 15);
        } else if (winner.getSpecialKills() >= 45) {
            playerAnnounce(winner.getName() + LanguageHandler.translate(CustomMessageId.FFA_KILL_NAME_5) + winner.getSpecialKills() + LanguageHandler.translate(CustomMessageId.FFA_KILL_NAME_6));
            AbyssPointsService.addAp(winner, 100000);
			ItemService.addItem(winner, 186000147, 20);
        }
    }

    @Override
    public void onLogin(Player p) {
        p.setInFFAPVP(false);
        p.setSpecialKills(0);
        TeleportService2.moveToBindLocation(p, true);
    }

    @Override
    public void playerAnnounce(final String message) {
        instance.doOnAllPlayers(new Visitor<Player>(){

        @Override
        public void visit(Player object) {
            PacketSendUtility.sendBrightYellowMessageOnCenter(object, message);
        }
        });
    }

    @Override
    public void resetEnd(Player player) {		
        player.setInFFAPVP(false);
        player.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.HP, player.getLifeStats().getMaxHp() + 1);
        player.getLifeStats().increaseMp(SM_ATTACK_STATUS.TYPE.MP, player.getLifeStats().getMaxMp() + 1);
        player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
        player.getKnownList().clear();
        player.getKnownList().doUpdate();
    }

    @Override
    public void resetStart(Player player) {
        player.getController().cancelCurrentSkill();
        player.setSpecialKills(0);
        player.setInFFAPVP(true);
        player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
        player.getCommonData().setDp(0);
        instance.register(player.getObjectId());
    }
                  
    protected WorldMapInstance createInstance() {
        WorldMapInstance instance = InstanceService.getNextAvailableInstance(worldId);
        return instance;
    }
}