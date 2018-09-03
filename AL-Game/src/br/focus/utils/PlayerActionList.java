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
package br.focus.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Maestross
 */
public class PlayerActionList
{
	private Map<Creature, Integer>	hated	= Collections.synchronizedMap(new HashMap<Creature, Integer>());
	private Map<Player, Integer>	healers	= Collections.synchronizedMap(new HashMap<Player, Integer>());

	private boolean					lock	= false;

	public PlayerActionList()
	{

	}

	public void addDamage(Creature origin, int value)
	{
		if(lock)
			return;
		if(!hated.containsKey(origin))
			hated.put(origin, 0);
		hated.put(origin, hated.get(origin) + value);
	}

	public void addHeal(Player healer, int value)
	{
		if(lock)
			return;
		if(!healers.containsKey(healer))
			healers.put(healer, 0);
		healers.put(healer, healers.get(healer) + value);
	}

	public ArrayList<Player> getFinalHealList()
	{
		lock = true;
		ArrayList<Player> sortedPlayers = new ArrayList<Player>();
		synchronized(hated)
		{
			for(Entry<Player, Integer> entry : healers.entrySet())
			{
				int retainedIndex = 0;
				for(Player c : sortedPlayers)
				{
					int pointedValue = healers.get(c);
					int cValue = healers.get(entry.getKey());
					if(cValue > pointedValue)
					{
						break;
					}
					retainedIndex++;
				}
				sortedPlayers.add(retainedIndex, entry.getKey());
			}
		}
		lock = false;
		return sortedPlayers;
	}

	public ArrayList<Creature> getFinalHatedList()
	{
		lock = true;
		ArrayList<Creature> sortedCreatures = new ArrayList<Creature>();
		synchronized(hated)
		{
			for(Entry<Creature, Integer> entry : hated.entrySet())
			{
				int retainedIndex = 0;
				for(Creature c : sortedCreatures)
				{
					int pointedValue = hated.get(c);
					int cValue = hated.get(entry.getKey());
					if(cValue > pointedValue)
					{
						break;
					}
					retainedIndex++;
				}
				sortedCreatures.add(retainedIndex, entry.getKey());
			}
		}
		lock = false;
		return sortedCreatures;
	}

	public void clearAll()
	{
		clearHated();
		clearHealers();
	}

	public void clearHated()
	{
		hated.clear();
	}

	public void clearHealers()
	{
		healers.clear();
	}

}
