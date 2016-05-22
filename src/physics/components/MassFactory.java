package physics.components;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.core.Component;

/**
 * Created by Alexander on 20.05.2016.
 * Factory for producing Mass objects.
 */
public class MassFactory implements ComponentFactory {

    public MassFactory(){}

    public void setParameter(float x){
        this.x=x;

    }

    public Mass produce() {
        Mass m1= new Mass(x);
        return  m1;
    }

    float x;
}
