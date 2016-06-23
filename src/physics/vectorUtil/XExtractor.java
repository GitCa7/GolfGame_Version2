package physics.vectorUtil;

import com.badlogic.gdx.math.Vector3;

/**
 * on 22.06.2016.
 * @author martin
 */
public class XExtractor implements CoordinateExtractor
{
    @Override
    public float extract(Vector3 v) {
        return v.x;
    }

    public void set(Vector3 setTo, float value)
    {
        setTo.set(value, setTo.y, setTo.z);
    }


}
