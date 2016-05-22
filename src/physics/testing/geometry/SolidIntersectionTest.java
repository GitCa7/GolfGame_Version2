package physics.testing.geometry;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import physics.geometry.spatial.*;
import org.junit.Test;
import physics.testing.ArrayUtil;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @autor martin
 * created 16.05.2016
 */
public class SolidIntersectionTest
{
	//@TODO fix failing tests: check test input first (GeoGebra)

	@Test
	/**
	 * tests wether intersection between two boxes is detected
	 */
	public void positiveBoxesTest()
	{
		Vector3 of1 = new Vector3 (5, 2, 2);
		Vector3 d11 = new Vector3 (4, 2, 0);
		Vector3 d12 = new Vector3 (0, 0, 3);
		Vector3 d13 = new Vector3 (-3, 6, 0);

		Vector3 of2 = new Vector3 (6, 3, 4);
		Vector3 d21 = new Vector3 (-5, 2, 1);
		Vector3 d22 = new Vector3 (2, 5, 0);
		Vector3 d23 = new Vector3 (5, -2, 29);

		Box b1 = new BoxBuilder (of1, ArrayUtil.construct (d11, d12, d13)).build();
		Box b2 = new BoxBuilder (of2, ArrayUtil.construct (d21, d22, d23)).build();

		SolidIntersection siTest = new SolidIntersection (b1, b2);
		assertTrue (siTest.doIntersect());
	}


	@Test
	/**
	 * tests wether intersection between two tetrahedra is detected
	 */
	public void positiveTetrahedraTest()
	{
		Vector3 p11 = new Vector3 (1, 2, 1);
		Vector3 p12 = new Vector3 (4, 5, 1);
		Vector3 p13 = new Vector3 (0, 6, 1);
		Vector3 p14 = new Vector3 (2, 3, 4);

		Vector3 p21 = new Vector3 (3, 3, 2);
		Vector3 p22 = new Vector3 (10, 0, 2);
		Vector3 p23 = new Vector3 (5, 6, 2);
		Vector3 p24 = new Vector3 (5, 4, 5);

		Tetrahedron t1 = new TetrahedronBuilder (ArrayUtil.construct (p11, p12, p13, p14)).build();
		Tetrahedron t2 = new TetrahedronBuilder (ArrayUtil.construct (p21, p22, p23, p24)).build();

		SolidIntersection siTest = new SolidIntersection (t1, t2);
		assertTrue (siTest.doIntersect());
	}


	@Test
	/**
	 * tests wether intersection between a box and a tetrahedron is detected
	 */
	public void positiveBoxTetrahedronTest()
	{
		Vector3 o1 = new Vector3 (2, 2, 2);
		Vector3 d11 = new Vector3 (4, 1, 0);
		Vector3 d12 = new Vector3 (0, 0, 5);
		Vector3 d13 = new Vector3 (1, -4, 0);

		Vector3 p21 = new Vector3 (1.5f, 3, 3);
		Vector3 p22 = new Vector3 (1.5f, 3, 7);
		Vector3 p23 = new Vector3 (3, 3, 4);
		Vector3 p24 = new Vector3 (2, 0, 5);

		Box b1 = new BoxBuilder (o1, ArrayUtil.construct (d11, d12, d13)).build();
		Tetrahedron t2 = new TetrahedronBuilder (ArrayUtil.construct (p21, p22, p23, p24)).build();

		SolidIntersection siTest = new SolidIntersection (b1, t2);
		assertTrue (siTest.doIntersect());
	}

	@Test
	/**
	 * tests whether non-existing intersection is not detected
	 */
	public void negativeBoxesTest()
	{
		Vector3 o1 = new Vector3 (0, 0, 1);
		Vector3 d11 = new Vector3 (-2, 2, 0);
		Vector3 d12 = new Vector3 (2, 2, 0);
		Vector3 d13 = new Vector3 (0, 0, 3);

		Vector3 o2 = new Vector3 (2.5f, 2.5f, 2);
		Vector3 d21 = new Vector3 (1, 0, 0);
		Vector3 d22 = new Vector3 (0, 1, 0);
		Vector3 d23 = new Vector3 (0, 0, 1);

		Box b1 = new BoxBuilder (o1, ArrayUtil.construct (d11, d12, d13)).build();
		Box b2 = new BoxBuilder (o2, ArrayUtil.construct (d21, d22, d23)).build();

		SolidIntersection siTest = new SolidIntersection (b1, b2);
		assertFalse (siTest.doIntersect());
	}

	@Test
	/**
	 * tests whether non-existing intersection is not detected
	 */
	public void negativeTetrahedraTest()
	{
		Vector3 p11 = new Vector3 (0, 0, 0);
		Vector3 p12 = new Vector3 (3, 0, 0);
		Vector3 p13 = new Vector3 (0, 4, 0);
		Vector3 p14 = new Vector3 (0, 0, 4);

		Vector3 p21 = new Vector3 (7, 9, 0);
		Vector3 p22 = new Vector3 (4, 9, 0);
		Vector3 p23 = new Vector3 (7, 5, 0);
		Vector3 p24 = new Vector3 (7, 9, 4);

		Tetrahedron t1 = new TetrahedronBuilder (ArrayUtil.construct (p11, p12, p13, p14)).build();
		Tetrahedron t2 = new TetrahedronBuilder (ArrayUtil.construct (p21, p22, p23, p24)).build();

		SolidIntersection siTest = new SolidIntersection (t1, t2);
		assertFalse (siTest.doIntersect());
	}

	@Test
	/**
	 * tests whether non-existing intersection is not detected
	 */
	public void negativeBoxTetrahedronTest()
	{
		Vector3 o1 = new Vector3 (0, 0, 0);
		Vector3 d11 = new Vector3 (4, 0, 0);
		Vector3 d12 = new Vector3 (0, 0, 1);
		Vector3 d13 = new Vector3 (0, 6, 0);

		Vector3 p21 = new Vector3 (4, 7, 1);
		Vector3 p22 = new Vector3 (6, 7, 6);
		Vector3 p23 = new Vector3 (6, 9, 6);
		Vector3 p24 = new Vector3 (8, 8, 6);

		Tetrahedron t1 = new TetrahedronBuilder (ArrayUtil.construct (p21, p22, p23, p24)).build();
		Box b2 = new BoxBuilder (o1, ArrayUtil.construct (d11, d12, d13)).build();

		SolidIntersection siTest = new SolidIntersection (t1, b2);
		assertFalse (siTest.doIntersect());
	}
}
