package framework.systems;

import physics.systems.EntitySystemFactory;

/**
 * Factory class producing turn systems.
 * @author martin
 */
public class TurnSystemFactory implements EntitySystemFactory
{

    public TurnSystem produce()  { return new TurnSystem(); }
}
