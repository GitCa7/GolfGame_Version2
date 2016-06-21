package physics.geometry.spatial;

import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

import com.badlogic.gdx.math.Vector3;
import physics.geometry.planar.Plane;
import physics.geometry.planar.Shape;

/**
 * class modeling a solid in 3d space.
 * Every solid needs to pass a list of vertices and a list of sides.
 * A solid shall be immutable.
 * @author martin
 *
 */
public abstract class Solid
{
	/** number of independent vectors spanning solid */
	public static final int INDEP_VECS = 3;


	/**
	 * class to be thrown if invalid arguments are passed to a method
	 * of a solid object.
	 * @author martin
	 */
	public Solid(){}
	public static class SolidException extends IllegalArgumentException
	{
		public SolidException (String message) { super (message); }
	}
	
	/**
	 * parametric constructor
	 * @param vertices vertices of solid
	 */
	public Solid (Vector3[] vertices, Shape[] sides)
	{
		mVertices = vertices;
		mSides = sides;
		mSidePlanes = new Plane[sides.length];
		init();
	}
	
	/**
	 * @return vertices stored
	 */
	public Vector3[] getVertices() { return mVertices; }
	
	/**
	 * @return sides of solid
	 */
	public Shape[] getSides() { return mSides; }

	/**
	 * @return the planes in which the sides are located, the normals pointing inwards
     */
	public Plane[] getSidePlanes() { return mSidePlanes; }

	/**
	 * @return the center vertex of this solid
     */
	public Vector3 getCenter() { return mCenter; }

	/**
	 * @param p a point, given by vector
	 * @return true if p is within the solid, false otherwise
	 */
	public abstract boolean isWithin (Vector3 p);

	/**
	 * tests equality for solids
	 * @param comp solid to compare with
	 * @return true if all vertices of this are vertices of comp and all vertices of comp are contained in this
	 */
	public boolean equals (Solid comp)
	{
		TreeSet<Vector3> tVertices = new TreeSet<> (Arrays.asList (mVertices));
		TreeSet<Vector3> compVertices = new TreeSet<> (Arrays.asList (comp.getVertices()));

		return (tVertices.containsAll (compVertices) && compVertices.containsAll (tVertices));
	}


	private void init()
	{
		setPlanes();
		setCenter();
	}

	/**
	 * instantiates plane instances for every side and norms their normals inwards
	 */
	private void setPlanes()
	{
		for (int cPlane = 0; cPlane < mSides.length; ++cPlane)
		{
			HashSet<Vector3> sideVertices = new HashSet<>(Arrays.asList(mSides[cPlane].getVertices()));
			int cVertexNotInPlane = 0;
			while (cVertexNotInPlane < mVertices.length && sideVertices.contains(mVertices[cVertexNotInPlane]))
				++cVertexNotInPlane;

			mSidePlanes[cPlane] = new Plane(mSides[cPlane].getVertices());
			mSidePlanes[cPlane].setNormalOrientation(mVertices[cVertexNotInPlane]);
		}
	}

	/**
	 * sets the center vertex
	 */
	private void setCenter()
	{
		mCenter = new Vector3();
		for (Vector3 vertex : mVertices)
			mCenter.add(vertex);
		mCenter.scl(1f / mVertices.length);
	}
	
	
	private Vector3[] mVertices;
	private Shape[] mSides;
	/** planes containing the sides with normal pointing inwards */
	private Plane[] mSidePlanes;
	/** average of all points */
	private Vector3 mCenter;
}
