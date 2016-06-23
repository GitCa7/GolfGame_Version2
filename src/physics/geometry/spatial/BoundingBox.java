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

    public float getLowestX() {
        float min = mBoundingBox.getSides()[0].getVertices()[0].x;
        for (int i = 1; i < mBoundingBox.getSides().length; i++) {
            for (int j = 0; j < mBoundingBox.getSides().length; j++) {
                if (min>mBoundingBox.getSides()[i].getVertices()[i].x){
                    min = mBoundingBox.getSides()[i].getVertices()[i].x;
                }
            }
        }
        return  min;
    }

    public float getHighestX() {
        float max = mBoundingBox.getSides()[0].getVertices()[0].x;
        for (int i = 1; i < mBoundingBox.getSides().length; i++) {
            for (int j = 0; j < mBoundingBox.getSides().length; j++) {
                if (max<mBoundingBox.getSides()[i].getVertices()[i].x){
                    max = mBoundingBox.getSides()[i].getVertices()[i].x;
                }
            }
        }
        return  max;
    }

    public float getLowestY() {
        float min = mBoundingBox.getSides()[0].getVertices()[0].y;
        for (int i = 1; i < mBoundingBox.getSides().length; i++) {
            for (int j = 0; j < mBoundingBox.getSides().length; j++) {
                if (min>mBoundingBox.getSides()[i].getVertices()[i].y){
                    min = mBoundingBox.getSides()[i].getVertices()[i].y;
                }
            }
        }
        return  min;
    }

    public float getHighestY() {
        float max = mBoundingBox.getSides()[0].getVertices()[0].y;
        for (int i = 1; i < mBoundingBox.getSides().length; i++) {
            for (int j = 0; j < mBoundingBox.getSides().length; j++) {
                if (max<mBoundingBox.getSides()[i].getVertices()[i].y){
                    max = mBoundingBox.getSides()[i].getVertices()[i].y;
                }
            }
        }
        return  max;
    }
    public float getLowestZ() {
        float min = mBoundingBox.getSides()[0].getVertices()[0].z;
        for (int i = 1; i < mBoundingBox.getSides().length; i++) {
            for (int j = 0; j < mBoundingBox.getSides().length; j++) {
                if (min>mBoundingBox.getSides()[i].getVertices()[i].z){
                    min = mBoundingBox.getSides()[i].getVertices()[i].z;
                }
            }
        }
        return  min;
    }
    public float getHighestZ() {
        float max = mBoundingBox.getSides()[0].getVertices()[0].z;
        for (int i = 1; i < mBoundingBox.getSides().length; i++) {
            for (int j = 0; j < mBoundingBox.getSides().length; j++) {
                if (max<mBoundingBox.getSides()[i].getVertices()[i].z){
                    max = mBoundingBox.getSides()[i].getVertices()[i].z;
                }
            }
        }
        return  max;
    }
    private SolidTranslator mBoundingBox;
}
