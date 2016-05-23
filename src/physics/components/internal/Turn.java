package physics.components.internal;

import physics.components.Component;

/**
 * empty component to be added to player's whose turn it is
 * @author martin
 */
public class Turn implements Component
{

    @Override
    public Component clone()
    {
        return new Turn();
    }
}
