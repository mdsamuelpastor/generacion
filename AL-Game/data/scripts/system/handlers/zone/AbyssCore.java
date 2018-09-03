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
package zone;

import java.io.IOException;

import javolution.util.FastMap;

import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.ObserverType;
import com.aionemu.gameserver.geoEngine.GeoWorldLoader;
import com.aionemu.gameserver.geoEngine.collision.CollisionResults;
import com.aionemu.gameserver.geoEngine.math.Matrix3f;
import com.aionemu.gameserver.geoEngine.math.Ray;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.geoEngine.scene.Node;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.handler.ZoneHandler;
import com.aionemu.gameserver.world.zone.handler.ZoneNameAnnotation;

/**
 * @author MrPoke
 */
@ZoneNameAnnotation("CORE_400010000")
public class AbyssCore implements ZoneHandler {

	FastMap<Integer, Observer> observed = new FastMap<Integer, Observer>();

	private Node geometry;

	public AbyssCore() {
		try {
			this.geometry = (Node) GeoWorldLoader.loadMeshs("data/geo/models/na_ab_lmark_col_01a.mesh").values().toArray()[0];
			this.geometry.setTransform(new Matrix3f(1.15f, 0, 0, 0, 1.15f, 0, 0, 0, 1.15f), new Vector3f(2140.104f, 1925.5823f, 2303.919f), 1f);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		geometry.updateModelBound();
	}

	@Override
	public void onEnterZone(Creature creature, ZoneInstance zone) {
		Creature acting = creature.getActingCreature();
		if (acting instanceof Player && !((Player) acting).isGM()) {

			Observer observer = new Observer(creature, geometry);
			creature.getObserveController().addObserver(observer);
			observed.put(creature.getObjectId(), observer);
		}
	}

	@Override
	public void onLeaveZone(Creature creature, ZoneInstance zone) {
		Creature acting = creature.getActingCreature();
		if (acting instanceof Player && !((Player) acting).isGM()) {
			Observer observer = observed.get(creature.getObjectId());
			if (observer != null) {
				creature.getObserveController().removeObserver(observer);
				observed.remove(creature.getObjectId());
			}
		}
	}

	class Observer extends ActionObserver {

		private Creature creature;
		private Vector3f oldPos;
		private Node geometry;

		/**
		 * @param observerType
		 */
		public Observer(Creature creature, Node geometry) {
			super(ObserverType.MOVE);
			this.creature = creature;
			this.geometry = geometry;
			this.oldPos = new Vector3f(creature.getX(), creature.getY(), creature.getZ());
		}

		@Override
		public void moved() {
			Vector3f pos = new Vector3f(creature.getX(), creature.getY(), creature.getZ());
			Vector3f dir = oldPos.clone();
			Float limit = pos.distance(dir);
			dir.subtractLocal(pos).normalizeLocal();
			Ray r = new Ray(pos, dir);
			r.setLimit(limit);
			CollisionResults results = new CollisionResults();
			results.setOnlyFirst(true);
			geometry.collideWith(r, results, creature.getInstanceId());
			if (results.size() != 0) {
				creature.getController().die();
			}
			oldPos = pos;
		}
	}
}
