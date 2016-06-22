package physics.vectorUtil;

import com.badlogic.gdx.math.Vector3;
import physics.collision.PruneAndSweep;

/**
 * on 22.06.2016.
 * @author martin
 */
public class YExtractor implements CoordinateExtractor {
    @Override
    public float extract(Vector3 v) {
        return v.y;
    }
}
