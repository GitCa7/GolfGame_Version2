package framework.systems;

import framework.EntitySystemFactory;

/**
 * Factory class producing turn systems.
 * @author martin
 */
public class TurnSystemFactory extends EntitySystemFactory
{

    public TurnSystem produce()
    {
        checkAndThrowPriorityException();
        TurnSystem t = new TurnSystem();
        attachListener(t);
        return t;
    }
}
