package framework.systems;

/**
 * Factory class producing turn systems.
 * @author martin
 */
public class TurnSystemFactory extends EntitySystemFactory
{

    public TurnSystem produce()
    {
        TurnSystem t = new TurnSystem();
        initSystem(t);
        return t;
    }
}
