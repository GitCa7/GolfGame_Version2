package physics.collision;

import com.badlogic.ashley.core.Entity;
import physics.constants.Families;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * class detecting collisions between physics.entities
 * @autor martin
 * created 13.05.2016
 */
public class CollisionDetector
{


	public CollisionDetector ()
	{

	}

	/**
	 *
	 * @return a collider for each unordered pair of (active|passive) or (active/active) physics.entities
	 */
	public ArrayList<ColliderPair<ColliderEntity>> getAnyColliding()
	{
		//clone sets
		HashSet<BodyPair> currentBodies = (HashSet<BodyPair>) mAll.clone();
		ArrayList<ColliderPair<ColliderEntity>> colliding = new ArrayList<>();

		//for every active|all pair check for physics.collision
		for (BodyPair activeBody : mActive)
		{
			currentBodies.remove (activeBody);
			for (BodyPair passiveBody : currentBodies)
			{
				//if colliding: add as collider pair
				//@TODO store intersection detectors permanently
				BodyIntersectionDetector checkBodies = new BodyIntersectionDetector (activeBody.mBody, passiveBody.mBody);
				if (checkBodies.doIntersect())
					cPair = checkBodies.getBodyCollision();
					ColliderBody cb1 = cPair.getFirst();
					ColliderBody cb2 = cPair.getSecond();
					ColliderEntity ec1 = new ColliderEntity (activeBody.mEntity,cb1);
					ColliderEntity ec2 = new ColliderEntity (passiveBody.mEntity, cb2);
					ColliderPair<ColliderEntity> ePair = new ColliderPair<> (ec1, ec2);
					colliding.add (ePair);
			}
		}

		return colliding;
	}

	/**
	 * adds e to the set of physics.entities to check for collisions with.
	 * @param e an entity belonging to the colliding family
	 * @throws IllegalArgumentException if e was already stored
	 */
	public void add (Entity e)
	{
		BodyPair ePair = new BodyPair (e);
		boolean notPresent = mAll.add (ePair);
		if (!notPresent)
			throw new IllegalArgumentException ("entity " + e + " was already contained");
		if (Families.ACCELERABLE.matches (e))
			mActive.add (ePair);
	}

	/**
	 * removes  e from the set of physics.entities to check for collisions if e is stored there.
	 * Otherwise nothing happens.
	 * @param e an entity
	 */
	public void remove (Entity e)
	{
		BodyPair ePair = new BodyPair (e);
		if (Families.ACCELERABLE.matches (e))
			mActive.remove (ePair);
		mAll.remove (ePair);
	}

	private HashSet<BodyPair> mActive, mAll;
	private ColliderPair<ColliderBody> cPair;
}
