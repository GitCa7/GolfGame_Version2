package physics.collision;

import physics.components.Body;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Interface for classes returning a list of supposed collisions given a list of bodies on 21.06.2016.
 */
public abstract class BroadCollisionFinder implements Cloneable
{

    public BroadCollisionFinder()
    {
        mAllBodies = null;
        mMovingBodies = null;
    }

    public abstract BroadCollisionFinder clone();

    /**
     * @return all bodies stored
     */
    public Collection<EntityAndBody> getAllBodies() { return mAllBodies; }

    /**
     * @return the moving bodies stored
     */
    public Collection<EntityAndBody> getMovingBodies() { return mMovingBodies; }

    /**
     * @return at least all collisions between any moving entity and other entity (static/moving).
     * May return incomplete collider pairs only storing an entity and the associated body.
     */
    public abstract Collection<ColliderPair<ColliderEntity>> possibleCollisions();

    /**
     * sets the bodies to find collisions for
     * @param all all bodies to check
     * @param moving only moving bodies
     */
    public void setBodies(Collection<EntityAndBody> all, Collection<EntityAndBody> moving)
    {
        mAllBodies = all;
        mMovingBodies = moving;
    }

    protected ColliderEntity getIncompleteCollider(EntityAndBody entity)
    {
        ColliderSolid colliderSolid = new ColliderSolid(null, null);
        ColliderBody colliderBody = new ColliderBody(entity.mBody, colliderSolid);
        return new ColliderEntity(entity.mEntity, colliderBody);
    }

    private Collection<EntityAndBody> mAllBodies, mMovingBodies;
}
