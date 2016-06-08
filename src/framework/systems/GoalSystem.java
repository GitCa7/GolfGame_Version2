package framework.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import framework.components.Goal;
import framework.components.Ownership;
import framework.constants.CompoMappers;
import framework.constants.Families;
import physics.components.Body;
import physics.components.Velocity;
import physics.geometry.spatial.Solid;
import physics.systems.EntitySystem;

/**
 * Entity system class operating on entities seeking a goal and owned by another entity.
 * If the goal seeking entity reached the goal, this entity and its owner are removed from the engine.
 */
public class GoalSystem extends EntitySystem
{
    /**
     * cefault constructor
     */
    public GoalSystem()
    {

    }

    @Override
    public void addEntity(Entity e)
    {
        if (Families.GOAL_SEEKING.matches(e) && Families.OWNED.matches(e))
            entities().add(e);
    }

    @Override
    public void removeEntity(Entity e)
    {
        if (Families.GOAL_SEEKING.matches(e) && Families.OWNED.matches(e))
            entities().remove(e);
    }

    public void addedToEngine (Engine e)
    {
        if (mEngine != null)
            throw new IllegalStateException("goal system cannot be added to multiple engines!");
        mEngine = e;

        for (Entity add : e.getEntitiesFor(Families.GOAL_SEEKING))
        {
            if (Families.OWNED.matches(add))
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
            System.out.println ("updating an entity for goal");
            Goal goal = CompoMappers.GOAL.get(e);
            Body body = physics.constants.CompoMappers.BODY.get(e);
            Velocity v = physics.constants.CompoMappers.VELOCITY.get(e);

            if (isBodyInGoal(body, goal))
            {
                Ownership owner = CompoMappers.OWNERSHIP.get(e);
                mEngine.removeEntity(owner.mOwner);
                mEngine.removeEntity(e);
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
    //    DOES NOT WORK, ADD EXTRINSIC STATE TO BODY SOLIDS
        for (Solid s : b)
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
