package physics.collision;
import com.badlogic.gdx.math.Vector3;
import physics.geometry.spatial.Solid;

/**
 * Created by Alexander on 20.05.2016.
 */
public class ColliderSolid extends ColliderPair{


    public ColliderSolid(Vector3 CollidingVector, Solid CollidingSolid){
        super(CollidingVector, CollidingSolid);

    }


    public Solid getCollidingSolid(){return mCollidingSolid;}

    public Vector3 getCollidingVector(){return mCollidingVector;}




    private Vector3 mCollidingVector;
    private Solid mCollidingSolid;
}
