package physics.components;
import java.util.HashSet;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.core.Component;
import physics.geometry.spatial.Solid;
import java.util.ArrayList;

/**
 * Created by Alexander on 20.05.2016.
 * Factory to produce bodies.
 */
public class BodyFactory implements ComponentFactory {

    public BodyFactory(){
        mSolidList=new ArrayList<Solid>();
    }


    public void addSolid(Solid x){
        mSolidList.add(x);

    }
    public void clear(){
        mSolidList.clear();
    }

    public Body produce() {
        Body b1= new Body();
        for (Solid s: mSolidList) {
            b1.add(s);
        }
        return  b1;
    }

    ArrayList<Solid> mSolidList;

}


