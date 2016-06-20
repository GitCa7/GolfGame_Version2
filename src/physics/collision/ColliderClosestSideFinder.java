package physics.collision;
import physics.components.Velocity;
import physics.constants.CompoMappers;
import physics.geometry.ClosestSideFinder;
import physics.geometry.planar.Plane;
import com.badlogic.gdx.math.*;
import physics.geometry.spatial.SolidTranslator;


/**
 * Class finding the plane on which the collision is occurring
 * @author Alexander
 * @author martin
 * Created by Alexander on 22.05.2016.
 */
public class ColliderClosestSideFinder {


    /**
     * @param active the entity considered active, having at least a velocity component
     * @param passive the entity considered passive
     */
    public ColliderClosestSideFinder (ColliderEntity active, ColliderEntity passive)
    {
        mActive = active;
        mPassive = passive;
    }

    /**
     * computes the collision plane. The normal points towards the inside of the passive solid involved in the collision.
     * @return the collision plane
     */
    public Plane find()
    {
        // if one of active's vertices is within passive => active collides with a side of passive
        if (mActive.hasCollidingVertex())
        {
            //return side closest to active's colliding vertex
            ClosestSideFinder sideFinder = new ClosestSideFinder(mPassive.getCollidingSolid());
            Plane collisionPlane = sideFinder.closestSide(mActive.getCollidingVertex());
            return collisionPlane;
        }
        // if active collides with a vertex of passive
        else
        {
            //assume a plane orthogonal to the velocity as collision plane
            Velocity activeVelocity = CompoMappers.VELOCITY.get(mActive.getEntity());
            Vector3 collisionVertex = mPassive.getCollidingVertex();
            return new Plane (activeVelocity.cpy(), collisionVertex.cpy());

        }
    }

    /**
     * Computes the collision plane whose normal points inside the passive solid.
     * Additionally, the plane's normal should point in the same direction as direction.
     * If the strictlySameDirection flag is set, the normal may not be orthogonal, otherwise it may.
     * @param direction direction vector to compare with normal
     * @param strictlySameDirection boolean flag
     * @return the plane containing the closest side satisfying the conditions above.
     */
    public Plane findClosestIntersecting(Vector3 direction, boolean strictlySameDirection)
    {
        // if one of active's vertices is within passive => active collides with a side of passive
        if (mActive.hasCollidingVertex())
        {
            //return side closest to active's colliding vertex
            ClosestSideFinder sideFinder = new ClosestSideFinder(mPassive.getCollidingSolid());
            Plane collisionPlane = sideFinder.closestIntersectingSide(mActive.getCollidingVertex(), direction, strictlySameDirection);
            return collisionPlane;
        }
        // if active collides with a vertex of passive
        else
        {
            //assume a plane orthogonal to the velocity as collision plane
            Velocity activeVelocity = CompoMappers.VELOCITY.get(mActive.getEntity());
            Vector3 collisionVertex = mPassive.getCollidingVertex();
            return new Plane (activeVelocity.cpy(), collisionVertex.cpy());

        }
    }

    ColliderEntity mActive;
    ColliderEntity mPassive;
}
