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

import gnu.trove.map.hash.TIntObjectHashMap;

import com.aionemu.gameserver.model.templates.randomspawns.RandomSpawnTemplate;


/**
 *
 * @author pixfid
 */
@XmlRootElement(name = "random_spawns")
@XmlAccessorType(XmlAccessType.FIELD)
public class RandomSpawnData {

    @XmlElement(name = "random_spawn")
    protected List<RandomSpawnTemplate> list;
    private TIntObjectHashMap<RandomSpawnTemplate> randomspawnData;
    private static int[] npcid_key;


    void afterUnmarshal(Unmarshaller u, Object parent) {
        randomspawnData = new TIntObjectHashMap<RandomSpawnTemplate>();
        for (RandomSpawnTemplate it : list) {
            randomspawnData.put(it.getNpcid(), it);

        }
        setNpcid_key(randomspawnData.keys());
        list = null;
    }

    public int size() {
        return randomspawnData.size();
    }

    public RandomSpawnTemplate getRandomSpawnTemplate(int npcId) {
        return randomspawnData.get(npcId);
    }

    /**
     * @return the npcid_key
     */
    public static int[] getNpcid_key() {
        return npcid_key;
    }

    /**
     * @param npcid_key the npcid_key to set
     */
    public void setNpcid_key(int[] npcid_key) {
        RandomSpawnData.npcid_key = npcid_key;
    }
}
