package physics.geometry.spatial;

/**
 * singleton class for tetrahedron pool
 * @author martin
 */
public class TetrahedronPool extends physics.generic.Pool<TetrahedronParameter>
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
