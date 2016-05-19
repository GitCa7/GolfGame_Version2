package PHY_Collision;

import PHY_Components.Body;
import PHY_Geometry.spatial.*;

/**
 * @autor martin
 * created 16.05.2016
 */
public class ColliderBody<T extends Solid>
{
	/**
	 *
	 * @param colliding body colliding
	 * @param collidingPart solid collision is occuring at
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
	 * @return the solid collision is occurring at
	 */
	public Solid getCollidingSolid() { return mCollidingPart; }

	private Body mColliding;
	private Solid mCollidingPart;
}
