package physics.systems;

import framework.EntitySystemFactory;

/** Creates SpinApplyFactory
 * Created by marcel on 21.05.2016.
 */

public class SpinApplyFactory extends EntitySystemFactory {

    public SpinApplyFactory(){
    }

    @Override
    public SpinApply produce() {
        checkAndThrowPriorityException();
        SpinApply sa = new SpinApply();
        return sa;
    }
}
