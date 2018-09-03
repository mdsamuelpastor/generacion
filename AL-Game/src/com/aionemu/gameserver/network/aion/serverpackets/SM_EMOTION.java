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


import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * Emotion packet
 * 
 * @author SoulKeeper
 */
public class SM_EMOTION extends AionServerPacket {

	/**
	 * Object id of emotion sender
	 */
	private int senderObjectId;

	/**
	 * Some unknown variable
	 */
	private EmotionType emotionType;

	/**
	 * ID of emotion
	 */
	private int emotion;

	/**
	 * Object id of emotion target
	 */
	private int targetObjectId;

	/**
	 * Temporary Speed..
	 */
	private float speed;
	private int state;
	private int baseAttackSpeed;
	private int currentAttackSpeed;

	/**
	 * Coordinates of player
	 */
	private float x;
	private float y;
	private float z;
	private byte heading;

	/**
	 * This constructor should be used when emotion and targetid is 0
	 * 
	 * @param creature
	 * @param emotionType
	 */
	public SM_EMOTION(Creature creature, EmotionType emotionType) {
		this(creature, emotionType, 0, 0);
	}

	/**
	 * Constructs new server packet with specified opcode
	 * 
	 * @param senderObjectId
	 *          who sended emotion
	 * @param unknown
	 *          Dunno what it is, can be 0x10 or 0x11
	 * @param emotionId
	 *          emotion to play
	 * @param emotionId
	 *          who target emotion
	 */
	public SM_EMOTION(Creature creature, EmotionType emotionType, int emotion, int targetObjectId) {
		this.senderObjectId = creature.getObjectId();
		this.emotionType = emotionType;
		this.emotion = emotion;
		this.targetObjectId = targetObjectId;
		this.state = creature.getState();
		Stat2 aSpeed = creature.getGameStats().getAttackSpeed();
		this.baseAttackSpeed = aSpeed.getBase();
		this.currentAttackSpeed = aSpeed.getCurrent();
		this.speed = creature.getGameStats().getMovementSpeedFloat();
	}

	/**
	 * @param Obj
	 * @param doorId
	 */
  public SM_EMOTION(int Objid, EmotionType emotionType, int state) {
		this.senderObjectId = Objid;
		this.emotionType = emotionType;
    this.state = state;
	}

	/**
	 * New
	 */
	public SM_EMOTION(Player player, EmotionType emotionType, int emotion, float x, float y, float z, byte heading, int targetObjectId) {
		this.senderObjectId = player.getObjectId();
		this.emotionType = emotionType;
		this.emotion = emotion;
		this.x = x;
		this.y = y;
		this.z = z;
		this.heading = heading;
		this.targetObjectId = targetObjectId;

		this.state = player.getState();
		this.speed = player.getGameStats().getMovementSpeedFloat();
		Stat2 aSpeed = player.getGameStats().getAttackSpeed();
		this.baseAttackSpeed = aSpeed.getBase();
		this.currentAttackSpeed = aSpeed.getCurrent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(senderObjectId);
		writeC(emotionType.getTypeId());
		writeH(state);
    writeF(speed);
		switch (emotionType) {
			case SELECT_TARGET:
			case JUMP:
			case SIT:
			case STAND:
			case LAND_FLYTELEPORT:
			case WINDSTREAM_START_BOOST:
			case WINDSTREAM_END_BOOST:
			case FLY:
			case LAND:
			case RESURRECT:
			case ATTACKMODE:
			case NEUTRALMODE:
			case WALK:
			case RUN:
			case OPEN_PRIVATESHOP:
			case CLOSE_PRIVATESHOP:
			case POWERSHARD_ON:
			case POWERSHARD_OFF:
			case ATTACKMODE2:
			case NEUTRALMODE2:
			case START_FEEDING:
			case END_FEEDING:
			case END_SPRINT:
			case WINDSTREAM_END:
			case WINDSTREAM_EXIT:
			case END_DUEL:
				break;
			case DIE:
			case START_LOOT:
			case END_LOOT:
			case END_QUESTLOOT:
			case OPEN_DOOR:
				// looting end (quest)
				writeD(targetObjectId);
				break;
			case CHAIR_SIT:
			case CHAIR_UP:
				// stand (chair)
				writeF(x);
				writeF(y);
				writeF(z);
				writeC(heading);
				break;
			case START_FLYTELEPORT:
				// fly teleport (start)
				writeD(emotion); // teleport Id
				break;
			case WINDSTREAM:
				// entering windstream
				writeD(emotion); // teleport Id
				writeD(targetObjectId); // distance
				break;
	    case RIDE:
	    case RIDE_END:
	      if (targetObjectId != 0) {
	        writeD(targetObjectId);
	      }
	      writeH(0);
	      writeC(0);
	      writeD(0x3F);//63
	      writeD(0x3F);//63
	      writeC(0x40);//64
	      break;
			case START_SPRINT://???
	      writeD(0);
	      break;
			case EMOTE:
				// emote
				writeD(targetObjectId);
				writeH(emotion);
				writeC(1);
				break;
			case START_EMOTE2:
				// emote startloop
				writeH(baseAttackSpeed);
				writeH(currentAttackSpeed);
				break;
			default:
				if (targetObjectId != 0)
					writeD(targetObjectId);
		}
	}
}
