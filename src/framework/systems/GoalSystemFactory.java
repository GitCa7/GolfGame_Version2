package framework.systems;

import physics.systems.EntitySystemFactory;

/**
 * Factory class producing goal systems.
 * @author martin
 */
public class GoalSystemFactory implements EntitySystemFactory
{

    public GoalSystem produce() { return new GoalSystem(); }
}
