package physics.collision;
import physics.geometry.ClosestSideFinder;
import physics.geometry.spatial.Solid;
import physics.geometry.planar.Plane;
import com.badlogic.gdx.math.*;
import physics.geometry.spatial.SolidTranslator;


/**
 * Created by Alexander on 22.05.2016.
 */
public class ColliderClosestSideFinder {



    public ColliderClosestSideFinder () {}



    public Plane find(ColliderEntity active, ColliderEntity passive)
    {
        //Extract the Solid from the passive Entity
        SolidTranslator passiveSolid = passive.getCollidingSolid();
        ClosestSideFinder finder = new ClosestSideFinder(passiveSolid);

        //Extract the vector from the active entity
        Vector3 activeVector = active.getCollidingVector();

        return finder.closestSide(activeVector);
    }

    ColliderEntity mActive;
    ColliderEntity mPassive;
}
