package physics.testing.geometry;

import physics.geometry.planar.Plane;
import org.junit.Test;
import static org.junit.Assert.*;

import com.badlogic.gdx.math.Vector3;

import java.util.Random;

/**
 * @autor martin
 * created 10.05.2016
 */
public class PlaneTester
{
	public static final float EPS = .0001f;
	public static final Random NUMGEN = new Random(System.currentTimeMillis());

	/**
	 *
	 * @param offset offset vector of plane
	 * @param d1 direction vector of plane
	 * @param d2 direction vector of plane
	 * @param minSize minimum size of added points
	 * @param maxSize maximum size of added points
	 * @param minScalar minimum scalar applied to direction vectors
	 * @param maxScalar maximum scalar applied to direction vectors
	 * @return a set of points lying in a plane of size in [minSize + 3, maxSize + 3]
	 */
	public static Vector3[] generatePlaneSet (Vector3 offset, Vector3 d1, Vector3 d2, int minSize, int maxSize, float minScalar, float maxScalar)
	{
		int size = minSize + NUMGEN.nextInt (maxSize - minSize);
		Vector3[] ps = new Vector3[size + 3];
		ps[0] = offset;
		ps[1] = d1;
		ps[2] = d2;


		for (int cPoint = 3; cPoint < ps.length; ++cPoint)
		{
			float s1 = minSize + (maxSize - minSize) * NUMGEN.nextFloat();
			float s2 = minSize + (maxSize - minSize) * NUMGEN.nextFloat();

			Vector3 d1s = d1.cpy().scl (s1);
			Vector3 d2s = d2.cpy().scl (s2);

			ps[cPoint] = offset.cpy().add (d1s).add (d2s);
		}

		return ps;
	}


	@Test
	public void testFormPlane()
	{
		Vector3 offset = new Vector3 (1, 5, 2);
		Vector3 d1 = new Vector3 (9, 4, 3);
		Vector3 d2 = new Vector3 (9, 1, 7);

		Vector3[] ps = new Vector3[6];
		ps[0] = offset.cpy();
		ps[1] = offset.cpy().add (d1.cpy());
		ps[2] = offset.cpy().add (d1.cpy().scl (1.75f));
		ps[3] = offset.cpy().add (d1.cpy().scl (9)).add (d2.cpy().scl (-3));
		ps[4] = offset.cpy().add (d2.cpy().scl (7));
		ps[5] = offset.cpy().add (d2.cpy().scl (.75f)).add (d1.cpy().scl (1.1f));

		boolean noThrow = true;
		try
		{
			Plane plane = new Plane (ps);
		}
		catch (IllegalArgumentException e)
		{
			noThrow = false;
			e.printStackTrace();
		}

		assertTrue (noThrow);
	}


	@Test
	public void testNormal()
	{


		Vector3[] ps = new Vector3[3];
		ps[0] = new Vector3 (1, 4, 2);
		ps[1] = new Vector3 (2, 10, 3);//form 1, 6, 1
		ps[2] = new Vector3 (5, 0, 3);//forms 4, -4, 1

		Vector3 normal = new Vector3 (10, 3, -28);

		Plane plane = new Plane (ps);

		assertTrue (plane.getNormal().hasSameDirection(normal) || plane.getNormal().hasOppositeDirection(normal));
	}

	@Test
	public void testSetNormal()
	{
		Vector3[] ps = new Vector3[3];
		ps[0] = new Vector3 (4, 1, 2);
		ps[1] = new Vector3 (8, 1, 4);
		ps[2] = new Vector3 (9, 4, 6);

		Vector3 above = new Vector3 (9, 2, 6);
		Vector3 below = new Vector3 (9, 6, 4);

		Plane p = new Plane (ps);

		p.setNormalOrientation (above);
		assertTrue (p.testPoint (above) > 0 && p.testPoint (below) < 0);

		p.setNormalOrientation (below);
		assertTrue (p.testPoint (above) < 0 && p.testPoint (below) > 0);
	}




	private int mX;
}
