package physics.geometry.planar;

import com.badlogic.gdx.math.Vector3;

/**
 * Class modeling a line segement. Stores the endpoints of the segment only.
 * @author martin
 */
public class Line 
{
	/**
	 * parametric constructor
	 * @param start vector to start point
	 * @param end vector to end point
	 */
	public Line (Vector3 start, Vector3 end)
	{
		mStart = start;
		mEnd = end;
	}
	
	/**
	 * @return start vector stored
	 */
	public Vector3 getStart() { return mStart; }
	
	/**
	 * @return end vector stored
	 */
	public Vector3 getEnd() { return mEnd; }
	
	/**
	 * @return the difference between start and end
	 */
	public Vector3 direction()
	{
		return mEnd.cpy().sub (mStart);
	}
	
	private Vector3 mStart, mEnd;
}
