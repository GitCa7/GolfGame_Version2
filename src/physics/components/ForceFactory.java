package physics.components;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.core.Component;

/**
 * Created by Alexander on 20.05.2016.
 * Factory for producing Force objects.
 */
public class ForceFactory implements ComponentFactory {

    public ForceFactory(){}

    public void setParameter(float x, float y, float z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public Force produce() {
        Force f1= new Force();
        f1.set(x,y,z);
        return  f1;
    }

    float x;
    float y;
    float z;
}
