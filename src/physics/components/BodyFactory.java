package physics.components;
import framework.components.ComponentFactory;
import physics.geometry.spatial.SolidTranslator;

import java.util.ArrayList;

/**
 * Created by Alexander on 20.05.2016.
 * Factory to produce bodies.
 */
public class BodyFactory implements ComponentFactory
{

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


