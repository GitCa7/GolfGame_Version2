package physics.systems;

import framework.systems.EntitySystem;
import framework.systems.EntitySystemFactory;

/**
 * Class producing wind systems
 * created 21.06.16
 *
 * @author martin
 */
public class WindSystemFactory extends EntitySystemFactory
{

    @Override
    public EntitySystem produce()
    {
        WindSystem windSystem = new WindSystem();
        initSystem(windSystem);
        return windSystem;
    }
}
