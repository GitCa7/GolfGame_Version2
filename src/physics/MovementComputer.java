package physics;

import com.badlogic.gdx.math.Vector3;
import physics.generic.ode.ODEquation;

/**
 * Class for calculating the movement of a ball only using differential equations.
 * created 22.06.16
 *
 * @author martin
 */
public class MovementComputer
{

    public class VelocityDE implements ODEquation
    {
        @Override
        public double evaluate(double t, double[] ys)
        {
            return ys[0] / t;
        }
    }

    public class PositionDE implements ODEquation
    {

        public PositionDE(double v0) { mV0 = v0; }

        @Override
        public double evaluate(double t, double[] ys)
        {
            return (mV0 + ys[0]) * t;
        }

        private double mV0;
    }

    public MovementComputer(float initTime, float finalTime, int timeSteps)
    {
        setSolveInterval(initTime, finalTime, timeSteps);
        mSolution = new Vector3[2];
    }

    public Vector3 getSolutionPosition()
    {

    }

    public Vector3 getSolutionVelocity()
    {
        
    }

    /**
     * sets the interval for which to compute a solution and the step size
     * @param initTime
     * @param finalTime
     * @param timeSteps
     */
    public void setSolveInterval(float initTime, float finalTime, int timeSteps)
    {
        mInitTime = initTime;
        mFinalTime = finalTime;
        mTimeSteps = timeSteps;
    }

    /**
     * solve for the given force, velocity, position and mass for the stored time interval
     * @param force
     * @param velocity
     * @param position
     * @param mass
     */
    public void solve(Vector3 force, Vector3 velocity, Vector3 position, float mass)
    {

    }


    private void solveOneDimension(Vector3 force, Vector3 velocity, Vector3 position, float mass, float init)


    private ODEquation mPositionDE, VelocityDE;
    private Vector3[] mSolution;

    private float mInitTime, mFinalTime;
    private int mTimeSteps;
}
