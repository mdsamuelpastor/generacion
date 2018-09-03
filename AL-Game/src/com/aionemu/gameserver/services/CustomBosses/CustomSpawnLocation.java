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
package com.aionemu.gameserver.services.CustomBosses;

/**
 * @author Maestross
 */
 
public class CustomSpawnLocation {
    private int worldId;
    private float x;
    private float y;
    private float z;
    public boolean isUsed;

    /**
     * @return the worldId
     */
    public int getWorldId() {
        return worldId;
    }

    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @return the z
     */
    public float getZ() {
        return z;
    }

    public CustomSpawnLocation(int worldId, float x, float y, float z)
    {
        this.isUsed = false;
        this.worldId = worldId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

  

}
