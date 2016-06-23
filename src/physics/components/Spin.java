package physics.components;

import com.badlogic.gdx.math.*;
import framework.components.Component;

public class Spin extends Vector3 implements Component
{
	/**
	 * @return a clone of the spinning vector scaled by the number of revolutions
	 */
	public Vector3 spinVector()
	{
		return super.cpy().scl (mRevolutions);
	}


	public Spin clone()
	{
		Spin sp = new Spin();
		sp.set (this);
		return sp;
	}


	public float mRevolutions;
}
