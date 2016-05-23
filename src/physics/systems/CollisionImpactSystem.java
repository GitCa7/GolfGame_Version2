package physics.systems;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.*;
import physics.collision.*;
import physics.components.Force;
import physics.components.Friction;
import physics.constants.CompoMappers;
import physics.constants.PhysicsCoefficients;

import physics.geometry.VectorProjector;
import physics.geometry.spatial.Solid;
import physics.geometry.planar.Plane;
import physics.constants.CompoMappers;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * Created by Alexander on 22.05.2016.
 */
        /*
        Collision detection => repository
        repository => collision impact system
        repository => normal force system
        */

public class CollisionImpactSystem
{

    public CollisionImpactSystem(){
        mActive = new HashSet<>();
    }

    public void update (float dTime)
    {

        //detect collisions
        ArrayList<ColliderPair<ColliderEntity>> colliding = mDetect.getAnyColliding();
        //for each physics.collision detected
        for (ColliderPair collPair : colliding)
        {
            //if entity 1 is active
            if (((ColliderEntity)collPair.mFirst).isActive())
            {
                //compute force excerted
                Entity active = ((ColliderEntity)collPair.mFirst).getColliding();
                CollisionComputer computeImpact = new CollisionComputer (active, ((ColliderEntity)collPair.mSecond).getColliding());
                Vector3 impact = computeImpact.collisionForce();

                assert (CompoMappers.FORCE.has (active));
                Force driving = CompoMappers.FORCE.get (active);
                driving.add (impact);
            }
            //if entity 2 is active
            if (((ColliderEntity)collPair.mSecond).isActive())
            {
                //copute force excerted
                Entity active =((ColliderEntity)collPair.mSecond).getColliding();
                CollisionComputer computeImpact = new CollisionComputer (active, ((ColliderEntity)collPair.mFirst).getColliding());
                Vector3 impact = computeImpact.collisionForce();

                assert (CompoMappers.FORCE.has (active));
                Force driving = CompoMappers.FORCE.get (active);
                driving.add (impact);
            }
        }

    }
        //get collider pairs
        //for each collider pair
        //f = compute
        //apply f on active entity


    private Vector3 compute (ColliderEntity active, ColliderEntity passive, float dTime)
    {
        //given initial velocity v, normal unit vector nu
        //decompose v into u orthogonal to the plane and u parallel to the plane
        ColliderClosestSideFinder mFinder = new ColliderClosestSideFinder();
        Plane closestPlane = mFinder.find(active, passive);
        Vector3 currentDirection = CompoMappers.VELOCITY.get(active.getColliding()).cpy();//Get the force and copy it to avoid changes

        //Constructing the triangle
        VectorProjector mProjector = new VectorProjector(closestPlane.getNormal());
        Vector3 parPlane = mProjector.project(currentDirection);  //u = v * nu * nu (orthogonal projection, use vector projector)
        Vector3 orthoVec = currentDirection.sub(parPlane);   //w = v -u

        //get Stuff
        float activeMass = CompoMappers.MASS.get(active.getColliding()).mMass;
        float friction = CompoMappers.FRICTION.get(active.getColliding()).get(Friction.State.DYNAMIC, Friction.Type.MOVE);
        float restitution = PhysicsCoefficients.RESTITUTION_COEFFICIENT;

        //New force
        Vector3 newDirection = parPlane.scl(friction).sub(orthoVec.scl(restitution)); //v' = f*w - r*u
        Vector3 needToApply = newDirection.sub(currentDirection).scl(activeMass).scl(1/dTime);
        return needToApply;
    }


    private HashSet<Entity> mActive;
    private ColliderClosestSideFinder mClosestSideFinder;
}
