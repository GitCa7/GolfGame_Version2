package framework.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import framework.components.NextPlayer;
import framework.components.Turn;
import framework.constants.CompoMappers;
import framework.constants.Families;
import physics.systems.EntitySystem;

/**
 * system handling turn transitions
 * @author martin
 */
public class TurnSystem extends EntitySystem
{


    public void addedToEngine (Engine e)
    {
        for (Entity add : e.getEntitiesFor (Families.TURN_TAKING))
            entities().add (add);
    }


    public void addEntity(Entity e)
    {
        if (Families.TURN_TAKING.matches(e))
            entities().add(e);
    }


    public void removeEntity(Entity e)
    {
        if (Families.TURN_TAKING.matches(e))
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
            Turn turn = CompoMappers.TURN.get(process);
            if (turn.mDone)
            {
                NextPlayer nextPlayer = CompoMappers.NEXT_PLAYER.get(process);
                Turn nextPlayerTurn = CompoMappers.TURN.get(nextPlayer.mNext);

                turn.mTurn = false;
                nextPlayerTurn.mTurn = true;
            }

        }
    }


}
