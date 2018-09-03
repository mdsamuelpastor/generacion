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
 * 
 * @author -Enomine-
 *
 */

public class FastTrackConfig {
	
	@Property(key = "fast_track.server_num", defaultValue = "2")
    public static int FAST_TRACK_SERVERID;
    
    @Property(key = "fast_track.up_to", defaultValue = "55")
    public static int FAST_TRACK_UPTO;
    
    @Property(key = "fast_track.enable", defaultValue = "true")
    public static boolean FAST_TRACK_ENABLE;

}
