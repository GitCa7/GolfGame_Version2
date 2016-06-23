package physics.collision.structure;

/**
 * inner class storing two actors as colliders (i.e. physics.entities) involved in physics.collision
 * @author martin
 */
public class ColliderPair<E extends ColliderSolid>
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


	public int hashCode()
	{
		return mFirst.hashCode() % mSecond.hashCode();
	}

	public boolean equals(Object another)
	{
		ColliderPair<E> compare = (ColliderPair<E>) another;

		boolean firstMatch = this.getFirst().equals(compare.getFirst()) || this.getFirst().equals(compare.getSecond());
		boolean secondMatch = this.getSecond().equals(compare.getFirst()) || this.getSecond().equals(compare.getSecond());
		return firstMatch && secondMatch;
	}

	public E getFirst(){return mFirst;}
	public E getSecond(){return mSecond;}


	public void setFirst(E first){mFirst=first;}
	public void setSecond(E second){mSecond=second;}


	public E mFirst, mSecond;
}
