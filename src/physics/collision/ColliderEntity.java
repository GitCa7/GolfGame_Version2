package physics.collision;

import com.badlogic.ashley.core.Entity;
import physics.constants.Families;

/**
 * stores colliding entity together with the part of the entity (i.e. a solid it is composed of)
 * causing the physics.collision
 * @autor martin
 * created 13.05.2016
 */
public class ColliderEntity extends ColliderBody
{
	/**
	 *
	 * @param collidingEntity entity colliding
	 * @param collidingBody solid, part of colliding, causing the physics.collision
	 */
	public ColliderEntity(Entity collidingEntity, ColliderBody collidingBody)
	{
		super (collidingBody.getBody(), collidingBody);
		mColliding = collidingEntity;


		assert (Families.COLLIDING.matches (mColliding));
	}

	/**
	 *
	 * @return entitiy stored7
	 *
	 */
	public Entity getEntity() { return mColliding; }


	/**
	 *
	 * @return true if entity stored is actively involved in collisions, i.e. belongs to accelerable family
	 */
	public boolean isActive() { return (Families.ACCELERABLE.matches (getEntity())); }

	public boolean equals(ColliderEntity another)
	{
		boolean entityEqual = this.getEntity().equals(another.getEntity());
		boolean vertexEqual = this.getCollidingVertex().equals(another.getCollidingVertex());
		boolean solidEqual = this.getCollidingSolid().equals(another.getCollidingSolid());
		return entityEqual && vertexEqual && solidEqual;
	}

	private Entity mColliding;
}
