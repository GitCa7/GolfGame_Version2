package physics.collision;

import physics.components.Body;
import physics.geometry.spatial.Solid;

/**
 * @autor martin
 * created 16.05.2016
 */
public class ColliderBody<T extends Solid>
{
	/**
	 *
	 * @param colliding body colliding
	 * @param collidingPart solid physics.collision is occuring at
	 */
	public ColliderBody (Body colliding, Solid collidingPart)
	{
		mColliding = colliding;
		mCollidingPart = collidingPart;
	}

	/**
	 *
	 * @return body colliding stored
	 */
	public Body getCollidingBody() { return mColliding; }

	/**
	 *
	 * @return the solid physics.collision is occurring at
	 */
	public Solid getCollidingSolid() { return mCollidingPart; }

	private Body mColliding;
	private Solid mCollidingPart;
}
