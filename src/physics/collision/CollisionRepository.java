package physics.collision;

import com.badlogic.ashley.core.Entity;
import physics.generic.Cacher;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class containing pairs of colliding entities together with information about the collision.
 * Acts as repository which can be shared among other objects.
 * Offers customization on how to handle entity queries.
 * @autor martin
 * created 20.05.2016
 */
public class CollisionRepository
{
	/**
	 * default constructor
	 */
	public CollisionRepository()
	{
		mColliderPairs = new ArrayList<>();
		mCache = new Cacher<>();
	}

	/**
	 * @return the collider pairs stored in this repository
	 */
	public ArrayList<ColliderPair<ColliderEntity>> getColliderPairs() { return mColliderPairs; }

	/**
	 * @param e a given entity
	 * @return a list of collider pairs where one element is e
	 * @IllegalArgumentException if there are no collisions for e
	 */
	public ArrayList<ColliderPair<ColliderEntity>> getCollisionsFor (Entity e)
	{
		ArrayList<ColliderPair<ColliderEntity>> collisions = new ArrayList<>();

		for (ColliderPair<ColliderEntity> cp : mColliderPairs)
		{
			if (cp.mFirst.getColliding().equals(e)||cp.mSecond.getColliding().equals(e) )
				collisions.add (cp);
		}
		return collisions;
	}


	/**
	 * @param e a given entity
	 * @return true if there is a collision in which e takes part, false otherwise
	 */
	public boolean hasEntity (Entity e)
	{
		for (ColliderPair<ColliderEntity> cp : mColliderPairs)
		{
			if (cp.mFirst.getColliding().equals(e)||cp.mSecond.getColliding().equals(e))
				return true;
		}

		return false;
	}

	/**
	 * adds p to the list of entities stored, without further checks
	 * @param p a given collider pair to add
	 */
	public void addColliderPair (ColliderPair<ColliderEntity> p)
	{
		mColliderPairs.add (p);
	}

	public void clear () {
		mColliderPairs.clear();
		mCache.forget();
	}

	/**
	 * class storing entity query
	 */
	protected class EntityQuery
	{
		public EntityQuery (Entity e, ArrayList<ColliderPair> cs)
		{
			mEntity = e;
			mPairs = cs;
		}

		public Entity mEntity;
		public ArrayList<ColliderPair> mPairs;
	}

	/**rentityQueryCache
	 * @return the cacher storing the last entity query
	 */
	protected Cacher<EntityQuery> getQueryCache()
	{
		return mCache;
	}

	/** stores list of colliding pairs */
	private ArrayList<ColliderPair<ColliderEntity>> mColliderPairs;
	/** stores previous query */
	private Cacher<EntityQuery> mCache;
}
