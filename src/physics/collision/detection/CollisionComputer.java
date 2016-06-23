package physics.collision.detection;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import physics.geometry.planar.Shape;

/**
 * computes impact of assumed physics.collision between an active and a passive entity on the active entity.
 * Does not modify the physics.entities.
 * @autor martin
 * created 14.05.2016
 */
public class CollisionComputer
{
	/**
	 *
	 * @param active active entity: entity involved in physics.collision and belonging to the accelerable family
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
		return null;
	}

	/**
	 *
	 * @return the force excerted on the active object during physics.collision
	 * Yo I need Martin for this. Does this even do anything?
	 *
	 */
	public Vector3 collisionForce()
	{
		return null;
	}


	private Entity mActive, mPassive;
}
