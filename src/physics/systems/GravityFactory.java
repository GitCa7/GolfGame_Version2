package physics.systems;

/** Creates GravityFactory
 * Created by marcel on 21.05.2016.
 */

public class GravityFactory implements EntitySystemFactory {

    public GravityFactory(){
    }

    @Override
    public Gravity produce() {
        Gravity g = new Gravity();
        return g;
    }
}
