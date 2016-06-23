package framework.internal.components;

import framework.components.Component;

/**
 * Component to be added to the game state entity. Indicates whether the game is active.
 * The game shall be active, if there is at least one player disposing of a ball which has not yet
 * reached the hole.
 * @author martin
 */
public class Active implements Component
{
    /**
     * default constructor initializing the active flag to false
     */
    public Active()
    {
        mActive = false;
    }


    public Active clone()
    {
        Active newActive = new Active();
        newActive.mActive = this.mActive;
        return newActive;
    }

    public boolean mActive;
}
