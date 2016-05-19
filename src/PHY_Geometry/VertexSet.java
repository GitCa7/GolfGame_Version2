package PHY_Geometry;

import java.util.Arrays;
import java.util.HashSet;

import com.badlogic.gdx.math.Vector3;

/**
 * class providing utility for dealing with sets of vertices
 * @autor martin
 * created 09.05.2016
 */
public class VertexSet extends HashSet<Vector3>
{
    /**
     * @param vertices set of vertices to store
     */
    public VertexSet (Vector3[] vertices)
    {
        super (Arrays.asList(vertices));
    }

    /**
     * @return vectors from one arbitrary vertex of the set to every other vertex of this set or
     * an empty array if there are less than 2 elements in the set
     */
    public Vector3[] vectors()
    {
        if (size() <= 1)
            return new Vector3[0];

        Vector3[] vecs = new Vector3[size() - 1];

        Object[] vertices = toArray();
        Vector3 base = (Vector3) vertices[0];
        for (int cVertex = 1; cVertex < vertices.length; ++cVertex)
            vecs[cVertex - 1] = ((Vector3) vertices[cVertex]).cpy().sub (base);

        return vecs;
    }

    /**
     * @param s another set of vertices
     * @return all elements in this which are not contained in s
     */
    public VertexSet notContainedIn (Vector3[] s)
    {
        VertexSet diff = new VertexSet (this.toArray (new Vector3[size()]));
        for (Vector3 elem : s)
            diff.remove (elem);
        return diff;
    }

}
