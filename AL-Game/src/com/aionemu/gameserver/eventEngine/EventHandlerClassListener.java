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

import javassist.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.scripting.classlistener.ClassListener;
import com.aionemu.commons.utils.ClassUtils;
import com.aionemu.gameserver.eventEngine.handlers.EventHandler;


/**
 * @author Maestross
 *
 */
public class EventHandlerClassListener implements ClassListener {

	private static final Logger log = LoggerFactory.getLogger(EventHandlerClassListener.class);

	@SuppressWarnings("unchecked")
	@Override
	public void postLoad(Class<?>[] classes) {
		for (Class<?> c : classes) {
    {
      if (log.isDebugEnabled())
        log.debug("Load class " + c.getName());
      if ((!isValidClass(c)) || (!ClassUtils.isSubclass(c, EventHandler.class)))
        continue;
      Class<? extends EventHandler> tmp = (Class<? extends EventHandler>) c;
      if (tmp == null)
        continue;
      EventEngine.getInstance().addEventHandlerClass(tmp);
    }
	 }
  }


	public void preUnload(Class<?>[] classes) {
    if (log.isDebugEnabled())
    	for (Class<?> c : classes)
				log.debug("Unload class " + c.getName());
  }

  public boolean isValidClass(Class<?> paramClass)
  {
    int i = paramClass.getModifiers();
    if ((Modifier.isAbstract(i)) || (Modifier.isInterface(i)))
      return false;
    return Modifier.isPublic(i);
  }
}
