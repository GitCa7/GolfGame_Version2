package physics.collision;

import physics.components.Body;
import physics.geometry.spatial.Solid;
import physics.geometry.spatial.SolidIntersection;

import java.util.Iterator;

/**
 * class computing whether bodies intersect and which parts exactly do
 * @autor martin
 * created 15.05.2016
 */
public class BodyIntersectionDetector
{
	//@TODO implementation

	/**
	 *
	 * @param b1 first body for computations
	 * @param b2 second body for computations
	 */
	public BodyIntersectionDetector (Body b1, Body b2)
	{
		mB1 = b1;
		mB2 = b2;
		mHasIntersection=false;
	}

	/**
	 *
	 * @return a collider pair containing which parts of the bodies intersect.
	 * @throws IllegalStateException if no intersection occurred
	 */
	public ColliderPair intersection()
	{
		return null;
	}


	public boolean doIntersect()
	{
		return mHasIntersection;
	}

	/**
	 * searches all pairs of body physics.components for an intersection
	 */
	public ColliderPair checkForIntersection() {
		Iterator<Solid> iB1 = mB1.iterator();
		while (iB1.hasNext()) {
			Solid s1 = iB1.next();
			Iterator<Solid> iB2 = mB2.iterator();
			while (iB2.hasNext()) {
				Solid s2 = iB2.next();
				SolidIntersection intersection = new SolidIntersection(s1, s2);
				intersection.checkForIntersection();
				if (intersection.ismHasIntersection()) {
					mHasIntersection=true;
					ColliderSolid collidingPair = new ColliderSolid(intersection.getMIntersection(), s2);
					ColliderSolid collidingPair1 = new ColliderSolid(intersection.getMIntersection1(), s1);
					ColliderPair<ColliderPair> pair = new ColliderPair(collidingPair, collidingPair1);
					ColliderBody bc1 = new ColliderBody(mB1, collidingPair);
					ColliderBody bc2 = new ColliderBody(mB2, collidingPair1);

					return new ColliderPair(collidingPair, collidingPair1);

				}
			}
		}
		return new ColliderPair(null, null);
	}

	private Body mB1, mB2;
	private boolean mHasIntersection;
	private ColliderPair mIntersection;
}
