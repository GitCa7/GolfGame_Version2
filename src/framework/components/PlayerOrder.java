package framework.components;

import com.badlogic.ashley.core.Entity;
import framework.entities.Player;
import physics.components.Component;

/**
 * created on 23.05.16.
 * @author martin
 */
public class PlayerOrder implements Component
{
    /**
     * @param next initial next player
     */
    public PlayerOrder(Player previous, Player next)
    {
        mPrevious = previous;
        mNext = next;
    }

    @Override
    public Component clone()
    {
        //@TODO how should cloning work??? Always need to clone group...
        return null;
    }

    public Player mPrevious, mNext;
}
