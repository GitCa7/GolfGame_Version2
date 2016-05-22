package physics.geometry.planar;

import com.badlogic.gdx.math.Vector3;

/**
 * class building triangle objects
 * @autor martin
 * created 08.05.2016
 */
public class TriangleBuilder
{
    /**
     * parametric constructor
     * @param vertices vertices to build triangles from
     */
    public TriangleBuilder (Vector3[] vertices)
    {
        mVertices = vertices;

        if (vertices.length != Triangle.VERTICES)
            throw new Shape.ShapeException (vertices.length + " is invalid number of vertices to construct triangle");
    }

    /**
     *
     * @return line segments connecting the vertices stored
     */
    public Line[] border()
    {
        Line[] border = new Line[Triangle.SIDES];
        int cnt = 0;
        for (int cVertex1 = 0; cVertex1 < mVertices.length - 1; ++cVertex1)
        {
            for (int cVertex2 = cVertex1 + 1; cVertex2 < mVertices.length; ++cVertex2)
            {
                border[cnt] = new Line (mVertices[cVertex1], mVertices[cVertex2]);
                ++cnt;
            }
        }
        return border;
    }

    /**
     *
     * @return constructs new triangle object owning shared resources
     */
    public Triangle build()
    {
        return new Triangle (mVertices, border());
    }

    private Vector3[] mVertices;
}
