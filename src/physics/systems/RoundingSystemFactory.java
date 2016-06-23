package physics.systems;

import framework.systems.EntitySystem;
import framework.systems.EntitySystemFactory;

/**
 * created 20.06.16
 *
 * @author martin
 */
public class RoundingSystemFactory extends EntitySystemFactory
{

    @Override
    public EntitySystem produce()
    {
        RoundingSystem newSystem = new RoundingSystem();
        initSystem(newSystem);
        return newSystem;
    }
}
