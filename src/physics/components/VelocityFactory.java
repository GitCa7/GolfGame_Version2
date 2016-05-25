package physics.components;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Alexander on 20.05.2016.
 * Factory for producing Velocity objects.
 */
public class VelocityFactory implements ComponentFactory {


    public VelocityFactory(){}

    public void setVector(Vector3 velocity){
        mVelocity = velocity;
    }

    public Velocity produce() {
        Velocity v1= new Velocity();
        v1.set(mVelocity.cpy());
        return  v1;
    }

    private Vector3 mVelocity;
}
