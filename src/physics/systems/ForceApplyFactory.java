package physics.systems;

/**
 * Created by marcel on 21.05.2016.
 */
public class ForceApplyFactory implements EntitySystemFactory {

    public ForceApplyFactory(){
    }

    @Override
    public ForceApply produce() {
        ForceApply fa = new ForceApply();
        return fa;
    }
}
