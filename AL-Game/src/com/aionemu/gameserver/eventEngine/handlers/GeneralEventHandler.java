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
package com.aionemu.gameserver.eventEngine.handlers;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.services.EventEngineService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;


/**
 * @author Maestross
 *
 */
public class GeneralEventHandler implements EventHandler {

	protected final long creationTime = System.currentTimeMillis();
  protected EventEngineService eventInstance;
  protected int mapId;
  
  public void onEventCreate(EventEngineService eventEngine) {
    this.eventInstance = eventEngine;
    this.mapId = eventEngine.getMapId();
  }

  public void onPlayerLogin(Player player) {
  }

  public void onPlayerLogOut(Player player) {
  }

  public void onEnterEvent(Player player) {
  }

  public void onLeaveEvent(Player player) {
  }

  public void onReviveEvent(Player player) {
  }

  public void doReward(Player player) {
  }

  public void onDie(Player paramPlayer, Creature creature) {
  }

  public void onDie(Npc npc) {
  }

  public void onStopEvent() {
  }

  public void moveToEvent(Player player) {
  }
  
  protected VisibleObject spawn(int id, float x, float y, float z, byte h) {
    SpawnTemplate spawnTemplate = SpawnEngine.addNewSingleTimeSpawn(this.mapId, id, x, y, z, h);
    return SpawnEngine.spawnObject(spawnTemplate, 1);
  }

  protected VisibleObject spawn(int id, float x, float y, float z, byte h, int staticId) {
  	SpawnTemplate spawnTemplate = SpawnEngine.addNewSingleTimeSpawn(this.mapId, id, x, y, z, h);
  	spawnTemplate.setStaticId(staticId);
    return SpawnEngine.spawnObject(spawnTemplate, 1);
  }

  protected VisibleObject spawn(int id, float x, float y, float z, byte h, String masterName, int respawnTime) {
  	SpawnTemplate spawnTemplate = SpawnEngine.addNewSingleTimeSpawn(this.mapId, id, x, y, z, h, respawnTime, masterName);
    return SpawnEngine.spawnObject(spawnTemplate, 1);
  }

  protected VisibleObject spawn(int id, float x, float y, float z, byte h, String masterName, int respawnTime, int masterId) {
  	SpawnTemplate spawnTemplate = SpawnEngine.addNewSpawn(this.mapId, id, x, y, z, h, respawnTime, masterName, masterId);
    return SpawnEngine.spawnObject(spawnTemplate, 1);
  }
}
