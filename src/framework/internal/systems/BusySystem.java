package framework.internal.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import framework.constants.Families;
import framework.internal.components.Busy;
import physics.components.Force;
import physics.components.Position;
import physics.components.Velocity;
import physics.constants.CompoMappers;
import framework.EntitySystem;
import physics.constants.GlobalObjects;
import physics.constants.PhysicsCoefficients;
import physics.generic.History;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * class updating busy components of global objects
 * @author martin
 */
public class BusySystem extends EntitySystem
{

    public BusySystem()
    {
        mMoving = new LinkedList<>();
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
            mMoving.add (new EntityPositionHistory(add));
    }


    public void addEntity(Entity e)
    {
        if (Families.GLOBAL_STATE.matches(e))
            entities().add(e);
        if (physics.constants.Families.MOVING.matches(e))
            mMoving.add(new EntityPositionHistory(e));

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
        Iterator<EntityPositionHistory> iMoving = mMoving.iterator();
        while (!detectedMove && iMoving.hasNext())
        {
            EntityPositionHistory m = iMoving.next();
            m.update();
            if (!testForInactivity(m))
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

    /**
     * @param entity entity to test
     * @return true if the entity's position did not change throughout its history stored
     */
    private boolean testForInactivity(EntityPositionHistory entity)
    {
        if (entity.hasSpace())
            return false;

        for (int cSize = 0; cSize < entity.getSize(); ++cSize)
        {
            if (!GlobalObjects.ROUND.epsilonEquals(entity.getElement(cSize).len(), 0f))
                return false;
        }

        return true;
    }

    private class EntityPositionHistory extends History<Position>
    {
        public EntityPositionHistory(Entity coupled)
        {
            super(PhysicsCoefficients.MAX_ZERO_UPDATES);
            mEntity = coupled;
        }

        public Entity getEntity() { return mEntity; }

        public boolean equals(EntityPositionHistory comp)
        {
            return this.mEntity.equals(comp.mEntity);
        }

        public void update()
        {
            push(CompoMappers.POSITION.get(mEntity));
        }

        private Entity mEntity;
    }

    private LinkedList<EntityPositionHistory> mMoving;
    private float mMovementThreshold;
}
