package framework.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import framework.components.Goal;
import framework.components.Ownership;
import framework.components.PlayerOrder;
import framework.constants.CompoMappers;
import framework.constants.Families;
import physics.components.Body;
import physics.components.Velocity;
import physics.constants.GlobalObjects;
import physics.constants.PhysicsCoefficients;
import physics.geometry.spatial.SolidTranslator;

import java.util.ArrayList;

/**
 * Entity system class operating on entities seeking a goal and owned by another entity.
 * If the goal seeking entity reached the goal, this entity and its owner are removed from the engine.
 */
public class GoalSystem extends EntitySystem
{
    public static boolean DEBUG = false;

    /**
     * cefault constructor
     */
    public GoalSystem()
    {
        mGoalSeekers = new ArrayList<>();
    }

    public GoalSystem clone()
    {
        GoalSystem newSystem = new GoalSystem();
        newSystem.setPriority(priority);
        return newSystem;
    }

    @Override
    public void addEntity(Entity e)
    {
        if (Families.GOAL_SEEKING.matches(e))
            mGoalSeekers.add(new EntityPositionHistory(e));
    }

    @Override
    public void removeEntity(Entity e)
    {
        if (Families.GOAL_SEEKING.matches(e))
            mGoalSeekers.remove(new EntityPositionHistory(e));
    }

    public void addedToEngine (Engine e)
    {
        if (mEngine != null)
            throw new IllegalStateException("goal system cannot be added to multiple engines!");
        mEngine = e;

        for (Entity add : e.getEntities())
        {
            if (Families.GOAL_SEEKING.matches(add))
                mGoalSeekers.add(new EntityPositionHistory(add));
        }
    }

    public void removedFromEngine(Engine e)
    {
        mEngine = null;
        mGoalSeekers.clear();
    }

    /**
     * Checks for all entities whether they are at rest and in the goal.
     * If this holds for an entity, the entity itself and its owner are removed from the engine.
     * @param dTime time difference from previous update to current update
     */
    public void update(float dTime)
    {

        for (EntityPositionHistory entity : mGoalSeekers)
        {
            entity.update();
            if (entity.isStable())
            {
                Goal goal = CompoMappers.GOAL.get(entity.getEntity());
                Body body = physics.constants.CompoMappers.BODY.get(entity.getEntity());
                Velocity v = physics.constants.CompoMappers.VELOCITY.get(entity.getEntity());

                if (isBodyInGoal(body, goal) && GlobalObjects.ROUND.epsilonEquals(v.len(), 0))
                {
                    if (DEBUG)
                        System.out.print (entity.getEntity() + " reached goal ");
                    if (CompoMappers.OWNERSHIP.has(entity.getEntity()))
                    {
                        Ownership owner = CompoMappers.OWNERSHIP.get(entity.getEntity());
                        if (CompoMappers.PLAYER_ORDER.has(owner.mOwner))
                        {
                            PlayerOrder order = CompoMappers.PLAYER_ORDER.get(owner.mOwner);

                            PlayerOrder previousOrder = CompoMappers.PLAYER_ORDER.get(order.mPrevious.mEntity);
                            PlayerOrder nextOrder = CompoMappers.PLAYER_ORDER.get(order.mNext.mEntity);

                            previousOrder.mNext = order.mNext;
                            nextOrder.mPrevious = order.mPrevious;
                        }
                        mEngine.removeEntity(owner.mOwner);

                        if (DEBUG)
                            System.out.print(" and is linked to owner " + owner);
                    }
                    mEngine.removeEntity(entity.getEntity());
                    if (DEBUG)
                        System.out.println();
                }
            }
        }

        for (Entity e : entities())
        {
            Goal goal = CompoMappers.GOAL.get(e);
            Body body = physics.constants.CompoMappers.BODY.get(e);
            Velocity v = physics.constants.CompoMappers.VELOCITY.get(e);

            if (isBodyInGoal(body, goal) && GlobalObjects.ROUND.epsilonEquals(v.len(), 0))
            {
                if (DEBUG)
                    System.out.print (e + " reached goal ");
                if (CompoMappers.OWNERSHIP.has(e))
                {
                    Ownership owner = CompoMappers.OWNERSHIP.get(e);
                    if (CompoMappers.PLAYER_ORDER.has(owner.mOwner))
                    {
                        PlayerOrder order = CompoMappers.PLAYER_ORDER.get(owner.mOwner);

                        PlayerOrder previousOrder = CompoMappers.PLAYER_ORDER.get(order.mPrevious.mEntity);
                        PlayerOrder nextOrder = CompoMappers.PLAYER_ORDER.get(order.mNext.mEntity);

                        previousOrder.mNext = order.mNext;
                        nextOrder.mPrevious = order.mPrevious;
                    }
                    mEngine.removeEntity(owner.mOwner);

                    if (DEBUG)
                        System.out.print(" and is linked to owner " + owner);
                }
                mEngine.removeEntity(e);
                if (DEBUG)
                    System.out.println();
            }
        }
    }

    /**
     * @param b a given body
     * @param g a given goal component
     * @return true if for all solids in b all vertices are within g
     */
    private boolean isBodyInGoal(Body b, Goal g)
    {
        for (SolidTranslator s : b)
        {
            for (Vector3 vertex : s.getVertices())
            {
                if (!g.mGoalSpace.isWithin(vertex))
                    return false;
            }
        }
        return true;
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


    private ArrayList<EntityPositionHistory> mGoalSeekers;
    private Engine mEngine;
}
