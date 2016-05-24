package framework.components;

import com.badlogic.ashley.core.Entity;
import framework.entities.Player;
import physics.components.Component;

/**
 * Created by martin on 23.05.16.
 */
public class NextPlayer implements Component
{
    /**
     * @param next initial next player
     */
    public NextPlayer (Player next)
    {
        mNext = next;
    }

    @Override
    public Component clone()
    {
        //@TODO how should cloning work??? Always need to clone group...
        return null;
    }

    public Player mNext;
}
