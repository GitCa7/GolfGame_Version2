package physics.components;
import java.util.HashSet;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.core.Component;
import physics.geometry.spatial.Solid;

/**
 * Created by Alexander on 20.05.2016.
 * Factory to produce bodies.
 */
public class BodyFactory implements ComponentFactory {

    public BodyFactory(){}

    //@TODO store list
    public void setParameter(Solid x, Solid y, Solid z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    //@TODO clone
    public Body produce() {
        Body b1= new Body();
        b1.add(x);
        b1.add(y);
        b1.add(z);
        return  b1;
    }

    Solid x;
    Solid y;
    Solid z;
}


