package physics.systems;

import framework.EntitySystemFactory;

/** Creates a ForceApplyFactory
 * Created by marcel on 21.05.2016.
 */

public class ForceApplyFactory extends EntitySystemFactory {

    public ForceApplyFactory(){
    }

    @Override
    public ForceApply produce()
    {
        checkAndThrowPriorityException();
        ForceApply fa = new ForceApply();
        attachListener(fa);
        return fa;
    }
}
