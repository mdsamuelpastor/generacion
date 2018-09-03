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
package com.aionemu.gameserver.network.aion.clientpackets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LEGION_INFO;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.services.NameRestrictionService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Simple, Maestross
 */
public class CM_LEGION extends AionClientPacket {

	private static final Logger log = LoggerFactory.getLogger(CM_LEGION.class);

	/**
	 * exOpcode and the rest
	 */
	private int exOpcode;
	private short deputyPermission;
	private short centurionPermission;
	private short legionarPermission;
	private short volunteerPermission;
	private int rank;
	private String legionName;
	private String charName;
	private String newNickname;
	private String announcement;
	private String newSelfIntro;

	/**
	 * Constructs new instance of CM_LEGION packet
	 * 
	 * @param opcode
	 */
	public CM_LEGION(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		exOpcode = readC();

		switch (exOpcode) {
			/** Create a legion **/
			case 0:
				readD();
				legionName = readS();
				break;
			/** Invite to legion **/
			case 1:
				readD(); // empty
				charName = readS();
				break;
			/** Leave legion **/
			case 2:
				readD(); // empty
				readH(); // empty not S
				break;
			/** Kick member from legion **/
			case 4:
				readD(); // empty
				charName = readS();
				break;
			/** Appoint a new Brigade General **/
			case 5:
				readD();
				charName = readS();
				break;
			/** Appoint Centurion **/
			case 6:
				rank = readD();
				charName = readS();
				break;
			/** Demote to Legionary **/
			case 7:
				readD();
				charName = readS();
				break;
			/** Refresh legion info **/
			case 8:
				readD();
				readH();
				break;
			/** Edit announcements **/
			case 9:
				readD();
				announcement = readS();
				break;
			/** Change self introduction **/
			case 10:
				readD();
				newSelfIntro = readS();
				break;
			/** Edit permissions **/
			case 13:
				deputyPermission = (short) readH(); // 0x00 - 0x1C or // 0x00 - 0x1E
				centurionPermission = (short) readH(); // 0x00 - 0x1C or // 0x00 - 0x1E
				legionarPermission = (short) readH(); // 0x00 - 0x04 or // 0x00 - 0x18
				volunteerPermission = (short) readH(); // 0x00 - 0x18
				break;
			/** Level legion up **/
			case 14:
				readD(); // empty
				readH(); // empty
				break;
			case 15:
				charName = readS();
				newNickname = readS();
				break;
			case 3:
            case 11:
            case 12:
			default:
				log.info("Unknown Legion exOpcode? 0x" + Integer.toHexString(exOpcode).toUpperCase());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		final Player activePlayer = getConnection().getActivePlayer();
		if (activePlayer.isLegionMember()) {
			final Legion legion = activePlayer.getLegion();

			if (charName != null) {
				LegionService.getInstance().handleCharNameRequest(exOpcode, activePlayer, charName, newNickname, rank);
			}
			else {
				switch (exOpcode) {
					/** Refresh legion info **/
					case 8:
						sendPacket(new SM_LEGION_INFO(legion));
						break;
					/** Edit announcements **/
					case 9:
						LegionService.getInstance().handleLegionRequest(exOpcode, activePlayer, announcement);
						break;
					/** Change self introduction **/
					case 10:
						LegionService.getInstance().handleLegionRequest(exOpcode, activePlayer, newSelfIntro);
						break;
					/** Edit permissions **/
					case 13:
						if (activePlayer.getLegionMember().isBrigadeGeneral())
							LegionService.getInstance().changePermissions(legion, deputyPermission, centurionPermission, legionarPermission, volunteerPermission);
						break;
					/** Misc. **/
					case 11:
                    case 12:
					default:
						LegionService.getInstance().handleLegionRequest(exOpcode, activePlayer);
						break;
				}
			}
		}
		else {
			switch (exOpcode) {
				/** Create a legion **/
				case 0:
					if (NameRestrictionService.isForbiddenWord(legionName)) {
						PacketSendUtility.sendMessage(activePlayer, "You are trying to use a forbidden name. Choose another one!");
					}
					else
						LegionService.getInstance().createLegion(activePlayer, legionName);
					break;
			}
		}
	}
}