package physics.geometry.spatial;

import com.badlogic.gdx.math.Vector3;
import physics.collision.structure.ColliderPair;
import physics.collision.structure.ColliderSolid;

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
	public SolidIntersection (SolidTranslator s1, SolidTranslator s2)
	{
		mS1 = s1;
		mS2 = s2;
		solidCollision = null;
		mHasIntersection = false;
	}

	/**
	 * @throws IllegalStateException if there is no intersection
	 * @return the collider pair storing information about intersection
     */
	public ColliderPair<ColliderSolid> getSolidCollision(){return solidCollision;}



	/**
	 * Precondition: the method checkForIntersection was called once
	 * @return true if the two solids stored intersect
	 */
	public boolean doIntersect()
	{
		return mHasIntersection;
	}

	/**
	 * searches for intersection. If an intersection was found, the solid collider is set.
	 * Note: the colliding vertex of at most one element of the collider pair may be null.
	 */
	public void checkForIntersection()
	{
		Vector3[] vertices1 = mS1.getVertices();
		Vector3[] vertices2 = mS2.getVertices();
		ColliderSolid colliderS1 = null, colliderS2 = null;
		int cVertex = 0;
		mHasIntersection = false;
		//iterate over vertices of s1
		while (cVertex < vertices1.length && colliderS1 == null)
		{
			//if vertex is within s2: set
			if (mS2.isWithin (vertices1[cVertex]))
				colliderS1 = new ColliderSolid(vertices1[cVertex],mS1);
			//increment
			++cVertex;
		}


		cVertex = 0;
		while (cVertex < vertices2.length && colliderS2 == null)
		{
			//if vertex is within s2: set
			if (mS1.isWithin(vertices2[cVertex]))
				colliderS2 = new ColliderSolid(vertices2[cVertex], mS2);
			//increment
			++cVertex;
		}

		//set collision
		if (colliderS1 != null || colliderS2 != null)
		{
			if (colliderS1 == null)
				colliderS1 = new ColliderSolid(null, mS1);
			else if (colliderS2 == null)
				colliderS2 = new ColliderSolid(null, mS2);
			solidCollision = new ColliderPair<>(colliderS1, colliderS2);
			mHasIntersection = true;
		}
	}

	private ColliderPair<ColliderSolid> solidCollision;
	private SolidTranslator mS1, mS2;
	private boolean mHasIntersection;
}
