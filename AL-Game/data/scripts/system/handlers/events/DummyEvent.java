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
package events;

import com.aionemu.gameserver.eventEngine.handlers.EventName;
import com.aionemu.gameserver.eventEngine.handlers.GeneralEventHandler;
import com.aionemu.gameserver.services.EventEngineService;


/**
 * @author Maestross
 *
 */
@EventName("Dummy")
public class DummyEvent extends GeneralEventHandler{

	@Override
	public void onEventCreate(EventEngineService eventEngine) {
    //DUMMY
  }
}
