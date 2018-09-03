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
package com.aionemu.gameserver.world.zone;

import javolution.util.FastMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ZoneName {

	private static final Logger log = LoggerFactory.getLogger(ZoneName.class);

	private static final FastMap<String, ZoneName> zoneNames = FastMap.newInstance();
	public static final String NONE = "NONE";
	public static final String ABYSS_CASTLE = "_ABYSS_CASTLE_AREA_";
	private String _name;

	private ZoneName(String name) {
		_name = name;
	}

	public String name() {
		return _name;
	}

	public int id() {
		return _name.hashCode();
	}

	public static final ZoneName createOrGet(String name) {
		name = name.toUpperCase();
		if (zoneNames.containsKey(name))
			return zoneNames.get(name);
		ZoneName newZone = new ZoneName(name);
		zoneNames.put(name, newZone);
		return newZone;
	}

	public static final int getId(String name) {
		name = name.toUpperCase();
		if (zoneNames.containsKey(name))
			return zoneNames.get(name).id();
		return zoneNames.get("NONE").id();
	}

	public static final ZoneName get(String name) {
		name = name.toUpperCase();
		if (zoneNames.containsKey(name))
			return zoneNames.get(name);
		log.warn("Missing zone : " + name);
		return zoneNames.get("NONE");
	}

	static {
		zoneNames.put("NONE", new ZoneName("NONE"));
		zoneNames.put("_ABYSS_CASTLE_AREA_", new ZoneName("_ABYSS_CASTLE_AREA_"));
	}
}
