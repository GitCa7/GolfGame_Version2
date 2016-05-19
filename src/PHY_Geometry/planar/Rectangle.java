package PHY_Geometry.planar;

import com.badlogic.gdx.math.Vector3;

/**
 * class modeling a rectangle in 3 dimensional space
 * @author martin
 */
public class Rectangle extends Shape
{
	public static int INDEPENDENT_VECTORS = 2;
	/** number of vertices */
	public static final int VERTICES = 2;
	/** number of sides */
	public static final int SIDES = 4;
	
	/**
	 * @param vertices offset of rectangle
	 * @param border array of exactly 2 mutually orthogonal vectors. Each point
	 * of the rectangle should be obtainable by a linear combination of the offset and the
	 * unscaled direction vectors.
	 */
	public Rectangle (Vector3[] vertices, Line[] border)
	{
		super (vertices, border);
	}

}
