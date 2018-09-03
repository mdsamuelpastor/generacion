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
package com.aionemu.gameserver.services.CustomBosses;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.AnnouncementsDAO;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Maestross
 */
 
public class CustomBossSpawn {
    private static final Logger log = LoggerFactory.getLogger(CustomBossSpawn.class);
    Npc boss;
    int id;
    private CustomSpawnLocation loc;
    Integer[] possibleSpawns;

    public Npc getNpc()
    {
        return boss;
    }

    public CustomBossSpawn(int id, Integer[] possibleSpawns)
    {
        this.id = id;
        this.possibleSpawns = possibleSpawns;
    }

    public boolean isPossibleSpawn(int mapId)
    {
        for(int i : possibleSpawns)
            if(mapId == i)
                return true;
        return false;
    }

    public void onDie(Player lastAttacker)
    {
        this.loc.isUsed = false;
        BossSpawnService.getInstance().SendMessage(BossSpawnService.getInstance().getBossName(boss) + LanguageHandler.translate(CustomMessageId.CUSTOM_BOSS_KILLED) + lastAttacker.getName());
		log.info("[CUSTOM_KILL] "+BossSpawnService.getInstance().getBossName(boss) + " was from " + lastAttacker.getName() + " killed");
		DAOManager.getDAO(AnnouncementsDAO.class).UniqueKill(lastAttacker, BossSpawnService.getInstance().getBossName(boss));
    }

    public boolean isSpawned()
    {
        if(boss == null)
            return false;
        else
            return boss.isSpawned();
    }

    public void Spawn(CustomSpawnLocation loc)
    {
        this.loc = loc;
        SpawnTemplate spawn = SpawnEngine.addNewSingleTimeSpawn(loc.getWorldId(), id, loc.getX(), loc.getY(), loc.getZ(), (byte)0);
        boss =(Npc) SpawnEngine.spawnObject(spawn, 1);
        this.loc.isUsed = true;
    }

    /**
     * @return the loc
     */
    public CustomSpawnLocation getLoc() {
        return loc;
    }

}
