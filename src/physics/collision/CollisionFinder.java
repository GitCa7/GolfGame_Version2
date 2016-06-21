package physics.collision;

import physics.components.Body;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Interface for classes returning a list of supposed collisions given a list of bodies on 21.06.2016.
 */
public abstract class CollisionFinder
{
    /**
     * @param bodies the bodies for which to determine the pairs which may be colliding
     */
    public CollisionFinder(Collection<EntityAndBody> bodies)
    {
        mBodies = bodies;
    }

    /**
     * @return the bodies stored
     */
    public Collection<EntityAndBody> getBodies() { return mBodies; }

    public abstract Collection<ColliderPair<ColliderEntity>> possibleCollisions();


    private Collection<EntityAndBody> mBodies;
}
