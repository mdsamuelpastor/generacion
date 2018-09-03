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
package com.aionemu.gameserver.services;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.lambdaj.Lambda;

import com.aionemu.gameserver.eventEngine.EventEngine;
import com.aionemu.gameserver.eventEngine.handlers.EventHandler;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceMember;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;


/**
 * @author Maestross
 *
 */
public class EventEngineService {

	private static final Logger log = LoggerFactory.getLogger(EventEngineService.class);
  private String currentEvent = "";
  private EventHandler eventHendler = EventEngine.getInstance().getNewEventHandler(this.currentEvent);
  private FastList<Player> playersInEvent = FastList.newInstance();
  private int mapId;
  private FastMap<Integer, ScheduledFuture<?>> shedules = FastMap.newInstance();
  private PlayerAllianceMember member;
  private Player player;
  
  public EventEngineService() {
    setEventHendler(this.eventHendler);
    log.info("Event Service Loading!");
  }
  
  public void onLogOut(Player player) {
    if (isPlayerInEvent(player))
    {
      getEventHandler().onPlayerLogOut(player);
      scheduleRemove(player);
    }
  }
  
  public void onLogIn(Player player) {
  	if (isPlayerInEvent(player))
    {
      getEventHandler().onPlayerLogin(player);
      cancelScheduleRemove(player.getObjectId().intValue());
      if (!player.isInAlliance2())
        addPlayerToAlliance(player, member);
    }
  }
  
  public void onEnterEvent(Player player) {
    if (isPlayerInEvent(player))
      getEventHandler().onEnterEvent(player);
  }
  
  public void onLeaveEvent(Player player) {
    if (isPlayerInEvent(player))
    {
      getEventHandler().onLeaveEvent(player);
    }
  }
  
  public void onReviveEvent(Player player) {
    if (isPlayerInEvent(player))
      getEventHandler().onReviveEvent(player);
  }
  
  public void onDie(Player player, Creature creature) {
    getEventHandler().onDie(player, creature);
  }
  
  public void onDie(Npc npc) {
    if ((!this.currentEvent.equals("")) && (npc.getWorldId() == this.mapId))
      getEventHandler().onDie(npc);
  }
  
  public void onStopEvent() {
    getEventHandler().onStopEvent();
  }
  
  public void moveToEvent(Player player) {
  	enterToGroup(player, member);
    getEventHandler().moveToEvent(player);
  }
  
  public void onEventCreate(EventEngineService eventEngine) {
    this.eventHendler.onEventCreate(eventEngine);
  }
  
  public void onEventStart(String name, int mapId) {
    this.currentEvent = name;
    this.eventHendler = EventEngine.getInstance().getNewEventHandler(name);
    setEventHendler(this.eventHendler);
    this.mapId = mapId;
    ThreadPoolManager.getInstance().schedule(new Runnable()
    {
      public void run()
      {
        EventEngineService.this.onStopEvent();
      }
    }
    , 3300000L);
    onEventCreate(this);
  }
  
  public void clearEvent() {
    this.currentEvent = "";
    this.eventHendler = EventEngine.getInstance().getNewEventHandler(this.currentEvent);
    setEventHendler(this.eventHendler);
    this.playersInEvent.clear();
    this.mapId = 0;
  }
  
  public final EventHandler getEventHandler() {
    return this.eventHendler;
  }
  
  private void setEventHendler(EventHandler eventEngine) {
    this.eventHendler = eventEngine;
  }
  
  public void addPlayer(Player player) {
    this.playersInEvent.add(player);
  }
  
  public FastList<Player> getPlayersInEvent() {
    return this.playersInEvent;
  }
  
  public Player getPlayerInEvent(Player player) {
    FastList.Node max = getPlayersInEvent().head();
    FastList.Node min = getPlayersInEvent().tail();
    while ((max = max.getNext()) != min)
      if (((Player)max.getValue()).equals(player))
        return (Player)max.getValue();
    return null;
  }
  
  public boolean isPlayerInEvent(Player player) {
    return getPlayersInEvent().contains(player);
  }
  
  public int getMapId() {
    return this.mapId;
  }
  
  private void cancelScheduleRemove(int time) {
    if (this.shedules.containsKey(Integer.valueOf(time)))
    {
      ((ScheduledFuture)this.shedules.get(Integer.valueOf(time))).cancel(true);
      this.shedules.remove(Integer.valueOf(time));
    }
  }
  
  public void scheduleRemove(Player player) {
			if (isPlayerInEvent(player))
        getPlayersInEvent().remove(player);
  }
  
  private void addPlayerToAlliance(Player player, PlayerAllianceMember member) {
    List players = getPlayersInsideByRace(player.getRace());
    Iterator localIterator = players.iterator();
    while (localIterator.hasNext())
    {
      Player localPlayer = (Player)localIterator.next();
      if ((!localPlayer.isInAlliance2()) || (!localPlayer.getPlayerAlliance2().isFull()))
        continue;
      localPlayer.getPlayerAlliance2().addMember(member);
    }
  }
  
  public synchronized void enterToGroup(Player player, PlayerAllianceMember member) {
    List players = getPlayersInsideByRace(player.getRace());
    if ((players.size() == 1) && (!((Player)players.get(0)).isInAlliance2()))
    {
      PlayerAlliance alliance = PlayerAllianceService.createAlliance((Player)players.get(0), player);
      alliance.setGroupType(2);
    }
    else if ((!players.isEmpty()) && (((Player)players.get(0)).isInAlliance2()))
    {
      ((Player)players.get(0)).getPlayerAlliance2().addMember(member);
    }
    this.playersInEvent.add(player);
  }
  
  private List<Player> getPlayersInsideByRace(Race race) {
    return Lambda.select(this.playersInEvent, Lambda.having(((Player)Lambda.on(Player.class)).getRace(), Matchers.equalTo(race)));
  }
  
  public void sendRaceMsg(String msg, String msg2) {
  	Iterator<Player> iter = World.getInstance().getPlayersIterator();
		while (iter.hasNext()) {
        if (player.getRace() == Race.ASMODIANS)
          PacketSendUtility.sendYellowMessage(player, msg);
        else
          PacketSendUtility.sendYellowMessage(player, msg2);
      }
  }
  
  public static EventEngineService getInstance() {
    return SingletonHolder.instance;
  }

  private static class SingletonHolder {
    protected static final EventEngineService instance = new EventEngineService();
  }
}
