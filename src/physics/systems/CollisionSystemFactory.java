package physics.systems;


/**
 * Created by marcel on 21.05.2016.
 */
public class CollisionSystemFactory implements EntitySystemFactory {


    public CollisionSystemFactory(){
    }

    @Override
    public CollisionSystem produce() {
        CollisionSystem cs = new CollisionSystem();
        return cs;
    }
}
