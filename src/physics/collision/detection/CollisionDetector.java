package physics.collision.detection;

import com.badlogic.ashley.core.Entity;
import physics.collision.structure.ColliderEntity;
import physics.collision.structure.ColliderPair;
import physics.collision.structure.EntityAndBody;
import physics.constants.Families;

import java.util.Collection;
import java.util.HashSet;

/**
 * class detecting collisions between physics.entities
 * @autor martin
 * created 13.05.2016
 */
public class CollisionDetector implements Cloneable
{

	/**
	 * construct a collision detector using a one-phase approach
	 * @param finder a collision finder returning complete collider pairs
     */
	public CollisionDetector (NarrowCollisionFinder finder)
	{
		mActive = new HashSet<>();
		mAll = new HashSet<>();

		mBroadFinder = null;
		mNarrowFinder = finder;
	}

	/**
	 * construct collision detector differentiating between broad and narrow phase
	 * @param broadFinder a collision finder which may return incomplete collider pairs lacking solids/vertices
	 *                    used for the broad phase
	 * @param narrowFinder a collision finder returning complete collider pairs used for narrow phase
     */
	public CollisionDetector (BroadCollisionFinder broadFinder, NarrowCollisionFinder narrowFinder)
	{
		mActive = new HashSet<>();
		mAll = new HashSet<>();

		mBroadFinder = broadFinder;
		mNarrowFinder = narrowFinder;
	}

	/**
	 * @return a copy of this collision detector containing no entities to check
     */
	public CollisionDetector clone()
	{
		if (mBroadFinder == null)
			return new CollisionDetector(mNarrowFinder.clone());

		return new CollisionDetector(mBroadFinder.clone(), mNarrowFinder.clone());
	}

	/**
	 *
	 * @return a collider for each unordered pair of (active|passive) or (active/active) physics.entities
	 */
	public Collection<ColliderPair<ColliderEntity>> getAnyColliding()
	{
		if (mBroadFinder != null)
		{
			Collection<ColliderPair<ColliderEntity>> narrowInput = mBroadFinder.possibleCollisions();
			mNarrowFinder.setPossibleCollisions(narrowInput);
		}


		return mNarrowFinder.collisions();
	}

	/**
	 * set the entities for which to detect collisions
	 * @param allEntities collection of entities to check
     */
	public void setEntities(Collection<Entity> allEntities)
	{
		mAll.clear();
		mActive.clear();

		for (Entity entity : allEntities)
		{
			EntityAndBody entityAndBody = new EntityAndBody(entity);
			mAll.add(entityAndBody);
			if (Families.MOVING.matches(entity))
				mActive.add(entityAndBody);
		}

		initCollisionFinder();
	}

	private void initCollisionFinder()
	{
		if (mBroadFinder != null)
			mBroadFinder.setBodies(mAll, mActive);
		else
			mNarrowFinder.setCollidingEntities(mAll, mActive);
	}

	private HashSet<EntityAndBody> mActive, mAll;
	private BroadCollisionFinder mBroadFinder;
	private NarrowCollisionFinder mNarrowFinder;
}
