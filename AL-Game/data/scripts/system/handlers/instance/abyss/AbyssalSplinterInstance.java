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
package instance.abyss;

import java.util.List;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author zhkchi
 * @reworked vlog, Luzien, Maestross
 * @see http://gameguide.na.aiononline.com/aion/Abyssal+Splinter+Walkthrough
 */
@InstanceID(300220000)
public class AbyssalSplinterInstance extends GeneralInstanceHandler {

	private int destroyedFragments;
	private int killedPazuzuWorms = 0;
	private boolean bossSpawned = false;

	@Override
	public void onDie(Npc npc) {
		final int npcId = npc.getNpcId();
		switch (npcId) {
			case 216951: // Pazuzu the Life Current
				spawnPazuzuHugeAetherFragment();
				spawnPazuzuGenesisTreasureBoxes();
				spawnPazuzuAbyssalTreasureBox();
				spawnPazuzusTreasureBox();
				spawnFinalTreasureBox1();
				break;
			case 216950: // Kaluva the Fourth Fragment
				spawnKaluvaHugeAetherFragment();
				spawnKaluvaGenesisTreasureBoxes();
				spawnKaluvaAbyssalTreasureBox();
				spawnFinalTreasureBox2();
				break;
			case 216948: // rukril
			    spawnFinalTreasureBox3();
				break;
			case 216949: // ebonsoul
			    spawnFinalTreasureBox4();
				if (getNpc(npcId == 216949 ? 216948 : 216949) == null) {
					spawnDayshadeAetherFragment();
					spawnDayshadeGenesisTreasureBoxes();
					spawnDayshadeAbyssalTreasureChest();
				}
				else {
					sendMsg(npcId == 216948 ? 1400634 : 1400635); // Defeat Rukril/Ebonsoul in 1 min!
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {

							if (getNpc(npcId == 216949 ? 216948 : 216949) != null) {
								switch (npcId) {
									case 216948:
										spawn(216948, 447.1937f, 683.72217f, 433.1805f, (byte) 108); // rukril
										break;
									case 216949:
										spawn(216949, 455.5502f, 702.09485f, 433.13727f, (byte) 108); // ebonsoul
										break;
								}
							}
						}

					}, 60000);
				}
				npc.getController().onDelete();
				break;
			case 281907:
				Npc ebonsoul = getNpc(216949);
				if (ebonsoul != null && !ebonsoul.getLifeStats().isAlreadyDead()) {
					if (MathUtil.isIn3dRange(npc, ebonsoul, 5)) {
						ebonsoul.getEffectController().removeEffect(19159);
						deleteNpcs(instance.getNpcs(281907));
						break;
					}
				}
				npc.getController().onDelete();
				break;
			case 281908:
				Npc rukril = getNpc(216948);
				if (rukril != null && !rukril.getLifeStats().isAlreadyDead()) {
					if (MathUtil.isIn3dRange(npc, rukril, 5)) {
						rukril.getEffectController().removeEffect(19266);
						deleteNpcs(instance.getNpcs(281908));
						break;
					}
				}
				npc.getController().onDelete();
				break;
			case 216960: // Yamennes Painflare
			case 216952: // Yamennes Blindsight
				spawnYamennesGenesisTreasureBoxes();
				spawnYamennesAbyssalTreasureBox(npcId == 216952 ? 700937 : 700938);
				break;
			case 700955: // HugeAetherFragment
				destroyedFragments++;
				onFragmentKill();
				npc.getController().onDelete();
				break;

			case 281909:
				if (++killedPazuzuWorms == 5) {
					killedPazuzuWorms = 0;
					Npc pazuzu = getNpc(216951);
					if (pazuzu != null && !pazuzu.getLifeStats().isAlreadyDead()) {
						pazuzu.getEffectController().removeEffect(19145);
						pazuzu.getEffectController().removeEffect(19291);
					}
				}
				npc.getController().onDelete();
				break;
		}
	}

	private boolean isSpawned(int npcId) {
		return !instance.getNpcs(npcId).isEmpty();
	}

	@Override
	public void onInstanceDestroy() {
		destroyedFragments = 0;
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		if (npc.getNpcId() == 700856) {
			QuestEnv env = new QuestEnv(npc, player, 0, 0);
			QuestEngine.getInstance().onDialog(env);
			if (!isSpawned(216960) && !isSpawned(216952) && !bossSpawned) { // No bosses spawned
				if (!isSpawned(700955) && destroyedFragments == 3) { // No Huge Aether Fragments spawned (all destroyed)
					sendMsg(1400732);
					spawn(216960, 329.70886f, 733.8744f, 197.60938f, (byte) 0);
				}
				else {
					sendMsg(1400731);
					spawn(216952, 329.70886f, 733.8744f, 197.60938f, (byte) 0);
				}
				bossSpawned = true;
			}
		}
	}

	@Override
	public boolean onReviveEvent(Player player) {
		PlayerReviveService.revive(player, 25, 25, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		TeleportService2.teleportTo(player, mapId, instanceId, 703.39716f, 154.69728f, 453.30627f, (byte) 44);
		return true;
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);

		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}

	private void spawnPazuzuHugeAetherFragment() {
		spawn(700955, 669.576f, 335.135f, 465.895f, (byte) 0);
	}

	private void spawnPazuzuGenesisTreasureBoxes() {
		spawn(700934, 651.53204f, 357.085f, 466.8837f, (byte) 66);
		spawn(700934, 647.00446f, 357.2484f, 466.14117f, (byte) 0);
		spawn(700934, 653.8384f, 360.39508f, 466.8837f, (byte) 100);
	}

	private void spawnPazuzuAbyssalTreasureBox() {
		spawn(700860, 649.24286f, 361.33755f, 467.89145f, (byte) 33);
	}

	private void spawnPazuzusTreasureBox() {
		if (Rnd.get(0, 100) >= 80) { // 20% chance, not retail
			spawn(700861, 649.243f, 362.338f, 466.0451f, (byte) 0);
		}
	}

	private void spawnKaluvaHugeAetherFragment() {
		spawn(700955, 633.7498f, 557.8822f, 424.99347f, (byte) 6);
	}

	private void spawnKaluvaGenesisTreasureBoxes() {
		spawn(700934, 601.2931f, 584.66705f, 424.2829f, (byte) 6);
		spawn(700934, 597.2156f, 583.95416f, 424.2829f, (byte) 66);
		spawn(700934, 602.9586f, 589.2678f, 424.2829f, (byte) 100);
	}

	private void spawnKaluvaAbyssalTreasureBox() {
		spawn(700935, 598.82776f, 588.25946f, 424.29065f, (byte) 113);
	}

	private void spawnDayshadeAetherFragment() {
		spawn(700955, 452.89706f, 692.36084f, 433.96838f, (byte) 6);
	}

	private void spawnDayshadeGenesisTreasureBoxes() {
		spawn(700934, 408.10938f, 650.9015f, 439.28332f, (byte) 66);
		spawn(700934, 402.40375f, 655.55237f, 439.26288f, (byte) 33);
		spawn(700934, 406.74445f, 655.5914f, 439.2548f, (byte) 100);
	}

	private void spawnDayshadeAbyssalTreasureChest() {
		sendMsg(1400636); // A Treasure Box Appeared
		spawn(700936, 404.891f, 650.2943f, 439.2548f, (byte) 130);
	}

	private void spawnYamennesGenesisTreasureBoxes() {
		spawn(700934, 326.978f, 729.8414f, 198.46796f, (byte) 16);
		spawn(700934, 326.5296f, 735.13324f, 198.46796f, (byte) 66);
		spawn(700934, 329.8462f, 738.41095f, 198.46796f, (byte) 3);
	}

	private void spawnYamennesAbyssalTreasureBox(int npcId) {
		spawn(npcId, 330.891f, 733.2943f, 198.55286f, (byte) 113);
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}
	
	private void spawnFinalTreasureBox1() {
        spawn(700462, 345.6414f, 735.8173f, 419.50327f, (byte) 0);
        spawn(700462, 337.4277f, 790.7218f, 396.72617f, (byte) 98);
        spawn(700462, 307.4969f, 716.0779f, 368.53365f, (byte) 12);
    }

    private void spawnFinalTreasureBox2() {
        spawn(700462, 351.1394f, 734.4164f, 365.44296f, (byte) 27);
        spawn(700462, 367.7004f, 769.2610f, 347.43241f, (byte) 29);
        spawn(700462, 345.9116f, 798.9167f, 319.85782f, (byte) 104);
    }

    private void spawnFinalTreasureBox3() {
        spawn(700462, 319.6323f, 797.9679f, 296.40778f, (byte) 88);
        spawn(700462, 270.3465f, 763.9562f, 288.64235f, (byte) 115);
        spawn(700462, 399.6513f, 783.2535f, 291.10043f, (byte) 70);
    }

    private void spawnFinalTreasureBox4() {
        spawn(700462, 380.70517f, 695.5765f, 270.9548f, (byte) 36);
        spawn(700462, 288.96515f, 742.1645f, 252.8425f, (byte) 105);
        spawn(700462, 386.65081f, 476.6518f, 215.9278f, (byte) 61);
    }

	private void onFragmentKill() {
		switch (destroyedFragments) {
			case 1:
				// The destruction of the Huge Aether Fragment has destabilized the artifact!
				sendMsg(1400689);
				break;
			case 2:
				// The destruction of the Huge Aether Fragment has put the artifact protector on alert!
				sendMsg(1400690);
				break;
			case 3:
				// The destruction of the Huge Aether Fragment has caused abnormality on the artifact. The artifact protector is
				// furious!
				sendMsg(1400691);
				break;
		}
	}
}
