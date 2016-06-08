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
	//@TODO fix tetrahedra issue
	@Test
	/**
	 * tests whether intersection between two boxes is detected
	 */
	public void positiveBoxesTest()
	{
		Vector3 of1 = new Vector3 (5, 2, 2);
		Vector3 d11 = new Vector3 (4, 2, 0);
		Vector3 d12 = new Vector3 (0, 0, 3);
		Vector3 d13 = new Vector3 (-3, 6, 0);

		Vector3 of2 = new Vector3 (7, 5, 3.5f);//should be inside!
		Vector3 d21 = new Vector3 (5, 0, 0);
		Vector3 d22 = new Vector3 (0, 2, -3);
		Vector3 d23 = new Vector3 (0, 3, 2);


		Box b1 = new BoxBuilder (ArrayUtil.construct (d11, d12, d13)).build();
		Box b2 = new BoxBuilder (ArrayUtil.construct (d21, d22, d23)).build();

		SolidTranslator tb1 = new SolidTranslator(b1, of1);
		SolidTranslator tb2 = new SolidTranslator(b2, of2);

		SolidIntersection siTest = new SolidIntersection (tb1, tb2);
		assertTrue (siTest.doIntersect());
	}


	@Test
	/**
	 * tests wether intersection between two tetrahedra is detected
	 */
	public void positiveTetrahedraTest()
	{
		Vector3 p12 = new Vector3 (3, 3, 0);
		Vector3 p13 = new Vector3 (-1, 4, 0);
		Vector3 p14 = new Vector3 (1, 1, 3);
		Vector3 of1 = new Vector3(1, 2, 1);

		Vector3 p22 = new Vector3 (7, -3, 0);
		Vector3 p23 = new Vector3 (2, 3, 0);
		Vector3 p24 = new Vector3 (2, 1, 3);
		Vector3 of2 = new Vector3 (3, 3, 2);

		Tetrahedron t1 = new TetrahedronBuilder (ArrayUtil.construct (p12, p13, p14)).build();
		Tetrahedron t2 = new TetrahedronBuilder (ArrayUtil.construct (p22, p23, p24)).build();

		SolidTranslator tt1 = new SolidTranslator(t1, of1);
		SolidTranslator tt2 = new SolidTranslator(t2, of2);

		SolidIntersection siTest = new SolidIntersection (tt1, tt2);
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

		Vector3 o2 = new Vector3 (1.5f, 3, 3);
		Vector3 p22 = new Vector3 (0, 0, 4);
		Vector3 p23 = new Vector3 (1.5f, 0, 1);
		Vector3 p24 = new Vector3 (.5f, -3, 2);

		Box b1 = new BoxBuilder (ArrayUtil.construct (d11, d12, d13)).build();
		Tetrahedron t2 = new TetrahedronBuilder (ArrayUtil.construct (p22, p23, p24)).build();

		SolidTranslator tb1 = new SolidTranslator(b1, o1);
		SolidTranslator tt2 = new SolidTranslator(t2, o2);

		SolidIntersection siTest = new SolidIntersection (tb1, tt2);
		assertTrue (siTest.doIntersect());
	}

	@Test
	/**
	 * tests whether non-existing intersection is not detected
	 */
	public void negativeBoxesTest()
	{
		Vector3 of1 = new Vector3 (0, 0, 1);
		Vector3 d11 = new Vector3 (-2, 2, 0);
		Vector3 d12 = new Vector3 (2, 2, 0);
		Vector3 d13 = new Vector3 (0, 0, 3);

		Vector3 of2 = new Vector3 (2.5f, 2.5f, 2);
		Vector3 d21 = new Vector3 (1, 0, 0);
		Vector3 d22 = new Vector3 (0, 1, 0);
		Vector3 d23 = new Vector3 (0, 0, 1);

		Box b1 = new BoxBuilder (ArrayUtil.construct (d11, d12, d13)).build();
		Box b2 = new BoxBuilder (ArrayUtil.construct (d21, d22, d23)).build();

		SolidTranslator tb1 = new SolidTranslator(b1, of1);
		SolidTranslator tb2 = new SolidTranslator(b2, of2);

		SolidIntersection siTest = new SolidIntersection (tb1, tb2);
		assertFalse (siTest.doIntersect());
	}

	@Test
	/**
	 * tests whether non-existing intersection is not detected
	 */
	public void negativeTetrahedraTest()
	{
		Vector3 o1 = new Vector3 (0, 0, 0);
		Vector3 p12 = new Vector3 (3, 0, 0);
		Vector3 p13 = new Vector3 (0, 4, 0);
		Vector3 p14 = new Vector3 (0, 0, 4);

		Vector3 o2 = new Vector3 (7, 9, 0);
		Vector3 p22 = new Vector3 (-3, 0, 0);
		Vector3 p23 = new Vector3 (0, -4, 0);
		Vector3 p24 = new Vector3 (0, 0, 4);

		Tetrahedron t1 = new TetrahedronBuilder (ArrayUtil.construct (p12, p13, p14)).build();
		Tetrahedron t2 = new TetrahedronBuilder (ArrayUtil.construct (p22, p23, p24)).build();

		SolidTranslator tt1 = new SolidTranslator(t1, o1);
		SolidTranslator tt2 = new SolidTranslator(t2, o2);

		SolidIntersection siTest = new SolidIntersection (tt1, tt2);
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

		Vector3 o2 = new Vector3 (4, 7, 1);
		Vector3 p22 = new Vector3 (2, 0, 5);
		Vector3 p23 = new Vector3 (2, 2, 5);
		Vector3 p24 = new Vector3 (4, 1, 5);

		Tetrahedron t1 = new TetrahedronBuilder (ArrayUtil.construct (p22, p23, p24)).build();
		Box b2 = new BoxBuilder (ArrayUtil.construct (d11, d12, d13)).build();

		SolidTranslator tt1 = new SolidTranslator(t1, o1);
		SolidTranslator tb2 = new SolidTranslator(b2, o2);

		SolidIntersection siTest = new SolidIntersection (tt1, tb2);
		assertFalse (siTest.doIntersect());
	}
}
