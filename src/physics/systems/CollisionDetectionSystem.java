package physics.systems;


import java.util.ArrayList;
import java.util.HashSet;

import framework.EntitySystem;
import physics.collision.*;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;


import physics.constants.CompoMappers;
import physics.constants.Families;

/**
 * @autor martin
 * created 13.05.2016
 */
public class CollisionDetectionSystem extends EntitySystem implements RepositoryEntitySystem
{
	public static boolean DEBUG = false;


	/**
	 * default constructor. Constructs objects needed.
	 */
	public CollisionDetectionSystem()
	{
		mActive = new HashSet<>();
	}


	public CollisionDetectionSystem clone()
	{
		CollisionDetectionSystem newSystem = new CollisionDetectionSystem();
		newSystem.setPriority(priority);
		return newSystem;
	}

	/**
	 * adds relevant physics.entities of the engine to the system once the system was added to the
	 * system.
	 * @param e engine this was added to
	 */
	public void addedToEngine (Engine e)
	{
		for (Entity add : e.getEntities())
		{
			if (Families.COLLIDING.matches(add))
			{
				entities().add(add);
				if (Families.ACCELERABLE.matches(add))
					mActive.add(add);
			}
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
		CollisionDetector detector = new CollisionDetector();
		detector.addAll(entities());
		ArrayList<ColliderPair<ColliderEntity>> colliding = detector.getAnyColliding();
		if (!colliding.isEmpty())	{
			if(mDebug) {
				//System.out.println ("detected a collision!");
			}
		}

		for(ColliderPair<ColliderEntity> pair : colliding)
		{
			mRepository.addColliderPair(pair);
			if (DEBUG)
				debugOut(pair);
		}
	}





	public void addEntity(Entity e) {
		if (Families.COLLIDING.matches((e))) {
			entities().add(e);
			if (Families.ACCELERABLE.matches(e))
				mActive.add(e);
		}
	}


	public void removeEntity(Entity e)
	{
		if (Families.COLLIDING.matches((e)))
		{
			entities().remove (e);
			if (Families.ACCELERABLE.matches (e))
				mActive.remove (e);
		}
	}

	public void debugOut(ColliderPair<ColliderEntity> cPair)
	{
		System.out.print("detected collision between " + cPair.getFirst().getEntity());
		System.out.print(" at " + CompoMappers.POSITION.get(cPair.getFirst().getEntity()));
		System.out.print(" and " + cPair.getSecond().getEntity());
		System.out.println(" at " + CompoMappers.POSITION.get(cPair.getSecond().getEntity()));
	}


	/** store impacted by collisions */
	private HashSet<Entity> mActive;
	/** detects collisions within the set of physics.entities */
//	private CollisionDetector mDetect;
	private CollisionRepository mRepository;
	private final boolean mDebug = true;

}
