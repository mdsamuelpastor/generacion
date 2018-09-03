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
package com.aionemu.gameserver.controllers.movement;

/**
 * @author Mr. Poke
 */
public class MovementMask {

	public static final byte IMMEDIATE = 0;
	public static final byte GLIDE = (byte) 0x04;
	public static final byte FALL = (byte) 0x08;
	public static final byte VEHICLE = (byte) 0x10;
	public static final byte MOUSE = (byte) 0x20;
	public static final byte STARTMOVE = (byte) 0xC0;
	public static final byte NPC_WALK_SLOW = -22;
	public static final byte NPC_WALK_FAST = -24;
	public static final byte NPC_RUN_SLOW = -28;
	public static final byte NPC_RUN_FAST = -30;
	public static final byte NPC_STARTMOVE = -32;
}
