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
package com.aionemu.gameserver.utils.telnethandlers;

import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;

import com.aionemu.commons.scripting.classlistener.ClassListener;
import com.aionemu.commons.scripting.classlistener.OnClassLoadUnloadListener;
import com.aionemu.commons.utils.ClassUtils;
import com.aionemu.gameserver.telnet.TelnetExecutor;

/**
 * @author Divinity
 */
public class TelnetCommandLoader extends OnClassLoadUnloadListener implements ClassListener {

	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(TelnetCommandLoader.class);

	private final TelnetCommandHandler telnetCCH;

	/**
	 * @param telnetCCH
	 */
	public TelnetCommandLoader(TelnetCommandHandler telnetCCH) {
		this.telnetCCH = telnetCCH;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void postLoad(Class<?>[] classes) {
		for (Class<?> c : classes) {
			if (log.isDebugEnabled())
				log.debug(c.getName() + " class loading.");

			if (!isValidClass(c))
				continue;

			if (ClassUtils.isSubclass(c, TelnetCommand.class)) {
				Class<? extends TelnetCommand> tmp = (Class<? extends TelnetCommand>) c;
				if (tmp != null) {
					try {
						telnetCCH.registerTelnetCommand(tmp.newInstance());
					}
					catch (InstantiationException e) {
						e.printStackTrace();
					}
					catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}

		// call onClassLoad()
		super.postLoad(classes);

		log.info(TelnetExecutor.lang("TELNET_LOADED_COMMANDS", telnetCCH.getSize()));
	}

	@Override
	public void preUnload(Class<?>[] classes) {
		if (log.isDebugEnabled())
			for (Class<?> c : classes)
				// debug messages
				log.debug(c.getName() + " class unloading.");

		// call onClassUnload()
		super.preUnload(classes);

		telnetCCH.clearHandlers(); // unload all admin handlers.
	}

	public boolean isValidClass(Class<?> clazz) {
		final int modifiers = clazz.getModifiers();

		if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers))
			return false;

		if (!Modifier.isPublic(modifiers))
			return false;

		return true;
	}
}
