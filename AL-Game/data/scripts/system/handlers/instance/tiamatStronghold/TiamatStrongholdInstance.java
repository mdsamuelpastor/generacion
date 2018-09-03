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
package instance.tiamatStronghold;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javolution.util.FastMap;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Maestross, Eloann
 */
@InstanceID(300510000)
public class TiamatStrongholdInstance extends GeneralInstanceHandler {

    private int kills, kills2, kills3, kills4, kills5;
    private Map<Integer, StaticDoor> doors;
    protected boolean isInstanceDestroyed = false;
    private final AtomicInteger specNpcKilled = new AtomicInteger();
    private FastMap<Integer, VisibleObject> portal = new FastMap<Integer, VisibleObject>();

    @Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
        //SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(300510000, 730613, 1542.3802f, 1068.5604f, 493.45096f, (byte) 0); //Protect Wall.
        //template.setStaticId(35);
        //portal.put(730613, SpawnEngine.spawnObject(template, instanceId));
        spawn(800456, 1572.65f, 1058.53f, 492.01f, (byte) 0); //Kahrun (Reian Leader).
        doors = instance.getDoors();
    }

    @Override
    public void onDie(Npc npc) {
        Player player = npc.getAggroList().getMostPlayerDamage();
        switch (npc.getObjectTemplate().getTemplateId()) {
            case 219417: //Protectorate Elite Fighter.
                kills++;
                if (kills < 4) {
                } else if (kills == 4) {
                    PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "<Protectorate Elite Infantryman> appear", ChatType.BRIGHT_YELLOW_CENTER), true);
                    spawnProtectorateEliteInfantryman();
                }
                break;
            case 219418: //Protectorate Elite Infantryman.
                kills2++;
                if (kills2 < 4) {
                } else if (kills2 == 4) {
                    PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "<Protectorate Elite Scout> appear", ChatType.BRIGHT_YELLOW_CENTER), true);
                    spawnProtectorateEliteScout();
                }
                break;
            case 219419: //Protectorate Elite Scout.
                kills3++;
                if (kills3 < 4) {
                } else if (kills3 == 4) {
                    PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "<Protectorate Elite Healer> appear", ChatType.BRIGHT_YELLOW_CENTER), true);
                    spawnProtectorateEliteHealer();
                }
                break;
            case 219420: //Protectorate Elite Healer.
                kills4++;
                if (kills4 < 4) {
                } else if (kills4 == 4) {
                    PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "<Protectorate Elite Mounted Officer> appear", ChatType.BRIGHT_YELLOW_CENTER), true);
                    spawnProtectorateEliteMountedOfficer();
                }
                break;
            case 219421: //Protectorate Elite Mounted Officer.
                kills5++;
                if (kills5 < 4) {
                } else if (kills5 == 4) {
                    despawnNpc(getNpc(730612)); //Protect Wall.
                    despawnNpc(getNpc(800335)); //Kahrun (Reian Leader).
                }
                break;
            case 219400: //Invicible Shabokan.
                openDoor(48);
                PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "You finally secured <Stronghold Gateway> well done !!", ChatType.BRIGHT_YELLOW_CENTER), true);
                spawnStrongholdGatewaySecure();
                break;
            case 219422: //Protectorate Elite.
                int killedCount = specNpcKilled.incrementAndGet();
                if (killedCount == 3) { //Protectorate Elite.
                    PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "<Traitor Kumbanda> appear", ChatType.BRIGHT_YELLOW_CENTER), true);
                    spawn(219403, 887.386f, 1319.52f, 394.534f, (byte) 0); //Traitor Kumbanda.
                }
                break;
            case 219440: //Laksyaka Elite Guard Captain.
                openDoor(49);
                break;
            case 219411: //Laksyaka Colonel I.
            case 219412: //Laksyaka Colonel II.
                Npc npc1 = instance.getNpc(219411); //Laksyaka Colonel I.
                Npc npc2 = instance.getNpc(219412); //Laksyaka Colonel II.
                if (isDead(npc1) && isDead(npc2)) {
                    PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "2 <Elevator Door> was opened", ChatType.BRIGHT_YELLOW_CENTER), true);
                    openDoor(103);
                    openDoor(369);
                }
                break;
            case 219403: //Traitor Kumbanda.
                openDoor(11);
                openDoor(51);
                openDoor(54);
                openDoor(56);
                openDoor(78);
                openDoor(79);
                PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "<Brigade General Laksyaka> appear", ChatType.BRIGHT_YELLOW_CENTER), true);
                spawn(219404, 637.67f, 1319.27f, 487.709f, (byte) 0); //Brigade General Laksyaka.
                break;
            case 219404: //Brigade General Laksyaka.
                PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "<Central Passage Teleport> appear, use it", ChatType.BRIGHT_YELLOW_CENTER), true);
                PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "<Adjudant Anuhart> appear", ChatType.BRIGHT_YELLOW_CENTER), true);
                spawn(219405, 753.685f, 1068.07f, 501.218f, (byte) 0); //Adjudant Anuhart.
                spawn(730622, 640.817f, 1306.63f, 487.587f, (byte) 0); //Central Passage Teleport.
                break;
            case 219405: //Adjudant Anuhart.
                openDoor(37);
                openDoor(610);
                PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "<Brigade General Tahabata> appear", ChatType.BRIGHT_YELLOW_CENTER), true);
                spawn(219406, 677.165f, 1068.07f, 497.752f, (byte) 0); //Brigade General Tahabata.
                break;
            case 219406: //Brigade General Tahabata.
                SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(300510000, 730622, 650.329f, 1068.94f, 497.752f, (byte) 0); //Central Passage Teleport.
                template.setStaticId(82);
                portal.put(730622, SpawnEngine.spawnObject(template, instanceId));
                break;
            case 219427: //Worker Golem.
                kills++;
                if (kills < 3) { //Worker Golem.
                } else if (kills == 3) {
                    PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "<Research Center Acces Controler> appear, use it for open door", ChatType.BRIGHT_YELLOW_CENTER), true);
                    spawn(701494, 1084.272583f, 813.113159f, 499.853973f, (byte) 0); //Research Center Acces Controler.
                }
                break;
            case 219428: //Cloaker.
                PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "<Professor HewaHewa> was spawned", ChatType.BRIGHT_YELLOW_CENTER), true);
                spawn(219456, 1033.369f, 675.97284f, 441.69833f, (byte) 0); //Professor HewaHewa.
                break;
            case 219456: //Professor HewaHewa.
                PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "1 <Surkana Activator> appear, destroy it", ChatType.BRIGHT_YELLOW_CENTER), true);
                spawn(219430, 992.81964f, 596.2532f, 439.92868f, (byte) 119); //Surkana Activator.
                break;
            case 219430: //Surkana Activator.
                openDoor(20);
                spawn(219401, 1027.49f, 466.736f, 442.239f, (byte) 0); //Brigade General Chantra.
                break;
            case 219401: //Brigade General Chantra.
                openDoor(53);
                break;
            case 219432: //Protectorate Chef Navigator.
                openDoor(706);
                PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "The shield who protect <Brigade General Terath> is down", ChatType.BRIGHT_YELLOW_CENTER), true);
                spawn(219402, 1023.79f, 300.81f, 409.086f, (byte) 0); //Brigade General Terath.
                break;
            case 219402: //Brigade General Terath.
                PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "[Congratulation Daeva]: you finish <TIAMAT STRONGHOLD>", ChatType.BRIGHT_YELLOW_CENTER), true);
                PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "[Tiamat Stronghold Chest]: appear, take your reward !!!", ChatType.BRIGHT_YELLOW_CENTER), true);
                spawn(730629, 1121.38f, 1069.81f, 496.862f, (byte) 0); //Tiamat Stronghold Exit.
                SpawnTemplate portalExit = SpawnEngine.addNewSingleTimeSpawn(300510000, 730622, 1029.65f, 267.142f, 409.086f, (byte) 0); //Central Passage Teleport.
                portalExit.setStaticId(83);
                portal.put(730622, SpawnEngine.spawnObject(portalExit, instanceId));
                SpawnTemplate statue = SpawnEngine.addNewSingleTimeSpawn(300510000, 730641, 1092.08f, 1069.65f, 497.358f, (byte) 0); //Tahabata's Statue.
                statue.setStaticId(187);
                portal.put(730641, SpawnEngine.spawnObject(statue, instanceId));
                spawnTiamatStrongholdChest();
                break;
        }
    }

    private void spawnTiamatStrongholdChest() {
        spawn(701501, 1105.73f, 1063.87f, 497.174f, (byte) 24); //Tiamat Stronghold Chest.
    }

    private void spawnProtectorateEliteInfantryman() {
        spawn(219418, 1560.08f, 1061.26f, 492.16f, (byte) 58); //Protectorate Elite Infantryman.
        spawn(219418, 1559.88f, 1075.19f, 492.16f, (byte) 60); //Protectorate Elite Infantryman.
        spawn(219418, 1554.85f, 1076.33f, 492.16f, (byte) 98); //Protectorate Elite Infantryman.
        spawn(219418, 1553.87f, 1062.09f, 492.16f, (byte) 113); //Protectorate Elite Infantryman.
    }

    private void spawnProtectorateEliteScout() {
        spawn(219419, 1560.08f, 1061.26f, 492.16f, (byte) 58); //Protectorate Elite Scout.
        spawn(219419, 1559.88f, 1075.19f, 492.16f, (byte) 60); //Protectorate Elite Scout.
        spawn(219419, 1554.85f, 1076.33f, 492.16f, (byte) 98); //Protectorate Elite Scout.
        spawn(219419, 1553.87f, 1062.09f, 492.16f, (byte) 113); //Protectorate Elite Scout.
    }

    private void spawnProtectorateEliteHealer() {
        spawn(219420, 1560.08f, 1061.26f, 492.16f, (byte) 58); //Protectorate Elite Healer.
        spawn(219420, 1559.88f, 1075.19f, 492.16f, (byte) 60); //Protectorate Elite Healer.
        spawn(219420, 1554.85f, 1076.33f, 492.16f, (byte) 98); //Protectorate Elite Healer.
        spawn(219420, 1553.87f, 1062.09f, 492.16f, (byte) 113); //Protectorate Elite Healer.
    }

    private void spawnProtectorateEliteMountedOfficer() {
        spawn(219421, 1560.08f, 1061.26f, 492.16f, (byte) 58); //Protectorate Elite Mounted Officer.
        spawn(219421, 1559.88f, 1075.19f, 492.16f, (byte) 60); //Protectorate Elite Mounted Officer.
        spawn(219421, 1554.85f, 1076.33f, 492.16f, (byte) 98); //Protectorate Elite Mounted Officer.
        spawn(219421, 1553.87f, 1062.09f, 492.16f, (byte) 113); //Protectorate Elite Mounted Officer.
    }

    private void spawnStrongholdGatewaySecure() {
        spawn(800374, 1188.22f, 1074f, 491.503f, (byte) 0); //Elite Reian Assaulter.
        spawn(800374, 1188.07f, 1066f, 491.506f, (byte) 0); //Elite Reian Assaulter.
        spawn(800376, 1190.4629f, 1070.8563f, 491.4767f, (byte) 0); //Elite Reian Assaulter.
        spawn(800378, 1190.32f, 1068f, 491.481f, (byte) 0); //Elite Reian Sniper.
        spawn(800378, 1190.57f, 1073.99f, 491.492f, (byte) 0); //Elite Reian Sniper.
        spawn(800380, 1207.39f, 1070.98f, 491.33f, (byte) 0); //Elite Reian Fighter.
        spawn(800336, 1178.74f, 1069.14f, 491.887f, (byte) 0); //Kahrun.
        spawn(800347, 1178.84f, 1072.28f, 491.88f, (byte) 0); //Garnon.
        spawn(800461, 1185.3f, 1069.43f, 491.457f, (byte) 0); //Sorus.
        spawn(800463, 1207.39f, 1070.98f, 491.33f, (byte) 0); //Elite Reian Fighter.
        spawn(800463, 1207.59f, 1067.98f, 491.33f, (byte) 0); //Elite Reian Fighter.
        spawn(800463, 1200.88f, 1063.99f, 491.33f, (byte) 0); //Elite Reian Fighter.
        spawn(800463, 1200.7f, 1065.97f, 491.33f, (byte) 0); //Elite Reian Fighter.
        spawn(800463, 1201f, 1071.81f, 491.33f, (byte) 0); //Elite Reian Fighter.
        spawn(800463, 1200.79f, 1073.99f, 491.33f, (byte) 0); //Elite Reian Fighter.
        spawn(800463, 1192.34f, 1067.8f, 491.33f, (byte) 0); //Elite Reian Fighter.
        spawn(800463, 1192.4f, 1071f, 491.33f, (byte) 0); //Elite Reian Fighter.
    }

    @Override
    public void onEnterZone(Player player, ZoneInstance zone) {
        if (zone.getAreaTemplate().getZoneName() == ZoneName.get("TIAMAT_STRONGHOLD_1_SZ_S5_1")) {
            PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "Kill <Invicible Shabokan> to appear <Brigade General Tahabata>", ChatType.BRIGHT_YELLOW_CENTER), true);
        } else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("TIAMAT_STRONGHOLD_1_SZ_S2")) {
            PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "Kill 3 <Worker Golem> to appear <Research Center Acces Controler>", ChatType.BRIGHT_YELLOW_CENTER), true);
        } else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("TIAMAT_STRONGHOLD_1_SZ_S3")) {
            PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "Kill <Protectorate Chef Navigator> to appear <Brigade General Terath>", ChatType.BRIGHT_YELLOW_CENTER), true);
        }
    }

    protected void despawnNpc(Npc npc) {
        if (npc != null) {
            npc.getController().onDelete();
        }
    }

    protected void despawnNpcs(List<Npc> npcs) {
        for (Npc npc : npcs) {
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

    @Override
    public boolean onReviveEvent(Player player) {
        PlayerReviveService.revive(player, 25, 25, false, 0);
        player.getGameStats().updateStatsAndSpeedVisually();
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
        TeleportService2.teleportTo(player, mapId, instanceId, 1534.8584f, 1067.791f, 491.3514f, (byte) 60);
        return true;
    }

    @Override
    public boolean onDie(final Player player, Creature lastAttacker) {
        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);

        PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
        return true;
    }

    private boolean isDead(Npc npc) {
        return (npc == null || npc.getLifeStats().isAlreadyDead());
    }

    private void openDoor(int doorId) {
        StaticDoor door = doors.get(doorId);
        if (door != null) {
            door.setOpen(true);
        }
    }

    @Override
    public void onInstanceDestroy() {
        doors.clear();
        isInstanceDestroyed = true;
    }
}