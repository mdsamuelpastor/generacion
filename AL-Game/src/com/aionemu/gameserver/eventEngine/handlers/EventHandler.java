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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.EventEngineService;


/**
 * @author Maestross
 *
 */
public abstract interface EventHandler {

	public abstract void onEventCreate(EventEngineService eventEngine);

  public abstract void onPlayerLogin(Player player);

  public abstract void onPlayerLogOut(Player player);

  public abstract void onEnterEvent(Player player);

  public abstract void onLeaveEvent(Player player);

  public abstract void onReviveEvent(Player player);

  public abstract void doReward(Player player);

  public abstract void onDie(Player player, Creature creature);

  public abstract void onDie(Npc npc);

  public abstract void onStopEvent();

  public abstract void moveToEvent(Player paramPlayer);
}
