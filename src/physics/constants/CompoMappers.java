package physics.constants;

import com.badlogic.ashley.core.*;

import physics.components.*;


/**
 * class storing component mappers
 */
public class CompoMappers 
{
	//static properties
	public static final ComponentMapper<Mass> MASS = ComponentMapper.getFor (Mass.class);
	public static final ComponentMapper<Body> BODY = ComponentMapper.getFor (Body.class);
	//position
	public static final ComponentMapper<Position> POSITION = ComponentMapper.getFor (Position.class);
	//velocity
	public static final ComponentMapper<Velocity> VELOCITY = ComponentMapper.getFor (Velocity.class);
	public static final ComponentMapper<Spin> SPIN = ComponentMapper.getFor (Spin.class);
	//acceleration
	public static final ComponentMapper<Force> FORCE = ComponentMapper.getFor (Force.class);
	public static final ComponentMapper<GravityForce> GRAVITY_FORCE = ComponentMapper.getFor (GravityForce.class);
	//friction
	public static final ComponentMapper<Friction> FRICTION = ComponentMapper.getFor(Friction.class);
	//wind
	public static final ComponentMapper<Wind> WIND = ComponentMapper.getFor(Wind.class);
}
