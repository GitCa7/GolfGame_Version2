package PHY_Systems;


import java.util.ArrayList;
import java.util.HashSet;

import PHY_Collision.*;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;


import com.badlogic.gdx.math.Vector3;
import PHY_Components.*;
import PHY_Constants.Families;

/**
 * @autor martin
 * created 13.05.2016
 */
public class CollisionSystem extends EntitySystem
{
	/**
	 * default constructor. Constructs objects needed.
	 */
	public CollisionSystem()
	{
		mActive = new HashSet<>();
		mDetect = new CollisionDetector();
	}

	/**
	 * adds relevant entities of the engine to the system once the system was added to the
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
	 * computes impact of collisions on entities affected
	 * @param dTime time delta to previous state
	 */
	public void update (float dTime)
	{
		//detect collisions
		ArrayList<ColliderPair> colliding = mDetect.getAnyColliding();
		//for each collision detected
		for (ColliderPair collPair : colliding)
		{
			//if entity 1 is active
			if (collPair.mFirst.isActive())
			{
				//compute force excerted
				Entity active = collPair.mFirst.getColliding();
				CollisionComputer computeImpact = new CollisionComputer (active, collPair.mSecond.getColliding());
				Vector3 impact = computeImpact.collisionForce();

				assert (CompoMappers.FORCE.has (active));
				Force driving = CompoMappers.FORCE.get (active);
				driving.add (impact);
			}
			//if entity 2 is active
			if (collPair.mSecond.isActive())
			{
				//copute force excerted
				Entity active = collPair.mSecond.getColliding();
				CollisionComputer computeImpact = new CollisionComputer (active, collPair.mFirst.getColliding());
				Vector3 impact = computeImpact.collisionForce();

				assert (CompoMappers.FORCE.has (active));
				Force driving = CompoMappers.FORCE.get (active);
				driving.add (impact);
			}
		}

	}

	/** store impacted by collisions */
	private HashSet<Entity> mActive;
	/** detects collisions within the set of entities */
	private CollisionDetector mDetect;
}
