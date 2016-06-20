package physics.systems;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.*;
import framework.EntitySystem;
import physics.collision.*;
import physics.components.Force;
import physics.constants.CompoMappers;
import physics.geometry.planar.Plane;
import physics.geometry.VectorProjector;

import java.util.ArrayList;

/**
 * System applying the normal force to all gravity attracted entities.
 * This will cancel out the part of the gravitational force, which would
 * make the entity move through the entity supporting it.
 * Created by marcel on 21.05.2016.
 * @author marcel
 * @author martin
 */

public class NormalForceSystem extends EntitySystem  implements RepositoryEntitySystem
{

    public static boolean DEBUG = false;

    public NormalForceSystem()
    {
        mRepo = null;
    }


    public NormalForceSystem clone()
    {
        NormalForceSystem newSystem = new NormalForceSystem();
        newSystem.setPriority(priority);
        return newSystem;
    }

    public void addEntity (Entity add)
    {

    }

    public void removeEntity(Entity rem)
    {

    }

    public void addedToEngine(Engine e)
    {

    }

    /**
     * Sets the collision repository used to retrieve information about ongoing collisions.
     * This is necessary for the working of the system. This system will only work if there is
     * another client of the repository modifying the information in the repository according to the
     * current state of the game.
     * @param repo the collision repository shared
     */
    public void setRepository(CollisionRepository repo) { mRepo = repo; }

    /**
     * for every entity on which a force can be excerted, the normal force will be added to this entity.
     * This is the vector projection of the opposite current force excerted onto the normal vector supporting the entity.
     * @param dtime time elapsed between this and the last update
     */
    public void update (float dtime)
    {
        if (mRepo == null)
            throw new IllegalStateException("collision repository was not set, system is not operable yet");

        //creates a list with collisions
        ArrayList<ColliderPair<ColliderEntity>> collisions = mRepo.getColliderPairs();

        if (!collisions.isEmpty())
            //System.out.println ("received collision");

        //get collider pairs
        for (ColliderPair<ColliderEntity> p : collisions) {
            //for each collider pair
            if (p.getFirst().isActive()) {
                //compute
                Vector3 newV = compute(p.getFirst(), p.getSecond());
                Force f = CompoMappers.FORCE.get(p.getFirst().getEntity());
                f.add(newV);
                if (DEBUG)
                    debugOut(p.getFirst().getEntity(), newV);
            }
            if (p.getSecond().isActive()) {
                //compute
                Vector3 newV = compute(p.getSecond(), p.getFirst());
                Force f = CompoMappers.FORCE.get(p.getSecond().getEntity());
                f.add(newV);
                //apply f on active entity
                if (DEBUG)
                    debugOut(p.getSecond().getEntity(), newV);
            }
        }
    }


    public void debugOut(Entity appliedTo, Vector3 normalForce)
    {
        System.out.println("apply normal force " + normalForce + " to " + appliedTo + " at " + CompoMappers.POSITION.get(appliedTo));
    }

    private Vector3 compute (ColliderEntity active, ColliderEntity passive)
    {
        //get current force
        Force f = CompoMappers.FORCE.get(active.getEntity());

        //given a force g pushing on surface, normal unit vector nu of this surface
        Plane p = new ColliderClosestSideFinder(active, passive).findClosestIntersecting(f, false);


        //if force points inside passive => f has same direction as p's normal
        if (f.hasSameDirection(p.getNormal()))
        {
            // Vector Projection of Fg onto normal Vector of P to get Fn
            VectorProjector vp = new VectorProjector(p.getNormal());
            return vp.project(f.cpy().scl(-1));
        }

        //otherwise: no normal force applicable
        return new Vector3();
    }

    private CollisionRepository mRepo;
}