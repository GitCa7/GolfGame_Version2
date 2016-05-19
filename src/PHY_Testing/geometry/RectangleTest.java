package PHY_Testing.geometry;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;

import com.badlogic.gdx.math.Vector3;

import PHY_Geometry.planar.*;
import PHY_Testing.ArrayUtil;

public class RectangleTest 
{
    /**
     * @param debug flag for debug output
     * @param offset offset vector of rectangle
     * @param dirs direction vector of rectangle
     * @return true if each vertex occurs twice
     */
    public static boolean sidesTest (boolean debug, Vector3 offset, Vector3... dirs)
    {
        Rectangle r = new RectangleBuilder (offset, dirs).build();

        HashMap<Vector3, Integer> countVertex = new HashMap<>();
        for (Vector3 vertex : r.getVertices())
            countVertex.put (vertex, 0);

        for (Line side : r.getBorder())
        {
            if (countVertex.containsKey (side.getStart()))
                countVertex.put (side.getStart(), countVertex.get (side.getStart()) + 1);
            else
            {
                if (debug)
                    System.out.println ("start of line " + side.getStart() + " not in vertices " + r.getVertices());
                return false;
            }

            if (countVertex.containsKey (side.getEnd()))
                countVertex.put (side.getEnd(), countVertex.get (side.getEnd()) + 1);
            else
            {
                if (debug)
                    System.out.println("end of line " + side.getEnd() + " not in vertices " + r.getVertices());
                return false;
            }
        }

        for (Vector3 vertex : r.getVertices())
        {
            if (countVertex.get (vertex) != Rectangle.INDEPENDENT_VECTORS)
            {
                if (debug)
                    System.out.println(vertex + " occurs " + countVertex.get(vertex) + " times");
                return false;
            }
        }

        return true;
    }

    @Test
    /**
     * test rectangle's side for standard basis
     */
    public void testSidesStandardBasis()
    {
        boolean debug = true;

        Vector3 offset = new Vector3 (0, 5, 1);
        Vector3 xDir = new Vector3 (4, 0, 0);
        Vector3 yDir = new Vector3 (0, 3, 0);

        assertTrue (sidesTest (debug, offset, xDir, yDir));
    }

    @Test
    /**
     * test rectangle's side for non-standard basis
     */
    public void testSidesRotatedBasis()
    {
        boolean debug = false;

        Vector3 offset = new Vector3 (4, 1, 87);
        Vector3 d1 = new Vector3 (1, 1, 0);
        Vector3 d3 = new Vector3 (-5, 5, 0);

        assertTrue (sidesTest (debug, offset, d1, d3));
    }

    @Test
    /**
     * test for exception if more than 2 direction vectors are passed
     */
    public void testNumberDirVecsThrowUp()
    {
        Vector3 offset = new Vector3();
        Vector3 d1 = new Vector3();
        Vector3 d2 = new Vector3();
        Vector3 d3 = new Vector3();

        boolean thrown = false;
        try
        {
            RectangleBuilder b = new RectangleBuilder(offset, ArrayUtil.construct(d1, d2, d3));
        }
        catch (Shape.ShapeException se)
        {
            thrown = true;
        }

        assertTrue ("exception needs to be thrown if number of dir vectors is invalid", thrown);
    }

    @Test
    /**
     * test for non mutually orthogonal direction vectors
     */
    public void testNonOrthogonalThrowup()
    {
        Vector3 offset = new Vector3();
        Vector3 d1 = new Vector3 (1, 2, 3);
        Vector3 d2 = new Vector3 (5, 1, 8);

        boolean thrown = false;
        try
        {
            RectangleBuilder b = new RectangleBuilder(offset, ArrayUtil.construct(d1, d2));
        }
        catch (Shape.ShapeException se)
        {
            thrown = true;
        }

        assertTrue ("exception needs to be thrown if dir vecs are not mutually orthogonal", thrown);
    }
}
