package testing.unit.physics.generic;

import static org.junit.Assert.assertTrue;

import org.junit.*;

import physics.generic.Factorial;

public class FactorialTest 
{
	@Test
	/**
	 * tests for minimum value (0)
	 */
	public void testMin()
	{
		Integer min = 0;
		assertTrue (new Factorial (min).result() == 1);
	}
	
	@Test
	/**
	 * tests for maximum value
	 */
	public void testMax()
	{
		Integer max = Integer.MAX_VALUE;
		
		int expect = 1;
		for (int cnt = 2; cnt < max; ++cnt)
			expect *= cnt;
		expect *= max;
		
		assertTrue (new Factorial (max).result() == expect);
	}
}
