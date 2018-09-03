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

import java.util.Map;
import java.util.Set;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.flyring.FlyRing;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.flyring.FlyRingTemplate;
import com.aionemu.gameserver.model.utils3d.Point3D;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Ritsu, Maestros Guide:
 *         http://power.plaync.co.kr/aion/%EC%82%AC%ED%8A%B8%EB%9D%BC%EC%9D%98+%EB%B9%84%EB%B0%80%EC%B0%BD%EA%B3%A0
 */
@InstanceID(300470000)
public class SatraTreasureHoardInstance extends GeneralInstanceHandler {

	private Map<Integer, StaticDoor> doors;
	private boolean isStartTimer = false;

	@Override
	public void onInstanceDestroy() {
		doors.clear();
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		doors.get(77).setOpen(true);
		spawnTimerRing();
	}

	private void spawnTimerRing() {
		FlyRing f1 = new FlyRing(new FlyRingTemplate("SATRAS_01", mapId, new Point3D(501.13412, 672.4659, 177.10771), new Point3D(492.13412, 672.4659, 177.10771), new Point3D(496.54834, 671.5966,
			184.10771), 8), instanceId);
		f1.spawn();
	}

	@Override
	public boolean onPassFlyingRing(Player player, String flyingRing) {
		if (flyingRing.equals("SATRAS_01")) {
			if (!isStartTimer) {
				isStartTimer = true;
				System.currentTimeMillis();
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 600));
				doors.get(77).setOpen(false);
				spawn(701461, 478.623f, 692.772f, 176.398f, (byte) 15);
				Npc muzzledPunisher = getNpc(219347);
				if (muzzledPunisher == null) {
					spawnElaborateChest();
				}
				else {
					spawnHeavyChest();
				}
				// Delete chest according with a timer (5 Min > 10)
				// http://static.plaync.co.kr/powerbook/aion/23/83/3cbde3efd2422e9249efcfea.png
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						//chest1.getController().delete();
					}

				}, 300000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						//chest2.getController().delete();
					}

				}, 360000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						//chest3.getController().delete();
					}

				}, 420000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						//chest4.getController().delete();
					}

				}, 480000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						//chest5.getController().delete();
					}

				}, 540000);
			}
		}
		return false;
	}

	private void spawnHeavyChest() {
		spawn(701462, 446.962f, 744.254f, 178.071f, (byte) 0, 206);
		spawn(701462, 459.856f, 759.960f, 178.071f, (byte) 0, 81);
		spawn(701462, 533.697f, 760.551f, 178.071f, (byte) 0, 80);
		spawn(701462, 477.382f, 770.049f, 178.071f, (byte) 0, 83);
		spawn(701462, 497.030f, 773.931f, 178.071f, (byte) 0, 85);
		spawn(701462, 516.508f, 770.646f, 178.071f, (byte) 0, 122);
	}

	private void spawnElaborateChest() {
		spawn(701463, 446.962f, 744.254f, 178.071f, (byte) 0, 206);
		spawn(701463, 459.856f, 759.960f, 178.071f, (byte) 0, 81);
		spawn(701463, 533.697f, 760.551f, 178.071f, (byte) 0, 80);
		spawn(701463, 477.382f, 770.049f, 178.071f, (byte) 0, 83);
		spawn(701463, 497.030f, 773.931f, 178.071f, (byte) 0, 85);
		spawn(701463, 516.508f, 770.646f, 178.071f, (byte) 0, 122);
	}

	@Override
	public void onDie(Npc npc) {
		switch (npc.getNpcId()) {
			case 219347: // muzzled punisher
				break;
			case 219348: // punisher unleashed
				spawn(730588, 496.600f, 685.600f, 176.400f, (byte) 30); // Spawn Exit
				break;
			case 701464: // artifact spawn stronger boss
				Npc boss = getNpc(219347);
				if (boss != null && !boss.getLifeStats().isAlreadyDead()) {
					spawn(219348, boss.getX(), boss.getY(), boss.getZ(), boss.getHeading());
					boss.getController().onDelete();
				}
				break;
			case 219346:
				doors.get(118).setOpen(true);
				doors.get(108).setOpen(true);
				break;
			case 219345:
				doors.get(86).setOpen(true);
				doors.get(117).setOpen(true);
				break;
			case 219344:
			    doors.get(88).setOpen(true);
				doors.get(84).setOpen(true);
		}
	}

	@Override
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().geCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		Integer object = instance.getSoloPlayerObj();
		switch (npcId) {
			case 219347:
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, object, npcId, 185000118, 1));
				break;
			case 219348:
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, object, npcId, 185000119, 1));
				break;
		}
	}
	
	@Override
	public boolean onReviveEvent(Player player) {
		PlayerReviveService.revive(player, 25, 25, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		TeleportService2.teleportTo(player, mapId, instanceId, 510.27213f, 176.19012f, 158.49658f, (byte) 31);
		return true;
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);

		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
