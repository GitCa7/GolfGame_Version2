package physics.systems;


import java.util.ArrayList;
import java.util.HashSet;

import framework.EntitySystem;
import framework.testing.RepositoryEntitySystem;
import physics.collision.*;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;


import physics.constants.Families;
import physics.constants.GlobalObjects;
import physics.geometry.planar.Plane;

/**
 * @autor martin
 * created 13.05.2016
 */
public class CollisionDetectionSystem extends EntitySystem implements RepositoryEntitySystem
{
	/**
	 * default constructor. Constructs objects needed.
	 */
	public CollisionDetectionSystem()
	{
		mActive = new HashSet<>();
		mDetect = new CollisionDetector();
	}


	public CollisionDetectionSystem clone()
	{
		return new CollisionDetectionSystem();
	}

	/**
	 * adds relevant physics.entities of the engine to the system once the system was added to the
	 * system.
	 * @param e engine this was added to
	 */
	public void addedToEngine (Engine e)
	{
		for (Entity add : e.getEntitiesFor (Families.COLLIDING))
		{
			entities().add (add);
			mDetect.add (add);
			if (Families.ACCELERABLE.matches (add))
				mActive.add (add);
		}
	}

	public void setRepository(CollisionRepository repository){
		mRepository=repository;
	}

	/**
	 * computes impact of collisions on physics.entities affected
	 * @param dTime time delta to previous state
	 */
	public void update (float dTime)
	{
		//detect collisions
		mRepository.clear();
		ArrayList<ColliderPair<ColliderEntity>> colliding = mDetect.getAnyColliding();
		if (!colliding.isEmpty())	{
			if(mDebug) {
				//System.out.println ("detected a collision!");
			}
		}

		float epsilon = (float) GlobalObjects.ROUND.getEpsilon();

		for(ColliderPair<ColliderEntity> pair : colliding)
		{
			//do this elsewhere (i.e. needed for collisions
			//if (	(pair.getFirst().isActive() && !testCloseness(pair.getFirst(), pair.getSecond(), epsilon) ||
			//		(pair.getSecond().isActive() && !testCloseness(pair.getSecond(), pair.getFirst(), epsilon))))
					mRepository.addColliderPair(pair);
		}
	}





	public void addEntity(Entity e) {
		if (Families.COLLIDING.matches((e))) {
			entities().add(e);
			mDetect.add(e);
			if (Families.ACCELERABLE.matches(e))
				mActive.add(e);
		}
	}


	public void removeEntity(Entity e)
	{
		if (Families.COLLIDING.matches((e))) {
			entities().remove (e);
			mDetect.remove (e);
			if (Families.ACCELERABLE.matches (e))
				mActive.remove (e);
		}
	}


	private boolean testCloseness(ColliderEntity active, ColliderEntity passive, float eps)
	{
		ColliderClosestSideFinder findSide = new ColliderClosestSideFinder(active, passive);
		Plane closest = findSide.find();
		if (closest.testPoint(active.getCollidingVertex()) < eps)
			return true;
		return false;
	}

	/** store impacted by collisions */
	private HashSet<Entity> mActive;
	/** detects collisions within the set of physics.entities */
	private CollisionDetector mDetect;
	private CollisionRepository mRepository;
	private final boolean mDebug = true;

}
