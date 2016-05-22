package physics.collision;
import com.badlogic.gdx.math.Vector3;
import physics.geometry.spatial.Solid;

/**
 * Created by Alexander on 20.05.2016.
 * stores collidingVector together with a collidingSolid
 */
public class ColliderSolid extends ColliderPair{

    /**
      * @param CollidingSolid Solid involved in collision
      * @param CollidingVector Vector3 that is part of CollidingSolid that causes the collision.
     *
     */


    public ColliderSolid(Vector3 CollidingVector, Solid CollidingSolid){
        super(CollidingVector, CollidingSolid);

    }

    /**
     *
     * @return Solid stored
     */

    public Solid getCollidingSolid(){return mCollidingSolid;}

    /**
     *
     * @return Vector3 stored
     */

    public Vector3 getCollidingVector(){return mCollidingVector;}




    private Vector3 mCollidingVector;
    private Solid mCollidingSolid;
}
