package framework.internal.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import framework.constants.Families;
import framework.internal.components.Busy;
import physics.constants.CompoMappers;
import framework.systems.EntitySystem;
import physics.constants.GlobalObjects;
import physics.constants.PhysicsCoefficients;

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
            if (!m.isStable())
                detectedMove = true;
        }

        //set result of search in busy components
        for (Entity gs : entities())
        {
            Busy b = framework.constants.CompoMappers.BUSY.get(gs);
            b.mBusy = detectedMove;
        }
    }

    private class EntityPositionHistory
    {
        public EntityPositionHistory(Entity coupled)
        {
            mEntity = coupled;
            mLast = physics.constants.CompoMappers.POSITION.get(coupled).cpy();
        }

        public Entity getEntity() { return mEntity; }

        public boolean equals(EntityPositionHistory comp)
        {
            return this.mEntity.equals(comp.mEntity);
        }

        public boolean isStable()
        {
            return mStableUpdates >= PhysicsCoefficients.MAX_ZERO_UPDATES;
        }

        public void update()
        {
            Vector3 newPos = CompoMappers.POSITION.get(mEntity);
            if (!GlobalObjects.ROUND.epsilonEquals(newPos.dst(mLast), 0f))
            {
                mStableUpdates = 0;
            }
            else
                ++mStableUpdates;

            mLast = newPos.cpy();
        }

        public void resetStableCounter()
        {
            mStableUpdates = 0;
        }


        private Entity mEntity;
        private Vector3 mLast;
        private int mStableUpdates;
    }

    private LinkedList<EntityPositionHistory> mMoving;
}
