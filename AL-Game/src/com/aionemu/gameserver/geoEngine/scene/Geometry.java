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
package com.aionemu.gameserver.geoEngine.scene;

import com.aionemu.gameserver.geoEngine.bounding.BoundingVolume;
import com.aionemu.gameserver.geoEngine.collision.Collidable;
import com.aionemu.gameserver.geoEngine.collision.CollisionResults;
import com.aionemu.gameserver.geoEngine.math.Matrix3f;
import com.aionemu.gameserver.geoEngine.math.Matrix4f;
import com.aionemu.gameserver.geoEngine.math.Ray;
import com.aionemu.gameserver.geoEngine.math.Vector3f;

public class Geometry extends Spatial {

	/**
	 * The mesh contained herein
	 */
	protected Mesh mesh;

	protected Matrix4f cachedWorldMat = new Matrix4f();

	/**
	 * Do not use this constructor. Serialization purposes only.
	 */
	public Geometry() {
	}

	/**
	 * Create a geometry node without any mesh data.
	 * 
	 * @param name
	 *          The name of this geometry
	 */
	public Geometry(String name) {
		super(name);
	}

	/**
	 * Create a geometry node with mesh data.
	 * 
	 * @param name
	 *          The name of this geometry
	 * @param mesh
	 *          The mesh data for this geometry
	 */
	public Geometry(String name, Mesh mesh) {
		this(name);
		if (mesh == null)
			throw new NullPointerException();

		this.mesh = mesh;
	}

	@Override
	public int getVertexCount() {
		return mesh.getVertexCount();
	}

	@Override
	public int getTriangleCount() {
		return mesh.getTriangleCount();
	}

	public void setMesh(Mesh mesh) {

		this.mesh = mesh;
	}

	public Mesh getMesh() {
		return mesh;
	}

	/**
	 * @return The bounding volume of the mesh, in model space.
	 */
	public BoundingVolume getModelBound() {
		return mesh.getBound();
	}

	/**
	 * Updates the bounding volume of the mesh. Should be called when the mesh has been modified.
	 */
	@Override
	public void updateModelBound() {
		mesh.updateBound();
		worldBound = getModelBound().transform(cachedWorldMat, worldBound);
	}

	public Matrix4f getWorldMatrix() {
		return cachedWorldMat;
	}

	@Override
	public void setModelBound(BoundingVolume modelBound) {
		mesh.setBound(modelBound);
	}

	@Override
	public int collideWith(Collidable other, CollisionResults results, int instanceId) {
		if (other instanceof Ray) {
			if (!worldBound.intersects(((Ray) other)))
				return 0;
		}
		// NOTE: BIHTree in mesh already checks collision with the
		// mesh's bound
		int added = mesh.collideWith(other, cachedWorldMat, worldBound, results);
		return added;
	}

	/*
	 * (non-Javadoc)
	 * @see aionjHungary.geoEngine.scene.Spatial#setTransform(aionjHungary.geoEngine.math.Matrix3f,
	 * aionjHungary.geoEngine.math.Vector3f)
	 */
	@Override
	public void setTransform(Matrix3f rotation, Vector3f loc, float scale) {
		cachedWorldMat.loadIdentity();
		cachedWorldMat.setRotationMatrix(rotation);
		cachedWorldMat.scale(scale);
		cachedWorldMat.setTranslation(loc);
	}
}
