package PHY_Collision;

/**
 * inner class storing two actors as colliders (i.e. entities) involved in collision
 * @author martin
 */
public class ColliderPair
{
	/**
	 *
	 * @param first collider A involved in collision with collider B
	 * @param second collider B involved in collision with collider A
	 */
	public ColliderPair (ColliderEntity first, ColliderEntity second)
	{
		mFirst = first;
		mSecond = second;
	}

	public ColliderEntity mFirst, mSecond;
}
