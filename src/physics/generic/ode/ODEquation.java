package physics.generic.ode;

/**
 * abstract class for differential equation of form y' = f(t, y),
 * where the function value f(t, y) shall depend on the concrete implementation.
 * created 15.06.16
 *
 * @author martin
 */
public interface ODEquation
{
    public double evaluate(double t, double[] ys);
}
