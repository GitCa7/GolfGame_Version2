package physics.components;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.core.Component;

/**
 * Created by Alexander on 20.05.2016.
 * Factory for producing Friction objects
 */
public class FrictionFactory implements ComponentFactory {

    public FrictionFactory()
    {
        mFluctuate = false;
    }

    public void setParameter(float staticMove, float dynamicMove, float staticSpin, float dynamicSpin){
        mStaticMove = staticMove;
        mDynamicMove = dynamicMove;
        mStaticSpin = staticSpin;
        mDynamicSpin = dynamicSpin;
    }


    public void setFluctuation(float magnitude)
    {
        mFluctuationMagnitude = magnitude;
        mFluctuate = true;
    }
    /**
     * I decided to create new float, because I did not know how to copy those.
     * @return
     */

    public Friction produce() {
        float staticMove=mStaticMove;
        float dynamicMove=mDynamicMove;
        float staticSpin=mStaticSpin;
        float dynamicSpin=mDynamicSpin;
        Friction F1= new Friction(staticMove,dynamicMove,staticSpin,dynamicSpin);
        F1.setFluctuating(mFluctuate);
        F1.setFluctuationMagnitude(mFluctuationMagnitude);
        return  F1;
    }

    float mStaticMove,mDynamicMove,mStaticSpin,mDynamicSpin;
    boolean mFluctuate;
    float mFluctuationMagnitude;
}
