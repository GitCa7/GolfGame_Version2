package physics.systems;

import framework.systems.EntitySystemFactory;

/** Creates SpinApplyFactory
 * Created by marcel on 21.05.2016.
 */

public class SpinApplyFactory extends EntitySystemFactory {

    public SpinApplyFactory(){
    }

    @Override
    public SpinApply produce() {
        SpinApply sa = new SpinApply();
        initSystem(sa);
        return sa;
    }
}
