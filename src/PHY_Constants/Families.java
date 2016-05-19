package PHY_Constants;

import com.badlogic.ashley.core.Family;
import PHY_Components.*;

/**
 * class storing family objects for categorization
 * @author martin
 */
public class Families 
{
	/** family object for moving objects, having a position and velocity component**/
	public static Family MOVING = Family.all (Position.class, Velocity.class).get();
	/** family object for spinning objects*/
	public static Family SPINNING = Family.all (Position.class, Velocity.class, Spin.class).get();
	/** family object for moving objects which have an acceleration component**/
	public static Family ACCELERABLE  = Family.all (Position.class, Velocity.class, Force.class).get();
	/**family object for objects attracted by gravity, having a mass, force and gravity component**/
	public static Family GRAVITY_ATTRACTED = Family.all (Mass.class, Force.class, GravityForce.class).get();
	/**family object for objects affected by friction, having a mass, velocity and gravity component**/
	public static Family FRICTION = Family.all (Friction.class, Mass.class, Velocity.class).get();
	/** family object for objects involved in collisions */
	public static Family COLLIDING = Family.all (Position.class, Mass.class, Body.class).get();

}
