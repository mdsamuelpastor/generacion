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

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum TribeClass {

	ABDARK_AABDRAGON,
	ABDRAGON_AABDARK,
	ABYSSDRAKANGATE,
	AGGRESSIVE1_AAGGRESSIVE2,
	AGGRESSIVE2_AAGGRESSIVE1,
	AGGRESSIVEESCORT,
	AGGRESSIVEMONSTER,
	AGGRESSIVESINGLEMONSTER,
	AGGRESSIVESUPPORTMONSTER,
	AGGRESSIVESUPPORTMONSTER2,
	AGGRESSIVETOIDELIM,
	AGGRESSIVETOPCPET,
	AGGRESSIVETOSHULACK,
	AGGRESSIVETOSHULACK2,
	AGGRESSIVE_ALL,
	AGGRESSIVE_DARK,
	AGGRESSIVE_DARK_HSPECTRE,
	AGGRESSIVE_DRAGON,
	AGGRESSIVE_LIGHT,
	AGGRESSIVE_LIGHT_HSPECTRE,
	AGRESSIVETOMONSTER,
	AHELLHOUND,
	AIREL1,
	AIREL2,
	AIREL3,
	AIRELBOSS,
	ANTI_CRYSTAL,
	APRETOR,
	ARCHERYBASFELT2_ATARGETBASFELT2_DF1,
	ARCHERYBASFELT2_ATARGETBASFELT2_LF1,
	ARCHERYBASFELT_ATARGETBASFELT_DF1,
	ARCHERYBASFELT_ATARGETBASFELT_LF1,
	ASIST_D(true),
	ATAURIC,
	ATKDRAKAN,
	BAT_FAMILY_ELITE,
	BMDGUARDIAN,
	BMLGUARDIAN,
	BOMB_LIZARDMAN,
	BRAX,
	BROHUM,
	BROWNIE,
	BROWNIECOWARD,
	BROWNIEFELLER_HZAIF_LF1,
	BROWNIEGUARD,
	CALYDON,
	CALYDON_POLYMORPH,
	CHERUBIM2ND,
	CHILDMONSTER,
	CONSIADE,
	CONSIADE_SUM,
	CRESTLICH,
	CRYSTAL,
	CRYSTAL_NMDD,
	CRYSTAL_SUM,
	CYCLOPSBOSS,
	D1_HKERUBIM_LF1,
	DARK_LICH,
	DARK_MOB,
	DARK_NPC,
	DARK_SUR_MOB,
	DARU,
	DARU_HZAIF,
	DECOY,
	DECOY_HUNGER,
	DRAGGMOB_ADRGUARD1,
	DRAGON,
	DRAGON_CTRL,
	DRAGON_SLAVE,
	DRAKANDF3BOSS,
	DRAKANDF3SLAVE,
	DRAKANDOOR,
	DRAKANPOLYMORPH,
	DRAKAN_DGUARD,
	DRAKAN_LGUARD,
	DRAKEPURPLE_MASTER,
	DRAKEPURPLE_SLAVE,
	DRAKY_BOMB_EX,
	DRAKY_BOMB_MASTER,
	DRAMATA,
	DRAMATATIMERA,
	DRAMATATIMERB,
	DRAMA_EVE_NONPC_A,
	DRAMA_EVE_NONPC_B,
	DRAMA_EVE_NONPC_DARKA,
	DRAMA_EVE_NONPC_DARKB,
	DRAMA_KIMEIA_DARKNPC,
	DRAMA_KIMEIA_MOB,
	DRAMA_KIMEIA_NPC,
	DUMMY,
	DUMMY2,
	DUMMY2_DGUARD(Race.ASMODIANS),
	DUMMY2_LGUARD(Race.ELYOS),
	DUMMY_DGUARD(Race.ASMODIANS),
	DUMMY_LGUARD(Race.ELYOS),
	ELEMENTAL_AIR,
	ELEMENTAL_EARTH,
	ELEMENTAL_FIRE,
	ELEMENTAL_WATER,
	ENEMY_AGUARD_DARK,
	ESCORT,
	ETTIN,
	F4GUARD_DARK(Race.ASMODIANS),
	F4GUARD_DRAGON(Race.DRAGON),
	F4GUARD_LIGHT(Race.ELYOS),
	F4RAID,
	FANATIC,
	FARMER_HKERUBIM_LF1,
	FETHLOT,
	FIELD_OBJECT_ALL,
	FIELD_OBJECT_ALL_MONSTER,
	FIELD_OBJECT_DARK(true),
	FIELD_OBJECT_LIGHT(true),
	FIREEL1,
	FIREEL2,
	FIREEL3,
	FIREELBOSS,
	FIREFUNGY,
	FIRETEMPLE_MOB,
	FRIENDLYTOIDELIM,
	FRILLFAIMAMBABY,
	FRILLFAIMAMCOUPLE,
	FRILLFAIMAMMOM,
	FUNGUS,
	FUNGY,
	GARGOYLE,
	GARGOYLE_ELITE,
	GENERAL(true),
	GENERALDA_ALIDR,
	GENERALDR_ALIDA,
	GENERALDR_ALIDA_SUPPORT,
	GENERAL_ADADR,
	GENERAL_DARK(true),
	GENERAL_DARK_IDLDF4A_INTRO,
	GENERAL_DARK_LYCAN,
	GENERAL_DRAGON(true),
	GENERAL_IDLDF4A_INTRO,
	GENERAL_KRALL,
	GENERAL_LDF4A_PUBLIC_YUN,
	GHOSTDARK,
	GHOSTLIGHT,
	GHTIMER,
	GMASTER,
	GOBLIN,
	GOLEM_SWITCH,
	GRIFFO,
	GRIFFON,
	GSLAVE,
	GUARD(Race.ELYOS, true),
	GUARDDARK_ALEHPAR(Race.ASMODIANS),
	GUARDIAN,
	GUARD_D1NOATTACK,
	GUARD_DARK(Race.ASMODIANS, true),
	GUARD_DARKAENEMY(Race.ASMODIANS),
	GUARD_DARKMA(Race.ASMODIANS),
	GUARD_DARK_ALYCANARATMAN_DF1(Race.ASMODIANS),
	GUARD_DRAGON(Race.DRAGON, true),
	GUARD_DRAGONMA(Race.DRAGON),
	GUARD_FTARGETBASFELT_DF1,
	GUARD_FTARGETBASFELT_LF1,
	GUARD_LIGHTMA(Race.ELYOS),
	GUARD_LIGHT_AKERUBIM_LF1(Race.ELYOS),
	GURURU_D1,
	GURURU_DECO,
	HIPPOLIZARD,
	HOLYSERVANT,
	HOLYSERVANT_DEBUFFER,
	HOLYSERVANT_DESPAWN,
	HOSTILEONLYMONSTER,
	HOSTILE_ALL,
	IDCATACOMBS_DRAKE,
	IDCATACOMBS_DRAKE_SUM,
	IDCATACOMBS_TAROS,
	IDELEMENTAL2HEALSUM,
	IDELIM,
	IDELIM_FRIEND,
	IDLDF4A_DECOY,
	IDLF1_MONSTER,
	IDRAKSHA_DRAGONTOOTH,
	IDRAKSHA_DRAKAN,
	IDRAKSHA_NORMAL,
	IDRAKSHA_RAKSHA,
	IDTEMPLE_BUGS,
	IDTEMPLE_STONE,
	IDYUN_ANCIENT,
	IDYUN_ANTIBOMBER,
	IDYUN_BOMBER,
	IDYUN_D1,
	IDYUN_DOOR,
	IDYUN_FIST,
	IDYUN_HDRAKAN,
	IDYUN_MEROPS,
	IDYUN_OBJECTS,
	IDYUN_ODRAKAN,
	IDYUN_POLYMORPH,
	IDYUN_RDRAKAN,
	IDYUN_SIEGEWEAPON,
	IDYUN_TARGET,
	IDYUN_VASARTI,
	IDYUN_XDRAKAN,
	KALNIF_AMINX,
	KALNIF_ATOG,
	KERUBIM_AD1_LF1,
	KERUBIM_AFARMER_LF1,
	KRALL,
	KRALLMASTER,
	KRALLWIZARDCY,
	KRALL_PC,
	KRALL_TRAINING,
	LASBERG,
	LDF4A_CALYDON,
	LDF4A_LG_SKILL,
	LDF4A_LG_SKILL_RECEIVE,
	LDF4A_NEPILIM,
	LDF4A_NEPILIM_SUMMON,
	LDF4A_OWLLAU,
	LDF4A_POLY_SHULACK,
	LDF4A_PUBLIC_MONSTER,
	LDF4A_SANDWARM,
	LDF4A_YUN_GUARD,
	LDF4B_AGGRESSIVEYUNSOLDIER,
	LDF4B_ATTACKWAGON,
	LDF4B_FANATIC,
	LDF4B_MINE,
	LDF4B_SPARRING_DGUARD,
	LDF4B_SPARRING_DGUARD2,
	LDF4B_SPARRING_GUARD,
	LDF4B_SPARRING_GUARD2,
	LDF4B_SPARRING_Y,
	LDF4B_SPARRING_Y2,
	LDF4B_WAGON,
	LEHPAR,
	LEHPAR_AGUARDDARK,
	LEHPAR_APRETOR,
	LICH_SOULEDSTONE,
	LIGHT_DENLABIS,
	LIGHT_LICH,
	LIGHT_MOB,
	LIGHT_NPC,
	LIGHT_SUR_MOB,
	LIZARDMAN,
	LIZARDMAN_BOMB,
	LIZARDMAN_KB,
	LUPYLLINI,
	LYCAN,
	LYCANDF2MASTER,
	LYCANDF2SLAVE1,
	LYCANDF2SLAVE2,
	LYCANMASTER,
	LYCAN_AGUARD_DARK_DF1,
	LYCAN_HUNTER,
	LYCAN_MAGE,
	LYCAN_PC,
	LYCAN_PET,
	LYCAN_PET_TRAINING,
	LYCAN_SUM,
	LYCAN_TRAINING,
	L_DRGUARD_ADRAGGMOB1,
	MAIDENGOLEM_ELITE,
	MANDURITWEAK,
	MERDION,
	MINX,
	MINX_HKALNIF,
	MINX_HZAIF,
	MONSTER(true),
	MONSTER_LDF4A_PUBLIC_LIZARDMAN,
	MOSBEARBABY,
	MOSBEARFATHER,
	MUTA,
	MUTA_HOCTASIDE,
	NEUT,
	NEUTBUG,
	NEUTQUEEN,
	NEUTRAL_DGUARD,
	NEUTRAL_LGUARD,
	NLIZARDMAN,
	NLIZARDMAN2,
	NLIZARDPET,
	NLIZARDPRIEST,
	NLIZARDRAISER,
	NNAGA,
	NNAGA_BOSS_SERVANT,
	NNAGA_ELEMENTAL,
	NNAGA_ELEMENTALIST,
	NNAGA_PRIEST,
	NNAGA_PRIESTBOSS,
	NNAGA_SERVANT,
	NOFIGHT,
	NONE(true),
	NPC(true),
	OCTASIDEBABY,
	OCTASIDE_AMUTA,
	ORC,
	PARENTSMONSTER,
	PC(true),
	PC_DARK(true),
	PC_DRAGON(true),
	PET,
	PET_DARK,
	POLYMORPHFUNGY,
	POLYMORPHPARROT,
	PREDATOR,
	PRETOR_ALEHPAR,
	PREY,
	PROTECTGUARD_DARK(Race.ASMODIANS),
	PROTECTGUARD_LIGHT(Race.ELYOS),
	QUESTGUARD_DARK,
	QUESTGUARD_LIGHT,
	RANMARK,
	RATMAN,
	RATMANDFWORKER,
	RATMANWORKER,
	RATMAN_AGUARD_DARK_DF1,
	ROBBERALDER_ASPRIGG_DF1,
	SAMM,
	SAM_ELITE,
	SEIREN,
	SEIREN_MASTER,
	SEIREN_SNAKE,
	SHELLIZARDBABY,
	SHELLIZARDMOM,
	SHULACK,
	SHULACK_ATTACKED,
	SHULACK_ATTACKING,
	SHULACK_DECK,
	SHULACK_DECK_KILLER,
	SHULACK_SLAVE,
	SHULACK_SUPPORT,
	SOULEDSTONE,
	SOULEDSTONE_MINI,
	SPAKY,
	SPALLER,
	SPALLERCTRL,
	SPECTRE_AALIGHTDARK,
	SPRIGGREFUSE_DF1,
	SPRIGG_HROBBERALDER_DF1,
	SUCCUBUS_ELITE,
	SWELLFISH,
	TARGETBASFELT2_DF1,
	TARGETBASFELT_DF1,
	TAURIC,
	TDOWN_DRAKAN,
	TESTBATTLE_NPC,
	TEST_DARK_ADRAGON,
	TEST_DARK_AETC,
	TEST_DARK_ALIGHT,
	TEST_DRAGON_ADARK,
	TEST_DRAGON_AETC,
	TEST_DRAGON_ALIGHT,
	TEST_ETC_ADARK,
	TEST_ETC_ADRAGON,
	TEST_ETC_ALIGHT,
	TEST_LIGHT_ADARK,
	TEST_LIGHT_ADRAGON,
	TEST_LIGHT_AETC,
	TOG,
	TOG_AKALNIF,
	TOG_AZAIF,
	TOWERMAN,
	TRICO,
	TRICON,
	UNDEADGRADIATOR_DF1,
	USEALL(true),
	USEALLNONETOMONSTER,
	WAVE_SWARM1,
	WAVE_SWARM2,
	WAVE_TREE,
	XDRAKAN,
	XDRAKAN_DGUARD,
	XDRAKAN_ELEMENTALIST,
	XDRAKAN_LGUARD,
	XDRAKAN_PET,
	XDRAKAN_PRIEST,
	XDRAKAN_SERVANT,
	XIPETO,
	XIPETOBABY,
	YDUMMY,
	YDUMMY2,
	YDUMMY2_DGUARD,
	YDUMMY2_GUARD,
	YDUMMY2_LGUARD,
	YDUMMY_DGUARD,
	YDUMMY_GUARD,
	YDUMMY_LGUARD,
	YUN_GUARD,
	ZAIF,
	ZAIF_ABROWNIEFELLER_LF1,
	ZAIF_ADARU,
	ZAIF_AMINX,
	ZAIF_ATOG,
	TIAMAT,
	GOD_KAISINEL,
	GOD_MARCHUTAN,
	KAHRUN,
	LDF5_BRAX,
	LDF5_VESPA,
	LDF5_SPAKLE,
	LDF5_LUPYLLINI,
	LDF5_WORKER,
	LDF5_SHULACK_KEEPER,
	IDTIAMAT_ANCIENT,
	IDTIAMAT_XDRAKAN,
	LDF5_DARU,
	IDTIAMAT_SPAWNHEAL,
	IDTIAMAT_AREAHIDE,
	IDEVENT01_TOWER,
	IDEVENT01_POLYMORPHL,
	IDEVENT01_POLYMORPHD,
	IDEVENT01_MC,

	TIAMATREMNANT_DRAKAN,
	TIAMATREMNANT_LIZARD,
	TIAMATREMNANT_LIZARD_INJURY,
	NEUTRAL_GUARD,
	VRITRA,
	IDEVENT01_SUMMON,
	LIGHT_SUR_MOB_DF2ADIRECTPORTAL,
	LIGHT_LICH_DF2ADIRECTPORTAL,
	LDF5_CALYDON,
	LDF5_OWLLAU,
	LDF5_GURURU,
	ELEMENTAL_LIGHT,
	NEUTRAL_GUARD_ON_ATTACK,
	NEUTRAL_GUARD_ON_ATTACK01,
	XDRAKAN_UNATTACK,
	//4.0
	IDF5_SIEGEWEAPON,
	IDF5_SIEGEWEAPON_ATTACK,
	IDF5_R2_SYNC1,
	IDF5_R2_SYNC1_ATTACK,
	IDF5_R2_SYNC2,
	IDF5_R2_SYNC2_ATTACK,
	IDF5_R2_SYNC3,
	IDF5_R2_SYNC3_ATTACK,
	LDF5_FUNGY,
	LDF5_V_CHIEF_L,
	LDF5_V_CHIEF_D,
	LDF5_V_CHIEF_DR,
	LDF5_V_KILLER_L,
	LDF5_V_KILLER_D,
	LDF5_V_KILLER_DR,
	LDF5_BABARIAN,
	IDF5_TD_WEAPON_PC,
	IDF5_TD_DOOR,
	IDF5_TD_ASSULT,
	IDF5_TD_SIEGE,
	TEST_ATTACKTOPC,
	TEST_ATTACKTOPC_DARK,
	TEST_ATTACKTONPC,
	TEST_SUPPORTNPC,
	TEST_DRAKAN,
	LDF_V_CHIEF_L,
	LDF_V_CHIEF_D,
	LDF_V_KILLER_LEHPAR,
	LDF_V_KILLER_KRALL,
	LDF_V_KILLER_LYCAN,
	LDF_V_GUARD_LIGHT,
	LDF_V_GUARD_DARK,
	IDF5_TD_COMMANDER_LI,
	IDF5_TD_COMMANDER_DA,
	IDLDF5_UNDER_RUNE,
	LDF5B_KILLER,
	LDF5B_DOOR_LI,
	LDF5B_DOOR_DA,
	LDF5B_DOOR_DR,
	FIELD_OBJECT_ALL_HOSTILEMONSTER,
	LDF5B_FOBJ_HOSTILEPC,
	IDF5_TD_GUARD_LIGHT,
	IDF5_TD_GUARD_DARK,
	IDRUNEWP_VRITRA,
	IDRUNEWP_ESCORT,
	IDRUNEWP_ANCIENTARM,
	LDF5_DEBRIE,
	LDF5_NEUT,
	IDLDF5RE_SOLO_Q,
	LDF5_SHULACK_DIRECT,
	LDF5_NATIVE_DIRECT,
	SHULACK_SLAVE_NOTAGGRESSIVE,
	LDF5B_OUT_DOOR_KILLER_LI,
	LDF5B_OUT_DOOR_KILLER_DA,
	LDF5_MUTA,
	USEALL_LDF5_TOWER_LI,
	USEALL_LDF5_TOWER_DA,
	MONSTER_FRIENDLY_LDFCHIEF,
	USEALL_TELEPORTER_LI,
	USEALL_TELEPORTER_DA,
	IDASTERIA_IU_MONSTER,
	IDASTERIA_IU_NPC,
	IDASTERIA_IU_POLYMORPHL,
	IDASTERIA_IU_POLYMORPHD,
	IDF5_R2_CANNON,
	IDF5_R2_CANNON_ATTACK,
	IDASTERIA_IU_ATK,
	USEALL_HOSTILEPC,
	IDRUNEWP_RUNEDEVICE,
	IDASTERIA_IU_MONSTER2,
	NONAGRRESSIVEFRIENDLYVRITRA,
	IDVRITRA_BASE_REBIRTH,
	IDRUNEWP_VRITRADEVICE,
	IDRUNEWP_AGGRESSIVEANCIENTARM,
	TIGRAN;

	private Race guardRace;
	private boolean isBasic;

	private TribeClass() {
	}

	private TribeClass(Race guardRace) {
		this.guardRace = guardRace;
	}

	private TribeClass(Race guardRace, boolean isBasic) {
		this.guardRace = guardRace;
		this.isBasic = isBasic;
	}

	private TribeClass(boolean isBasic) {
		this.isBasic = isBasic;
	}

	public boolean isGuard() {
		return guardRace != null;
	}

	public boolean isBasicClass() {
		return isBasic;
	}

	public boolean isLightGuard() {
		return guardRace == Race.ELYOS;
	}

	public boolean isDarkGuard() {
		return guardRace == Race.ASMODIANS;
	}

	public boolean isDrakanGuard() {
		return guardRace == Race.DRAGON;
	}
}
