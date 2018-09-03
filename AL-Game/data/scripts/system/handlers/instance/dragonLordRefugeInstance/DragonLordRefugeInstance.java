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
package instance.dragonLordRefugeInstance;

import java.util.*;

import com.aionemu.gameserver.model.*;
import com.aionemu.gameserver.instance.handlers.*;
import com.aionemu.gameserver.model.gameobjects.*;
import com.aionemu.gameserver.model.gameobjects.player.*;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.*;
import com.aionemu.gameserver.model.templates.spawns.*;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.utils.*;
import com.aionemu.gameserver.world.*;

/**
 * @author Maestross
 */

@InstanceID(300520000)
public class DragonLordRefugeInstance extends GeneralInstanceHandler
{
	protected boolean isInstanceDestroyed = false;
	private List<Integer> movies = new ArrayList<Integer>();
	
	@Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
		spawn(219407, 468.00766f, 514.5526f, 417.40436f, (byte) 0); //Calindi Flamelord.
		spawn(800429, 496.42648f, 516.493f, 240.26653f, (byte) 0); //Kahrun (Reian Leader).
    }
	
	@Override
    public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
            case 219407: //Calindi Flamelord.
				despawnNpc(npc);
                if (player != null) {
				    switch (player.getRace()) {
				        case ELYOS:
						    spawnTiamat();
						    spawnRush();
							sendMsg(1401531);
							spawnFissurefang();
							spawnGodEmpyreanEly();
				        break;
				        case ASMODIANS:
						    spawnTiamat();
						    spawnRush();
							sendMsg(1401532);
							spawnFissurefang();
							spawnGodEmpyreanAsmo();
				        break;
							default:
								break;
					}
				}
            break;
			/*case 219409: //Tiamat.
				despawnNpc(219409);
                if (player != null) {
				    switch (player.getRace()) {
				        case ELYOS:
						    spawnTiamatDying();
						    sendMsg(1401540);
				        break;
				        case ASMODIANS:
						    spawnTiamatDying();
						    sendMsg(1401541);
				        break;
					}
				}
            break;*/
			case 219410: //Tiamat Dying.
                if (player != null) {
				    switch (player.getRace()) {
				        case ELYOS:
				            sendMovie(player, 493);
							spawnChestTiamatDying();
							spawn(800350, 504.4801f, 515.12964f, 417.40436f, (byte) 60); //Kaisinel.
							despawnNpc(getNpc(701502)); //Siels Relict
				        break;
				        case ASMODIANS:
				            sendMovie(player, 494);
							spawnChestTiamatDying();
							spawn(800356, 504.4801f, 515.12964f, 417.40436f, (byte) 60); //Marchutan.
							despawnNpc(getNpc(701502)); //Siels Relict
				        break;
							default:
								break;
					}
				}
				spawn(800430, 500.61713f, 507.2179f, 417.40436f, (byte) 0); //Kahrun (Reian Leader).
				spawn(800464, 545.60693f, 518.90186f, 417.40436f, (byte) 105); //Reian Sorcerer 1
				spawn(800465, 546.8982f, 509.70044f, 417.40436f, (byte) 0); //Reian Sorcerer 2
            break;
			case 219413: //Fissurefang.
				sendMsg(1401533);
				if (player != null) {
				    switch (player.getRace()) {
					    case ELYOS:
						        spawnGodKaisinelTired();
								despawnNpc(getNpc(219564)); //Kaisinel
								spawnGraviwing();
						break;
						case ASMODIANS:
						        spawnGodMarchutanTired();
								despawnNpc(getNpc(219567)); //Marchutan
								spawnGraviwing();
						break;
							default:
								break;
					}
				}
            break;
			case 219414: //Graviwing.
				sendMsg(1401535);
				if (player != null) {
				    switch (player.getRace()) {
					    case ELYOS:
						    spawnWrathclaw();
						break;
						case ASMODIANS:
						    spawnWrathclaw();
						break;
							default:
								break;
					}
				}
            break;
			case 219415: //Wrathclaw.
				sendMsg(1401534);
				if (player != null) {
				    switch (player.getRace()) {
					    case ELYOS:
						    spawnPetriscale();
						break;
						case ASMODIANS:
						    spawnPetriscale();
						break;
							default:
								break;
					}
				}
            break;
			case 219416: //Petriscale.
				sendMsg(1401536);
				if (player != null) {
				    switch (player.getRace()) {
					    case ELYOS:
						        spawnTiamatDying();
							    despawnNpc(getNpc(219565)); //God Kaisinel Tired.
								despawnNpc(getNpc(219409)); //Tiamat (Normal) @I cant find any information about player fight against tiamat
						break;
						case ASMODIANS:
						        spawnTiamatDying();
							    despawnNpc(getNpc(219568)); //God Marchutan Tired.
								despawnNpc(getNpc(219409)); //Tiamat (Normal) @I cant find any information about player fight against tiamat
						break;
							default:
								break;
					}
				}
            break;
        }
    }
	
	//BOSS TIAMAT PHASE.
	private void spawnTiamat() {
        spawn(219409, 453.44815f, 514.5675f, 417.40436f, (byte) 0); //Tiamat.
    }
	
	private void spawnTiamatDying() {
        spawn(219410, 453.26138f, 514.5872f, 417.40436f, (byte) 0); //Tiamat Dying.
    }
	
	private void spawnChestTiamatDying() {
        spawn(701489, 485.79965f, 514.46466f, 417.40436f, (byte) 119); //Chest Tiamat Dying.
    }
	
	//PHASE GOD.
	private void spawnGodEmpyreanEly() {
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(mapId, 219564, 549.03613f, 514.5381f, 417.40436f, (byte) 59); //God Kaisinel.
                SpawnEngine.spawnObject(template, instanceId);
            }
        }, 1000);
    }
	
	private void spawnGodEmpyreanAsmo() {
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(mapId, 219567, 549.03613f, 514.5381f, 417.40436f, (byte) 59); //God Marchutan.
                SpawnEngine.spawnObject(template, instanceId);
            }
        }, 1000);
    }
	
	private void spawnGodMarchutanTired() {
        spawn(219568, 503.94376f, 514.78284f, 417.40436f, (byte) 59); //God Marchutan.
    }
	
	private void spawnFissurefang() {
        spawn(219413, 196.67767f, 176.11638f, 246.07117f, (byte) 8); //Fissurefang.
    }
	
	private void spawnGraviwing() {
        spawn(219414, 799.8529f, 176.94928f, 246.07117f, (byte) 39); //Graviwing.
    }
	
	private void spawnGodKaisinelTired() {
        spawn(219565, 503.94376f, 514.78284f, 417.40436f, (byte) 59); //God Kaisinel.
    }
	
	private void spawnWrathclaw() {
        spawn(219415, 199.11307f, 848.60956f, 246.07117f, (byte) 110); //Wrathclaw.
    }
	
	private void spawnPetriscale() {
        spawn(219416, 796.535f, 849.48615f, 246.07117f, (byte) 72); //Petriscale.
    }
	
	private void spawnRush() {
	    spawn(219659, 540.9507f, 466.07214f, 417.40436f, (byte) 42); //Noble Drakan Figther.
		spawn(219660, 544.04144f, 469.6464f, 417.40436f, (byte) 52); //Noble Drakan Wizard.
		spawn(219661, 536.7774f, 463.96362f, 417.40436f, (byte) 33); //Noble Drakan Sorcerer.
		spawn(219662, 542.7636f, 565.65045f, 417.40436f, (byte) 77); //Noble Drakan Clerc.
		spawn(219663, 538.6315f, 566.12714f, 417.40436f, (byte) 85); //Sardha Drakan Figther.
		spawn(219664, 544.4505f, 561.9321f, 417.40436f, (byte) 67); //Sardha Drakan Wizard.
		spawn(219665, 468.89908f, 463.28857f, 417.40436f, (byte) 16); //Sardha Drakan Sorcerer.
		spawn(219666, 467.41974f, 466.10922f, 417.40436f, (byte) 13); //Sardha Drakan Clerc.
	}
	
	protected void despawnNpc(Npc npc) {
        if (npc != null) {
            npc.getController().onDelete();
        }
    }
	
	protected void despawnNpcs(List<Npc> npcs) {
        for (Npc npc: npcs) {
            npc.getController().onDelete();
        }
    }
	
	protected Npc getNpc(int npcId) {
        if (!isInstanceDestroyed) {
            return instance.getNpc(npcId);
        }
        return null;
    }
	
    protected List<Npc> getNpcs(int npcId) {
        if (!isInstanceDestroyed) {
            return instance.getNpcs(npcId);
        }
        return null;
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
		TeleportService2.teleportTo(player, mapId, instanceId, 540.33655f, 515.00146f, 417.40436f, (byte) 59);
		return true;
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);

		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
	
    @Override
    public void onInstanceDestroy() {
		movies.clear();
		isInstanceDestroyed = true;
    }
}