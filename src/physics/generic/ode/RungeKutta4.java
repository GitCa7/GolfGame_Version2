package physics.generic.ode;

/**
 * created 16.06.16
 *
 * @author martin
 */
public class RungeKutta4 extends ODESolver
{

    @Override
    protected double evalutate(double t, double y, double deltaT)
    {
        double k1, k2, k3, k4;
        k1 = deltaT * mEquation.evaluate(t, y);
        k2 = deltaT * mEquation.evaluate(t + .5 * deltaT, y + .5 * k1);
        k3 = deltaT * mEquation.evaluate(t + .5 * deltaT, y + .5 * k2);
        k4 = deltaT * mEquation.evaluate(t + deltaT, y + k3);
        return (y + (k1 + 2 * k2 + 2 * k3 + k4) / 6);
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
