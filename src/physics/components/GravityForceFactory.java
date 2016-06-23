package physics.components;

import com.badlogic.gdx.math.*;
import framework.components.ComponentFactory;

/**
 * Created by Alexander on 20.05.2016.
 * Factory for producing GravityForce objects.
 */
public class GravityForceFactory implements ComponentFactory
{

    public GravityForceFactory(){}

    public void setParameter (Vector3 vector){
        mVector=vector;
    }

    public GravityForce produce() {
        GravityForce g1= new GravityForce(mVector.cpy());
        return g1;
    }

    Vector3 mVector;
}
