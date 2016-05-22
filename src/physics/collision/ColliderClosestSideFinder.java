package physics.collision;
import physics.geometry.ClosestSideFinder;
import physics.geometry.spatial.Solid;
import physics.geometry.planar.Plane;


/**
 * Created by Alexander on 22.05.2016.
 */
public class ColliderClosestSideFinder {



    public ColliderClosestSideFinder (ColliderEntity active, ColliderEntity passive)
    {
        mActive=active;
        mPassive=passive;
    }



    public Plane find()
    {
        /*Use ClosestSideFinder here*/

        return null;
    }

    ColliderEntity mActive;
    ColliderEntity mPassive;
}
