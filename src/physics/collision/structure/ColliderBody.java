package physics.collision.structure;

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
	public Body getBody() { return mColliding; }


	public boolean equals(ColliderBody another)
	{
		return (another.getCollidingSolid().equals(this.getCollidingSolid()) && another.getBody().equals(this.getBody()));
	}

	private Body mColliding;
}
