package physics.systems;

/**
 * class producing friction systems
 * @author martin
 */
public class FrictionSystemFactory implements EntitySystemFactory
{

    @Override
    public EntitySystem produce()
    {
        return new FrictionSystem();
    }
}
