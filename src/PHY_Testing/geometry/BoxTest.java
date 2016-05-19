package PHY_Testing.geometry;

import com.badlogic.gdx.math.*;

import PHY_Geometry.spatial.Box;

import static org.junit.Assert.*;

import PHY_Geometry.spatial.*;
import org.junit.Test;

import PHY_Testing.ArrayUtil;

import PHY_Geometry.spatial.Solid.SolidException;

public class BoxTest 
{
	/**
	 * @param offset offset vector of box
	 * @param dir1 direction vector of box
	 * @param dir2 direction vector of box, linearly independent from dir1
	 * @param dir3 direction vector of box, linearly independent from dir1, dir2
	 * @return true if the lengths of the vectors match the dimensions of the constructed box
	 */
	public static boolean lengthTest (Vector3 offset, Vector3 dir1, Vector3 dir2, Vector3 dir3)
	{
		float d1 = dir1.len(), d2 = dir2.len(), d3 = dir3.len();
		Box b = new BoxBuilder (offset, ArrayUtil.construct (dir1, dir2, dir3)).build();
		
		float[] dims = b.getDimensions();
		return (dims[0] == d1 && dims[1] == d2 && dims[2] == d3);
	}
	
	
	@Test
	/**
	 * tests whether a box based on standard basis vectors stores the dimensions
	 * matching the direction vectors
	 */
	public void testLengthStandardBasis()
	{
		int dep = 3, wid = 5, hig = 9;
		Vector3 offset = new Vector3 (1, 2, 5);
		Vector3 xDir = new Vector3 (dep, 0, 0);
		Vector3 yDir = new Vector3 (0, wid, 0);
		Vector3 zDir = new Vector3 (0, 0, hig);
		
		assertTrue (lengthTest (offset, xDir, yDir, zDir));
	}
	
	@Test
	/**
	 * tests whether a box not based on standard basis vectors stores the dimensions 
	 * matching the direction vectors
	 */
	public void testLengthCustomBasis()
	{
		Vector3 offset = new Vector3 (5, 7, 2);
		Vector3 d1, d2, d3;
		d1 = new Vector3 (1, 1, 1);
		d2 = new Vector3 (-2, 1, 1);
		d3 = new Vector3 (0, -1, 1);
		
		assertTrue (lengthTest (offset, d1, d2, d3));
	}
	
	
	@Test
	/**
	 * tests whether exception is thrown if the number of direction vectors is not
	 * appropriate
	 */
	public void badNumberOfDirectionVectors()
	{
		Vector3 offset = new Vector3();
		Vector3[] dirs = {new Vector3(), new Vector3()};
		
		boolean thrown = false;
		try
		{
			Box b = new BoxBuilder (offset, dirs).build();
		}
		catch (SolidException se)
		{
			thrown = true;
		}
		
		assertTrue (thrown);
	}
	
	@Test
	/**
	 * tests whether exception is thrown if direction vectors are not mutually exclusive
	 */
	public void nonOrthogonalTest()
	{
		Vector3 offset = new Vector3 (3, 6, 1);
		Vector3 d1, d2, d3;
		d1 = new Vector3 (4, 7, 1);
		d2 = new Vector3 (1, 6, 3);
		d3 = new Vector3 (7, 8, 1);
		
		boolean thrown = false;
		try
		{
			Box b = new BoxBuilder (offset, ArrayUtil.construct (d1, d2, d3)).build();
		}
		catch (SolidException se)
		{
			thrown = true;
		}
		assertTrue (thrown);
	}
	
	@Test
	/**
	 * tests whether each vertex of the box is exactly 3 times a vertex of a side
	 */
	public void sideTest()
	{
		
	}
}
