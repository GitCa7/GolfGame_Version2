package physics.geometry.spatial;

import com.badlogic.gdx.math.Vector3;
import physics.constants.GlobalObjects;
import physics.geometry.VertexSet;
import physics.geometry.planar.Plane;
import physics.geometry.planar.Triangle;

/**
 * class modelling a tetrahedron
 * @author martin
 */
public class Tetrahedron extends Solid
{
	/** number of vertices */
	public static final int VERTICES = 4;

    /**
     *
     * @param vertices vertices of tetrahedron
     * @param sides sides of tetrahedron
     */
    public Tetrahedron (Vector3[] vertices, Triangle[] sides)
    {
        super (vertices, sides);
    }

	/**
	 * @return sides of the tetrahedron. Note: uses Solid.getSides() and CASTS Shapes to Triangles.
	 */
    public Triangle[] getSides() { return (Triangle[]) super.getSides(); }

	/**
	 * @return the planes containing the faces of the tetrahedron which are thus enclosing the volume of the tetrahedron.
	 * The normal vector of each side points to the fourth vertex of the tetrahedron which is not contained in the plane.
	 */
	public Plane[] enclosingInwardPlanes()
	{
		VertexSet allVertices = new VertexSet (this.getVertices());

		Triangle[] sides = getSides();
		Plane[] planes = new Plane[sides.length];

		for (int cSide = 0; cSide < sides.length; ++cSide)
		{
			//plane containing triangle
			Plane trianglePlane = new Plane (sides[cSide].getVertices());
			//find point not contained in trianglePlane
			VertexSet notInTriangle = allVertices.notContainedIn (sides[cSide].getVertices());
			assert (notInTriangle.size() == 1);
			Vector3 fourth = (Vector3) notInTriangle.toArray()[0];

			trianglePlane.setNormalOrientation (fourth);
			planes[cSide] = trianglePlane;
		}

		return planes;
	}

    /**
     *
     * @param p a point, given by vector
     * @return true if p is within the tetrahedron
     */
    public boolean isWithin (Vector3 p)
    {
		//check plane equation: always positive for inward pointing normals => true
		for (Plane tPlane : enclosingInwardPlanes())
		{
			float d = tPlane.testPoint (p);
			if (!GlobalObjects.ROUND.epsilonEquals (d, 0) && !(d > 0f))
				return false;
		}
		return true;
    }
}
