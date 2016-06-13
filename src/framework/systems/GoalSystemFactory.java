package framework.systems;

import framework.EntitySystemFactory;

/**
 * Factory class producing goal systems.
 * @author martin
 */
public class GoalSystemFactory extends EntitySystemFactory
{

    public GoalSystem produce()
    {
        checkAndThrowPriorityException();
        GoalSystem g =  new GoalSystem();
        attachListener(g);
        return g;
    }
}
