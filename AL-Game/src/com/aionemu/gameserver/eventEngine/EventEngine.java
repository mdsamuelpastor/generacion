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
package com.aionemu.gameserver.eventEngine;

import java.io.File;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javolution.util.FastMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.scripting.classlistener.AggregatedClassListener;
import com.aionemu.commons.scripting.classlistener.OnClassLoadUnloadListener;
import com.aionemu.commons.scripting.classlistener.ScheduledTaskClassListener;
import com.aionemu.commons.scripting.scriptmanager.ScriptManager;
import com.aionemu.gameserver.GameServerError;
import com.aionemu.gameserver.eventEngine.handlers.EventHandler;
import com.aionemu.gameserver.eventEngine.handlers.EventName;
import com.aionemu.gameserver.eventEngine.handlers.GeneralEventHandler;
import com.aionemu.gameserver.model.GameEngine;
import com.aionemu.gameserver.utils.Util;


/**
 * @author Maestross
 *
 */
public class EventEngine implements GameEngine {

	private static final Logger log = LoggerFactory.getLogger(EventEngine.class);
  private static ScriptManager scriptManager = new ScriptManager();
  public static final File EVENT_DESCRIPTOR_FILE = new File("./data/scripts/system/eventhandlers.xml");
  public static final EventHandler DUMMY_EVENT_HANDLER = new GeneralEventHandler();
  private FastMap<String, Class<? extends EventHandler>> handlers = new FastMap().shared();
  
  @Override
  public void load(CountDownLatch progressLatch) {
    log.info("Event engine load started");
    scriptManager = new ScriptManager();
    AggregatedClassListener acl = new AggregatedClassListener();
    acl.addClassListener(new OnClassLoadUnloadListener());
    acl.addClassListener(new ScheduledTaskClassListener());
    acl.addClassListener(new EventHandlerClassListener());
    scriptManager.setGlobalClassListener(acl);
    try
    {
      scriptManager.load(EVENT_DESCRIPTOR_FILE);
      log.info("Loaded " + this.handlers.size() + " event handlers.");
    }
    catch (Exception localException)
    {
      throw new GameServerError("Can't initialize event handlers.", localException);
    }
    finally {
			if (progressLatch != null)
				progressLatch.countDown();
		}
  }
  
  @Override
  public void shutdown() {
    log.info("Event engine shutdown started");
    scriptManager.shutdown();
    scriptManager = null;
    this.handlers.clear();
    log.info("Event engine shutdown complete");
  }
  
  public void reload() {
    Util.printSection("Event");
    log.info("Event engine reload started");
    ScriptManager localScriptManager;
    try
    {
      localScriptManager = new ScriptManager();
      AggregatedClassListener acl = new AggregatedClassListener();
      acl.addClassListener(new OnClassLoadUnloadListener());
      acl.addClassListener(new ScheduledTaskClassListener());
      acl.addClassListener(new EventHandlerClassListener());
      localScriptManager.setGlobalClassListener(acl);
      try
      {
        localScriptManager.load(EVENT_DESCRIPTOR_FILE);
      }
      catch (Exception localException2)
      {
        throw new GameServerError("Can't initialize event handlers.", localException2);
      }
    }
    catch (Exception localException1)
    {
      throw new GameServerError("Can't reload Event engine.", localException1);
    }
    if (localScriptManager != null)
    {
      shutdown();
      load(null);
    }
  }
  
  public EventHandler getNewEventHandler(String string) {
    Class localClass = (Class)this.handlers.get(string);
    EventHandler localEventHandler = null;
    if (localClass != null)
      try
      {
        localEventHandler = (EventHandler)localClass.newInstance();
      }
      catch (Exception localException)
      {
        log.warn("Can't instantiate event handler " + string, localException);
      }
    if (localEventHandler == null)
      localEventHandler = DUMMY_EVENT_HANDLER;
    return localEventHandler;
  }
  
  final void addEventHandlerClass(Class<? extends EventHandler> paramClass) {
    EventName eventName = (EventName)paramClass.getAnnotation(EventName.class);
    if (eventName != null)
      this.handlers.put(eventName.value(), paramClass);
  }
  
  public Map<String, Class<? extends EventHandler>> getHendlers() {
    return this.handlers;
  }
  
  public static final EventEngine getInstance() {
    return SingletonHolder.instance;
  }
  
  private static class SingletonHolder {
    protected static final EventEngine instance = new EventEngine();
  }
}
