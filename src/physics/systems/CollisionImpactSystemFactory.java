package physics.systems;
import physics.systems.CollisionImpactSystem;

/** Creates a CollisionImpactSystemFactory
 * Created by marcel on 21.05.2016.
 */

public class CollisionImpactSystemFactory implements EntitySystemFactory {


    public CollisionImpactSystemFactory(){
    }

    @Override
    public CollisionImpactSystem produce() {
        CollisionImpactSystem cs = new CollisionImpactSystem();
        return cs;
    }
}
