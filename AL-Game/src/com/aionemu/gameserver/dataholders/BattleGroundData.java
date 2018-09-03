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
package com.aionemu.gameserver.dataholders;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.BattleGroundTemplate;
import com.aionemu.gameserver.model.templates.SpawnInfo;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Maestross
 */
@XmlRootElement(name = "battlegrounds")
@XmlAccessorType(XmlAccessType.FIELD)
public class BattleGroundData
{
	@XmlElement(name = "battleground")
	private List<BattleGroundTemplate>				bgList;

	@XmlElement(name = "bg_agent")
	private List<SpawnInfo>							agentLocations;

	private TIntObjectHashMap<BattleGroundTemplate>	bgData	= new TIntObjectHashMap<BattleGroundTemplate>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for(BattleGroundTemplate bg : bgList)
		{
			bgData.put(bg.getTplId(), bg);
		}
	}

	public int size()
	{
		return bgData.size();
	}

	public List<BattleGroundTemplate> getAllTemplates()
	{
		return bgList;
	}

	public BattleGroundTemplate getBattleGroundTemplate(int tplId)
	{
		return bgData.get(tplId);
	}

	public List<BattleGroundTemplate> getBgList()
	{
		return bgList;
	}

	public List<SpawnInfo> getAgentLocations()
	{
		return agentLocations;
	}

	public TIntObjectHashMap<BattleGroundTemplate> getBgData()
	{
		return bgData;
	}

	/**
	 * @param battlegroundsTemplates
	 */
	public void setTemplates(List<BattleGroundTemplate> battlegroundsTemplates)
	{
		for(BattleGroundTemplate bg : battlegroundsTemplates)
		{
			bgData.put(bg.getTplId(), bg);
		}
	}

}
