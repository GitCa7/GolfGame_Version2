package framework.internal.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import framework.constants.Families;
import framework.internal.components.Busy;
import physics.components.Force;
import physics.components.Velocity;
import physics.constants.CompoMappers;
import framework.EntitySystem;

import java.util.HashSet;
import java.util.Iterator;

/**
 * class updating busy components of global objects
 * @author martin
 */
public class BusySystem extends EntitySystem
{

    public BusySystem()
    {
        mMoving = new HashSet<>();
    }


    public BusySystem clone()
    {
        return new BusySystem();
    }

    public void addedToEngine (Engine e)
    {
        for (Entity add : e.getEntitiesFor(Families.GLOBAL_STATE))
            super.entities().add (add);

        for (Entity add : e.getEntitiesFor(physics.constants.Families.MOVING))
            mMoving.add (add);
    }


    public void addEntity(Entity e)
    {
        if (Families.GLOBAL_STATE.matches(e))
            entities().add(e);
        if (physics.constants.Families.MOVING.matches(e))
            mMoving.add(e);

    }

    public void removeEntity(Entity e)
    {
        if (Families.GLOBAL_STATE.matches(e))
            entities().remove(e);
        if (physics.constants.Families.MOVING.matches(e))
            mMoving.remove(e);
    }

    /**
     * sets all busy components to true if there is an object moving or to false if there is none
     * @param dT delta time
     */
    public void update (float dT)
    {
        //search for moving entity
        boolean detectedMove = false;
        Iterator<Entity> iMoving = mMoving.iterator();
        while (!detectedMove && iMoving.hasNext())
        {
            Entity m = iMoving.next();
            Velocity v = CompoMappers.VELOCITY.get(m);
            Force f = CompoMappers.FORCE.get(m);
            if (v.len() > mMovementThreshold || f.len() > mMovementThreshold)
                detectedMove = true;
        }

        //set result of search in busy components
        for (Entity gs : entities())
        {
            Busy b = framework.constants.CompoMappers.BUSY.get(gs);
            b.mBusy = detectedMove;
        }
    }

    /**
     * sets the threshold defining which objects are still moving
     * @param threshold minimum magnitude of velocity vector
     */
    public void setThreshold(float threshold)
    {
        mMovementThreshold = threshold;
    }

    private HashSet<Entity> mMoving;
    private float mMovementThreshold;
}
