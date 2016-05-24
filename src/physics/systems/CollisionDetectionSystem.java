package physics.systems;


import java.util.ArrayList;
import java.util.HashSet;

import physics.collision.ColliderEntity;
import physics.collision.ColliderPair;
import physics.collision.CollisionComputer;
import physics.collision.CollisionDetector;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;


import com.badlogic.gdx.math.Vector3;
import physics.components.Force;
import physics.constants.CompoMappers;
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

	/**
	 * computes impact of collisions on physics.entities affected
	 * @param dTime time delta to previous state
	 */
	public void update (float dTime)
	{
		//detect collisions
		ArrayList<ColliderPair<ColliderEntity>> colliding = mDetect.getAnyColliding();
		//@TODO Use Repository
	}

	/** store impacted by collisions */
	private HashSet<Entity> mActive;
	/** detects collisions within the set of physics.entities */
	private CollisionDetector mDetect;

	@Override
	public void addEntity(Entity e) {

	}

	@Override
	public void removeEntity(Entity e) {

	}
}
