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
package com.aionemu.gameserver.network.aion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.aionemu.gameserver.network.aion.serverpackets.*;

/**
 * This class is holding opcodes for all server packets. It's used only to have all opcodes in one place
 * 
 * @author Luno, alexa026, ATracer, avol, orz, cura, Maestross
 */
public class ServerPacketsOpcodes {

	private static Map<Class<? extends AionServerPacket>, Integer> opcodes = new HashMap<Class<? extends AionServerPacket>, Integer>();

	static int getOpcode(Class<? extends AionServerPacket> packetClass) {
		Integer opcode = opcodes.get(packetClass);
		if (opcode == null) {
			throw new IllegalArgumentException("There is no opcode for " + packetClass + " defined.");
		}
		return opcode;
	}

	private static void addPacketOpcode(Class<? extends AionServerPacket> packetClass, int opcode, Set<Integer> idSet) {
		if (opcode < 0) {
			return;
		}
		if (idSet.contains(opcode)) {
			throw new IllegalArgumentException(String.format("There already exists another packet with id 0x%02X", opcode));
		}
		idSet.add(opcode);
		opcodes.put(packetClass, opcode);
	}

	static {
		Set<Integer> idSet = new HashSet<Integer>();

		addPacketOpcode(SM_VERSION_CHECK.class, 0x00, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_STATS_INFO.class, 0x01, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_STATUPDATE_HP.class, 0x03, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_STATUPDATE_MP.class, 0x04, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ATTACK_STATUS.class, 0x05, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_STATUPDATE_DP.class, 0x06, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_DP_INFO.class, 0x07, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_STATUPDATE_EXP.class, 0x08, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_NPC_ASSEMBLER.class, 0x0A, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ENTER_WORLD_CHECK.class, 0x0D, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_NPC_INFO.class, 0x0E, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_PLAYER_SPAWN.class, 0x0F, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_FORTRESS_INFO.class, 0xF3, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_GATHERABLE_INFO.class, 0x11, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_TELEPORT_LOC.class, 0x14, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_PLAYER_MOVE.class, 0x15, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_DELETE.class, 0x16, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LOGIN_QUEUE.class, 0x17, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_MESSAGE.class, 0x18, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SYSTEM_MESSAGE.class, 0x19, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_INVENTORY_INFO.class, 0x1A, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_INVENTORY_ADD_ITEM.class, 0x1B, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_DELETE_ITEM.class, 0x1C, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_INVENTORY_UPDATE_ITEM.class, 0x1D, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_UI_SETTINGS.class, 0x1E, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_PLAYER_STANCE.class, 0x1F, idSet);//Testing 3.5.0.6
		addPacketOpcode(SM_PLAYER_INFO.class, 0x20, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_CASTSPELL.class, 0x21, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_GATHER_STATUS.class, 0x22, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_GATHER_UPDATE.class, 0x23, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_UPDATE_PLAYER_APPEARANCE.class, 0x24, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_EMOTION.class, 0x25, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_GAME_TIME.class, 0x26, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_TIME_CHECK.class, 0x27, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LOOKATOBJECT.class, 0x28, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_TARGET_SELECTED.class, 0x29, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SKILL_CANCEL.class, 0x2A, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_CASTSPELL_RESULT.class, 0x2B, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SKILL_LIST.class, 0x2C, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SKILL_REMOVE.class, 0x2D, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SKILL_ACTIVATION.class, 0x2E, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ABNORMAL_STATE.class, 0x31, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ABNORMAL_EFFECT.class, 0x32, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SKILL_COOLDOWN.class, 0x33, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_QUESTION_WINDOW.class, 0x34, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ATTACK.class, 0x36, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_MOVE.class, 0x37, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_HEADING_UPDATE.class, 0x39, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_TRANSFORM.class, 0x3A, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_DIALOG_WINDOW.class, 0x3C, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SELL_ITEM.class, 0x3E, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_VIEW_PLAYER_DETAILS.class, 0x41, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_WEATHER.class, 0x43, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_PLAYER_STATE.class, 0x44, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LEVEL_UPDATE.class, 0x46, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_QUEST_LIST.class, 0x47, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_KEY.class, 0x48, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SUMMON_PANEL_REMOVE.class, 0x49, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_EXCHANGE_REQUEST.class, 0x4A, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_EXCHANGE_ADD_ITEM.class, 0x4B, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_EXCHANGE_ADD_KINAH.class, 0x4D, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_EXCHANGE_CONFIRMATION.class, 0x4E, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_EMOTION_LIST.class, 0x4F, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_TARGET_UPDATE.class, 0x51, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_INFLUENCE_RATIO.class, 0x55, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_PLASTIC_SURGERY.class, 0x53, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_FORTRESS_STATUS.class, 0x56, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_CAPTCHA.class, 0x57, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_RENAME.class, 0x58, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SHOW_NPC_ON_MAP.class, 0x59, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO.class, 0x60, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_QUIT_RESPONSE.class, 0x62, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_CHAT_WINDOW.class, 0x63, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_PET.class, 0x65, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ITEM_COOLDOWN.class, 0x67, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_UPDATE_NOTE.class, 0x68, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_PLAY_MOVIE.class, 0x69, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_INSTANCE_SCORE.class, 0x79, idSet);//Checked 3.5.0.6 //New Arena Instances must be added
		addPacketOpcode(SM_QUEST_COMPLETED_LIST.class, 0x7B, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_QUEST_ACTION.class, 0x7C, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_NEARBY_QUESTS.class, 0x7F, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_PING_RESPONSE.class, 0x80, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_CUBE_UPDATE.class, 0x82, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_FRIEND_LIST.class, 0x84, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_PRIVATE_STORE.class, 0x86, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ABYSS_RANK_UPDATE.class, 0x88, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_MAY_LOGIN_INTO_GAME.class, 0x89, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ABYSS_RANKING_PLAYERS.class, 0x8A, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ABYSS_RANKING_LEGIONS.class, 0x8B, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_INSTANCE_INFO.class, 0x8D, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_PONG.class, 0x8E, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_KISK_UPDATE.class, 0x90, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_PRIVATE_STORE_NAME.class, 0x91, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_BROKER_SERVICE.class, 0x92, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_MOTION.class, 0x94, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_TRADE_IN_LIST.class, 0x97, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SUMMON_OWNER_REMOVE.class, 0x9A, idSet);//Checked
		addPacketOpcode(SM_SUMMON_PANEL.class, 0x99, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SUMMON_UPDATE.class, 0x9B, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_TRANSFORM_IN_SUMMON.class, 0x9C, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_TOLL_INFO.class, 0x9F, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_MAIL_SERVICE.class, 0xA1, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SUMMON_USESKILL.class, 0xA2, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_WINDSTREAM.class, 0xA3, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_WINDSTREAM_ANNOUNCE.class, 0xA4, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_REPURCHASE.class, 0xA7, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_WAREHOUSE_INFO.class, 0xA8, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_WAREHOUSE_ADD_ITEM.class, 0xA9, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_DELETE_WAREHOUSE_ITEM.class, 0xAA, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_WAREHOUSE_UPDATE_ITEM.class, 0xAB, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_IN_GAME_SHOP_CATEGORY_LIST.class, 0xAC, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_IN_GAME_SHOP_LIST.class, 0xAD, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_IN_GAME_SHOP_ITEM.class, 0xAE, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_TITLE_INFO.class, 0xB0, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_CHARACTER_SELECT.class, 0xB1, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_CRAFT_ANIMATION.class, 0xB4, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_CRAFT_UPDATE.class, 0xB5, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ASCENSION_MORPH.class, 0xB6, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ITEM_USAGE_ANIMATION.class, 0xB7, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_CUSTOM_SETTINGS.class, 0xB8, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_DUEL.class, 0xB9, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_PET_EMOTE.class, 0xBB, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_QUESTIONNAIRE.class, 0xBF, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_DIE.class, 0xC1, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_RESURRECT.class, 0xC2, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_FORCED_MOVE.class, 0xC3, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_TELEPORT_MAP.class, 0xC4, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_USE_OBJECT.class, 0xC5, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_L2AUTH_LOGIN_CHECK.class, 0xC7, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_CHARACTER_LIST.class, 0xC8, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_CREATE_CHARACTER.class, 0xC9, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_DELETE_CHARACTER.class, 0xCA, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_RESTORE_CHARACTER.class, 0xCB, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_TARGET_IMMOBILIZE.class, 0xCC, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LOOT_STATUS.class, 0xCD, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LOOT_ITEMLIST.class, 0xCE, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_RECIPE_LIST.class, 0xCF, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_MANTRA_EFFECT.class, 0xD0, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SIEGE_LOCATION_INFO.class, 0xD1, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SIEGE_LOCATION_STATE.class, 0xD2, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_PLAYER_SEARCH.class, 0xD3, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO2.class, 0xD9, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SHIELD_EFFECT.class, 0xDA, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO3.class, 0xDC, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_FRIEND_RESPONSE.class, 0xDE, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_BLOCK_RESPONSE.class, 0xDF, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_BLOCK_LIST.class, 0xE0, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_FRIEND_NOTIFY.class, 0xE1, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ONLINE_STATUS.class, 0xE3, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_CHANNEL_INFO.class, 0xE5, idSet);//Checked 3.5.0.6 //Maybe Packet Structure wrong
		addPacketOpcode(SM_CHAT_INIT.class, 0xE6, idSet);//Checked 3.5.0.6 //Maybe Packet Structure wrong
		addPacketOpcode(SM_MACRO_LIST.class, 0xE7, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_MACRO_RESULT.class, 0xE8, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_NICKNAME_CHECK_RESPONSE.class, 0xE9, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SET_BIND_POINT.class, 0xEB, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_RIFT_ANNOUNCE.class, 0xEC, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ABYSS_RANK.class, 0xED, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_PETITION.class, 0xEF, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_FRIEND_UPDATE.class, 0xF0, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LEARN_RECIPE.class, 0xF1, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_RECIPE_DELETE.class, 0xF2, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_FLY_TIME.class, 0xF4, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SHOW_BRAND.class, 0xF9, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ALLIANCE_READY_CHECK.class, 0xFA, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_PRICES.class, 0x1FC, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_TRADELIST.class, 0xFD, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_RECONNECT_KEY.class, 0xFF, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_INSTANCE_STAGE_INFO.class, 0x8C, idSet);//Testing 3.5.0.6
		addPacketOpcode(SM_RECEIVE_BIDS.class, 0x103, idSet);//Checked 3.5.0.6	
		addPacketOpcode(SM_OBJECT_USE_UPDATE.class, 0x108, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_PACKAGE_INFO_NOTIFY.class, 0x10A, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_INSTANCE_COUNT_INFO.class, 0x93, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_DRAWING_TOOL.class, 0xA0, idSet);//Testing 3.5.0.6
		//Fast Track Server
		addPacketOpcode(SM_FAST_TRACK.class, 0x96, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_SERVER_IDS.class, 0x114, idSet);//Checked 3.5.0.6
		//Alli
		addPacketOpcode(SM_ALLIANCE_INFO.class, 0xF5, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_ALLIANCE_MEMBER_INFO.class, 0xF6, idSet);//Checked 3.5.0.6
		//Group
		addPacketOpcode(SM_GROUP_INFO.class, 0x5A, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_GROUP_MEMBER_INFO.class, 0x5B, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_AUTO_GROUP.class, 0x7A, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_GROUP_LOOT.class, 0x87, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_FIND_GROUP.class, 0xA6, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LEAVE_GROUP_MEMBER.class, 0xF7, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_GROUP_DATA_EXCHANGE.class, 0xB2, idSet);//Checked 3.5.0.6
		//Legion
		addPacketOpcode(SM_LEGION_UPDATE_NICKNAME.class, 0x0B, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LEGION_TABS.class, 0x0C, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LEGION_INFO.class, 0x6E, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LEGION_ADD_MEMBER.class, 0x6F, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LEGION_LEAVE_MEMBER.class, 0x70, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LEGION_UPDATE_MEMBER.class, 0x71, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LEGION_UPDATE_TITLE.class, 0x72, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LEGION_UPDATE_SELF_INTRO.class, 0x77, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LEGION_MEMBERLIST.class, 0x9D, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LEGION_EDIT.class, 0x9E, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LEGION_SEND_EMBLEM.class, 0xD5, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LEGION_SEND_EMBLEM_DATA.class, 0xD6, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_LEGION_UPDATE_EMBLEM.class, 0xD7, idSet);//Checked 3.5.0.6
		//Housing 
		addPacketOpcode(SM_HOUSE_EDIT.class, 0x52, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_HOUSE_OWNER_INFO.class, 0x107, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_HOUSE_UPDATE.class, 0x3D, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_HOUSE_OBJECT.class, 0x10C, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_HOUSE_OBJECTS.class, 0x10E, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_HOUSE_BIDS.class, 0x100, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_DELETE_HOUSE_OBJECT.class, 0x10D, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_HOUSE_RENDER.class, 0x10F, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_HOUSE_ACQUIRE.class, 0x113, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_DELETE_HOUSE.class, 0x110, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_HOUSE_REGISTRY.class, 0x74, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_HOUSE_SCRIPTS.class, 0x83, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_VILLAGE_LEVEL.class, 0xE2, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_VILLAGE_TASK.class, 0x118, idSet);//Checked 3.5.0.6
		addPacketOpcode(SM_RETURN_PLAYER.class, 0xDD, idSet);//Checked 3.5.0.6
		//UnknownPackets:
		//addPacketOpcode(SM_UNK.class, 0x104, idSet);
		//addPacketOpcode(SM_UNK2.class, 0x1A5, idSet); Always send after SM_Prices, 2 Times, first has C0x01 second has C 0x00
		//Default Custom Packet
		addPacketOpcode(SM_CUSTOM_PACKET.class, 99999, idSet);//Default Custom Packet
	}
}