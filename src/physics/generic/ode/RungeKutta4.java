package physics.generic.ode;

/**
 * created 16.06.16
 *
 * @author martin
 */
public class RungeKutta4 extends ODESolver
{

    @Override
    protected double evaluate(ODEquation equation, double t, double[] ys, double deltaT, int index)
    {
        double k1, k2, k3, k4;
        k1 = deltaT * equation.evaluate(t, ys);
        k2 = deltaT * equation.evaluate(t + .5 * deltaT, addToVector(ys, .5 * k1));
        k3 = deltaT * equation.evaluate(t + .5 * deltaT, addToVector(ys, .5 * k2));
        k4 = deltaT * equation.evaluate(t + deltaT, addToVector(ys, k3));
        return (ys[index] + (k1 + 2 * k2 + 2 * k3 + k4) / 6);
    }

    @Override
    protected double nextDeltaT(ODEquation equation, double t, double[] ys, double[] nextYs, double deltaT, int index)
    {
        return deltaT;
    }

    @Override
    protected boolean accept(ODEquation equation, double t, double[] ys, double[] nextYs, double deltaT, int index)
    {
        return true;
    }
}
