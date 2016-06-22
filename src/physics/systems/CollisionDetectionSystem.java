package physics.systems;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import framework.EntitySystem;
import physics.collision.*;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;


import physics.constants.CompoMappers;
import physics.constants.Families;

/**
 * System checking all colliding entities for collisions.
 * Maintains information about collisions in a collision repository.
 * Note: Entities added are not immediatly added to the collision detector used internally.
 * Instead this happens whenever the system is updated next, so adding entities individually
 * does not cause big performance issues.
 * @autor martin
 * created 13.05.2016
 */
public class CollisionDetectionSystem extends EntitySystem implements RepositoryEntitySystem
{

	public static boolean DEBUG = false;


	/**
	 * default constructor. Constructs objects needed.
	 */
	public CollisionDetectionSystem(CollisionDetector detector)
	{
		mActive = new HashSet<>();
		mDetect = detector;
		mDetectorUpdatePending = false;
	}


	public CollisionDetectionSystem clone()
	{
		CollisionDetectionSystem newSystem = new CollisionDetectionSystem(mDetect.clone());
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
				mDetectorUpdatePending = true;
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
		if (mDetectorUpdatePending)
			updateDetector();

		//detect collisions
		mRepository.clear();

		Collection<ColliderPair<ColliderEntity>> colliding = mDetect.getAnyColliding();

		for(ColliderPair<ColliderEntity> pair : colliding)
		{
			mRepository.addColliderPair(pair);
			if (DEBUG)
				debugOut(pair);
		}
	}


	public void addEntity(Entity e)
	{
		if (Families.COLLIDING.matches((e)))
		{
			mDetectorUpdatePending = true;
			entities().add(e);
			if (Families.ACCELERABLE.matches(e))
				mActive.add(e);
		}
	}


	public void removeEntity(Entity e)
	{
		if (Families.COLLIDING.matches((e)))
		{
			mDetectorUpdatePending = true;
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


	private void updateDetector()
	{
		mDetect.setEntities(entities());
		mDetectorUpdatePending = false;
	}

	/** store impacted by collisions */
	private HashSet<Entity> mActive;
	/** detects collisions within the set of physics.entities */
	private CollisionDetector mDetect;
	private CollisionRepository mRepository;

	private boolean mDetectorUpdatePending;
}
