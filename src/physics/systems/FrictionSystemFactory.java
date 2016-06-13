package physics.systems;

import framework.EntitySystem;
import framework.EntitySystemFactory;

/**
 * class producing friction systems
 * @author martin
 */
public class FrictionSystemFactory extends EntitySystemFactory
{

    @Override
    public EntitySystem produce()
    {
        FrictionSystem f = new FrictionSystem();
        attachListener(f);
        return f;
    }
}
