package physics.components;

import framework.components.ComponentFactory;

/**
 * Created by Alexander on 20.05.2016.
 * Factory for producing Mass objects.
 */
public class MassFactory implements ComponentFactory
{

    public MassFactory(){}

    public void setParameter(float x){
        this.x=x;

    }

    public Mass produce() {
        float mass=x;
        Mass m1= new Mass(mass);
        return  m1;
    }

    float x;
}
