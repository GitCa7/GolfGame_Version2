package physics.components;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.core.Component;

/**
 * Created by Alexander on 20.05.2016.
 * Factory for producing Velocity objects.
 */
public class VelocityFactory implements ComponentFactory {


    public VelocityFactory(){}

    public void setParameter(float x, float y, float z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public Velocity produce() {
        Velocity v1= new Velocity(x,y,z);
        return  v1;
    }

    float x;
    float y;
    float z;
}
