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
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

/**
 * @author ATracer, Maestros
 */
public class WorldConfig {

	/**
	 * World region size
	 */
	@Property(key = "gameserver.world.region.size", defaultValue = "128")
	public static int WORLD_REGION_SIZE;

	/**
	 * Trace active regions and deactivate inactive
	 */
	@Property(key = "gameserver.world.region.active.trace", defaultValue = "true")
	public static boolean WORLD_ACTIVE_TRACE;
	
	/**
	 * Enable the Christmas decorations
	 */
	@Property(key = "gameserver.decoration.mode", defaultValue = "0")
	public static int ENABLE_DECOR;

    /**
     * Enable the Christmas decorations
     */
    @Property(key = "gameserver.enable.decoration.christmas", defaultValue = "false")
    public static boolean ENABLE_DECORATIONS_CHRISTMAS;
	
    /**
     * Enable the Halloween decorations
     */
    @Property(key = "gameserver.enable.decoration.halloween", defaultValue = "false")
    public static boolean ENABLE_DECORATIONS_HALLOWEEN;
	
    /**
     * Enable the Valentine decorations
     */
    @Property(key = "gameserver.enable.decoration.valentine", defaultValue = "false")
    public static boolean ENABLE_DECORATIONS_VALENTINE;
	
}
