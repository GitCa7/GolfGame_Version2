package physics.entities;

import com.badlogic.ashley.core.Entity;

/**
 * ball entity, tag class
 * @author martin
 */
public class Ball extends Entity 
{
	/**
	 * @param radius a radius
	 * @param density a density constant
	 * @return the mass of a sphere of radius, density
	 */
	public static float mass (float radius, float density)
	{
		return (float) (4 * Math.PI * Math.pow (radius, 3) * density / 4);
	}
	
	/**
	 * constructs ball having no physics.components
	 * note: the position needs to be set manually by adding a position component to this ball
	 */
	public Ball (Entity ballEntity)
	{
		mEntity = ballEntity;
	}

	public Entity mEntity;
}
