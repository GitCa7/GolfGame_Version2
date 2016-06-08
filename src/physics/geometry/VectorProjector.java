package physics.geometry;
import com.badlogic.gdx.math.Vector3;


/**
 * Created by Alexander on 22.05.2016.
 * Computes a vector projection
 */

public class VectorProjector {

    /**
     *
     * @param projectOn Vector3 that another Vector3 is projected upon
     */

    public VectorProjector (Vector3 projectOn)
    {
        mProjectOn = projectOn.cpy().nor();
    }

    /**
     *
     * @param another Vector3 that is projected upon mProjectOn.
     * @return return the projected Vector3
     */

    public Vector3 project (Vector3 another)
    {
        float lengthProjected =another.dot(mProjectOn);
        Vector3 projectedVector= mProjectOn.cpy().scl(lengthProjected);
        return projectedVector;
    }

    private Vector3 mProjectOn;
}





