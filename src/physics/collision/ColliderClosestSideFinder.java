package physics.collision;
import physics.geometry.ClosestSideFinder;
import physics.geometry.spatial.Solid;
import physics.geometry.planar.Plane;
import com.badlogic.gdx.math.*;



/**
 * Created by Alexander on 22.05.2016.
 */
public class ColliderClosestSideFinder {



    public ColliderClosestSideFinder () {}



    public Plane find(ColliderEntity active, ColliderEntity passive)
    {
        //Extract the Solid from the passive Entity
        Solid passiveSolid = mPassive.getCollidingSolid();
        ClosestSideFinder finder = new ClosestSideFinder(passiveSolid);

        //Extract the vector from the active entity
        Vector3 activeVector = mActive.getCollidingVector();

        return finder.closestSide(activeVector);
    }

    ColliderEntity mActive;
    ColliderEntity mPassive;
}
