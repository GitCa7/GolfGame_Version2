package PHY_Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.*;

public class Spin extends Vector3 implements Component
{
	/**
	 * @return a clone of the spinning vector scaled by the number of revolutions
	 */
	public Vector3 spinVector()
	{
		return super.cpy().scl (mRevolutions);
	}
	
	public float mRevolutions;
}
