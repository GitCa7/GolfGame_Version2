package physics.systems;
import com.badlogic.ashley.core.*;
import com.badlogic.gdx.math.*;
import framework.testing.RepositoryEntitySystem;
import physics.collision.*;
import physics.components.Friction;
import physics.components.Velocity;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.constants.PhysicsCoefficients;

import physics.geometry.ClosestSideFinder;
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
    /*
    In general: How does this system interact with the collisionImpactSystem and the CollisionComputer, what does the Collisions Computer do?
    Probably will have to change all of this but nevermind, cuz' I'll know and understand why and how
     */

    public CollisionImpactSystem(){
        mActive = new HashSet<>();
    }


    public CollisionImpactSystem clone()
    {
        return new CollisionImpactSystem();
    }

    public void setRepository(CollisionRepository repository){
        mRepository=repository;
    }

    public void update (float dTime)
    {

        //detect collisions
        ArrayList<ColliderPair<ColliderEntity>> colliding = mRepository.getColliderPairs();

        //for each physics.collision detected
        for (ColliderPair collPair : colliding)
        {
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
        //decompose v into u orthogonal to the plane and w parallel to the plane
        //get collision plane
        Plane closestPlane = new ColliderClosestSideFinder(active, passive).find();
        //Get the velocity
        Velocity currentDirection = CompoMappers.VELOCITY.get(active.getEntity());

        //Constructing the triangle/computing u, w
        VectorProjector mProjector = new VectorProjector(closestPlane.getNormal());
        //u = -v * nu * nu (orthogonal projection, use vector projector)
        Vector3 orthoVec = mProjector.project(currentDirection);
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

    @Override
    public void addedToEngine (Engine e)
    {
        for (Entity add : e.getEntitiesFor (Families.COLLIDING))
        {
            entities().add (add);
            mActive.add (add);
            if (Families.ACCELERABLE.matches (add))
                mActive.add (add);
        }
    }

    public void addEntity(Entity e) {
        if (Families.COLLIDING.matches((e))) {
            entities().add(e);
            mActive.add(e);
            if (Families.ACCELERABLE.matches(e))
                mActive.add(e);
        }
    }


    public void removeEntity(Entity e)
    {
        if (Families.COLLIDING.matches((e))) {
            entities().remove (e);
            mActive.remove (e);
            if (Families.ACCELERABLE.matches (e))
                mActive.remove (e);
        }
    }


    private HashSet<Entity> mActive;
    private ColliderClosestSideFinder mClosestSideFinder;
    private CollisionRepository mRepository;


}

