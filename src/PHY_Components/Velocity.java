package PHY_Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.*;

/**
 * class storing a velocity
 * @author martin
 */
public class Velocity extends Vector3 implements Component 
{
	/**
	 * default constructor: [0,0,0] velocity
	 */
	public Velocity () {}
	
	/**
	 * parametric constructor
	 * @param x x velocity
	 * @param y y velcoity
	 * @param z z velocity
	 */
	public Velocity (float x, float y, float z)
	{
		super (x, y, z);
	}
}
