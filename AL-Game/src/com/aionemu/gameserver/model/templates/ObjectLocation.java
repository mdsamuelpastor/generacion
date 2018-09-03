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
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObjectLocation")
public class ObjectLocation
{
	@XmlAttribute(name = "xe", required = true)
	private float	xe;
	@XmlAttribute(name = "ye", required = true)
	private float	ye;
	@XmlAttribute(name = "ze", required = true)
	private float	ze;

	@XmlAttribute(name = "he", required = true)
	private byte	he;

	@XmlAttribute(name = "xa", required = true)
	private float	xa;
	@XmlAttribute(name = "ya", required = true)
	private float	ya;
	@XmlAttribute(name = "za", required = true)
	private float	za;

	@XmlAttribute(name = "ha", required = true)
	private byte	ha;

	public float getXe()
	{
		return xe;
	}

	public float getYe()
	{
		return ye;
	}

	public float getZe()
	{
		return ze;
	}

	public byte getHe()
	{
		return he;
	}

	public float getXa()
	{
		return xa;
	}

	public float getYa()
	{
		return ya;
	}

	public float getZa()
	{
		return za;
	}

	public byte getHa()
	{
		return ha;
	}
}
