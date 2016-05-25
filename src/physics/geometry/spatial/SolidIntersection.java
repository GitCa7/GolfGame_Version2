package physics.geometry.spatial;

import com.badlogic.gdx.math.Vector3;
import physics.collision.ColliderPair;
import physics.collision.ColliderSolid;

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
		mIntersection1 = null;
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
		solidCollision=new ColliderPair<ColliderSolid>(cS1,cS2);
		int cVertex = 0;
		int cVertex1 = 0;
		mHasIntersection = false;
		//iterate over vertices of s1
		while (cVertex < mS1.getVertices().length && !mHasIntersection)
		{
			//if vertex is within s2: set
			if (mS2.isWithin (mS1.getVertices()[cVertex]))
			{
				mHasIntersection = true;
				mIntersection = mS1.getVertices()[cVertex];
				cS1 =new ColliderSolid(mIntersection,mS2);
				solidCollision.setFirst(cS1);
			}
			//increment
			++cVertex;
		}

		while (cVertex < mS2.getVertices().length && !mHasIntersection) {
			//if vertex is within s2: set
			if (mS1.isWithin(mS2.getVertices()[cVertex])) {
				mHasIntersection = true;
				mIntersection1 = mS2.getVertices()[cVertex];
				cS2 = new ColliderSolid(mIntersection1, mS1);
				solidCollision.setSecond(cS2);
			}
			//increment
			++cVertex1;
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

	public boolean ismHasIntersection(){return mHasIntersection;}
	public Vector3 getMIntersection(){return mIntersection;}
	public Vector3 getMIntersection1(){return mIntersection1;}
	public ColliderPair<ColliderSolid> getSolidCollision(){return solidCollision;}

	private ColliderPair<ColliderSolid> solidCollision;
	private ColliderSolid cS1;
	private ColliderSolid cS2;
	private Solid mS1, mS2;
	private Vector3 mIntersection;
	private Vector3 mIntersection1;
	private boolean mHasIntersection;

}
