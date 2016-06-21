package physics.geometry.planar;

import com.badlogic.gdx.math.Vector3;
import physics.constants.GlobalObjects;

/**
 * class modeling a plane in 3d space. Stores plane as equation of the form n1*x1 + n2*x2 + n3*x3 = 0.
 * Provides utility for checking the relative positon of a point, computing the normal vector etc.
 *
 * @autor martin
 * created 10.05.2016
 */

public class Plane
{

	/**
	 * @param points array of vectors representing points
	 * @return two vectors, being the difference of 3 distinct points in points, such that these two are linearly independent.
	 * The last entry is null if all points lie on a line.
	 */
	public static Vector3[] getTwoLinearlyIndependent (Vector3[] points)
	{
		Vector3[] indep = new Vector3[2];

		for (int cVec = 1; cVec < points.length; ++cVec)
		{
			if (indep[0] == null && !points[0].epsilonEquals (points[cVec], (float) GlobalObjects.ROUND.getEpsilon()))
				indep[0] = points[cVec].cpy().sub (points[0]);
			else
			{
				Vector3 diffVec = points[cVec].cpy().sub (points[0]);
				//cosine of angle between indep[0] and diffVec
				float eps = (float) GlobalObjects.ROUND.getEpsilon();
				if (!diffVec.isOnLine(indep[0], eps))
				{
					indep[1] = diffVec;
					return indep;
				}
			}
		}

		return indep;
	}

	/**
	 * construct plane from a normal vector and an offset vector
	 * @param normal normal vector
	 * @param offset offset vector
     */
	public Plane (Vector3 normal, Vector3 offset)
	{
		mNormal = normal;
		mOffset = offset;

		mNormal.nor();
		setDistance();
	}

	/**
	 * construct plane from a set of at least 3 points.
	 * Automatically computes the normal and offset.
	 * @param points array of points being in the plane of 3 or more elements
	 * @throws IllegalArgumentException if there is a point in plane which does not belong to the same plane as the others
	 */
	public Plane (Vector3[] points)
	{
		//if not enough points
		if (points.length < 3)
			throw new IllegalArgumentException ("too few points to construct a plane: " + points.length);

		Vector3[] indep = getTwoLinearlyIndependent (points);

		//if points lie on a line
		if (indep[1] == null)
		{
			getTwoLinearlyIndependent(points);
			throw new IllegalArgumentException("points lie on a line/are identical");
		}

		mOffset = points[0];
		setNormal (indep[0], indep[1]);
		setDistance();

		if (!inPlane (points))
			throw new IllegalArgumentException ("points lie in a 3d space");
	}

	/**
	 * @return the normal vector stored
	 */
	public Vector3 getNormal() { return mNormal; }

	/**
	 * @return the offset vector of the plane
     */
	public Vector3 getOffset() { return mOffset; }

	/**
	 * @param p some point
	 * @return 0 if p is in the plane, > 0 if p is not in the plane and pointing the same direction as the normal,
	 * < 0 otherwise
	 */
	public float testPoint (Vector3 p)
	{
		return (p.cpy().sub(mOffset).dot (mNormal));
	}

	/**
	 * @param points vector of points which should lie in the plane
	 * @return true if all points lie in the plane
	 */
	public boolean inPlane (Vector3[] points)
	{
		for (Vector3 p : points)
		{
			if (!GlobalObjects.ROUND.epsilonEquals (testPoint (p), 0f))
				return false;
		}
		return true;
	}

	/**
	 * @param p a point not in the plane
	 * Postcondition: sets the normal vector such that the dot product of the normal and p is positive
	 */
	public void setNormalOrientation (Vector3 p)
	{
		float dotProd = testPoint (p);
		if (!GlobalObjects.ROUND.epsilonEquals (dotProd, 0f) && dotProd < 0)
			mNormal.scl (-1f);
	}

	/**
	 * @return the smallest distance of the plane to the origin
     */
	public float getDistanceToOrigin(){return mDistanceOrigin;}

	/**
	 * @param point an arbitrary point
	 * @return the distance to the plane
     */
	public float getDistanceToPlane(Vector3 point)
	{
		return mNormal.dot(point) + getDistanceToOrigin();
	}

	/**
	 * @param v1 a vector in the plane, lin. independent from v2
	 * @param v2 a vector in the plane, lin independent from v1
	 * Postcondition: sets normal vector to the cross product of v1, v2
	 */
	private void setNormal (Vector3 v1, Vector3 v2)
	{
		mNormal = v1.cpy();
		mNormal.crs (v2);
		mNormal.nor();
	}


	private void setDistance()
	{
		mDistanceOrigin = -mNormal.dot(mOffset);
	}


	private Vector3 mOffset, mNormal;
	private float mDistanceOrigin;
}
