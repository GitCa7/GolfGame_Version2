package physics.generic.ode;

import java.util.Arrays;

/**
 * Template method class for solving differnetial equations of the form y' = f(t, y).
 * Allows for non-adaptive and adaptive methods alike.
 * created 15.06.16
 *
 * @author martin
 */
public abstract class ODESolver
{

    /**
     * Solves the initial value problem and sets the initial values to the solution at the final time step
     * @param finalT the final t value
     * @param numberSteps the initial number of steps to get to finalT. Depending on the method used, this
     *                    may change.
     * @return the solution of the differential equation at finalT
     */
    public double[] solve(double finalT, int numberSteps)
    {
        if (mEquations == null)
            throw new IllegalStateException("equation is not set for ode solver, cannot solve");

        //set initial conditions
        double t = mInitialT;
        double[] ys = mInitialYs;
        double dt = (finalT - mInitialT) / numberSteps;

        double[] nextYs = new double[mInitialYs.length];

        for (int cStep = 0; cStep < numberSteps; ++cStep)
        {
            //update t
            t  = mInitialT + cStep * dt;


            for (int cEquation = 0; cEquation < mInitialYs.length; ++cEquation)
            {
                do {
                    //set intermediary values
                    nextYs[cEquation] = evaluate(mEquations[cEquation], t, ys, dt, cEquation);
                    dt = nextDeltaT(mEquations[cEquation], t, ys, nextYs, dt, cEquation);

                } while (!accept(mEquations[cEquation], t, ys, nextYs, dt, cEquation));
            }
            //set new number of steps (depending on finalT - mInitialT)
            numberSteps = (int) Math.round((finalT - mInitialT) / dt);
            cStep = (int) Math.round((t - mInitialT) / dt);
            //update y
            ys = Arrays.copyOf(nextYs, nextYs.length);
        }

        mInitialT = finalT;
        mInitialYs = ys;

        return ys;
    }

    /**
     * @return the initial y value
     */
    public double[] getInitialYs() { return mInitialYs; }

    /**
     * @return the initial t value
     */
    public double getInitialT() { return mInitialT; }

    /**
     * computes the value of y for the next time step
     * @param t current value of t
     * @param y current value of y
     * @param deltaT change of t from this step to the next step
     * @return the solution at t + deltaT
     */
    protected abstract double evaluate(ODEquation equation, double t, double[] ys, double deltaT, int index);

    /**
     * compute the value of deltaT for the next iteration
     * @param t current value of t
     * @param y current value of y
     * @param nextY value computed for y at t + deltaT
     * @param deltaT change of t from this step to the next step
     * @return the change of t to be used for the next iteration
     */
    protected abstract double nextDeltaT(ODEquation equation, double t, double[] ys, double[] nextY, double deltaT, int index);

    /**
     * @param t current value of t
     * @param y current value of y
     * @param nextY value computed for y at t + deltaT
     * @param deltaT change of t from this step to the next step
     * @return True if the value of nextY is accepted. If the value is accepted, the algorithm
     * will proceed to compute the following next y value. If the value is not accepted, the
     * algorithm will recompute the next y value using the current conditions.
     */
    protected abstract boolean accept(ODEquation equation, double t, double[] ys, double[] nextY, double deltaT, int index);


    protected double[] addToVector(double[] vec, double value)
    {
        double[] newVec = new double[vec.length];
        for (int cElem = 0; cElem < vec.length; ++cElem)
            newVec[cElem] = vec[cElem] + value;
        return newVec;
    }


    public void setEquations(ODEquation ... equations)
    {
        mEquations = equations;
    }

    public void setInitialYs(double ... ys)
    {
        mInitialYs = ys;
    }

    public void setInitialT(double t)
    {
        mInitialT = t;
    }

    public void set(ODEquation[] equations, double[] initialYs, double initialT)
    {
        mEquations = equations;
        mInitialYs = initialYs;
        mInitialT = initialT;
    }

    protected ODEquation[] mEquations;
    private double mInitialT;
    private double[] mInitialYs;
}
