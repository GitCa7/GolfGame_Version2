package physics.collision;

import com.badlogic.ashley.core.Entity;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.geometry.spatial.Solid;

/**
 * stores colliding entity together with the part of the entity (i.e. a solid it is composed of)
 * causing the physics.collision
 * @autor martin
 * created 13.05.2016
 */
public class ColliderEntity<T extends Solid> extends ColliderBody<T>
{
	/**
	 *
	 * @param collidingEntity entity colliding
	 * @param collidingBody solid, part of colliding, causing the physics.collision
	 */
	public ColliderEntity(Entity collidingEntity, ColliderBody collidingBody)
	{
		super (collidingBody.getCollidingBody(), collidingBody.getCollidingSolid());
		mColliding = collidingEntity;

		assert (Families.COLLIDING.matches (mColliding));
		assert (CompoMappers.BODY.get (mColliding).equals (collidingBody));
	}

	/**
	 *
	 * @return entitiy stored
	 */
	public Entity getCollidingEntity() { return mColliding; }

	/**
	 *
	 * @return true if entity stored is actively involved in collisions, i.e. belongs to accelerable family
	 */
	public boolean isActive() { return Families.ACCELERABLE.matches (getCollidingEntity()); }


	private Entity mColliding;
}
