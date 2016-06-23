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
    public ColliderSolid(Vector3 collidingVector, SolidTranslator collidingSolid){
        mCollidingSolid=collidingSolid;
        mCollidingVector=collidingVector;

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

    public boolean equals (ColliderSolid another)
    {
        return (this.getCollidingVertex().equals(another.getCollidingVertex()) && this.getCollidingSolid().equals(another.getCollidingSolid()));
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
}
