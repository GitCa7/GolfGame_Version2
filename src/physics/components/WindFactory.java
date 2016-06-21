package physics.components;

import framework.EntitySystem;
import framework.EntitySystemFactory;

import java.util.Random;

/**
 * created 21.06.16
 *
 * @author martin
 */
public class WindFactory implements ComponentFactory
{
    public WindFactory()
    {
        mMinMagnitude = 0;
        mMaxMagnitude = 0;
        mMinDuration = 0;
        mMaxDuration = 0;
        mFrequency = 0;
        mRandom = new Random(System.currentTimeMillis());
    }

    public void setParameter (float minMagnitude, float maxMagnitude, double frequency, int minDuration, int maxDuration)
    {
        mMinMagnitude = minMagnitude;
        mMaxMagnitude = maxMagnitude;
        mMinDuration = minDuration;
        mMaxDuration = maxDuration;
        mFrequency = frequency;
    }

    @Override
    public Component produce()
    {
        Wind w = new Wind(mMinMagnitude, mMaxMagnitude, mFrequency, mMinDuration, mMaxDuration, mRandom);
        return w;
    }


    public float mMinMagnitude, mMaxMagnitude;
    public double mFrequency;
    public int mMinDuration, mMaxDuration;
    public Random mRandom;
}
