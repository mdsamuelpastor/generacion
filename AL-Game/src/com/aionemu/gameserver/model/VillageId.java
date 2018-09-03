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
package com.aionemu.gameserver.model;


/**
 * @author Maestross
 * 
 * The enum for the villages
 */
public enum VillageId {

	NONE(0),//No Village
	
	VILLAGE_1_ELY(1001),//Village Ely Himmelsleuchten Dorf PROSPERITYS_LIGHT_VILLAGE
	VILLAGE_2_ELY(1002),//Village Ely Goldtal Dorf GOLDENDELL_VILLAGE
	VILLAGE_3_ELY(1003),//Village Ely Leuchtkäfer Dorf GLIMMERTREE_VILLAGE
	VILLAGE_4_ELY(1004),//Village Ely Meeresbriese Dorf VIBRANT_GARDEN_VILLAGE
	VILLAGE_5_ELY(1005),//Village Ely Küstendorf SUNSET_COAST_VILLAGE
	VILLAGE_6_ELY(1006),//Village Ely Dornbusch Dorf THORN_TREE_VILLAGE
	VILLAGE_7_ELY(1007),//Village Ely Statuen Dorf SEAPATH_VILLAGE
	VILLAGE_8_ELY(1008),//Village Ely Brilliant Insel Dorf BRILLIANT_ISLE_VILLAGE
	VILLAGE_9_ELY(1009),//Village Ely Palmen Dorf PALMCREST_VILLAGE
	VILLAGE_10_ELY(1010),//Village Ely Silberberg Dorf SILVERSPIRE_VILLAGE
	VILLAGE_11_ELY(1011),//Village Ely Sommerfeld Dorf SUMMERFIELD_VILLAGE
	VILLAGE_12_ELY(1012),//Village Ely Strand-Dorf SANDBANK_VILLAGE
	VILLAGE_13_ELY(1013),//Village Ely Perlenbucht Dorf PEARL_COVE_VILLAGE
	VILLAGE_14_ELY(1014),//Village Ely Felsen Dorf ROCKROOF_VILLAGE
	VILLAGE_15_ELY(1015),//Village Ely Bergkuppen Dorf STONECREST_VILLAGE
	VILLAGE_16_ELY(1016),//Village Ely Schilfteich Dorf REEDPOND_VILLAGE
	VILLAGE_17_ELY(1017),//Village Ely Gipfelkronen Dorf PEAKS_CROWN_VILLAGE
	VILLAGE_18_ELY(1018),//Village Ely Elrokhügel Dorf ELROCO_HILL_VILLAGE
	VILLAGE_19_ELY(1019),//Village Ely Gipfel Dorf HILLCLIMB_VILLAGE
	VILLAGE_20_ELY(1020),//Village Ely Funken Dorf BETUA_GLEN_VILLAGE
	VILLAGE_21_ELY(1021),//Village Ely Klippen Dorf MILLENNIUM_TREE_VILLAGE
	VILLAGE_22_ELY(1022),//Village Ely Brunnen Dorf RIVERWELL_VILLAGE
	VILLAGE_23_ELY(1023),//Village Ely Nebel Dorf MISTY_VISTA_VILLAGE
	VILLAGE_24_ELY(1024),//Village Ely Quell Dorf HOTSPRINGS_VILLAGE
	VILLAGE_25_ELY(1025),//Village Ely Windmühlen Dorf MILL_RIDGE_VILLAGE
	
	VILLAGE_1_ASMO(1),//Village Asmo 
	VILLAGE_2_ASMO(2),//Village Asmo
	VILLAGE_3_ASMO(3),//Village Asmo
	VILLAGE_4_ASMO(4),//Village Asmo
	VILLAGE_5_ASMO(5),//Village Asmo
	VILLAGE_6_ASMO(6),//Village Asmo
	VILLAGE_7_ASMO(7),//Village Asmo
	VILLAGE_8_ASMO(8),//Village Asmo
	VILLAGE_9_ASMO(9),//Village Asmo
	VILLAGE_10_ASMO(10),//Village Asmo
	VILLAGE_11_ASMO(11),//Village Asmo
	VILLAGE_12_ASMO(12),//Village Asmo
	VILLAGE_13_ASMO(13),//Village Asmo
	VILLAGE_14_ASMO(14),//Village Asmo
	VILLAGE_15_ASMO(15),//Village Asmo
	VILLAGE_16_ASMO(16),//Village Asmo
	VILLAGE_17_ASMO(17),//Village Asmo
	VILLAGE_18_ASMO(18),//Village Asmo
	VILLAGE_19_ASMO(19),//Village Asmo
	VILLAGE_20_ASMO(20),//Village Asmo
	VILLAGE_21_ASMO(21),//Village Asmo
	VILLAGE_22_ASMO(22),//Village Asmo
	VILLAGE_23_ASMO(23),//Village Asmo
	VILLAGE_24_ASMO(24),//Village Asmo
	VILLAGE_25_ASMO(25);//Village Asmo
	
	private int id;

	private VillageId(int id) {
		this.id = id;
	}

	public int id() {
		return id;
	}
}
