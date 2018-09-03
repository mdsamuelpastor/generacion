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
package instance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.summons.UnsummonType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;


@InstanceID(320150000)
public class PadmarashkaCaveInstance extends GeneralInstanceHandler
{	
	private final AtomicInteger specNpcKilled = new AtomicInteger();
	private List<Integer> movies = new ArrayList<Integer>();
    
    @Override
    public void onDie(Npc npc) {
        Player player = npc.getAggroList().getMostPlayerDamage();
      switch (npc.getObjectTemplate().getTemplateId()) {
			case 282613: //Padmarashka's Eggs.
			case 282614: //Huge Padmarashka's Eggs.
				int killedCount = specNpcKilled.incrementAndGet();
				if (killedCount < 8) {
					PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "Padmarashka on alert!!!", ChatType.BRIGHT_YELLOW_CENTER), true);
				} if (killedCount == 8) {
					sendMovie(player, 488);
					PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "<Padmarashka> appeared", ChatType.BRIGHT_YELLOW_CENTER), true);
					spawn(281450, 577.09973f, 176.46365f, 66.11253f, (byte) 37); //Padmarashka.
				}
            break;
			}
    }
    
    @Override
  	public void onEnterZone(Player player, ZoneInstance zone) {
  		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("REIAN_FOOTHOLD_320150000")) {
  			PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player,
  				"Kill 8 <Padmarashka's Eggs> or <Huge Padmarashka's Eggs> to appear <Padmarashka>", ChatType.BRIGHT_YELLOW_CENTER), true);
  		}  		
  	}		
	
	 private void sendMovie(Player player, int movie) {
        if (!movies.contains(movie)) {
            movies.add(movie);
            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
        }
    }
    
    @Override
  	public boolean onReviveEvent(Player player) {
  		PlayerReviveService.revive(player, 25, 25, false, 0);
  		player.getGameStats().updateStatsAndSpeedVisually();
  		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
  		TeleportService2.teleportTo(player, mapId, instanceId, 372.50397f, 531.5783f, 67.34865f, (byte) 97);
  		return true;
  	}
	
		@Override
    public boolean onDie(final Player player, Creature lastAttacker) {
        Summon summon = player.getSummon();
        if (summon != null) {
            summon.getController().release(UnsummonType.UNSPECIFIED);
        }
        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, lastAttacker == null ? 0 : lastAttacker.getObjectId()), true);
        PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
        return true;
    }
}