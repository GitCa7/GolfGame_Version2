package physics.collision;

import com.badlogic.ashley.core.Entity;
import physics.constants.Families;

import java.util.ArrayList;
import java.util.Collection;
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
		mActive = new HashSet<>();
		mAll = new HashSet<>();
	}

	/**
	 *
	 * @return a collider for each unordered pair of (active|passive) or (active/active) physics.entities
	 */
	public ArrayList<ColliderPair<ColliderEntity>> getAnyColliding()
	{
		//clone sets
		HashSet<EntityAndBody> currentBodies = (HashSet<EntityAndBody>) mAll.clone();

		ArrayList<ColliderPair<ColliderEntity>> colliding = new ArrayList<>();

		//for every active|all pair check for physics.collision
		for (EntityAndBody activeBody : mActive)
		{
			currentBodies.remove (activeBody);
			for (EntityAndBody passiveBody : currentBodies)
			{
				//if colliding: add as collider pair
				//@TODO store intersection detectors permanently
				BodyIntersectionDetector checkBodies = new BodyIntersectionDetector (activeBody.mBody, passiveBody.mBody);
				checkBodies.checkForIntersection();
				if (checkBodies.doIntersect())
				{
					ColliderPair<ColliderBody> cPair = checkBodies.getBodyCollision();
					ColliderBody cb1 = cPair.getFirst();
					ColliderBody cb2 = cPair.getSecond();
					ColliderEntity ec1 = new ColliderEntity(activeBody.mEntity, cb1);
					ColliderEntity ec2 = new ColliderEntity(passiveBody.mEntity, cb2);
					ColliderPair<ColliderEntity> ePair = new ColliderPair<>(ec1, ec2);
					colliding.add(ePair);
				}
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
		EntityAndBody ePair = new EntityAndBody(e);
		boolean notPresent = mAll.add (ePair);
		if (!notPresent)
			throw new IllegalArgumentException ("entity " + e + " was already contained");
		if (Families.ACCELERABLE.matches (e))
			mActive.add (ePair);
	}

	/**
	 * adds all elements of es to the collision detector. For all entities added,
	 * collisions will be checked.
	 * @param es a collection of entities all belonging to the colliding family
     */
	public void addAll(Collection<Entity> es)
	{
		for (Entity e : es)
			add (e);
	}

	/**
	 * removes  e from the set of physics.entities to check for collisions if e is stored there.
	 * Otherwise nothing happens.
	 * @param e an entity
	 */
	public void remove (Entity e)
	{
		EntityAndBody ePair = new EntityAndBody(e);
		mActive.toArray()[0].equals(ePair);
		if (Families.ACCELERABLE.matches (e))
			mActive.remove (ePair);
		mAll.remove (ePair);
	}
	
	private HashSet<EntityAndBody> mActive, mAll;
}
