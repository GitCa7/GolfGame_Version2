package physics.collision.structure;

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

		if (super.hashCode() == 0)
			mHash = mColliding.hashCode();
		else
			mHash =  mColliding.hashCode() % super.hashCode();

		assert (Families.COLLIDING.matches (mColliding));
	}

	/**
	 *
	 * @return entitiy stored7
	 *
	 */
	public Entity getEntity() { return mColliding; }


	public int hashCode()
	{
		return mHash;
	}

	/**
	 *
	 * @return true if entity stored is actively involved in collisions, i.e. belongs to accelerable family
	 */
	public boolean isActive() { return (Families.ACCELERABLE.matches (getEntity())); }

	public boolean equals(Object another)
	{
		ColliderEntity comp = (ColliderEntity) another;
		return this.mColliding.equals(comp.mColliding) && super.equals(comp);
	}

	private Entity mColliding;
	private int mHash;
}
