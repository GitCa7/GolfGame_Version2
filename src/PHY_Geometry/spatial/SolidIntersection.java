package PHY_Geometry.spatial;

import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;

import java.util.Iterator;

/**
 * class determining intersections between two solids
 * @autor martin
 * created 16.05.2016
 */
public class SolidIntersection
{
	/**
	 *
	 * @param s1 first solid
	 * @param s2 second solid
	 */
	public SolidIntersection (Solid s1, Solid s2)
	{
		mS1 = s1;
		mS2 = s2;
		mIntersection = null;
		mHasIntersection = false;
	}

	/**
	 *
	 * @return a vertex of the first solid within the second solid is returned.
	 * @throws IllegalStateException if no intersection exists
	 */
	public Vector3 intersection() throws IllegalStateException
	{
		runInitialTest();
		if (!mHasIntersection)
			throw new IllegalStateException ("no intersection was found");
		return mIntersection;
	}

	/**
	 *
	 * @return true if the two solids stored intersect
	 */
	public boolean doIntersect()
	{
		runInitialTest();
		return mHasIntersection;
	}

	/**
	 * searches for intersection. If an intersection was found, the intersecting vertex is set.
	 */
	public void checkForIntersection()
	{
		int cVertex = 0;
		mHasIntersection = false;
		//iterate over vertices of s1
		while (cVertex < mS1.getVertices().length && !mHasIntersection)
		{
			//if vertex is within s2: set
			if (mS2.isWithin (mS1.getVertices()[cVertex]))
			{
				mHasIntersection = true;
				mIntersection = mS1.getVertices()[cVertex];
			}
			//increment
			++cVertex;
		}
	}

	/**
	 * tests whether initial test was executed. If it was not executed the method call will invoke checkForIntersection
	 * to do so.
	 */
	private void runInitialTest()
	{
		if (mIntersection == null)
			checkForIntersection();
	}

	private Solid mS1, mS2;
	private Vector3 mIntersection;
	private boolean mHasIntersection;
}
