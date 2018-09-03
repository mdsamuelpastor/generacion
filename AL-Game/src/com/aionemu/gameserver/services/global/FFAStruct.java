/*
 * This file is part of NextGenCore <Ver:3.9>.
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
package com.aionemu.gameserver.services.global;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Maestross
 */
 
public interface FFAStruct {
                  
    int worldId = 300350000;
                  
    void TeleIn(Player player);
                  
    void TeleOut(Player player);
                  
    ScheduledFuture<?> announceTask(int delayInMinutes);
                  
    int getPlayerCount();
                  
    boolean isEnemy(Player effector, Player effected);
                  
    boolean isInFFA(Player pl);
                  
    boolean onDieDefault(Creature attacker, Creature attacked);
                  
    void onLogin(Player p);
                  
    void playerAnnounce(final String message);
                  
    void resetEnd(Player player);
                  
    void resetStart(Player player);
}