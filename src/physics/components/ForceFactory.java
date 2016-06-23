package physics.components;

import com.badlogic.gdx.math.Vector3;
import framework.components.ComponentFactory;

/**
 * Created by Alexander on 20.05.2016.
 * Factory for producing Force objects.
 */
public class ForceFactory implements ComponentFactory
{

    public ForceFactory()
    {
        mForce = new Vector3();
    }

    public void setVector(Vector3 force){
        mForce = force;
    }

    public Force produce() {
        Force f1= new Force();
        f1.set(mForce.cpy());
        return  f1;
    }

    private Vector3 mForce;
}
