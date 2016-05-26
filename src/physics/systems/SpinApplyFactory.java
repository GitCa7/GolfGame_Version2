package physics.systems;

/** Creates SpinApplyFactory
 * Created by marcel on 21.05.2016.
 */

public class SpinApplyFactory implements EntitySystemFactory {

    public SpinApplyFactory(){
    }

    @Override
    public SpinApply produce() {
        SpinApply sa = new SpinApply();
        return sa;
    }
}
