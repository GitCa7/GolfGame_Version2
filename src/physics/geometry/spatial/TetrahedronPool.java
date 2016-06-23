package physics.geometry.spatial;

import physics.generic.pool.Pool;

/**
 * singleton class for tetrahedron pool
 * @author martin
 */
public class TetrahedronPool extends Pool<Tetrahedron>
{

    static
    {
        mPool = null;
    }


    public static TetrahedronPool getInstance()
    {
        if (mPool == null)
            mPool = new TetrahedronPool();
        return mPool;
    }

    private static TetrahedronPool mPool;
}
