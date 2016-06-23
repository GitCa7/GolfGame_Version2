package physics.constants;

/**
 * class storing physics related coefficients
 * @author martin
 */
public class PhysicsCoefficients 
{	
	public static final float STATIC_FRICTION = 0.3f;
	public static final float DYNAMIC_FRICTION = 0.12f;
	public static final float SPIN_FRICTION = 0.3f;

	public static final float FRICTION_FLUCTUATION =0.0f;

	public static final float FORCE_TO_MOMENTUM_COEFFICIENT = 1/10f;
	public static final float COLLISION_COEFFICIENT = 12f;

	public static final float RESTITUTION_COEFFICIENT = 0.35f;
	
	public static final float TIME_COEFFICIENT = 1f;

	public static final float SPIN_COEFFICIENT = 0.75f;
	
	public static final float GRAVITY_EARTH = 4.81f;

	public static final float TERRAIN_THICKNESS = 10000;

	public static final float WIND_FREQUENCY = .001f;
	public static final float WIND_MIN_INTENSITY = 10f;
	public static final float WIND_MAX_INTENSITY = 20f;
	public static final int WIND_MIN_DURATION = 10;
	public static final int WIND_MAX_DURATION = 15;

	public static final int MAXIMUM_ARITHMETIC_PRECISION = 4;
	public static final int ARITHMETIC_PRECISION = 3;
	public static final int ARITHMETIC_TOLERANCE = 1;

	public static final int AI_ARITHMETIC_PRECISION = 3;
	public static final int AI_ARITHMETIC_TOLERANCE = 0;

	public static int ODE_STEPS = 100;

	public static final int MAX_ZERO_UPDATES = 50;
}
