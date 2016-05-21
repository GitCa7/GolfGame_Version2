package physics.components;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.core.Component;

/**
 * Created by Alexander on 20.05.2016.
 */
public class PositionFactory implements ComponentFactory
{
    public PositionFactory(){}

    public void setParameter(float x, float y, float z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public Position produce() {
        Position p1= new Position(x,y,z);
        return  p1;
    }

    float x;
    float y;
    float z;
}




