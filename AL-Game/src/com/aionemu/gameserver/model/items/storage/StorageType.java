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
package com.aionemu.gameserver.model.items.storage;

/**
 * @author kosyachok, IlBuono, Maestross
 */
public enum StorageType {
	CUBE(0, 129, 9),
	REGULAR_WAREHOUSE(1, 96, 8),
	ACCOUNT_WAREHOUSE(2, 16, 8),
	LEGION_WAREHOUSE(3, 80, 8),
	PET_BAG_6(32, 6, 6),
	PET_BAG_12(33, 12, 6),
	PET_BAG_18(34, 18, 6),
	PET_BAG_24(35, 24, 6),
	PET_BAG_30(36, 30, 6),
	HOUSE_STORAGE_01(60, 9, 9),
	HOUSE_STORAGE_02(61, 9, 9),
	HOUSE_STORAGE_03(62, 9, 9),
	HOUSE_STORAGE_04(63, 9, 9),
	HOUSE_STORAGE_05(64, 9, 9),
	HOUSE_STORAGE_06(65, 9, 9),
	HOUSE_STORAGE_07(66, 9, 9),
	HOUSE_STORAGE_08(67, 9, 9),
	HOUSE_STORAGE_09(68, 18, 9),
	HOUSE_STORAGE_10(69, 18, 9),
	HOUSE_STORAGE_11(70, 18, 9),
	HOUSE_STORAGE_12(71, 18, 9),
	HOUSE_STORAGE_14(73, 18, 9),
	HOUSE_STORAGE_16(75, 27, 9),
	HOUSE_STORAGE_18(77, 27, 9),
	BROKER(126),
	MAILBOX(127);

	private int id;
	private int limit;
	private int length;

	private StorageType(int id) {
		this.id = id;
	}

	private StorageType(int id, int limit, int length) {
		this.id = id;
		this.limit = limit;
		this.length = length;
	}

	public int getId() {
		return id;
	}

	public int getLimit() {
		return limit;
	}

	public int getLength() {
		return length;
	}

	public static StorageType getStorageTypeById(int id) {
		for (StorageType st : values()) {
			if (st.id == id)
				return st;
		}
		return null;
	}

	public static int getStorageId(int limit, int length) {
		for (StorageType st : values()) {
			if (st.limit == limit && st.length == length)
				return st.id;
		}
		return -1;
	}
}
