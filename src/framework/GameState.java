package framework;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector3;
import framework.components.Ownership;
import framework.constants.Families;
import physics.constants.CompoMappers;
import physics.entities.Ball;

import java.util.ArrayList;

/**
 * Class managing entities in a simulated golf game which are moving.
 * All entities not moving are neglected.
 * All turn taking entities are neglected.
 * All ownership components are neglected
 * created 16.06.16
 *
 * @author martin
 */
public class GameState
{
    /**
     * sets the list of mutable entities this class manages.
     * Excludes TURN_TAKING entities
     * @param mutableEntities all mutable entities contained in an engine
     * @param playerBall the ball being modified
     */
    public GameState (ImmutableArray<Entity> mutableEntities, Ball playerBall)
    {
        mMutableEntities = new ArrayList<>(mutableEntities.size());
        mPlayerBall = playerBall;

        for (Entity in : mutableEntities)
        {
            if (!Families.TURN_TAKING.matches(in) && Families.MODIFIABLE.matches(in))
                mMutableEntities.add(in);
        }

    }

    /**
     * sets the list of mutable entities this class manages.
     * Excludes TURN_TAKING entities
     * @param mutableEntities all mutable entities contained in an engine
     * @param playerBall the ball being modified
     */
    public GameState (ArrayList<Entity> mutableEntities, Ball playerBall)
    {
        mMutableEntities = new ArrayList<>(mutableEntities.size());
        mPlayerBall = playerBall;

        for (Entity in : mutableEntities)
        {
            if (!Families.TURN_TAKING.matches(in) && Families.MODIFIABLE.matches(in))
                mMutableEntities.add(in);
        }

    }

    /**
     * @return all mutable entities stored
     */
    public ArrayList<Entity> getMutableEntities() { return mMutableEntities; }

    /**
     * @return a new game state containing a deep copy of all moving entities and their components
     */
    public GameState clone()
    {
        ArrayList<Entity> newEntities = new ArrayList<>(mMutableEntities.size());
        Ball newPlayerBall = null;

        for (Entity cloning : mMutableEntities)
        {
            Entity clone = cloneEntity(cloning);
            newEntities.add(clone);
            if (cloning.equals(mPlayerBall.mEntity))
                newPlayerBall = new Ball (clone);
        }

        assert (newPlayerBall != null);

        return new GameState(newEntities, newPlayerBall);
    }

    /**
     * @return the ball being modified of the current state
     */
    public Ball getPlayerBall() { return mPlayerBall; }

    /**
     * @return the position of the ball being modified
     */
    public Vector3 getBallPosition() { return CompoMappers.POSITION.get(mPlayerBall.mEntity); }

    /**
     * @return the position of the goal matching the ball being modified
     */
    public Vector3 getTargetPosition() { return framework.constants.CompoMappers.GOAL.get(mPlayerBall.mEntity).mGoalSpace.getPosition(); }

    /**
     * @param cloning the entity to clone
     * @return a clone of cloning, not adding ownership components
     */
    private Entity cloneEntity(Entity cloning)
    {
        Entity newEntity = new Entity();
        for (Component oldComponent : cloning.getComponents())
        {
            if (oldComponent.getClass() != Ownership.class)
            {
                physics.components.Component castedOldComponent = (physics.components.Component) oldComponent;
                newEntity.add(castedOldComponent.clone());
            }
        }

        return newEntity;
    }



    private ArrayList<Entity> mMutableEntities;
    private Ball mPlayerBall;
}
