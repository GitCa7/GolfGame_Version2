package framework.components;

import framework.entities.Player;

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
    /**
     * @return a new player order component whose previous and next player is set to null.
     * Needs to be set manually.
     */
    public Component clone()
    {
        return new PlayerOrder(null, null);
    }

    public Player mPrevious, mNext;
}
