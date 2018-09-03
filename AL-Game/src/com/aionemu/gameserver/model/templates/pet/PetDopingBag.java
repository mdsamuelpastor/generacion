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
package com.aionemu.gameserver.model.templates.pet;

import java.util.Arrays;

public class PetDopingBag {

	private int[] itemBag = null;
	private boolean isDirty = false;

	public void setFoodItem(int itemId) {
		setItem(itemId, 0);
	}

	public int getFoodItem() {
		if (itemBag == null || itemBag.length < 1)
			return 0;
		return itemBag[0];
	}

	public void setDrinkItem(int itemId) {
		setItem(itemId, 1);
	}

	public int getDrinkItem() {
		if (itemBag == null || itemBag.length < 2)
			return 0;
		return itemBag[1];
	}

	public void setItem(int itemId, int slot) {
		if (itemBag == null) {
			itemBag = new int[slot + 1];
			isDirty = true;
		}
		else if (slot > itemBag.length - 1) {
			itemBag = Arrays.copyOf(itemBag, slot + 1);
			isDirty = true;
		}
		if (itemBag[slot] != itemId) {
			itemBag[slot] = itemId;
			isDirty = true;
		}
	}

	public int[] getScrollsUsed() {
		if ((itemBag == null) || (itemBag.length < 3))
			return new int[0];
		return Arrays.copyOfRange(itemBag, 2, itemBag.length);
	}

	public boolean isDirty() {
		return isDirty;
	}
}
