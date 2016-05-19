package PHY_Collision;

import com.badlogic.ashley.core.Entity;
import PHY_Components.Body;
import PHY_Constants.CompoMappers;
import PHY_Constants.Families;
import PHY_Geometry.spatial.*;


/**
 * stores colliding entity together with the part of the entity (i.e. a solid it is composed of)
 * causing the collision
 * @autor martin
 * created 13.05.2016
 */
public class ColliderEntity<T extends Solid> extends ColliderBody<T>
{
	/**
	 *
	 * @param collidingEntity entity colliding
	 * @param collidingBody solid, part of colliding, causing the collision
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
	 * @return true if entity stored is actively involved in collisions
	 */
	public boolean isActive() { return Families.ACCELERABLE.matches (getCollidingEntity()); }


	private Entity mColliding;
}
