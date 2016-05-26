package physics.systems;

/** Creates MovementFactory
 * Created by marcel on 21.05.2016.
 */

public class MovementFactory implements EntitySystemFactory{

    public MovementFactory(){
    }

    @Override
    public Movement produce() {
        Movement m = new Movement();
        return m;
    }
}
