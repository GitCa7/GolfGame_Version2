package physics.collision;

import physics.components.Body;

/**
 * @autor martin
 * created 16.05.2016
 */
public class ColliderBody extends ColliderSolid
{
	/**
	 *
	 * @param colliding body colliding
	 */
	public ColliderBody (Body colliding, ColliderSolid solidCollider)
	{
		super((solidCollider.hasCollidingVertex() ? solidCollider.getCollidingVertex() : null),
				solidCollider.getCollidingSolid());
		mColliding = colliding;
	}

	/**
	 * @return body colliding stored
	 */
	public Body getCollidingBody() { return mColliding; }

	private Body mColliding;
}
