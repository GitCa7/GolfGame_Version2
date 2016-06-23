package physics.components;

import com.badlogic.gdx.math.Vector3;
import framework.components.ComponentFactory;

/**
 * Created by Alexander on 20.05.2016.
 * Factory for producing Position objects.
 */
public class PositionFactory implements ComponentFactory
{
    public PositionFactory(){}


    public void setVector(Vector3 position){
        mPosition = position;
    }

    public Position produce() {
        Position p1= new Position();
        p1.set(mPosition.cpy());
        return p1;
    }

    private Vector3 mPosition;
}




