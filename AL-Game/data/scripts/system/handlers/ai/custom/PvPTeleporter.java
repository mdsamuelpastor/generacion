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
package ai.custom;

import com.aionemu.commons.utils.Rnd;

import com.aionemu.gameserver.ai2.*;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.utils3d.Point3D;
import com.aionemu.gameserver.services.teleport.TeleportService2;

import ai.ActionItemNpcAI2;

/**
 * @author Maestross
 */

@AIName("pvpteleporter")
public class PvPTeleporter extends ActionItemNpcAI2 {
	
	@Override
	protected void handleUseItemFinish(Player player) {
		switch (getNpcId()) {
			case 000000://This must be the npc id of the required teleporter
				switch (player.getRace()) {
					case ELYOS:
				    Point3D loc = randomSpawn();
			      TeleportService2.teleportTo(player, 600030000, (float)loc.x, (float)loc.y, (float)loc.z);
			      break;
					case ASMODIANS:
						Point3D loc2 = randomSpawn2();
				    TeleportService2.teleportTo(player, 600030000, (float)loc2.x, (float)loc2.y, (float)loc2.z);
				    break;
					default:
						break;
				}
		}
	}
	
	static Point3D[] positions = new Point3D[] {//ELYOS
    new Point3D(1523.4626f, 977.7388f, 231.10434f),
    new Point3D(2082.2405f, 1537.9965f, 209.15443f),
    new Point3D(1637.894f, 986.01825f, 228.7361f),
    new Point3D(1729.0146f, 1053.1683f, 227.75f),
    new Point3D(1899.1033f, 1120.3347f, 245.77092f),
    new Point3D(2045.4161f, 1341.26f, 229.25f),
    new Point3D(2021.1526f, 1369.4934f, 212.5f),
    new Point3D(1913.7345f, 1538.6421f, 177.00027f),
    new Point3D(2049.8105f, 1668.1853f, 212.8976f),
    new Point3D(2022.4045f, 1784.9244f, 229.25f),
    new Point3D(1725.7483f, 1710.2244f, 247.9999f),
    new Point3D(1689.9023f, 2060.252f, 231.125f),
    new Point3D(1563.2329f, 1759.5316f, 241.27197f),
    new Point3D(1471.9889f, 1937.8423f, 238.28925f),
    new Point3D(1563.4027f, 1950.0568f, 231.79042f),
  };
	
	static Point3D[] positions2 = new Point3D[] {//ASMODIANS
    new Point3D(1525.1692f, 2112.0498f, 233.69022f),
    new Point3D(1389.1436f, 2042.4314f, 231.375f),
    new Point3D(1316.3099f, 1842.5027f, 237.66077f),
    new Point3D(1526.8441f, 1785.3342f, 249.22559f),
    new Point3D(1691.7623f, 1714.8021f, 237.25f),
    new Point3D(2007.179f, 1821.7975f, 247.82368f),
    new Point3D(2082.2405f, 1537.9965f, 209.15443f),
    new Point3D(1777.4059f, 1318.5302f, 246.13301f),
    new Point3D(1634.679f, 1280.7448f, 245.09831f),
    new Point3D(1415.4315f, 1286.2549f, 237.24365f),
    new Point3D(1291.3625f, 1399.5737f, 239.37805f),
    new Point3D(1213.2638f, 1301.8093f, 245.43356f),
    new Point3D(1313.8843f, 1233.8235f, 245.25912f),
    new Point3D(1199.3785f, 1185.8705f, 245.125f),
    new Point3D(1373.2584f, 989.34576f, 228.30537f),
  };
	
	private Point3D randomSpawn() {
    int pos = Rnd.get(positions.length - 1);
    return positions[pos];
  }
	
	private Point3D randomSpawn2() {
    int pos = Rnd.get(positions2.length - 1);
    return positions2[pos];
  }
}