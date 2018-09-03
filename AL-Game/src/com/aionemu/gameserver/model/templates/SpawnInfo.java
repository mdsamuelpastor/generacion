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
package com.aionemu.gameserver.model.templates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Maestross
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpawnInfo")
public class SpawnInfo
{
	@XmlAttribute(name = "x", required = true)
	private float	x;
	@XmlAttribute(name = "y", required = true)
	private float	y;
	@XmlAttribute(name = "z", required = true)
	private float	z;

	@XmlAttribute(name = "h", required = true)
	private byte	h;

	@XmlAttribute(name = "worldId", required = false)
	private int		worldId;

	@XmlAttribute(name = "npcId", required = false)
	private int		npcId;

	public int getNpcId()
	{
		return npcId;
	}

	public int getWorldId()
	{
		return worldId;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

	public float getZ()
	{
		return z;
	}

	public byte getH()
	{
		return h;
	}

}
