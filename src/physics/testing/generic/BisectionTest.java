package physics.testing.generic;

import org.junit.Test;
import physics.generic.Bisection;

import static org.junit.Assert.assertTrue;

/**
 * created 11.06.16
 *
 * @author martin
 */
public class BisectionTest
{

    /**
     * solver to find root of linear function of form ax + b = f(x)
     */
    public class LinFunRoot extends Bisection<Double>
    {

        public double EPS = .001;

        public LinFunRoot(double start, double end, double a, double b)
        {
            super(start, end);
            mA = a;
            mB = b;
        }

        @Override
        public Double pickMiddle(Double subIntervalStart, Double subIntervalEnd)
        {
            return (subIntervalStart + subIntervalEnd) / 2;
        }

        public double funValue(Double x)
        {
            return (mA * x + mB);
        }

        @Override
        public int compare(Double point)
        {
            double y = funValue(point);
            if (y == 0)
                return 0;
            return (int) Math.signum(y);
        }

        @Override
        public boolean accept(Double point)
        {
            return Math.abs (funValue(point)) < EPS;
        }

        private double mA, mB;
    }


    @Test
    public void testSimplest()
    {
        double a = 1, b = 0;
        double root = b/a;

        LinFunRoot solver = new LinFunRoot(5, -3, a, b);
        solver.run();

        double solution = solver.funValue(solver.getSolution());
        assertTrue (Math.abs(solution) < solver.EPS);
    }

    @Test
    public void testTranslated()
    {
        double a = 1, b = 5;
        double root = b/a;

        LinFunRoot solver = new LinFunRoot(2, -6, a, b);
        solver.run();

        double solution = solver.funValue(solver.getSolution());
        assertTrue (Math.abs(solution) < solver.EPS);
    }

    @Test
    public void testSteep()
    {
        double a = 10, b = 1.5;
        double root = b/a;

        LinFunRoot solver = new LinFunRoot(1, -1, a, b);
        solver.run();

        double solution = solver.funValue(solver.getSolution());
        assertTrue (Math.abs(solution) < solver.EPS);
    }

    @Test
    public void testInvertedParameters()
    {
        double a = 1, b = 0;
        double root = 0;

        LinFunRoot solver = new LinFunRoot(-5, 3, a, b);
        solver.run();

        double solution = solver.funValue(solver.getSolution());
        assertTrue (Math.abs(solution) < solver.EPS);
    }
}
