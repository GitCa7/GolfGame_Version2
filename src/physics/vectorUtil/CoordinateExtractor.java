package physics.vectorUtil;

import com.badlogic.gdx.math.Vector3;

/**
 * Utilty interface for getting and setting particular coordinate value
 * @author martin
 * on 22.06.2016.
 */
public interface CoordinateExtractor
{
    public float extract(Vector3 v);

    public void set(Vector3 setTo, float value);
}
