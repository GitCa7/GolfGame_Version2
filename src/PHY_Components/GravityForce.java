package PHY_Components;
import com.badlogic.ashley.core.*;
import com.badlogic.gdx.math.*;

/**
 * component class modelling gravity
 * @author martin
 */
public class GravityForce extends Vector3 implements Component
{
	/**
	 * class used to build gravity objects
	 * @author martin
	 */
	public static class Builder
	{
		/**
		 * @param gravityConstant gravity constant to use\n
		 * gravity objects constructed will have a magnitude of gravityConstant
		 */
		public Builder (float gravityConstant)
		{
			mGravityConstant = gravityConstant;
		}
		
		/**
		 * @return gravity object being a vector of [0, 0, -gravityConstant]
		 */
		public GravityForce get (float mass)
		{
			float[] gravityVec = {0, 0, -mGravityConstant * mass};
			return (new GravityForce (gravityVec));
		}
		
		private float mGravityConstant;
	}
	
	/**
	 * @param gravityVec gravity vector to use
	 */
	private GravityForce (float[] gravityVec)
	{
		super (gravityVec);
	}
}
