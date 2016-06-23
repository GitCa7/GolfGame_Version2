package physics.vectorUtil;

import com.badlogic.gdx.math.Vector3;

/**
 * on 22.06.2016.
 * @author martin
 */
public class YExtractor implements CoordinateExtractor
{
    @Override
    public float extract(Vector3 v) {
        return v.y;
    }

    public void set(Vector3 setTo, float value)
    {
        setTo.set(setTo.x, value, setTo.z);
    }
}
