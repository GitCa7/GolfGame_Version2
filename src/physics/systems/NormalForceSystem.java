package physics.systems;
import com.badlogic.gdx.math.*;
import physics.collision.*;
import physics.components.Force;
import physics.constants.CompoMappers;
import physics.components.*;
import physics.constants.PhysicsCoefficients;
import physics.geometry.planar.Plane;
import physics.geometry.VectorProjector;

import java.util.ArrayList;

import static physics.constants.PhysicsCoefficients.GRAVITY_EARTH;
/** Creates NormalForce System to impelemnt Gravity
 * Created by marcel on 21.05.2016.
 */

public class NormalForceSystem
{
    public NormalForceSystem(){

        //mDetect = new CollisionDetector();
        mRepo = new CollisionRepository();
    }
    public void update (float dtime) {
        //creates a list with collisions
        ArrayList<ColliderPair<ColliderEntity>> collisions = mRepo.getColliderPairs();

        //get collider pairs
        for (ColliderPair<ColliderEntity> p : collisions) {
            //for each collider pair
            if (p.getFirst().isActive()) {
                //compute
                Vector3 newV = compute(p.getFirst(), p.getSecond());
                Force f = CompoMappers.FORCE.get(p.getFirst().getEntity());
                f.add(newV);

            }
            if (p.getSecond().isActive()) {
                //compute
                Vector3 newV = compute(p.getSecond(), p.getFirst());
                Force f = CompoMappers.FORCE.get(p.getSecond().getEntity());
                f.add(newV);
                //apply f on active entity
            }
        }
    }

    private Vector3 compute (ColliderEntity active, ColliderEntity passive)
    {
        //given a force g pushing on surface, normal unit vector nu of this surface
        ColliderClosestSideFinder closestSide = new ColliderClosestSideFinder();
        Plane p = closestSide.find(active, passive);

        // Fg = gravity * mass
        Mass activeMass = CompoMappers.MASS.get(active.getEntity());
        Vector3 Fg = CompoMappers.GRAVITY_FORCE.get(active.getEntity()).cpy().scl(activeMass.mMass);

        // Vector Projection of Fg onto normal Vector of P to get Fn
        VectorProjector vp = new VectorProjector(Fg);
        Vector3 newFg = vp.project(p.getNormal());
        newFg = newFg.scl(-1.00f);

         return newFg;
    }

    private CollisionRepository mRepo;
}