package PHY_Collision;



import java.util.Iterator;

import PHY_Components.Body;
import PHY_Geometry.spatial.Solid;
import PHY_Geometry.spatial.SolidIntersection;

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
		return false;
	}

	/**
	 * searches all pairs of body components for an intersection
	 */
	public void checkForIntersection()
	{
		Iterator<Solid> iB1 = mB1.iterator();
		while (iB1.hasNext())
		{
			Solid s1 = iB1.next();
			Iterator<Solid> iB2 = mB2.iterator();
			while (iB2.hasNext())
			{
				Solid s2 = iB2.next();
				SolidIntersection intersection = new SolidIntersection (s1, s2);

				ColliderEntity c1 = new ColliderEntity()

				mIntersection = new ColliderPair();
			}
		}
	}

	private Body mB1, mB2;
	private boolean mHasIntersection;
	private ColliderPair mIntersection;
}
