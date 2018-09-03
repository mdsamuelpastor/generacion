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
package com.aionemu.gameserver.utils.collections;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import javolution.util.FastList;

public class ListSplitter<T> {

	private T[] objects;
	private Class<?> componentType;
	private int splitCount;
	private int curentIndex = 0;
	private int length = 0;

	@SuppressWarnings("unchecked")
	public ListSplitter(List<T> list, int splitCount) {
		if (list != null && list.size() > 0) {
			this.splitCount = splitCount;
			length = list.size();
			objects = (T[]) list.toArray(new Object[(list instanceof FastList) ? length : 0]);

			componentType = objects.getClass().getComponentType();
		}
	}

	public List<T> getNext(int splitCount) {
		this.splitCount = splitCount;
		return getNext();
	}

	@SuppressWarnings("unchecked")
	public List<T> getNext() {
		Object[] subArray = (Object[]) Array.newInstance(componentType, Math.min(splitCount, length - curentIndex));
		if (subArray.length > 0) {
			System.arraycopy(objects, curentIndex, subArray, 0, subArray.length);
			curentIndex += subArray.length;
		}
		return (List<T>) Arrays.asList(subArray);
	}

	public int size() {
		return length;
	}

	public boolean isFirst() {
		return curentIndex <= splitCount;
	}

	public boolean isLast() {
		return curentIndex == length;
	}
}
