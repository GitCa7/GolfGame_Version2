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


public class NormalForceSystem
{
    public NormalForceSystem(){

        //mDetect = new CollisionDetector();
        mRepo = new CollisionRepository();
    }
    public void update (float dtime)
    {
        //creates a list with collisions
        ArrayList<ColliderPair<ColliderEntity>> collisions = mRepo.getColliderPairs();

            //get collider pairs
            for (ColliderPair p : collisions)
            {
                //for each collider pair
                if(p.getFirst().isActive()){
                //compute
                    Vector3 newV = compute(p.setFirst(), p.setSecond());
                    Force f = CompoMappers.FORCE.get(p.getFirst().getColliding());
                    f.add(newV);

                }
        //apply f on active entity
    }

    private Vector3 compute (ColliderEntity active, ColliderEntity passive)
    {
        //given a force g pushing on surface, normal unit vector nu of this surface
        ColliderClosestSideFinder closestSide = new ColliderClosestSideFinder();
        Plane p = closestSide.find(active, passive);

        // Fg = gravity * mass
        Mass activeMass = CompoMappers.MASS.get(active.getColliding());
        Vector3 Fg = CompoMappers.GRAVITY_FORCE.get(active.getColliding()).cpy().scl(activeMass.mMass);

        // Vector Projection of Fg onto normal Vector of P to get Fn
        VectorProjector vp = new VectorProjector(Fg);
        Vector3 newFg = vp.project(p.getNormal());
        newFg = newFg.scl(-1.00f);

         return newFg;
    }

    private CollisionRepository mRepo;
}