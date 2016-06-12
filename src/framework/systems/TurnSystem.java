package framework.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import framework.components.NextPlayer;
import framework.components.Turn;
import framework.constants.CompoMappers;
import framework.constants.Families;
import physics.components.Velocity;
import physics.constants.GlobalObjects;
import physics.systems.EntitySystem;
//import sun.org.mozilla.javascript.tools.shell.Global;

import java.util.HashSet;

/**
 * System handling turn transitions.
 * Detects moving and owned objects. Whenever such an object stops moving,
 * the system advances the turn pointer of the owner (if possible).
 * @author martin
 */
public class TurnSystem extends EntitySystem
{


    public TurnSystem()
    {
        mMoving = new HashSet<>();
    }

    public void addedToEngine (Engine e)
    {
        for (Entity add : e.getEntitiesFor(Families.OWNED))
        {
            if (physics.constants.Families.MOVING.matches(add))
                entities().add(add);
        }
    }


    public void addEntity(Entity e)
    {
        if (Families.OWNED.matches(e) && physics.constants.Families.MOVING.matches(e))
            entities().add(e);
    }


    public void removeEntity(Entity e)
    {
        if (Families.OWNED.matches(e) && physics.constants.Families.MOVING.matches(e))
            entities().remove(e);
    }

    /**
     * for every player that has the done flag active: reset turn flag and set turn flag to active of next player
     * @param dTime delta time (irrelevant)
     */
    public void update (float dTime)
    {
        for (Entity process : entities())
        {
            Velocity v = physics.constants.CompoMappers.VELOCITY.get(process);
            //if entity is not moving
            if (GlobalObjects.ROUND.epsilonEquals(v.len(), 0f))
            {
                //if entity was moving previously, try setting turn flags
                if (mMoving.contains(process))
                    updateOwner(process);
            }
            else
            {
                mMoving.add(process);
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
        if (CompoMappers.TURN.has(owner) && CompoMappers.NEXT_PLAYER.has(owner))
        {
            Turn ownerTurn = CompoMappers.TURN.get(owner);
            Entity followingOwner = CompoMappers.NEXT_PLAYER.get(owner).mNext.mEntity;
            assert (CompoMappers.TURN.has(followingOwner));

            Turn followingTurn = CompoMappers.TURN.get(followingOwner);

            ownerTurn.mTurn = false;
            followingTurn.mTurn = true;
        }
    }

    private HashSet<Entity> mMoving;

}
