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
package com.aionemu.gameserver.model.templates.randomspawns;

import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author pixfid
 * @rework Eloann
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RandomSpawn")
public class RandomSpawnTemplate {

    /**
     * XML attributes
     * Order should be reversed to XML attributes order
     */
    private List<Point> point;
    @XmlAttribute(name = "npc_id")
    private int npcid;
    @XmlAttribute(name = "respawn_time")
    private int interval;
    @XmlAttribute(name = "random")
    private int random;
    @XmlElement(name = "point")
    protected List<RandomSpawnTemplate> partList;

    public List<Point> getPoint() {
        if (point == null) {
            point = new ArrayList<Point>();
        }
        return this.point;
    }

    public int getNpcid() {
        return npcid;
    }

    public int getInterval() {
        return interval;
    }

    public int getRandom() {
        return random;
    }
}
