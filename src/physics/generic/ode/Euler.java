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
    protected double evaluate(ODEquation equation, double t, double[] ys, double deltaT, int index)
    {
        return ys[index] + deltaT * equation.evaluate(t, ys);
    }

    @Override
    protected double nextDeltaT(ODEquation equation, double t, double[] ys, double nextY, double deltaT, int index) {
        return deltaT;
    }

    @Override
    protected boolean accept(ODEquation equation, double t, double[] ys, double nextY, double deltaT, int index) {
        return true;
    }
}
