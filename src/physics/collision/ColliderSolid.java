package physics.collision;
import com.badlogic.gdx.math.Vector3;
import physics.geometry.spatial.Solid;
import physics.geometry.spatial.SolidTranslator;

/**
 * Created by Alexander on 20.05.2016.
 * stores collidingVector together with a collidingSolid
 */
public class ColliderSolid{

    /**
      * @param collidingSolid Solid involved in collision
      * @param collidingVector Vector3 that is part of CollidingSolid that causes the collision.
     *
     */


    public ColliderSolid(Vector3 collidingVector, SolidTranslator collidingSolid){
        mCollidingSolid=collidingSolid;
        mCollidingVector=collidingVector;

    }

    /**
     *
     * @return Solid stored
     */

    public SolidTranslator getCollidingSolid(){return mCollidingSolid;}

    /**
     *
     * @return Vector3 stored
     */

    public Vector3 getCollidingVector(){return mCollidingVector;}




    private Vector3 mCollidingVector;
    private SolidTranslator mCollidingSolid;
}
