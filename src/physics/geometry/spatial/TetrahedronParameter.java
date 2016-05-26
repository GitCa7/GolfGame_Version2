package physics.geometry.spatial;

import com.badlogic.gdx.math.Vector3;
import physics.generic.Parameter;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Alexander on 24.05.2016.
 */
public class TetrahedronParameter implements Parameter<Tetrahedron>
{

    /**
     *
     * @param defining vectors to 4 vertices defining the tetrahedron
     */
    public TetrahedronParameter(Vector3 ... defining)
    {
        mDefining=defining;
        norm();

        mBuilder = new TetrahedronBuilder(defining);

    }
    public Tetrahedron instantiate() {
        return mBuilder.build();
    }

    public Vector3[] getDefining(){return mDefining;}

    public boolean equals(TetrahedronParameter para) {
        HashSet<Vector3> set1= new HashSet<>(Arrays.asList(mDefining));
        return set1.containsAll(Arrays.asList(para.mDefining));
    }

    /**
     *
     * @param vertices array of vertices
     * @return the vertex in vertices the closest to the origin
     */
    private Vector3 findClostestToOrigin (Vector3[] vertices)
    {
        Vector3 closest = null;
        float closestDistance = Float.MAX_VALUE;

        for (Vector3 v : vertices)
        {
            float length = v.len();
            if (closestDistance > length)
            {
                closest = v;
                closestDistance = length;
            }
        }
        return closest;
    }

    /**
     * subtracts the closest vertex from all vertices in order to normalize the tetrahedron
     * with respect to the origin.
     */
    private void norm()
    {
        Vector3 closest = findClostestToOrigin(mDefining);
        for (Vector3 v : mDefining)
            v.sub(closest);
    }

    private TetrahedronBuilder mBuilder;
    private Vector3[] mDefining;
}
