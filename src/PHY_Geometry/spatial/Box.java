package PHY_Geometry.spatial;

import com.badlogic.gdx.math.Vector3;
import PHY_Geometry.planar.Rectangle;

/**
 * class modelling a solid of 6 rectangular sides
 * @author martin
 */
public class Box extends Solid
{
    /** number of points needed to define a box */
	public static final int	DEFINING_POINTS = 4;
    /** number of sides of a box */
	public static final int SIDES = 6;
    /** number of vertices of a box */
	public static final int VERTICES = 8;


    /**
	 * @param offset offset vector
	 * @param defining array of exactly 3 vectors, each representing a vertex connected
	 * to offset. Thus the difference of each vector of defining and offset needs to be orthogonal
	 * to every other difference.
     * @param vertices vertices of box.
     * @param sides sides of box
	 */
	public Box (Vector3 offset, Vector3[] defining, Vector3[] vertices, Rectangle[] sides)
	{
		//generate vertices from offset and directions
		super (vertices, sides);

		mOffset = offset;
		mDirections = defining;
		mDimensions = new float[DEFINING_POINTS - 1];
		for (int cDir = 0; cDir < mDirections.length; ++cDir)
			mDimensions[cDir] = mDirections[cDir].len();
	}

	/**
	 * @return rectangle objects forming this box' sides.
	 */
	public Rectangle[] getSides()
	{
        return (Rectangle[]) super.getSides();
	}
	
	/**
	 * @return the set vectors whose unscaled linear combinations, added to the offset, 
	 * yield any vertex of this box 
	 */
	public Vector3[] getBasis() { return mDirections; }
	
	/**
	 * @return array of lengths of the sides
	 */
	public float[] getDimensions() { return mDimensions; }
	
	/**
	 * @return the box's offset vector
	 */
	public Vector3 getOffset() { return mOffset; }
	
	/**
	 * @return the point diagonally opposite to the offset point. 
	 * Note: needs to be computed, is constructed every time anew.
	 */
	public Vector3 diagonalOffset()
	{
		Vector3 diag = mOffset.cpy();
		for (Vector3 dir : mDirections)
			diag.add (dir);
		return diag;
	}
	
	/**
	 * @param point some point
	 * @return true if point is within this box
	 */
	public boolean isWithin (Vector3 point)
	{
		Vector3 diagonal = diagonalOffset();
		
		float minX, maxX, minY, maxY, minZ, maxZ;
		minX = Math.min (getOffset().x, diagonal.x);
		maxX = Math.max (getOffset().x, diagonal.x);
		minY = Math.min (getOffset().y, diagonal.y);
		maxY = Math.max (getOffset().y, diagonal.y);
		minZ = Math.min (getOffset().z, diagonal.z);
		maxZ = Math.max (getOffset().z, diagonal.z);
		
		return (point.x > minX && point.x < maxX && 
				point.y > minY && point.y < maxY && 
				point.z > minZ && point.z < maxZ);
	}
	
	private Vector3 mOffset;
	private Vector3[] mDirections;
	private float[] mDimensions;
}
