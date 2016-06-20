package physics.testing.generic;

import org.junit.Test;
import physics.generic.ode.ODEquation;
import physics.generic.ode.RungeKutta4;

import static org.junit.Assert.assertTrue;

/**
 * created 16.06.16
 *
 * @author martin
 */
public class RungeKutta4Test
{

    @Test
    public void testVelocity()
    {
        ODEquation eq = new ODEquation()
        {
            public double evaluate(double t, double y)
            {
                return y / t;
            }
        };

        double initT = 1, initY = 2;
        double finalT = 10;
        int steps = 10;

        RungeKutta4 solver = new RungeKutta4(eq, initT, initY);
        double solutionObtained = solver.solve(finalT, steps);

        System.out.println ("solution for velocity at " + finalT + " " + solutionObtained);

        double solution = 20;
        double eps = 1;
        assertTrue(Math.abs(solutionObtained - solution) < eps);
    }
}
