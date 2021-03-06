package framework.systems;

/**
 * Factory class producing goal systems.
 * @author martin
 */
public class GoalSystemFactory extends EntitySystemFactory
{

    public GoalSystem produce()
    {
        GoalSystem g =  new GoalSystem();
        initSystem(g);
        return g;
    }
}
