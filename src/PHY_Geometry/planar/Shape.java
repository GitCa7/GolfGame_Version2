package PHY_Geometry.planar;

import com.badlogic.gdx.math.Vector3;

/**
 * Class modeling a shape in 3 dimensional space. 
 * Objects inheriting from this class implement basic features to access the properties
 * of the respective shape.
 * @author martin
 */
public abstract class Shape 
{
	/**
	 * custom exception class for shapes
	 */
	public static class ShapeException extends IllegalArgumentException
	{
		public ShapeException (String message) { super (message); }
	}

	/**
	 * parametric constructor
	 * @param vertices array of vertices of shape
	 */
	public Shape (Vector3[] vertices, Line[] border)
	{
		mVertices = vertices;
		mBorder = border;
	}
	
	/**
	 * @return vertices stored
	 */
	public Vector3[] getVertices() { return mVertices; }
	
	/**
	 * @return the shape's border as an array of line segments
	 */
	public Line[] getBorder() { return mBorder; }
	
	
	private Vector3[] mVertices;
	private Line[] mBorder;
}
