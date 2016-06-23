package physics.vectorUtil;

import com.badlogic.gdx.math.Vector3;
import physics.collision.PruneAndSweep;

/**
 * on 22.06.2016.
 * @author martin
 */
public class ZExtractor implements CoordinateExtractor
{
    @Override
    public float extract(Vector3 v) {
        return v.z;
    }

    public void set(Vector3 setTo, float value)
    {
        setTo.set(setTo.x, setTo.y, value);
    }
}
