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
package com.aionemu.gameserver.services.siegeservice;

public class SiegeException extends RuntimeException {

	private static final long serialVersionUID = 8834569185793190327L;

	public SiegeException() {
	}

	public SiegeException(String message) {
		super(message);
	}

	public SiegeException(String message, Throwable cause) {
		super(message, cause);
	}

	public SiegeException(Throwable cause) {
		super(cause);
	}
}
