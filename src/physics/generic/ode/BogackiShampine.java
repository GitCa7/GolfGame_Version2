package physics.generic.ode;

/**
 * ODE solver for the Bogacki-Shampine adaptive method
 * created 22.06.16
 *
 * @author martin
 */
public class BogackiShampine extends ODESolver
{

    /**
     * parametric constructor
     *
     * @param equation od equation to solve for
     * @param initialT initial t condition
     * @param initialY initial y condition
     */
    public BogackiShampine(ODEquation equation, double initialT, double initialY)
    {
        super(equation, initialT, initialY);
        mEpsilon = -1;
        mSafety = -1;
    }

    @Override
    protected double evalutate(double t, double y, double deltaT)
    {
        double k1, k2, k3, k4;
        k1 = deltaT * mEquation.evaluate(t, y);
        k2 = deltaT * mEquation.evaluate(t + .5 * deltaT, y + .5 * k1);
        k3 = deltaT * mEquation.evaluate(t + .75 * deltaT, y + .75 * k2);

        double nextY = y + 2/9 * k1 + 3/9 * k2 + 4/9 * k3;

        k4 = deltaT * mEquation.evaluate(t + deltaT, nextY);

        mNextYHat = y + 7/24 * k1 + 1/4 * k2 + 1/3 * k3 + 1/8 * k4;

        mStepSizeFactor = Math.sqrt(mEpsilon * deltaT / Math.abs(nextY - mNextYHat));

        return nextY;
    }

    @Override
    protected double nextDeltaT(double t, double y, double nextY, double deltaT)
    {
        double recommendedStepSize = deltaT * mStepSizeFactor;

        if (!accept(t, y, nextY, deltaT))
            return recommendedStepSize;

        return Math.max(deltaT, recommendedStepSize);
    }

    @Override
    protected boolean accept(double t, double y, double nextY, double deltaT)
    {
        if (Math.abs(nextY - mNextYHat) > mEpsilon * deltaT)
            return false;
        return true;
    }


    public void setEpsilon(double epsilon)
    {
        mEpsilon = epsilon;
    }


    public void setSafetyFactor(double safetyFactor)
    {
        mSafety = safetyFactor;
    }

    private void throwIfValuesNotSet()
    {
        if (mEpsilon <= 0)
            throw new IllegalStateException("epsilon is not properly initialized " + mEpsilon);
        if (mSafety <= 0)
            throw new IllegalArgumentException("safety factor is not propery initialized " + mSafety);
    }

    private double mEpsilon, mSafety;
    private double mStepSizeFactor;
    private double mNextYHat;
}
