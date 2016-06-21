package physics.geometry.planar;

import com.badlogic.gdx.math.Vector3;
import physics.constants.GlobalObjects;
import physics.geometry.VertexGenerator;
import physics.geometry.linear.Line;

/**
 * @autor martin
 * created 08.05.2016
 */
public class RectangleBuilder
{
    /**
     *
     * @param offset offset vector of rectangle to be built
     * @param directions direction vectors of rectangle to be built
     */
    public RectangleBuilder (Vector3 offset, Vector3[] directions)
    {
        mOffset = offset;
        mDirections = directions;

        if (directions.length != Rectangle.INDEPENDENT_VECTORS)
        {
            String message = "too " + (directions.length < Rectangle.INDEPENDENT_VECTORS ? "few" : "many") + " direction vectors";
            throw new Shape.ShapeException (message);
        }

        if (!directionsOrthogonal())
            throw new Shape.ShapeException ("direction vectors not mutually orthogonal");
    }

    /**
     *
     * @return sides of rectangle
     */
    public Line[] border()
    {
        Line[] border = new Line[Rectangle.SIDES];
        //sides belonging to offset
        System.arraycopy (linesOfVertex (mOffset, mDirections), 0, border, 0, Rectangle.SIDES / 2);

        //vertex diagonally opposite to offset
        Vector3 diag = mOffset.cpy();
        for (Vector3 dir : mDirections)
            diag.add (dir);
        //opposite direction vectors
        Vector3[] oppositeDirs = new Vector3[mDirections.length];
        for (int cDir = 0; cDir < oppositeDirs.length; ++cDir)
            oppositeDirs[cDir] = mDirections[cDir].cpy().scl (-1f);
        //sides belonging to opposite vertex
        System.arraycopy (linesOfVertex (diag, oppositeDirs), 0, border, Rectangle.SIDES / 2, Rectangle.SIDES / 2);

        return border;
    }

    /**
     *
     * @return vertices of rectangle
     */
    public Vector3[] vertices()
    {
        return new VertexGenerator (mOffset, mDirections).generate();
    }

    /**
     *
     * @return new rectangle instance holding shared resources
     */
    public Rectangle build()
    {
        return new Rectangle (vertices(), border());
    }

    /**
     * @return true if all vectors are mutually orthogonal
     */
    public boolean directionsOrthogonal()
    {
        for (int cVec1 = 0; cVec1 < mDirections.length; ++cVec1)
        {
            for (int cVec2 = cVec1 + 1; cVec2 < mDirections.length; ++cVec2)
            {
                if (!GlobalObjects.ROUND.epsilonEquals(mDirections[cVec1].dot (mDirections[cVec2]), 0f))
                    return false;
            }
        }
        return true;
    }

    /**
     *
     * @param vertex vertex of rectangle
     * @param dirs direction vectors from vertex
     * @return all lines of rectangle vertex is part of
     */
    private Line[] linesOfVertex (Vector3 vertex, Vector3[] dirs)
    {
        Line[] lines = new Line[Rectangle.SIDES / 2];
        for (int cDir = 0; cDir < dirs.length; ++cDir)
            lines[cDir] = new Line(vertex, vertex.cpy().add(dirs[cDir]));

        return lines;
    }

    private Vector3 mOffset;
    private Vector3[] mDirections;
}
