package physics.components;

import com.badlogic.gdx.math.*;
import framework.components.Component;

/**
 * class storing a velocity
 * @author martin
 */
public class Velocity extends Vector3 implements Component
{
	/**
	 * default constructor: [0,0,0] velocity
	 */
	public Velocity ()
	{
		mPrevious = null;
	}
	
	/**
	 * parametric constructor
	 * @param x x velocity
	 * @param y y velcoity
	 * @param z z velocity
	 */
	public Velocity (float x, float y, float z)
	{
		super (x, y, z);
		mPrevious = null;
	}

	public Velocity clone()
	{
		Velocity v = new Velocity();
		v.set (this);
		v.mPrevious = this.mPrevious.cpy();
		return v;
	}
	/**
	 * sets velocity to to and updates the previous field
	 * @param to velocity to set to
	 * @return the new velocity
	 */
	public Vector3 set(Vector3 to)
	{
		mPrevious = this.cpy();
		super.set(to);
		return this;
	}

	/**
	 * adds to to velocity and updates the previous field
	 * @param add velocity to add
	 * @return the new velocity
	 */
	public Vector3 add(Vector3 add)
	{
		mPrevious = this.cpy();
		super.add(add);
		return this;
	}

	/**
	 * subtracts to from velocity and updates the previous field
	 * @param sub velocity to subtract to
	 * @return the new velocity
	 */
	public Vector3 sub(Vector3 sub)
	{
		mPrevious = this.cpy();
		super.sub(sub);
		return this;
	}

	private Vector3 mPrevious;
}
