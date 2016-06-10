package physics.systems;

/** Creates GravitySystemFactory
 * Created by marcel on 21.05.2016.
 */

public class GravitySystemFactory implements EntitySystemFactory {

    public GravitySystemFactory(){
    }

    @Override
    public GravitySystem produce() {
        GravitySystem g = new GravitySystem();
        return g;
    }
}
