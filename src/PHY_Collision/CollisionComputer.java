package PHY_Collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import PHY_Geometry.planar.Shape;

/**
 * computes impact of assumed collision between an active and a passive entity on the active entity.
 * Does not modify the entities.
 * @autor martin
 * created 14.05.2016
 */
public class CollisionComputer
{
	/**
	 *
	 * @param active active entity: entity involved in collision and belonging to the accelerable family
	 * @param passive passive entity: counter part to active entity
	 */
	public CollisionComputer (Entity active, Entity passive)
	{

	}

	/**
	 *
	 * @return a side of the active entity's body which collides with any part of the body of the passive entity
	 */
	public Shape getCollidingSide()
	{

	}

	/**
	 *
	 * @return the force excerted on the active object during collision
	 */
	public Vector3 collisionForce()
	{

	}


	private Entity mActive, mPassive;
}
