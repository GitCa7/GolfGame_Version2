package PHY_Testing.logic;

import LogicAndExtras.Rounder;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @autor martin
 * created 11.05.2016
 */
public class RoundTester
{



	@Test
	public void testRoundDigits()
	{
		Rounder r = new Rounder (5);

		double number = 5.4328276;
		double expect = 5.43283;

		assertTrue (r.roundDigits (number) == expect);
	}

	@Test
	public void testRoundSignificant()
	{
		Rounder r = new Rounder (5);

		double number = 70.0128462;
		double expect = 70.013;

		assertTrue (r.roundSignificant (number) == expect);
	}

	@Test
	public void testEpsilonEquals()
	{
		Rounder r = new Rounder (5);

		double n1 = 0.016384134;
		double n2 = 0.016378276;
		double n3 = 0.0163727324;

		assertTrue ("numbers should be equal", r.epsilonEquals (n1, n2));
		assertFalse ("not equal", r.epsilonEquals (n1, n3));
	}

	@Test
	public void testSignificantEquals()
	{
		Rounder r = new Rounder (5);

		double n1 = 400.23628273;
		double n2 = 400.2362184;
		double n3 = 400.24001485;

		assertTrue ("numbers equal", r.significanceEquals (n1, n2));
		assertTrue ("numbers not equal", r.significanceEquals (n1, n3));
	}
}
