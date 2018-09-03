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
package com.aionemu.gameserver.dao;

import java.util.Set;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.Announcement;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * DAO that manages Announcements
 * 
 * @author Divinity
 */
public abstract class AnnouncementsDAO implements DAO {

	public abstract Set<Announcement> getAnnouncements();

	public abstract void addAnnouncement(final Announcement announce);

	public abstract boolean delAnnouncement(final int idAnnounce);

	/**
	 * Returns class name that will be uses as unique identifier for all DAO classes
	 * 
	 * @return class name
	 */
	@Override
	public final String getClassName() {
		return AnnouncementsDAO.class.getName();
	}

	/**
	 * @param lastAttacker
	 * @param bossName
	 */
	public void UniqueKill(Player lastAttacker, String bossName) {
		// TODO Auto-generated method stub
		
	}
}
