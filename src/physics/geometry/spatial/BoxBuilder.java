package physics.geometry.spatial;

import com.badlogic.gdx.math.Vector3;
import physics.generic.PowersetGenerator;
import physics.geometry.VertexGenerator;
import physics.geometry.planar.Rectangle;
import physics.geometry.planar.RectangleBuilder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * class building box objects
 * @author martin
 */
public class BoxBuilder
{

    /**
     * @param vertex a vertex of this box
     * @param dirs direction vectors defining the box from vertex on
     * @return all sides vertex is part of
     */
    public static Rectangle[] sidesOfVertex (Vector3 vertex, Vector3[] dirs)
    {
        //generate 2 vector power set
        PowersetGenerator<Vector3> gen2 = new PowersetGenerator<Vector3> (Arrays.asList (dirs));
        Rectangle[] rects = new Rectangle[Box.SIDES / 2];

        //for each element of 2 vector power set
        int cnt = 0;
        for (ArrayList<Vector3> elem : gen2.generate(2, 2))
        {
            //construct rectangle
            Vector3[] rectDirs = elem.toArray (new Vector3[elem.size()]);
            rects[cnt] = new RectangleBuilder (vertex, rectDirs).build();
        }

        return rects;
    }

    /**
     * parametric constructor
     * @param offset offset vector
     * @param directions direction vectors, given such that every point of the box can be obtained by
     *                   a non-scaled linear combination of the direction vectors
     */
    public BoxBuilder (Vector3 offset, Vector3[] directions)
    {
        mOffset = offset;
        mDirections = directions;

        if (mDirections.length != Box.DEFINING_POINTS - 1)
        {
            String message = "too " + (mDirections.length < Box.DEFINING_POINTS - 1 ? "few" : "many") + " direction vectors";
            throw new Solid.SolidException (message);
        }
        if (!formOrthogonal())
            throw new Solid.SolidException ("direction vectors are not mutually orthogonal");
    }

    /**
     * @return the sides belonging to the box data stored
     */
    public Rectangle[] sides()
    {
        int halfSides = Box.SIDES / 2;
        //generate rectangles belonging to offset
        Rectangle[] sides = new Rectangle[Box.SIDES];
        System.arraycopy (sidesOfVertex (mOffset, mDirections), 0, sides, 0, halfSides);

        //diagonally opposite point
        Vector3 diag = mOffset.cpy();
        for (Vector3 dir : mDirections)
            diag.add (dir);
        //generate vectors opposite to basis
        Vector3[] oppositeBasis = new Vector3[mDirections.length];
        for (int cBasis = 0; cBasis < mDirections.length; ++cBasis)
            oppositeBasis[cBasis] = mDirections[cBasis].cpy().scl (-1f);
        System.arraycopy (sidesOfVertex (diag, oppositeBasis), 0, sides, halfSides, halfSides);

        return sides;
    }

    /**
     * @return the vertices belonging to the box data stored
     */
    public Vector3[] vertices()
    {
        VertexGenerator gen = new VertexGenerator (mOffset, mDirections);
        return gen.generate();
    }

    /**
     * @return a new box object
     */
    public Box build()
    {
        return new Box (mOffset, mDirections, vertices(), sides());
    }

    /**
     * @return true if the direction vectors stored form a mutually orthogonal basis
     */
    public boolean formOrthogonal()
    {
        for (int nCompare = 0; nCompare < mDirections.length; ++nCompare)
        {
            for (int nComparing = 0; nComparing < mDirections.length; ++nComparing)
            {
                if (nCompare != nComparing && mDirections[nCompare].dot (mDirections[nComparing]) != 0)
                    return false;
            }
        }
        return true;
    }

    private Vector3 mOffset;
    private Vector3[] mDirections;
}
