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
 * @author Hilgert
 */
public class DredgionConfig {

	@Property(key = "gameserver.dredgion.timer", defaultValue = "120")
	public static long DREDGION_TIMER;

	@Property(key = "gameserver.dredgion2.enable", defaultValue = "true")
	public static boolean DREDGION2_ENABLE;

	@Property(key = "gameserver.dredgion.time", defaultValue = "0 0 0,12,20 ? * *")
	public static String DREDGION_TIMES;

	@Property(key = "gameserver.dredgion.winner.points.dred1", defaultValue = "3000")
	public static int WINNER_POINTS_DRED1;

	@Property(key = "gameserver.dredgion.winner.points.dred2", defaultValue = "4500")
	public static int WINNER_POINTS_DRED2;

	@Property(key = "gameserver.dredgion.winner.points.dred3", defaultValue = "6500")
	public static int WINNER_POINTS_DRED3;

	@Property(key = "gameserver.dredgion.looser.points.dred1", defaultValue = "1500")
	public static int LOOSER_POINTS_DRED1;

	@Property(key = "gameserver.dredgion.looser.points.dred2", defaultValue = "2500")
	public static int LOOSER_POINTS_DRED2;

	@Property(key = "gameserver.dredgion.looser.points.dred3", defaultValue = "4000")
	public static int LOOSER_POINTS_DRED3;

	@Property(key = "gameserver.dredgion.draw.points.dred1", defaultValue = "2250")
	public static int DRAW_POINTS_DRED1;

	@Property(key = "gameserver.dredgion.draw.points.dred2", defaultValue = "3750")
	public static int DRAW_POINTS_DRED2;

	@Property(key = "gameserver.dredgion.draw.points.dred3", defaultValue = "5000")
	public static int DRAW_POINTS_DRED3;
}
