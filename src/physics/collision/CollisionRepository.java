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
	 * abstract class mapping entities to collider pairs. Used to customize
	 * how entity queries should be handled.
	 */
	public interface EntityMapper
	{
		/**
		 * @param e a given entity
		 * @return list of collider pairs containing e
		 * @throws IllegalArgumentException if e is not contained
		 */
		public ArrayList<ColliderPair> getFor (Entity e);

		/**
		 * @param e a given entity
		 * @return true if entity exists in repository
		 */
		public boolean hasEntity (Entity e);

		/**
		 * adds c to the mapper's internals
		 * @param c some collider pair
		 */
		public void addColliderPair (ColliderPair c);
	}

	/**
	 * performs linear time searches instead of maintaining additional data structures
	 */
	public class NoMapper implements EntityMapper
	{
		@Override
		public ArrayList<ColliderPair> getFor(Entity e)
		{
			ArrayList<ColliderPair> pairsForEntity = new ArrayList<>();
			for (ColliderPair cPair : mColliderPairs)
			{
				if (cPair.hasEntity (e))
					pairsForEntity.add (cPair);
			}

			return pairsForEntity;
		}

		@Override
		public boolean hasEntity(Entity e)
		{
			return false;
		}

		@Override
		public void addColliderPair (ColliderPair c) {}
	}

	/**
	 * stores map to perform entity queries more quickly
	 */
	public class SetMapper implements EntityMapper
	{

		public SetMapper()
		{
			mEntityMap = new HashMap<>();
		}

		@Override
		public ArrayList<ColliderPair> getFor(Entity e)
		{
			return mEntityMap.get (e);
		}

		@Override
		public boolean hasEntity(Entity e)
		{
			return mEntityMap.containsKey (e);
		}

		@Override
		public void addColliderPair (ColliderPair c)
		{
			addEntity (c.getFirst(), c);
			addEntity (c.getSecond(), c);
		}

		/**
		 * adds p to the list mapped to e or creates a new mapping if e is not yet contained
		 * @param e
		 * @param p
		 */
		private void addEntity (Entity e, ColliderPair p)
		{
			if (hasEntity (e))
				mEntityMap.get (e).add (p);
			else
			{
				ArrayList<ColliderPair> newColliderList = new ArrayList<>();
				newColliderList.add (p);
				mEntityMap.put (e, newColliderList);
			}
		}

		private HashMap<Entity, ArrayList<ColliderPair>> mEntityMap;
	}

	/**
	 * default constructor
	 */
	public CollisionRepository()
	{
		mColliderPairs = new ArrayList<>();
		mEntityColliderMap = new HashMap<>();
	}

	/**
	 * @return the collider pairs stored in this repository
	 */
	public ArrayList<ColliderPair> getColliderPairs() { return mColliderPairs; }

	/**
	 * @param e a given entity
	 * @return a list of collider pairs where one element is e
	 * @IllegalArgumentException if there are no collisions for e
	 */
	public ArrayList<ColliderPair> getCollisionsFor (Entity e)
	{
		if (!mCache.isEmpty() && mCache.get().mEntity.equals (e))
			return mCache.get().mPairs;

		ArrayList<ColliderPair> pairs = mEntityMapper.getFor (e);
		mCache.forgetAndRemember (new EntityQuery (e, pairs));
		return pairs;
	}

	/**
	 * @param e a given entity
	 * @return true if there is a collision in which e takes part, false otherwise
	 */
	public boolean hasEntity (Entity e)
	{
		return mEntityMapper.hasEntity (e);
	}

	/**
	 * adds p to the list of entities stored, without further checks
	 * @param p a given collider pair to add
	 */
	public void addColliderPair (ColliderPair p)
	{
		mColliderPairs.add (p);
		mEntityMapper.addColliderPair (p);
	}

	/**
	 * class storing entity query
	 */
	private class EntityQuery
	{
		public EntityQuery (Entity e, ArrayList<ColliderPair> cs)
		{
			mEntity = e;
			mPairs = cs;
		}

		public Entity mEntity;
		public ArrayList<ColliderPair> mPairs;
	}

	/** stores list of colliding pairs */
	private ArrayList<ColliderPair> mColliderPairs;
	/** handles entity queries */
	private EntityMapper mEntityMapper;
	/** stores previous query */
	private Cacher<EntityQuery> mCache;
}
