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

		if (super.hashCode() == 0)
			mHash = mColliding.hashCode();
		else
			mHash = mColliding.hashCode() % super.hashCode();
	}

	/**
	 * @return body colliding stored
	 */
	public Body getBody() { return mColliding; }


	public int hashCode()
	{
		return mHash;
	}

	public boolean equals(Object another)
	{
		ColliderBody comp = (ColliderBody) another;
		return (comp.getBody().equals(this.getBody()) && super.equals(comp));
	}

	private Body mColliding;
	private int mHash;
}
