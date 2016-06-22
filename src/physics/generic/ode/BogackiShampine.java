package physics.generic.ode;

/**
 * ODE solver for the Bogacki-Shampine adaptive method
 * created 22.06.16
 *
 * @author martin
 */
public class BogackiShampine extends ODESolver
{

    public BogackiShampine()
    {
        mEpsilon = -1;
        mSafety = -1;
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

    @Override
    protected double evaluate(ODEquation equation, double t, double[] ys, double deltaT, int index)
    {
        double k1, k2, k3, k4;
        k1 = deltaT * equation.evaluate(t, ys);
        k2 = deltaT * equation.evaluate(t + .5 * deltaT, addToVector(ys,.5 * k1));
        k3 = deltaT * equation.evaluate(t + .75 * deltaT, addToVector(ys, .75 * k2));

        double nextY = ys[index] + 2f/9 * k1 + 3f/9 * k2 + 4f/9 * k3;

        double[] nextYVec = new double[1];
        nextYVec[0] = nextY;
        k4 = deltaT * equation.evaluate(t + deltaT, nextYVec);

        mNextYHat = ys[index] + 7f/24 * k1 + 1f/4 * k2 + 1f/3 * k3 + 1f/8 * k4;

        mStepSizeFactor = Math.sqrt(mEpsilon * deltaT / Math.abs(nextY - mNextYHat));

        return nextY;
    }

    @Override
    protected double nextDeltaT(ODEquation equation, double t, double[] ys, double nextY, double deltaT, int index)
    {
        double recommendedStepSize = deltaT * mStepSizeFactor;

        if (!accept(equation, t, ys, nextY, deltaT, index))
            return recommendedStepSize;

        return Math.max(deltaT, recommendedStepSize);
    }

    @Override
    protected boolean accept(ODEquation equation, double t, double[] ys, double nextY, double deltaT, int index)
    {
        if (Math.abs(nextY - mNextYHat) > mEpsilon * deltaT)
            return false;
        return true;
    }

    private double mEpsilon, mSafety;
    private double mStepSizeFactor;
    private double mNextYHat;
}
