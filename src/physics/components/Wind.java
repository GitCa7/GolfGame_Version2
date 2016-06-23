package physics.components;

import com.badlogic.gdx.math.Vector3;
import framework.components.Component;

import java.util.Random;

/**
 * Class simulating wind varying between a minimum and a maximum magnitude.
 * Shall occur at the frequency stored.
 * Its duration should range from the minimum to the maximum duration.
 * Arbitrary distributions may be chosen.
 * created 21.06.16
 *
 * @author martin
 */
public class Wind extends Vector3 implements Component
{

    public Wind(float minMagnitude, float maxMagnitude, double frequency, int minDuration, int maxDuration, Random gen)
    {
        mMinMagnitude = minMagnitude;
        mMaxMagnitude = maxMagnitude;
        mFrequency = frequency;
        mMinDuration = minDuration;
        mMaxDuration = maxDuration;
        mDurationCounter = 0;
        mGen = gen;
    }

    public Wind clone()
    {
        Wind w = new Wind(mMinMagnitude, mMaxMagnitude, mFrequency, mMinDuration, mMaxDuration, mGen);
        w.set(this);
        w.mDurationCounter = this.mDurationCounter;
        return w;
    }

    public float mMinMagnitude, mMaxMagnitude;
    public double mFrequency;
    public int mMinDuration, mMaxDuration, mDurationCounter;
    public Random mGen;
}
