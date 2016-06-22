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
import physics.geometry.spatial.SolidTranslator;
import framework.EntitySystem;

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
            entities().add(e);
    }

    @Override
    public void removeEntity(Entity e)
    {
        if (Families.GOAL_SEEKING.matches(e))
            entities().remove(e);
    }

    public void addedToEngine (Engine e)
    {
        if (mEngine != null)
            throw new IllegalStateException("goal system cannot be added to multiple engines!");
        mEngine = e;

        for (Entity add : e.getEntities())
        {
            if (Families.GOAL_SEEKING.matches(add))
                entities().add(add);
        }
    }

    public void removedFromEngine(Engine e)
    {
        mEngine = null;
        entities().clear();
    }

    /**
     * Checks for all entities whether they are at rest and in the goal.
     * If this holds for an entity, the entity itself and its owner are removed from the engine.
     * @param dTime time difference from previous update to current update
     */
    public void update(float dTime)
    {
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
                        if (CompoMappers.PLAYER_ORDER.has(order.mPrevious.mEntity))
                            CompoMappers.PLAYER_ORDER.get(order.mPrevious.mEntity).mNext = order.mNext;
                        if (CompoMappers.PLAYER_ORDER.has(order.mNext.mEntity))
                            CompoMappers.PLAYER_ORDER.get(order.mNext.mEntity).mPrevious = order.mPrevious;
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


    private Engine mEngine;
}
