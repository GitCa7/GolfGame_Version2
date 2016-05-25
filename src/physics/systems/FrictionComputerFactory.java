package physics.systems;
import com.badlogic.ashley.core.Entity;


/**
 * Created by marcel on 21.05.2016.
 */
public class FrictionComputerFactory {

    public FrictionComputerFactory(){
    }

    public void setParameter(Entity entity){
        mEntity=entity;
    }

    public FrictionComputer produce() {
        FrictionComputer fc = new FrictionComputer(mEntity);
        return fc;
    }

    private Entity mEntity;
}


