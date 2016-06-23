package physics.components;

import com.badlogic.gdx.math.*;
import framework.components.Component;

/**
 * class storing a position
 * @author martin
 */
public class Position extends Vector3 implements Component
{
	public Position ()
	{
		mPrevious = null;
	}

	public Position (float x, float y, float z)
	{
		super (x, y, z);
		mPrevious = null;
	}

	public Position clone()
	{
		Position p = new Position (this.x, this.y, this.z);
		p.mPrevious = this.mPrevious.cpy();
		return p;
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

	public Vector3 mPrevious;
}