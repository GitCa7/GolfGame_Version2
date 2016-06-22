package framework.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import framework.components.Turn;
import framework.constants.CompoMappers;
import framework.constants.Families;
import framework.internal.systems.BusySystem;
import physics.components.Position;
import physics.components.Velocity;
import physics.constants.GlobalObjects;
import framework.EntitySystem;
import physics.constants.PhysicsCoefficients;
import physics.generic.History;
//import sun.org.mozilla.javascript.tools.shell.Global;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * System handling turn transitions.
 * Detects moving and owned objects. Whenever such an object stops moving,
 * the system advances the turn pointer of the owner (if possible).
 * @author martin
 */
public class TurnSystem extends EntitySystem
{

    public static boolean DEBUG = false;

    public TurnSystem()
    {
        mMoving = new LinkedList<>();
    }


    public TurnSystem clone()
    {
        TurnSystem newSystem =  new TurnSystem();
        newSystem.setPriority(priority);
        return newSystem;
    }

    public void addedToEngine (Engine e)
    {
        for (Entity add : e.getEntities())
        {
            if (Families.OWNED.matches(add) && physics.constants.Families.MOVING.matches(add))
                mMoving.add(new EntityPositionHistory(add));
        }
    }


    public void addEntity(Entity e)
    {
        if (Families.OWNED.matches(e) && physics.constants.Families.MOVING.matches(e))
            mMoving.add(new EntityPositionHistory(e));
    }


    public void removeEntity(Entity e)
    {
        if (Families.OWNED.matches(e) && physics.constants.Families.MOVING.matches(e))
        {
            EntityPositionHistory removeHistory = new EntityPositionHistory(e);
            int cMoving = 0;
            while (cMoving < mMoving.size() && !mMoving.get(cMoving).equals(removeHistory))
                ++cMoving;

            if (cMoving < mMoving.size())
                mMoving.remove(cMoving);
        }
    }

    /**
     * for every player that has the done flag active: reset turn flag and set turn flag to active of next player
     * @param dTime delta time (irrelevant)
     */
    public void update (float dTime)
    {

        for (EntityPositionHistory moving : mMoving)
        {
            Entity owner = CompoMappers.OWNERSHIP.get(moving.getEntity()).mOwner;
            Turn ownerTurn = CompoMappers.TURN.get(owner);

            if (ownerTurn.mTurn)
            {
                moving.update();
                if (moving.isStable())
                {
                    updateOwner(moving.getEntity());
                    moving.resetStableCounter();
                    if (DEBUG)
                        System.out.println("detected entity stopped moving");
                }
            }
        }
    }

    /**
     * Extracts the owner of owned. If the owner has a turn and a next player component,
     * this method will set the flag of the turn component to false and the flag of the
     * turn component of the next player to true.
     * @param owned
     */
    private void updateOwner(Entity owned)
    {
        Entity owner = CompoMappers.OWNERSHIP.get(owned).mOwner;
        if (CompoMappers.TURN.has(owner) && CompoMappers.PLAYER_ORDER.has(owner))
        {
            Turn ownerTurn = CompoMappers.TURN.get(owner);
            if (ownerTurn.mTurn)
            {
                Entity followingOwner = CompoMappers.PLAYER_ORDER.get(owner).mNext.mEntity;
                assert (CompoMappers.TURN.has(followingOwner));

                Turn followingTurn = CompoMappers.TURN.get(followingOwner);

                ownerTurn.mTurn = false;
                followingTurn.mTurn = true;

                if (DEBUG)
                {
                    System.out.print("turn transition from " + CompoMappers.NAME.get(owner).mName);
                    System.out.println (" to " + CompoMappers.NAME.get(followingOwner).mName);
                }
            }
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
            Vector3 newPos = physics.constants.CompoMappers.POSITION.get(mEntity);
            if (!GlobalObjects.ROUND.epsilonEquals(newPos.dst(mLast), 0f))
                mStableUpdates = 0;
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
