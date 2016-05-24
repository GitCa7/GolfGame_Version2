package physics.collision;

import com.badlogic.ashley.core.Entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Extension to the collision repository to handle entity queries more efficiently.
 * @autor martin
 * created 21.05.2016
 */
public class SetCollisionRepository extends CollisionRepository
{
	/**
	 * default constructor initializing fields
	 */
	public SetCollisionRepository()
	{
		mEntityMap = new HashMap<>();
	}

	/**
	 * @param e a given entity
	 * @return all collisions in which e is involved. Hands out the internal list storing them. Do not modify.
	 */
	public ArrayList<ColliderPair<ColliderEntity>> getCollisionsFor (Entity e)
	{
		return mEntityMap.get (e);
	}

	/**
	 * @param e a given entity
	 * @return true if e is involved in a collision stored in this repository
	 */
	public boolean hasEntity (Entity e)
	{
		if (!getQueryCache().isEmpty() && getQueryCache().get().mEntity.equals (e))
			return true;
		return (mEntityMap.containsKey (e));
	}

	/**
	 * adds cPair to the repository
	 * @param cPair a given collider pair
	 */

	public void addColliderPair (ColliderPair<ColliderEntity> cPair)
	{
		addEntity (cPair.getFirst().getColliding(),cPair.getFirst());
		addEntity (cPair.getSecond().getColliding(),cPair.getSecond());
	}


	/**
	 * adds p to the list mapped to e or creates a new mapping if e is not yet contained
	 * @param e
	 * @param p
	 */
	private void addEntity (Entity e, ColliderPair<ColliderEntity> p)
	{
		if (hasEntity (e))
			mEntityMap.get (e).add (p);
		else
		{
			ArrayList<ColliderPair<ColliderEntity>> newColliderList = new ArrayList<>();
			newColliderList.add (p);
			mEntityMap.put (e, newColliderList);
		}
	}

	private HashMap<Entity, ArrayList<ColliderPair<ColliderEntity>>> mEntityMap;
}
