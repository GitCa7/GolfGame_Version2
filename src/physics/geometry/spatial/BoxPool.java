package physics.geometry.spatial;

import physics.generic.pool.Pool;

/**
 * singleton class for box pool
 * @author martin
 */
public class BoxPool extends Pool<Box>
{

    static
    {
        mPool = null;
    }

    public static BoxPool getInstance()
    {
        if (mPool == null)
            mPool = new BoxPool();
        return mPool;
    }

    private static BoxPool mPool;
}
