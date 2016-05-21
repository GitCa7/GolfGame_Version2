package physics.components;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.core.Component;

/**
 * Created by Alexander on 20.05.2016.
 */
public class FrictionFactory implements ComponentFactory {

    public FrictionFactory(){}

    public void setParameter(float staticMove, float dynamicMove, float staticSpin, float dynamicSpin){
        mStaticMove = staticMove;
        mDynamicMove = dynamicMove;
        mStaticSpin = staticSpin;
        mDynamicSpin = dynamicSpin;
    }

    public Friction produce() {
        Friction F1= new Friction(mStaticMove,mDynamicMove,mStaticSpin,mDynamicSpin);
        return  F1;
    }

    float mStaticMove,mDynamicMove,mStaticSpin,mDynamicSpin;
}
