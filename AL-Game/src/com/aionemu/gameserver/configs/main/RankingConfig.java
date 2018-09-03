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
 * @author Sarynth, GoodT
 */
public class RankingConfig {

	@Property(key = "gameserver.topranking.updaterule", defaultValue = "0 0 0 * * ?")
	public static String TOP_RANKING_UPDATE_RULE;
	
	/*
	 * Abyss rank settings - ap needed to abyss rank and player counts for ranks
	 */
	@Property(key = "gameserver.abyssrankpoints.GRADE8_SOLDIER", defaultValue = "1200")
	public static int GRADE8_SOLDIER;	
	
	@Property(key = "gameserver.abyssrankpoints.GRADE7_SOLDIER", defaultValue = "4220")
	public static int GRADE7_SOLDIER;
	
	@Property(key = "gameserver.abyssrankpoints.GRADE6_SOLDIER", defaultValue = "10990")
	public static int GRADE6_SOLDIER;
	
	@Property(key = "gameserver.abyssrankpoints.GRADE5_SOLDIER", defaultValue = "23500")
	public static int GRADE5_SOLDIER;
	
	@Property(key = "gameserver.abyssrankpoints.GRADE4_SOLDIER", defaultValue = "42780")
	public static int GRADE4_SOLDIER;
	
	@Property(key = "gameserver.abyssrankpoints.GRADE3_SOLDIER", defaultValue = "69700")
	public static int GRADE3_SOLDIER;
	
	@Property(key = "gameserver.abyssrankpoints.GRADE2_SOLDIER", defaultValue = "105600")
	public static int GRADE2_SOLDIER;
	
	@Property(key = "gameserver.abyssrankpoints.GRADE1_SOLDIER", defaultValue = "150800")
	public static int GRADE1_SOLDIER;
	
	@Property(key = "gameserver.abyssrankpoints.STAR1_OFFICER", defaultValue = "214100")
	public static int STAR1_OFFICER;
	
	@Property(key = "gameserver.abyssrankpoints.STAR1_OFFICERC", defaultValue = "1000")
	public static int STAR1_OFFICERC;
	
	@Property(key = "gameserver.abyssrankpoints.STAR2_OFFICER", defaultValue = "278700")
	public static int STAR2_OFFICER;
	
	@Property(key = "gameserver.abyssrankpoints.STAR2_OFFICERC", defaultValue = "700")
	public static int STAR2_OFFICERC;
	
	@Property(key = "gameserver.abyssrankpoints.STAR3_OFFICER", defaultValue = "344500")
	public static int STAR3_OFFICER;
	
	@Property(key = "gameserver.abyssrankpoints.STAR3_OFFICERC", defaultValue = "500")
	public static int STAR3_OFFICERC;
	
	@Property(key = "gameserver.abyssrankpoints.STAR4_OFFICER", defaultValue = "411700")
	public static int STAR4_OFFICER;
	
	@Property(key = "gameserver.abyssrankpoints.STAR4_OFFICERC", defaultValue = "300")
	public static int STAR4_OFFICERC;
	
	@Property(key = "gameserver.abyssrankpoints.STAR5_OFFICER", defaultValue = "488200")
	public static int STAR5_OFFICER;
	
	@Property(key = "gameserver.abyssrankpoints.STAR5_OFFICERC", defaultValue = "100")
	public static int STAR5_OFFICERC;
	
	@Property(key = "gameserver.abyssrankpoints.GENERAL", defaultValue = "565400")
	public static int GENERAL;
	
	@Property(key = "gameserver.abyssrankpoints.GENERALC", defaultValue = "30")
	public static int GENERALC;
	
	@Property(key = "gameserver.abyssrankpoints.GREAT_GENERAL", defaultValue = "643200")
	public static int GREAT_GENERAL;
	
	@Property(key = "gameserver.abyssrankpoints.GREAT_GENERALC", defaultValue = "10")
	public static int GREAT_GENERALC;
	
	@Property(key = "gameserver.abyssrankpoints.COMMANDER", defaultValue = "721600")
	public static int COMMANDER;
	
	@Property(key = "gameserver.abyssrankpoints.COMMANDERC", defaultValue = "3")
	public static int COMMANDERC;
	
	@Property(key = "gameserver.abyssrankpoints.SUPREME_COMMANDER", defaultValue = "800700")
	public static int SUPREME_COMMANDER;
	
	@Property(key = "gameserver.abyssrankpoints.SUPREME_COMMANDERC", defaultValue = "1")
	public static int SUPREME_COMMANDERC;	
}
