package physics.collision.structure;
import com.badlogic.gdx.math.Vector3;
import physics.geometry.spatial.SolidTranslator;

/**
 * Created by Alexander on 20.05.2016.
 * Stores a solid involved in a collision together with one of its vertices that is inside the other solid
 * the stored solid collides with.
 * If a solid is involved in a collision, but none of its vertices is inside the other object, the vertex in set to null.
 */
public class ColliderSolid{

    /**
     * @param collidingSolid Solid involved in collision
     * @param collidingVector Vector3 that is part of CollidingSolid that causes the collision.
     */
    public ColliderSolid(Vector3 collidingVector, SolidTranslator collidingSolid)
    {
        mCollidingSolid=collidingSolid;
        mCollidingVector=collidingVector;


        if (mCollidingVector == null && mCollidingSolid == null)
            mHash = 0;
        else if (mCollidingVector == null)
            mHash = mCollidingSolid.hashCode();
        else if (mCollidingSolid == null)
            mHash = mCollidingVector.hashCode();
        else
            mHash = mCollidingSolid.hashCode() % mCollidingVector.hashCode();
    }

    /**
     * @return Solid stored
     */

    public SolidTranslator getCollidingSolid(){return mCollidingSolid;}

    /**
     * @return Vector3 stored
     */
    public Vector3 getCollidingVertex()
    {
        if (!hasCollidingVertex())
            throw new IllegalStateException("none of this solid's vertices are involved in the collision");
        return mCollidingVector;
    }

    public int hashCode()
    {
        return mHash;
    }

    public boolean equals (Object another)
    {
        ColliderSolid comp = (ColliderSolid) another;

        boolean solidEqual = (mCollidingSolid == null ? comp.mCollidingSolid == null : comp.mCollidingSolid.equals(this.mCollidingSolid));
        if (!solidEqual)
            return false;

        boolean vertexEqual = (mCollidingVector == null ? comp.mCollidingVector == null : comp.mCollidingVector.equals(this.mCollidingVector));

        return solidEqual && vertexEqual;
    }

    /**
     * @return true if this solid has a vertex involved in the collision
     */
    public boolean hasCollidingVertex()
    {
        return (mCollidingVector != null);
    }


    private Vector3 mCollidingVector;
    private SolidTranslator mCollidingSolid;
    private int mHash;
}
