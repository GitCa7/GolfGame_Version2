package physics.constants;

/**
 * class storing physics related coefficients
 * @author martin
 */
public class PhysicsCoefficients 
{	
	public static final float STATIC_FRICTION = 0.2f;
	public static final float DYNAMIC_FRICTION = 0.4f;
	public static final float SPIN_FRICTION = 0.3f;

	public static final float RESTITUTION_COEFFICIENT = 0.6f;
	
	public static final float TIME_COEFFICIENT = 1f;
	
	public static final float SPIN_COEFFICIENT = 0.75f;
	
	public static final float GRAVITY_EARTH = 9.81f;

	public static final float TERRAIN_THICKNESS = 10000;

	public static final int ARITHMETIC_PRECISION = 5;
	public static final int ARITHMETIC_TOLERANCE = 1;

	public static final int AI_ARITHMETIC_PRECISION = 3;
	public static final int AI_ARITHMETIC_TOLERANCE = 0;

	public static final int MAX_ZERO_UPDATES = 15;
}
