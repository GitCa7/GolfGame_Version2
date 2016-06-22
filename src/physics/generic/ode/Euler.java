package physics.generic.ode;

/**
 * ODE solver for Euler's method
 * created 22.06.16
 *
 * @author martin
 */
public class Euler extends ODESolver
{

    /**
     * parametric constructor
     *
     * @param equation od equation to solve for
     * @param initialT initial t condition
     * @param initialY initial y condition
     */
    public Euler(ODEquation equation, double initialT, double initialY)
    {
        super(equation, initialT, initialY);
    }

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
