package physics.constants;

import physics.generic.Rounder;

/**
 * class storing various global access objects
 * @author martin
 */
public class GlobalObjects 
{
	public static Rounder ROUND = new Rounder (PhysicsCoefficients.ARITHMETIC_PRECISION, PhysicsCoefficients.ARITHMETIC_TOLERANCE);
}
