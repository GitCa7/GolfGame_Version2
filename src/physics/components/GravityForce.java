package physics.components;

import com.badlogic.gdx.math.*;

/**
 * component class modelling gravity
 * @author martin
 */
public class GravityForce extends Vector3 implements Component
{

	/**
	 * @param gravityVec gravity vector to use
	 */
	public GravityForce (Vector3 gravityVec)
	{
		super (gravityVec);
	}

	public GravityForce clone()
	{
		return new GravityForce (this);
	}
}
