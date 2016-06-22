package physics.generic.ode;

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
    public double solve(double finalT, int numberSteps)
    {
        if (mEquation == null)
            throw new IllegalStateException("equation is not set for ode solver, cannot solve");

        //set initial conditions
        double t = mInitialT, y = mInitialY;
        double dt = (finalT - mInitialT) / numberSteps;

        double nextY;

        for (int cStep = 0; cStep < numberSteps; ++cStep)
        {
            //update t
            t  = mInitialT + cStep * dt;

            do
            {
                //set intermediary values
                nextY = evalutate(t, y, dt);
                dt = nextDeltaT(t, y, nextY, dt);

            } while (!accept(t, y, nextY, dt));

            //set new number of steps (depending on finalT - mInitialT)
            numberSteps = (int) Math.round((finalT - mInitialT) / dt);
            cStep = (int) Math.round((t - mInitialT) / dt);
            //update y
            y = nextY;
        }

        mInitialT = finalT;
        mInitialY = y;

        return y;
    }

    /**
     * @return the initial y value
     */
    public double getInitialY() { return mInitialY; }

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
    protected abstract double evalutate(double t, double y, double deltaT);

    /**
     * compute the value of deltaT for the next iteration
     * @param t current value of t
     * @param y current value of y
     * @param nextY value computed for y at t + deltaT
     * @param deltaT change of t from this step to the next step
     * @return the change of t to be used for the next iteration
     */
    protected abstract double nextDeltaT(double t, double y, double nextY, double deltaT);

    /**
     * @param t current value of t
     * @param y current value of y
     * @param nextY value computed for y at t + deltaT
     * @param deltaT change of t from this step to the next step
     * @return True if the value of nextY is accepted. If the value is accepted, the algorithm
     * will proceed to compute the following next y value. If the value is not accepted, the
     * algorithm will recompute the next y value using the current conditions.
     */
    protected abstract boolean accept(double t, double y, double nextY, double deltaT);


    public void set(ODEquation equation, double initialY, double initialT)
    {
        mEquation = equation;
        mInitialY = initialY;
        mInitialT = initialT;
    }

    protected ODEquation mEquation;
    private double mInitialY, mInitialT;
}
