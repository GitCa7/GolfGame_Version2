package testing.unit.physics.geometry;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import physics.geometry.planar.Triangle;
import physics.geometry.spatial.*;
import org.junit.Test;
import static org.junit.Assert.*;


import java.util.Arrays;
import java.util.HashMap;

/**
 * @autor martin
 * created 12.05.2016
 */
public class TetrahedronTest
{

	/**
	 * @param tetraPoints 4 points defining tetrahedron
	 * @param testPoint point to test
	 * @return true if testPoint is within the tetrahedron, false otherwise
	 */
	public static boolean within (Vector3[] tetraPoints, Vector3 testPoint)
	{
		Tetrahedron tet = new TetrahedronBuilder (tetraPoints).build();
		return tet.isWithin (testPoint);
	}


	@Test
	/** test for exception when passing too few vertices */
	public void testTooFewVertices()
	{
		Vector3[] ps = new Vector3[Tetrahedron.VERTICES - 1];
		ps[0] = new Vector3 (1, 5, 7);
		ps[1] = new Vector3 (1, 7, 2);
		ps[2] = new Vector3 (7, 1, 3);

		boolean thrown = false;
		try
		{
			new TetrahedronBuilder (ps);
		}
		catch (Solid.SolidException se)
		{
			thrown = true;
		}

		assertTrue ("too few vertices should trigger exception", thrown);
	}

	@Test
	/** test for exception when passing too many vertices */
	public void testTooManyVertices()
	{
		Vector3[] ps = new Vector3[Tetrahedron.VERTICES + 1];
		ps[0] = new Vector3 (4, 1, 3);
		ps[1] = new Vector3 (8, 3, 2);
		ps[2] = new Vector3 (8, 2, 43);
		ps[3] = new Vector3 (7, 78, 1);
		ps[4] = new Vector3 (9, 2, 4);

		boolean thrown = false;
		try
		{
			new TetrahedronBuilder (ps);
		}
		catch (Solid.SolidException se)
		{
			thrown = true;
		}

		assertTrue ("too many vertices should trigger exception", thrown);
	}

	@Test
	/** test undefined behavior when passing identical points **/
	public void testIdentical()
	{
		Vector3[] ps = new Vector3[Tetrahedron.VERTICES];
		Arrays.fill (ps, new Vector3());

		try
		{
			new TetrahedronBuilder (ps).build();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@Test
	/**
	 * test whether sides are generated such that each vertex is contained in 3 sides
	 */
	public void sidesTest()
	{
		Vector3[] ps = new Vector3[4];
		ps[0] = new Vector3 (-1, -4, 1.5f);
		ps[1] = new Vector3 (1, 1, 1);
		ps[2] = new Vector3 (-3, .5f, 2);
		ps[3] = new Vector3 (-1, 0, 5);

		Tetrahedron tet = new TetrahedronBuilder (ps).build();
		//put vertices of tetrahedron into map
		HashMap<Vector3, Integer> occurrences = new HashMap<>();
		for (Vector3 vertex : tet.getVertices())
			occurrences.put (vertex, 0);
		//iterate over sides and count occurrences of vertices
		for (Triangle t : tet.getSides())
		{
			for (Vector3 vertex : t.getVertices())
			{
				if (occurrences.containsKey (vertex))
				{
					int before = occurrences.get (vertex);
					occurrences.put (vertex, before + 1);
				}
			}
		}
		//verify frequency
		for (int freq : occurrences.values())
			assertTrue ("each vertex should occur in 3 sides", freq == Tetrahedron.INDEP_VECS);
	}

	@Test
	/** test wether point is inside */
	public void testInTet()
	{
		Vector3[] ps = new Vector3[4];
		ps[0] = new Vector3 (2, 0, 0);
		ps[1] = new Vector3 (0, 3, 0);
		ps[2] = new Vector3 (1, 1.5f, 4);

		Vector3 within = new Vector3 (1, 1.3f, 3f);
		assertTrue ("vertex should be inside tetrahedron", within (ps, within));
	}

	@Test
	/** test for point outside */
	public void testNotInTet()
	{
		Vector3[] ps = new Vector3[Tetrahedron.VERTICES];
		ps[0] = new Vector3 (2, 0, 0);
		ps[1] = new Vector3 (-2, 1, 0);
		ps[2] = new Vector3 (1.5f, 5f, -1f);
		ps[3] = new Vector3 (2.5f, 4f, 3f);

		Vector3 notWithin = new Vector3 (1.5f, 3.5f, 3f);
		assertFalse ("vertex outside tetrahedron", within (ps, notWithin));
	}

	@Test
	/** test for point in triangle */
	public void testInSide()
	{
		Vector3[] ps = new Vector3[Tetrahedron.VERTICES];
		ps[0] = new Vector3 (1, 1, 1);
		ps[1] = new Vector3 (1, 3, 1);
		ps[2] = new Vector3 (-1, 2, 1);
		ps[3] = new Vector3 (0, 2, 4);

		Vector3 withinBaseTriangle = new Vector3 (0, 2, 1);
		assertTrue ("vertex within base should be within tetrahedron", within (ps, withinBaseTriangle));
	}
}

