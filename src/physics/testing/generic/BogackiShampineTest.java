package physics.testing.generic;

import org.junit.Test;
import physics.generic.ode.BogackiShampine;
import physics.generic.ode.ODEquation;
import physics.generic.ode.RungeKutta4;

import static org.junit.Assert.assertTrue;

/**
 * created 16.06.16
 *
 * @author martin
 */
public class BogackiShampineTest
{

    public static <T> T[] array(T ... values)
    {
        return values;
    }

    @Test
    public void testVelocity()
    {
        ODEquation eq = new ODEquation()
        {
            public double evaluate(double t, double[] y)
            {
                return y[0] / t;
            }
        };

        double initT = new Double(1);
        double[] initY = new double[1];
        initY[0] = 2;

        double finalT = 10;
        int steps = 10;

        BogackiShampine solver = new BogackiShampine();
        solver.setEpsilon(.00001);
        solver.setSafetyFactor(2);
        solver.set(array(eq), initY, initT);
        double[] solutionObtained = solver.solve(finalT, steps);

        System.out.println ("solution for velocity at " + finalT + " " + solutionObtained[0]);

        double solution = 20;
        double eps = 1;
        assertTrue(Math.abs(solutionObtained[0] - solution) < eps);
    }

    @Test
    public void testSine()
    {
        ODEquation eq = new ODEquation()
        {
            public double evaluate(double t, double[] y)
            {
                return Math.sin(t) * y[0];
            }
        };

        double initT = new Double(1);
        double[] initY = new double[1];
        initY[0] = 1;

        double finalT = 10;
        int steps = 10;

        BogackiShampine solver = new BogackiShampine();
        solver.setEpsilon(.000001);
        solver.setSafetyFactor(2);
        solver.set(array(eq), initY, initT);
        double[] solutionObtained = solver.solve(finalT, steps);

        System.out.println ("solution for f at " + finalT + " " + solutionObtained[0]);

        double solution = 4;
        double eps = .5;
        assertTrue(Math.abs(solutionObtained[0] - solution) < eps);
    }
}
