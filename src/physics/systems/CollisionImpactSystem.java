package physics.systems;
import com.badlogic.ashley.core.*;
import com.badlogic.gdx.math.*;
import physics.collision.*;
import physics.components.Friction;
import physics.components.Velocity;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.constants.GlobalObjects;
import physics.constants.PhysicsCoefficients;

import physics.geometry.VectorProjector;
import physics.geometry.planar.Plane;

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

public class CollisionImpactSystem extends framework.EntitySystem implements RepositoryEntitySystem
{

    public static boolean DEBUG = false;


    /*
    In general: How does this system interact with the collisionImpactSystem and the CollisionComputer, what does the Collisions Computer do?
    Probably will have to change all of this but nevermind, cuz' I'll know and understand why and how
     */

    public CollisionImpactSystem(){
        mActive = new HashSet<>();
    }


    public CollisionImpactSystem clone()
    {
        CollisionImpactSystem newSystem = new CollisionImpactSystem();
        newSystem.setPriority(priority);
        return newSystem;
    }

    public void setRepository(CollisionRepository repository){
        mRepository=repository;
    }

    public void update (float dTime)
    {
        //tolerance for collision impact
        float epsilon = (float) GlobalObjects.ROUND.getEpsilon();
        //detect collisions
        ArrayList<ColliderPair<ColliderEntity>> colliding = mRepository.getColliderPairs();

        //for each physics.collision detected
        for (ColliderPair collPair : colliding)
        {
            //do this elsewhere (i.e. needed for collisions
            //if (	(pair.getFirst().isActive() && !testCloseness(pair.getFirst(), pair.getSecond(), epsilon) ||
            //		(pair.getSecond().isActive() && !testCloseness(pair.getSecond(), pair.getFirst(), epsilon))))

            //if entity 1 is active
            if (((ColliderEntity)collPair.mFirst).isActive())
            {
                //Compute new Force
                ColliderEntity active = (ColliderEntity)collPair.mFirst;
                ColliderEntity passive= (ColliderEntity)collPair.mSecond;
                Vector3 forceToBeApplied = compute(active,passive,dTime);
                //Update Force
                Vector3 currentForce= CompoMappers.FORCE.get(active.getEntity());
                currentForce.add(forceToBeApplied);
                if (DEBUG && forceToBeApplied.len() > GlobalObjects.ROUND.getEpsilon())
                    debugOut(active.getEntity(), forceToBeApplied, passive.getEntity());
            }
            //if entity 2 is active
            if (((ColliderEntity)collPair.mSecond).isActive())
            {
                //Compute Force
                ColliderEntity active = (ColliderEntity)collPair.mSecond;
                ColliderEntity passive= (ColliderEntity)collPair.mFirst;
                Vector3 forceToBeApplied = compute(active,passive,dTime);
                //Update Force
                Vector3 currentForce= CompoMappers.FORCE.get(active.getEntity());
                currentForce.add(forceToBeApplied);

                if (DEBUG && forceToBeApplied.len() > GlobalObjects.ROUND.getEpsilon())
                    debugOut(active.getEntity(), forceToBeApplied, passive.getEntity());
            }
        }

    }

    /**
     * @param active the entity deemed active
     * @param passive the entity deemed passive
     * @param dTime time elapsed between the last update and this update
     * @return the force to be applied to account for the impact of the collision
     */
    private Vector3 compute (ColliderEntity active, ColliderEntity passive, float dTime)
    {

        //given initial velocity v, normal unit vector nu
        //decompose v into u orthogonal to the plane and w parallel to the plane
        //get collision plane
        Plane closestPlane = new ColliderClosestSideFinder(active, passive).find();
        //Get the velocity
        Velocity currentDirection = CompoMappers.VELOCITY.get(active.getEntity());

        if (currentDirection.hasSameDirection(closestPlane.getNormal()))
        {
            //Constructing the triangle/computing u, w
            VectorProjector mProjector = new VectorProjector(closestPlane.getNormal());
            //u = -v * nu * nu (orthogonal projection, use vector projector)
            Vector3 orthoVec = mProjector.project(currentDirection);

            //only apply collision force if active moves towards passive
            if (orthoVec.len() > GlobalObjects.ROUND.getEpsilon()) {
                //w = v - u
                Vector3 paraVec = currentDirection.cpy().sub(orthoVec);

                //get Stuff
                float activeMass = CompoMappers.MASS.get(active.getEntity()).mMass;
                float friction = CompoMappers.FRICTION.get(active.getEntity()).get(Friction.State.DYNAMIC, Friction.Type.MOVE);
                float restitution = PhysicsCoefficients.RESTITUTION_COEFFICIENT;

                //New force
                //v' = f*w - r*u
                Vector3 newDirection = paraVec.scl(friction).sub(orthoVec.scl(restitution));
                //F = m / dt * dv, dv = v' - v => F = m / dt * (v' - v)
                Vector3 needToApply = newDirection.sub(currentDirection).scl(activeMass / dTime);
                return needToApply;
            }
        }

        //if active does not move towards passive
        return new Vector3();
    }

    @Override
    public void addedToEngine (Engine e)
    {

    }

    public void addEntity(Entity e) {

    }


    public void removeEntity(Entity e)
    {

    }


    public void debugOut(Entity appliedTo, Vector3 force, Entity obstacle)
    {
        System.out.print("apply force " + force);
        System.out.print(" to entity " + appliedTo);
        System.out.print(" moving at v = " + CompoMappers.VELOCITY.get(appliedTo));
        System.out.print(" at position s = " + CompoMappers.POSITION.get(appliedTo));
        System.out.print(" due to collision with " + obstacle);
        System.out.print(" at " + CompoMappers.POSITION.get(obstacle));
        System.out.println();
    }

    private boolean testCloseness(ColliderEntity active, ColliderEntity passive, float eps)
    {
        ColliderClosestSideFinder findSide = new ColliderClosestSideFinder(active, passive);
        Plane closest = findSide.find();
        if (closest.testPoint(active.getCollidingVertex()) < eps)
            return true;
        return false;
    }

    private HashSet<Entity> mActive;
    private ColliderClosestSideFinder mClosestSideFinder;
    private CollisionRepository mRepository;


}

