package physics.geometry.spatial;

import com.badlogic.gdx.math.Vector3;
import physics.geometry.VectorProjector;
import physics.geometry.planar.Rectangle;

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
		//vector from offset ot point
		Vector3 offsetPoint = point.cpy().sub(mOffset);
		//projections of offsetPoint on direction vectors 1, 2
		Vector3 proj1 = new VectorProjector(mDirections[0]).project(offsetPoint);
		Vector3 proj2 = new VectorProjector(mDirections[1]).project(offsetPoint);
		Vector3 proj3 = new VectorProjector(mDirections[2]).project(offsetPoint);
		//check dot products to be between 0 and the square of length of direction vectors
		float dot1 = proj1.dot(mDirections[0]);
		float dot2 = proj2.dot(mDirections[1]);
		float dot3 = proj3.dot(mDirections[2]);
		return (0 <= dot1 && dot1 <= mDirections[0].dot(mDirections[0]) &&
				0 <= dot2 && dot2 <= mDirections[0].dot(mDirections[0]) &&
				0 <= dot3 && dot3 <= mDirections[0].dot(mDirections[0]));
	}
	
	private Vector3 mOffset;
	private Vector3[] mDirections;
	private float[] mDimensions;
}
