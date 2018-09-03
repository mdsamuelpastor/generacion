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
import java.util.Map;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author xTz
 */
@InstanceID(300310000)
public class RaksangInstance extends GeneralInstanceHandler {

	private Map<Integer, StaticDoor> doors;
	private int generatorKilled;
	private int ashulagenKilled;
	private int gargoyleKilled;
	private int rakshaHelpersKilled;
	private boolean isInstanceDestroyed;
	private List<Integer> movies = new ArrayList<Integer>();

	@Override
	public void onDie(Npc npc) {
		switch (npc.getNpcId()) {
			case 730453:
			case 730454:
			case 730455:
			case 730456:
				generatorKilled++;
				if (generatorKilled == 1) {
					sendMsg(1401133);
					doors.get(87).setOpen(true);
				}
				else if (generatorKilled == 2) {
					sendMsg(1401133);
					doors.get(167).setOpen(true);
				}
				else if (generatorKilled == 3) {
					sendMsg(1401133);
					doors.get(114).setOpen(true);
				}
				else if (generatorKilled == 4) {
					sendMsg(1401134);
					doors.get(165).setOpen(true);
				}
				despawnNpc(npc);
				break;
			case 217392:
				doors.get(103).setOpen(true);
				break;
			case 217469:
				doors.get(107).setOpen(true);
				break;
			case 217471:
			case 217472:
				gargoyleKilled++;
				if (gargoyleKilled == 2) {
					Npc magic = instance.getNpc(217473);
					if (magic != null) {
						sendMsg(1401159);
						magic.getEffectController().removeEffect(19126);
					}
				}
				despawnNpc(npc);
				break;
			case 217473:
				despawnNpc(npc);
				final Npc dust = (Npc) spawn(701075, 1068.630f, 967.205f, 138.785f, (byte) 0, 323);
				doors.get(105).setOpen(true);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (!isInstanceDestroyed && dust != null && !CreatureActions.isAlreadyDead(dust)) {
							CreatureActions.delete(dust);
						}
					}

				}, 4000);
				break;
			case 217455:
				ashulagenKilled++;
				if (ashulagenKilled == 1 || ashulagenKilled == 2 || ashulagenKilled == 3) {
					sendMsg(1401160);
				}
				else if (ashulagenKilled == 4) {
					spawn(217456, 615.081f, 640.660f, 524.195f, (byte) 0);
					sendMsg(1401135);
				}
				break;
			case 217425:
			case 217451:
			case 217456:
				rakshaHelpersKilled++;
				if (rakshaHelpersKilled < 3) {
					sendMsg(1401161);
				}
				else if (rakshaHelpersKilled == 3) {
					sendMsg(1401162);
				}
				break;
			case 217647:
			case 217475:
				rakshaHelpersKilled = 4;
				break;
		}
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		doors.clear();
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
	}

	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	@Override
	public boolean onReviveEvent(Player player) {
		PlayerReviveService.revive(player, 25, 25, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		TeleportService2.teleportTo(player, mapId, instanceId, 853.32745f, 947.3807f, 1206.625f, (byte) 72);
		return true;
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);

		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}

	@Override
	public void onEnterInstance(Player player) {
		PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 476));
	}

	/**
	 * @return the movies
	 */
	public List<Integer> getMovies() {
		return movies;
	}

	/**
	 * @param movies the movies to set
	 */
	public void setMovies(List<Integer> movies) {
		this.movies = movies;
	}

}
