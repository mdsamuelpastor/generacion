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
package com.aionemu.gameserver.spawnengine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.RandomSpawnData;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.templates.randomspawns.Point;
import com.aionemu.gameserver.model.templates.randomspawns.RandomSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;


/**
 * @author pixfid
 * @rework eloann
 */
public class RandomSpawnManager {

	private static Logger log = LoggerFactory.getLogger(RandomSpawnManager.class);
    private final List<VisibleObject> spawnedObjects;

    public static RandomSpawnManager getInstance() {
        return SingletonHolder.instance;
    }
    private int[] massrespawn;
    private final ArrayList<SpawnTemplate> toSpawn;
    private List<Point> points;
    private int[] singlerespawn;
    private List<Point> spoint;

    private RandomSpawnManager() {
        toSpawn = new ArrayList<SpawnTemplate>();
        spawnedObjects = new ArrayList<VisibleObject>();
        log.info("RandomSpawnManager: RandomSpawn handler initialized");
    }

    public SpawnTemplate getNpcs(int npcid) {
        if (npcid == 0) {
            massrespawn = RandomSpawnData.getNpcid_key();
            for (int i = 0; i < massrespawn.length; i++) {
                RandomSpawnTemplate massrst = DataManager.RANDOMSPAWNDATA.getRandomSpawnTemplate(massrespawn[i]);
                points = massrst.getPoint();
                int ce = Rnd.get(points.size());
                massrst.getInterval();
                SpawnTemplate spawnTemplate = createSpawnTemplate(points.get(ce).getMapid(), massrst.getNpcid(), points.get(ce).GetX(), points.get(ce).GetY(), points.get(ce).GetZ(), (byte) points.get(ce).GetH(), 0, 0);
                toSpawn.add(spawnTemplate);
            }
        } else if (npcid > 0) {
            singlerespawn = RandomSpawnData.getNpcid_key();
            for (int o = 0; o < singlerespawn.length; o++) {
                RandomSpawnTemplate singlerst = DataManager.RANDOMSPAWNDATA.getRandomSpawnTemplate(singlerespawn[o]);
                if (npcid == singlerst.getNpcid()) {
                    spoint = singlerst.getPoint();
                    int ce = Rnd.get(spoint.size());
                    singlerst.getInterval();
                    SpawnTemplate spawnTemplate = createSpawnTemplate(spoint.get(ce).getMapid(), singlerst.getNpcid(), spoint.get(ce).GetX(), spoint.get(ce).GetY(), spoint.get(ce).GetZ(), (byte) spoint.get(ce).GetH(), 0, 0);
                    return spawnTemplate;
                }
            }

        }
        return null;
    }

    private SpawnTemplate createSpawnTemplate(int worldId, int objectId, float x, float y, float z, byte heading, int walkerid, int randomwalk) {
        return new SpawnTemplate(new SpawnGroup2(worldId, objectId), x, y, z, heading, randomwalk, null, 0, walkerid);
    }

    public void spawn() {
        deleteObjects();
        getNpcs(0);
        spawnNpcs(toSpawn);
        log.info("RandomSpawnManager: " + toSpawn.size() + " Random objects spawned.");
    }

    private void spawnNpcs(List<SpawnTemplate> spawns) {
        for (SpawnTemplate spawnTemplate : spawns) {
            Set<Integer> instanceIds = World.getInstance().getWorldMap(spawnTemplate.getWorldId()).getInstanceIds();
            for (Integer instanceId : instanceIds) {
                spawnTemplate.setUse(true);
                VisibleObject object = SpawnEngine.spawnObject(spawnTemplate, instanceId);
                if (object != null) {
                    spawnedObjects.add(object);
                }
            }
        }
    }

    private void deleteObjects() {
        for (VisibleObject object : spawnedObjects) {
            object.getController().delete();
        }
        spawnedObjects.clear();
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final RandomSpawnManager instance = new RandomSpawnManager();
    }

    public void scheduleUpdate() {
        String[] time = CustomConfig.RANDOM_RESPAWN_TIME.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
        calendar.set(Calendar.SECOND, Integer.parseInt(time[2]));

        long delay = calendar.getTimeInMillis() - System.currentTimeMillis();
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                ThreadPoolManager.getInstance().schedule(new Runnable() {

                    @Override
                    public void run() {
                        spawn();
                    }
                }, 30000);
            }
        }, delay, CustomConfig.RANDOM_RESPAWN_DELAY * 3600000);
    }
}
