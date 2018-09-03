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
package br.focus.battleground;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.gameobjects.state.CreatureVisualState;
import com.aionemu.gameserver.model.templates.BattleGroundTemplate;
import com.aionemu.gameserver.model.templates.BattleGroundType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
/**
 * @author Maestross
 */
public abstract class BattleGround
{
	protected List<Player>			players	= new ArrayList<Player>();

	protected int					tplId;

	private long					startTime;

	protected WorldMapInstance		instance;

	public boolean					running	= false;

	protected BattleGroundTemplate	template;

	public BattleGround(int tplId, WorldMapInstance instance)
	{
		startTime = System.currentTimeMillis() / 1000;
		this.tplId = tplId;
		this.instance = instance;
		template = DataManager.BATTLEGROUND_DATA.getBattleGroundTemplate(tplId);
		BattleGroundManager.currentBattleGrounds.add(this);
	}

	public void increasePoints(Player player, int value)
	{
		PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.BATTLEGROUND_MESSAGE1,value));
		player.battlegroundSessionPoints += value;
		if(player.getBattleGround().getTemplate().getType() == BattleGroundType.CTF && value == player.getBattleGround().getTemplate().getRules().getFlagCap())
			player.battlegroundSessionFlags += 1;
		else if(value == player.getBattleGround().getTemplate().getRules().getKillPlayer())
			player.battlegroundSessionKills += 1;
	}

	public void decreasePoints(Player player, int value)
	{
		PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.BATTLEGROUND_MESSAGE2,value));

		player.battlegroundSessionPoints -= value;
		if(player.getBattleGround().getTemplate().getType() == BattleGroundType.ASSAULT && value == player.getBattleGround().getTemplate().getRules().getDie())
			player.battlegroundSessionDeaths += 1;

		if(player.battlegroundSessionPoints < 0)
			player.battlegroundSessionPoints = 0;

	}

	public List<Player> getPlayers()
	{
		return players;
	}

	public void addPlayer(Player player)
	{
		players.add(player);
	}

	public void removePlayer(Player player)
	{
		players.remove(player);
	}

	public long getStartTime()
	{
		return startTime;
	}

	public WorldMapInstance getInstance()
	{
		return instance;
	}

	public void teleportPlayer(Player player)
	{
		BattleGroundTemplate template = DataManager.BATTLEGROUND_DATA.getBattleGroundTemplate(tplId);
		if(player.getCommonData().getRace() == Race.ELYOS)
			TeleportService2.teleportTo(player, template.getWorldId(), template.getInsertPoint().getXe(), template.getInsertPoint().getYe(),
				template.getInsertPoint().getZe(), template.getInsertPoint().getHe());
		else
			TeleportService2.teleportTo(player, template.getWorldId(), template.getInsertPoint().getXa(), template.getInsertPoint().getYa(),
				template.getInsertPoint().getZa(), template.getInsertPoint().getHa());
	}

	public void broadcastToBattleGround(final String message, final Race targetRace)
	{
		for(Player p : players)
		{
			if(targetRace == null || p.getCommonData().getRace() == targetRace)
				PacketSendUtility.sendPacket(p, new SM_MESSAGE(0, null, message, ChatType.WHITE_CENTER));
		}
	}

	public void invitePlayer(final Player player)
	{
		PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null, LanguageHandler.translate(CustomMessageId.BATTLEGROUND_MESSAGE3,template.getName()), ChatType.WHITE_CENTER));
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				teleportPlayer(player);
			}
		}, 30000);
	}

	public void start()
	{

		for(Player p : players)
		{
			p.battlegroundWaiting = false;
			invitePlayer(p);
		}

		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				broadcastToBattleGround(template.getWaitTime() + LanguageHandler.translate(CustomMessageId.BATTLEGROUND_MESSAGE4), null);
				for(Player p : players)
				{
					// reset stats
					p.getLifeStats().setCurrentHpPercent(100);
					p.getLifeStats().setCurrentMpPercent(100);
					p.getCommonData().setDp(0);
					p.getEffectController().removeAllEffects();

					if(p.battlegroundObserve == 1)
					{
						p.battlegroundObserve = 2;
						p.setVisualState(CreatureVisualState.HIDE20);
						PacketSendUtility.broadcastPacket(p, new SM_PLAYER_STATE(p), true);
						PacketSendUtility.sendMessage(p, LanguageHandler.translate(CustomMessageId.BATTLEGROUND_MESSAGE5));
						p.setInvul(true);
						PacketSendUtility.sendMessage(p, LanguageHandler.translate(CustomMessageId.BATTLEGROUND_MESSAGE6));
					}
				}
			}
		}, 31 * 1000);

		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				broadcastToBattleGround(LanguageHandler.translate(CustomMessageId.BATTLEGROUND_MESSAGE7), null);
				running = true;
			}
		}, (template.getWaitTime() + 30) * 1000);

		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				if(running == true)
					broadcastToBattleGround(LanguageHandler.translate(CustomMessageId.BATTLEGROUND_MESSAGE8), null);
			}
		}, template.getBgTime() * 1000);

		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				for(Player p : players)
				{
					if(p.battlegroundObserve > 0)
					{
						p.battlegroundObserve = 3;
						PacketSendUtility.sendMessage(p, LanguageHandler.translate(CustomMessageId.BATTLEGROUND_MESSAGE9));
					}
				}
			}
		}, ((template.getBgTime() / 2) + 30) * 1000);

		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				if(running == true)
					end();
			}
		}, (template.getBgTime() + 30) * 1000);
	}

	public BattleGroundTemplate getTemplate()
	{
		return template;
	}

	public void setTemplate(BattleGroundTemplate template)
	{
		this.template = template;
	}

	public void end()
	{
		running = false;
		broadcastToBattleGround(LanguageHandler.translate(CustomMessageId.BATTLEGROUND_MESSAGE10), null);
		for(Player p : players)
		{
			if(p.battlegroundObserve > 0)
			{
				p.unsetVisualState(CreatureVisualState.HIDE20);
				PacketSendUtility.broadcastPacket(p, new SM_PLAYER_STATE(p), true);
				PacketSendUtility.sendMessage(p, LanguageHandler.translate(CustomMessageId.BATTLEGROUND_MESSAGE11));
				p.setInvul(false);
				PacketSendUtility.sendMessage(p, LanguageHandler.translate(CustomMessageId.BATTLEGROUND_MESSAGE12));
			}
		}
	}

	public List<Player> getRanking(Race race, boolean reward)
	{
		ArrayList<Player> ranking = new ArrayList<Player>();

		for(Player p : players)
		{
			if(p.getCommonData().getRace() != race)
				continue;
			if(p.battlegroundObserve >= 1)
				continue;
			if(ranking.size() == 0)
				ranking.add(p);
			else
			{
				for(int i = 0; i < ranking.size(); i++)
				{
					if(p.battlegroundSessionPoints > ranking.get(i).battlegroundSessionPoints)
					{
						ranking.add(i, p);
						break;
					}
				}
				if(!ranking.contains(p))
					ranking.add(p);
			}
		}
		return ranking;
	}

	public void commitPoints(Player player)
	{
		player.getCommonData().setBattleGroundPoints(player.getCommonData().getBattleGroundPoints() + player.battlegroundSessionPoints);
		player.getEffectController().removeAllEffects();
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				for(final Player p : players)
				{
					p.battlegroundObserve = 0;
					p.battlegroundSessionPoints = 0;
					p.battlegroundSessionKills = 0;
					p.battlegroundSessionDeaths = 0;
					p.battlegroundSessionFlags = 0;
					p.battlegroundBetE = 0;
					p.battlegroundBetA = 0;

					if(p.getWorldId() == 110010000 || p.getWorldId() == 120010000)
					{
						String message = LanguageHandler.translate(CustomMessageId.BATTLEGROUND_MESSAGE13);
						RequestResponseHandler responseHandler = new RequestResponseHandler(p){

							public void acceptRequest(Creature requester, Player responder)
							{
								if(p.getBattleGround() != null)
								{
									PacketSendUtility.sendMessage(p, LanguageHandler.translate(CustomMessageId.BATTLEGROUND_MESSAGE14));
									PacketSendUtility.sendMessage(p, LanguageHandler.translate(CustomMessageId.BATTLEGROUND_MESSAGE15));
									return;
								}
								else if(p.battlegroundWaiting)
								{
									PacketSendUtility.sendMessage(p, LanguageHandler.translate(CustomMessageId.BATTLEGROUND_MESSAGE16));
									PacketSendUtility.sendMessage(p, LanguageHandler.translate(CustomMessageId.BATTLEGROUND_MESSAGE17));
									return;
								}
								else
								{
									BattleGroundManager.sendRegistrationForm(p);
								}
								return;
							}

							public void denyRequest(Creature requester, Player responder)
							{
								return;
							}
						};
						boolean requested = p.getResponseRequester().putRequest(902247, responseHandler);
						if(requested)
						{
							PacketSendUtility.sendPacket(p, new SM_QUESTION_WINDOW(902247, 1, 1, message));
							return;
						}
					}
				}
			}
		}, 5 * 1000);

	}

	public int getTplId()
	{
		return tplId;
	}

	public int getWorldId()
	{
		return DataManager.BATTLEGROUND_DATA.getBattleGroundTemplate(tplId).getWorldId();
	}

}
