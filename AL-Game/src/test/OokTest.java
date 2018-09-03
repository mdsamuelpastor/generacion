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
package test;

import java.io.File;

import ng.engine.brainfuck.impl.OokEngine;

/**
 * @author Maestross
 * Simple test file for our new security system, its use the
 * ook coding language. the interpreter is written by me
 */

public class OokTest {

	public static void main(String[] args) throws Exception {
		new OokEngine(30000).interpret(new File(
				"sec/test.ook"));
	}

}