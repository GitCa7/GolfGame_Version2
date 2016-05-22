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
	 * pairs the respepective components if the do collider in the form of Colliders
	 * @return
	 */
	public void checkForIntersection() {
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
					sPair = intersection.getSolidCollision();
					ColliderSolid cS1 = sPair.getFirst();
					ColliderSolid cS2 = sPair.getSecond();
					ColliderBody bc1 = new ColliderBody(mB1, cS1);
					ColliderBody bc2 = new ColliderBody(mB2, cS2);
					mIntersection=new ColliderPair(bc1, bc2);
				}
			}
		}
	}

	public ColliderPair<ColliderBody> getBodyCollision(){return mIntersection;}

	private ColliderPair<ColliderSolid> sPair;
	private Body mB1, mB2;
	private boolean mHasIntersection;
	private ColliderPair <ColliderBody> mIntersection;
}
