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

    ColliderEntity mActive;
    ColliderEntity mPassive;
}
