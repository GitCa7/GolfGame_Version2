package physics.geometry.spatial;

import com.badlogic.gdx.math.Vector3;

import java.util.Collection;

/**
 * created 20.06.16
 *
 * @author martin
 */
public class BoundingBox
{

    public BoundingBox(Collection<SolidTranslator> solidsToBound)
    {
        mBoundingBox = computeBound(solidsToBound);
    }

    public SolidTranslator getBoundingBox()
    {
        return mBoundingBox;
    }

    private SolidTranslator computeBound(Collection<SolidTranslator> solidsToBound)
    {
        Vector3 min = new Vector3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        Vector3 max = new Vector3(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);

        for (SolidTranslator bound : solidsToBound)
        {
            Vector3[] vertices = bound.getVertices();
            for (Vector3 v : vertices)
                updateMinMax(min, max, v);
        }

        Vector3 d = new Vector3(max.x - min.x, 0, 0);
        Vector3 h = new Vector3(0, max.y - min.y, 0);
        Vector3 w = new Vector3(0, 0, max.z - min.z);
        BoxParameter bp = new BoxParameter(d, h, w);
        return new SolidTranslator(BoxPool.getInstance().getInstance(bp), min);
    }


    private void updateMinMax(Vector3 min, Vector3 max, Vector3 v)
    {
        if (v.x < min.x)
            min.x = v.x;
        if (v.y < min.y)
            min.y = v.y;
        if (v.z < min.z)
            min.z = v.z;

        if (v.x > max.x)
            max.x = v.x;
        if (v.y > max.y)
            max.y = v.y;
        if (v.z > max.z)
            max.z = v.z;
    }



    private SolidTranslator mBoundingBox;
}
