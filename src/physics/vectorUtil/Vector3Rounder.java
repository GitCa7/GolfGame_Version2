package physics.vectorUtil;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import physics.constants.GlobalObjects;
import physics.generic.Rounder;

/**
 * Utility class using the global rounder objects to instantiate a vector
 * whose coordinates are rounded down.
 * created 14.06.16
 *
 * @author martin
 */
public class Vector3Rounder
{

    /**
     * @param rounder rounder object to use to round individual coordinates
     */
    public Vector3Rounder(Rounder rounder)
    {
        mRounder = rounder;
    }

    /**
     * rounds rounding's coordinates and constructs a new Vector3 with them
     * @param rounding the vector to round
     * @return the rounded vector3
     */
    public Vector3 round(Vector3 rounding)
    {
        float x = (float) mRounder.roundDigits(rounding.x);
        float y = (float) mRounder.roundDigits(rounding.y);
        float z = (float) mRounder.roundDigits(rounding.z);
        return rounding.set(x, y, z);
    }




    private Rounder mRounder;
}
