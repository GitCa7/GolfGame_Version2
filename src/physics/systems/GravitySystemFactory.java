package physics.systems;

import framework.EntitySystemFactory;

/** Creates GravitySystemFactory
 * Created by marcel on 21.05.2016.
 */

public class GravitySystemFactory extends EntitySystemFactory {

    public GravitySystemFactory(){
    }

    @Override
    public GravitySystem produce()
    {
        GravitySystem g = new GravitySystem();
        initSystem(g);
        return g;
    }
}
