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

	public boolean equals(ColliderPair<E> another)
	{
		boolean firstMatch = this.getFirst().equals(another.getFirst()) || this.getFirst().equals(another.getSecond());
		boolean secondMatch = this.getSecond().equals(another.getFirst()) || this.getSecond().equals(another.getSecond());
		return firstMatch && secondMatch;
	}

	public E getFirst(){return mFirst;}
	public E getSecond(){return mSecond;}

	public void setFirst(E first){mFirst=first;}
	public void setSecond(E second){mSecond=second;}


	public E mFirst, mSecond;
}
