package physics.systems;

import framework.EntitySystemFactory;

/** Creates MovementFactory
 * Created by marcel on 21.05.2016.
 */

public class MovementFactory extends EntitySystemFactory {

    public MovementFactory(){
    }

    @Override
    public Movement produce()
    {
        Movement m = new Movement();
        attachListener(m);
        return m;
    }
}
