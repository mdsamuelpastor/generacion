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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.List;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerAppearance;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.items.GodStone;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.team.legion.LegionEmblemType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * This packet is displaying visible players.
 * 
 * @author -Nemesiss-, Avol, srx47 modified cura, Maestross, GoodT, -Enomine-
 */
public class SM_PLAYER_INFO extends AionServerPacket {

	/**
	 * Visible player
	 */
	private final Player player;
	private boolean enemy;

	/**
	 * Constructs new <tt>SM_PLAYER_INFO </tt> packet
	 * 
	 * @param player
	 *          actual player.
	 * @param enemy
	 */
	public SM_PLAYER_INFO(Player player, boolean enemy) {
		this.player = player;
		this.enemy = enemy;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		Player activePlayer = con.getActivePlayer();
		if (activePlayer == null || player == null) {
			return;
		}
		PlayerCommonData pcd = player.getCommonData();
		final int raceId;
		if (player.getAdminNeutral() > 1 || activePlayer.getAdminNeutral() > 1) {
			raceId = activePlayer.getRace().getRaceId();
		}
		else if (activePlayer.isEnemy(player)) {
			raceId = (activePlayer.getRace().getRaceId() == 0 ? 1 : 0);
		}		
		else
			raceId = player.getRace().getRaceId();

		final int genderId = pcd.getGender().getGenderId();
		final PlayerAppearance playerAppearance = player.getPlayerAppearance();

		writeF(player.getX());// x
		writeF(player.getY());// y
		writeF(player.getZ());// z
		writeD(player.getObjectId());
		/**
		 * A3 female asmodian A2 male asmodian A1 female elyos A0 male elyos
		 */
		writeD(pcd.getTemplateId());
		/**
		 * Transformed state - send transformed model id Regular state - send player model id (from common data)
		 */
		int model = player.getTransformModel().getModelId();
		writeD(model != 0 ? model : pcd.getTemplateId());
		writeC(0x00); // new 2.0 Packet --- probably pet info?
		writeD(player.getTransformModel().getType().getId());
	    writeC(enemy ? 0 : 38);
		writeC(raceId); // race
		writeC(pcd.getPlayerClass().getClassId());
		writeC(genderId); // sex
		writeH(player.getState());

		writeB(new byte[8]);

		writeC(player.getHeading());

		String playerName = "";
		String ffaPlayerName = "";
		//it looks a little bit difficult but on this way the playername will be untouched :)
		//& the fucking vip logo dont override the admin/gm tag :D
		if ((player.getClientConnection() != null) && (AdminConfig.ADMIN_TAG_ENABLE)) {
			if (player.isGM()) {
                playerName = getAdminTag();
            }
		}
		
		if (MembershipConfig.VIPTAG_DISPLAY) {
		    if(player.getClientConnection().getAccount().getMembership() > 0) {
		        //if he's gm = do nothing, else the gmtag wont display
                if (player.isGM()) {
                } //he's not gm so
			    else {
                    if(player.getClientConnection().getAccount().getMembership() == 1)
                    {
                        playerName = ("\uE024" + MembershipConfig.VIP_LEVEL1 + " ");
                    }
                    else if(player.getClientConnection().getAccount().getMembership() == 2)
                    {
                        playerName = ("\uE023" + MembershipConfig.VIP_LEVEL2 + " ");
                    }
			    } 
		    }
		}
		
		//Now after tag: <ADMIN>
        //                      <VIP>
        //add name
        playerName += player.getName();
		
		if (player.isInFFAPVP()) {
		    writeS(ffaPlayerName);
		} else {
		    writeS(playerName);
		}

		writeH(pcd.getTitleId());
		writeH(player.getCommonData().isHaveMentorFlag()? 1:0);
		
		writeH(player.getCastingSkillId());

		if (player.isLegionMember()) {
			writeD(player.getLegion().getLegionId());
			writeC(player.getLegion().getLegionEmblem().getEmblemId());
			writeC(player.getLegion().getLegionEmblem().getEmblemType().getValue());
			writeC(player.getLegion().getLegionEmblem().getEmblemType() == LegionEmblemType.DEFAULT ? 0x00 : 0xFF);
			writeC(player.getLegion().getLegionEmblem().getColor_r());
			writeC(player.getLegion().getLegionEmblem().getColor_g());
			writeC(player.getLegion().getLegionEmblem().getColor_b());
			writeS(player.getLegion().getLegionName());
		}
		else
			writeB(new byte[12]);
		int maxHp = player.getLifeStats().getMaxHp();
		int currHp = player.getLifeStats().getCurrentHp();
		writeC(100 * currHp / maxHp);// %hp
		writeH(pcd.getDp());// current dp
		writeC(0x00);// unk (0x00)

		List<Item> items = player.getEquipment().getEquippedForApparence();
		short mask = 0;
		for (Item item : items) {
			mask |= item.getEquipmentSlot();
		}

		writeH(mask);

		for (Item item : items) {
			if (item.getEquipmentSlot() < Short.MAX_VALUE * 2) {
				writeD(item.getItemSkinTemplate().getTemplateId());
				GodStone godStone = item.getGodStone();
				writeD(godStone != null ? godStone.getItemId() : 0);
				writeD(item.getItemColor());
				writeH(0x00);// unk (0x00)
			}
		}

		writeD(playerAppearance.getSkinRGB());
		writeD(playerAppearance.getHairRGB());
		writeD(playerAppearance.getEyeRGB());
		writeD(playerAppearance.getLipRGB());
		writeC(playerAppearance.getFace());
		writeC(playerAppearance.getHair());
		writeC(playerAppearance.getDeco());
		writeC(playerAppearance.getTattoo());
		writeC(playerAppearance.getFaceContour());
		writeC(playerAppearance.getExpression());
		
		writeC(5);// always 5 o0
		
		writeC(playerAppearance.getJawLine());
		writeC(playerAppearance.getForehead());

		writeC(playerAppearance.getEyeHeight());
		writeC(playerAppearance.getEyeSpace());
		writeC(playerAppearance.getEyeWidth());
		writeC(playerAppearance.getEyeSize());
		writeC(playerAppearance.getEyeShape());
		writeC(playerAppearance.getEyeAngle());

		writeC(playerAppearance.getBrowHeight());
		writeC(playerAppearance.getBrowAngle());
		writeC(playerAppearance.getBrowShape());

		writeC(playerAppearance.getNose());
		writeC(playerAppearance.getNoseBridge());
		writeC(playerAppearance.getNoseWidth());
		writeC(playerAppearance.getNoseTip());

		writeC(playerAppearance.getCheek());
		writeC(playerAppearance.getLipHeight());
		writeC(playerAppearance.getMouthSize());
		writeC(playerAppearance.getLipSize());
		writeC(playerAppearance.getSmile());
		writeC(playerAppearance.getLipShape());
		writeC(playerAppearance.getJawHeigh());
		writeC(playerAppearance.getChinJut());
		writeC(playerAppearance.getEarShape());
		writeC(playerAppearance.getHeadSize());
		// 1.5.x 0x00, shoulderSize, armLength, legLength (BYTE) after HeadSize

		writeC(playerAppearance.getNeck());
		writeC(playerAppearance.getNeckLength());
		writeC(playerAppearance.getShoulderSize());

		writeC(playerAppearance.getTorso());
		writeC(playerAppearance.getChest()); // only woman
		writeC(playerAppearance.getWaist());

		writeC(playerAppearance.getHips());
		writeC(playerAppearance.getArmThickness());
		writeC(playerAppearance.getHandSize());
		writeC(playerAppearance.getLegThicnkess());

		writeC(playerAppearance.getFootSize());
		writeC(playerAppearance.getFacialRate());

		writeC(0x00); // always 0
		writeC(playerAppearance.getArmLength());
		writeC(playerAppearance.getLegLength());
		writeC(playerAppearance.getShoulders());
		writeC(playerAppearance.getFaceShape());
		writeC(0x00); // always 0
		
		writeC(playerAppearance.getVoice());

		writeF(playerAppearance.getHeight());
		writeF(0.25f); // scale
		writeF(2.0f); // gravity or slide surface o_O
		writeF(player.getGameStats().getMovementSpeedFloat()); // move speed

		Stat2 attackSpeed = player.getGameStats().getAttackSpeed();
		writeH(attackSpeed.getBase());
		writeH(attackSpeed.getCurrent());
		writeC(player.getPortAnimation());

		writeS(player.hasStore() ? player.getStore().getStoreMessage() : "");// private store message

		/**
		 * Movement
		 */
		writeF(0);
		writeF(0);
		writeF(0);

		writeF(player.getX());// x
		writeF(player.getY());// y
		writeF(player.getZ());// z
		writeC(0x00); // move type

		if (player.isUsingFlyTeleport()) {
			writeD(player.getFlightTeleportId());
			writeD(player.getFlightDistance());
		}
		else if (player.isInPlayerMode(PlayerMode.WINDSTREAM)) {
			writeD(player.windstreamPath.teleportId);
			writeD(player.windstreamPath.distance);
		}
		writeC(player.getVisualState()); // visualState
		writeS(player.getCommonData().getNote()); // note show in right down windows if your target on player

		writeH(player.getLevel()); // [level]
		writeH(player.getPlayerSettings().getDisplay()); // unk - 0x04
		writeH(player.getPlayerSettings().getDeny()); // unk - 0x00
		writeH(player.getAbyssRank().getRank().getId()); // abyss rank
		writeH(0); // unk
		writeD(player.getTarget() == null ? 0 : player.getTarget().getObjectId());
		writeC(0); // suspect id
		writeD(player.isInAlliance2() ? player.getPlayerAlliance2().getTeamId() : player.isInGroup2() ? player.getPlayerGroup2().getTeamId() : 0);
		writeC(player.isMentor() ? 1 : 0);
		if (player.getHouses().size() != 0)
		  writeD(player.getHouseOwnerId());//HouseAddress
		else
			writeD(0);
		writeD(1); //unk 0x01
	}
	
	public String getAdminTag() {
        String playerName = "";
        //if he's a GM has the tag

        if (player.getAccessLevel() == 1) {
            playerName = ("\uE042" + AdminConfig.ADMIN_TAG_1.trim()
                    + "\uE043");
        } else if (player.getAccessLevel() == 2) {
            playerName = ("\uE042" + AdminConfig.ADMIN_TAG_2.trim()
                    + "\uE043");
        } else if (player.getAccessLevel() == 3) {
            playerName = ("\uE042" + AdminConfig.ADMIN_TAG_3.trim()
                    + "\uE043");
        } else if (player.getAccessLevel() == 4) {
            playerName = ("\uE042" + AdminConfig.ADMIN_TAG_4.trim()
                    + "\uE043");
        } else if (player.getAccessLevel() == 5) {
            playerName = ("\uE042" + AdminConfig.ADMIN_TAG_5.trim()
                    + "\uE043");
        } else if (player.getAccessLevel() == 6) {
            playerName = ("\uE042" + AdminConfig.ADMIN_TAG_6.trim()
                    + "\uE043");
        } else if (player.getAccessLevel() == 7) {
            playerName = ("\uE042" + AdminConfig.ADMIN_TAG_7.trim()
                    + "\uE043");
        } else if (player.getAccessLevel() == 8) {
            playerName = ("\uE042" + AdminConfig.ADMIN_TAG_8.trim()
                    + "\uE043");
        } else if (player.getAccessLevel() == 9) {
            playerName = ("\uE042" + AdminConfig.ADMIN_TAG_9.trim()
                    + "\uE043");
        } else if (player.getAccessLevel() == 10) {
            playerName = ("\uE042" + AdminConfig.ADMIN_TAG_10.trim()
                    + "\uE043");
        }


        return playerName;

    }
}
