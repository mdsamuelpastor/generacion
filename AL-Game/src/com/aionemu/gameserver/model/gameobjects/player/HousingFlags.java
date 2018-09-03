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
package com.aionemu.gameserver.model.gameobjects.player;

public enum HousingFlags {
	IS_OWNER(1),
	HAS_OWNER(1),
	BUY_STUDIO_ALLOWED(2),
	SINGLE_HOUSE(2),
	BIDDING_ALLOWED(4),

	HOUSE_OWNER((IS_OWNER.getId() | BIDDING_ALLOWED.getId()) & (BUY_STUDIO_ALLOWED.getId() ^ 0xFFFFFFFF)),
	SELLING_HOUSE(IS_OWNER.getId() | BUY_STUDIO_ALLOWED.getId()),

	SOLD_HOUSE(BIDDING_ALLOWED.getId() | BUY_STUDIO_ALLOWED.getId());

	private byte id;

	private HousingFlags(int id) {
		this.id = (byte) (id & 0xFF);
	}

	public byte getId() {
		return id;
	}
}
