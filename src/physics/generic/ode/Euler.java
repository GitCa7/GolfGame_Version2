package physics.generic.ode;

/**
 * ODE solver for Euler's method
 * created 22.06.16
 *
 * @author martin
 */
public class Euler extends ODESolver
{


    @Override
    protected double evalutate(double t, double y, double deltaT)
    {
        return y + deltaT * mEquation.evaluate(t, y);
    }

    @Override
    protected double nextDeltaT(double t, double y, double nextY, double deltaT)
    {
        return deltaT;
    }

    @Override
    protected boolean accept(double t, double y, double nextY, double deltaT)
    {
        return true;
    }
}
