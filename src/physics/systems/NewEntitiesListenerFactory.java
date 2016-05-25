package physics.systems;

import com.badlogic.ashley.core.Entity;

import java.util.HashSet;

/**
 * Created by marcel on 21.05.2016.
 */
public class NewEntitiesListenerFactory {

    public NewEntitiesListenerFactory(){

    }

    public void setParameters(HashSet<Entity> entitySet){
        mEntitySet = entitySet;
    }

    public NewEntitiesListener produce(){
        NewEntitiesListener nel = new NewEntitiesListener();
        return nel;
    }

    private HashSet<Entity> mEntitySet;
}
