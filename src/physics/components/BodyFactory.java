package physics.components;
import java.util.HashSet;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.core.Component;
import physics.geometry.spatial.Solid;
import physics.geometry.spatial.SolidTranslator;

import java.util.ArrayList;

/**
 * Created by Alexander on 20.05.2016.
 * Factory to produce bodies.
 */
public class BodyFactory implements ComponentFactory {

    public BodyFactory(){
        mSolidList=new ArrayList<>();
    }


    public void addSolid(SolidTranslator add)
    {
        mSolidList.add(add);

    }

    /**
     * removes all added solids
     */
    public void clear(){
        mSolidList.clear();
    }

    public Body produce()
    {
        Body newBody = new Body();
        for (SolidTranslator s : mSolidList)
            newBody.add(new SolidTranslator(s.getSolid(), s.getPosition()));

        return  newBody;
    }

    ArrayList<SolidTranslator> mSolidList;

}


