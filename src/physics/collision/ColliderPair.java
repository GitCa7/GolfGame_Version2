package physics.collision;

/**
 * inner class storing two actors as colliders (i.e. physics.entities) involved in physics.collision
 * @author martin
 */
public class ColliderPair<E>
{
	/**
	 *
	 * @param first collider A involved in physics.collision with collider B
	 * @param second collider B involved in physics.collision with collider A
	 */
	public ColliderPair (E first, E second)
	{
		mFirst = first;
		mSecond = second;
	}

	public E mFirst, mSecond;
}
