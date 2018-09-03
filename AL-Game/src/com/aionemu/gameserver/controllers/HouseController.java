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
package com.aionemu.gameserver.controllers;

import java.util.Iterator;

import javolution.util.FastMap;

import com.aionemu.gameserver.configs.main.HousingConfig;
import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.ObserverType;
import com.aionemu.gameserver.model.gameobjects.HouseObject;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DELETE_HOUSE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_RENDER;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_UPDATE;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

public class HouseController extends VisibleObjectController<House> {

	FastMap<Integer, ActionObserver> observed = new FastMap<Integer, ActionObserver>().shared();

	@Override
	public void see(VisibleObject object) {
		Player p = (Player) object;
		ActionObserver observer = new ActionObserver(ObserverType.MOVE);
		p.getObserveController().addObserver(observer);
		observed.put(p.getObjectId(), observer);
		AionServerPacket packet;
		if (getOwner().isInInstance())
			packet = new SM_HOUSE_UPDATE(getOwner().getAddress(), getOwner().getBuilding().getId(), getOwner().getOwnerId());
		else
			packet = new SM_HOUSE_RENDER(getOwner().getAddress(), getOwner().getBuilding().getId(), getOwner().getOwnerId());
		
		if(MathUtil.isIn3dRange(object, getOwner(), (int)HousingConfig.VISIBILITY_DISTANCE))
			PacketSendUtility.sendPacket(p, packet);
		if (getOwner().getRegistry() != null)
			for (HouseObject<?> obj : getOwner().getRegistry().getSpawnedObjects()) {
				obj.spawn();
			}
	}

	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange) {
		Player p = (Player) object;
		ActionObserver observer = observed.remove(p.getObjectId());
		if (isOutOfRange) {
			observer.moved();
			if (!getOwner().isInInstance())
				PacketSendUtility.sendPacket(p, new SM_DELETE_HOUSE(getOwner().getAddress().getId()));
		}
		p.getObserveController().removeObserver(observer);
	}

	public void updateAppearance() {
		ThreadPoolManager.getInstance().execute(new Runnable() {

			@Override
			public void run() {
				for (Iterator<Integer> i = observed.keySet().iterator(); i.hasNext();) {
					int playerId = i.next();
					Player player = World.getInstance().findPlayer(playerId);
					if (player != null) {
						PacketSendUtility.sendPacket(player, new SM_HOUSE_UPDATE(getOwner().getAddress(), getOwner().getBuilding().getId(), getOwner().getOwnerId()));
					}
				}
			}
		});
	}

	public void broadcastAppearance() {
		ThreadPoolManager.getInstance().execute(new Runnable() {

			@Override
			public void run() {
				for (Iterator<Integer> i = observed.keySet().iterator(); i.hasNext();) {
					int playerId = i.next();
					Player player = World.getInstance().findPlayer(playerId);
					if (player != null) {
						PacketSendUtility.sendPacket(player, new SM_HOUSE_RENDER(getOwner().getAddress(), getOwner().getBuilding().getId(), getOwner().getOwnerId()));
					}
				}
			}
		});
	}
}
