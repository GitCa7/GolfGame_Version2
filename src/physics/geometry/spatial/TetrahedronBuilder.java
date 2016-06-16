package physics.geometry.spatial;

import com.badlogic.gdx.math.Vector3;
import physics.generic.PowersetGenerator;
import physics.geometry.planar.Triangle;
import physics.geometry.planar.TriangleBuilder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * class building tetrahedron objects
 * @author martin
 */
public class TetrahedronBuilder
{
	/**
	 *
	 * @param vertices 3 vertices from which to build tetrahedra, the 4th being the origin (0|0|0)
	 */
	public TetrahedronBuilder(Vector3[] vertices)
	{
		
		if (vertices.length < Tetrahedron.VERTICES - 1)
			throw new Solid.SolidException (vertices.length + " vertices is not enough to build a tetrahedron");
		if (vertices.length > Tetrahedron.VERTICES - 1)
			throw new Solid.SolidException (vertices.length + " vertices are too many to build a tetrahedron");

		mVertices = new Vector3[Tetrahedron.VERTICES];
		System.arraycopy(vertices, 0, mVertices, 0, vertices.length);
		mVertices[Tetrahedron.VERTICES - 1] = new Vector3();
	}

	/**
	 *
	 * @return the 4 sides formed by the vertices
	 */
	public Triangle[] sides()
	{

		//get all elements of the power set of 3 elements
		PowersetGenerator<Vector3> genSidePoints = new PowersetGenerator(Arrays.asList (mVertices));

		ArrayList<ArrayList<Vector3>> sidesVertices = genSidePoints.generate (Triangle.VERTICES, Triangle.VERTICES);
		Triangle[] sides = new Triangle[sidesVertices.size()];

		for (int cSide = 0; cSide < sides.length; ++cSide)
		{
			Vector3[] vertices = new Vector3[sidesVertices.get (cSide).size()];
			vertices = sidesVertices.get (cSide).toArray (vertices);
			sides[cSide] = new TriangleBuilder (vertices).build();
		}

		return sides;
	}

	/**
	 *
	 * @return a new tetrahedron object. Tetrahedra built by this object share resources
	 */
	public Tetrahedron build()
	{
		return new Tetrahedron(mVertices, sides());
	}

	private Vector3[] mVertices;
}
