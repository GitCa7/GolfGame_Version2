package physics.systems;


import java.util.ArrayList;
import java.util.HashSet;

import framework.EntitySystem;
import physics.collision.*;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;


import physics.constants.Families;

/**
 * @autor martin
 * created 13.05.2016
 */
public class CollisionDetectionSystem extends EntitySystem
{
	/**
	 * default constructor. Constructs objects needed.
	 */
	public CollisionDetectionSystem()
	{
		mActive = new HashSet<>();
		mDetect = new CollisionDetector();
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
		if (!colliding.isEmpty())
			System.out.println ("detected a collision!");
		for(ColliderPair<ColliderEntity> pair:colliding){
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
	/** store impacted by collisions */
	private HashSet<Entity> mActive;
	/** detects collisions within the set of physics.entities */
	private CollisionDetector mDetect;
	private CollisionRepository mRepository;

}
