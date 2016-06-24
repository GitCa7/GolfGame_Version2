package framework.main;

import framework.logging.Logger;
import physics.collision.detection.BroadCollisionFinder;
import physics.collision.detection.CollisionDetector;
import physics.collision.detection.NaiveCollisionFinder;
import physics.collision.detection.NarrowCollisionFinder;
import physics.constants.PhysicsCoefficients;
import physics.generic.numerical.ode.BogackiShampine;
import physics.generic.numerical.ode.ODESolver;
import physics.generic.numerical.ode.RungeKutta4;

import java.util.Random;

/**
 * Data class for storing the game settings
 * created 22.06.16
 *
 * @author martin
 */
public class GameSettings
{
    public GameSettings()
    {
        mHitNoiseBound = 0f;
        mRandomGenerator = new Random(System.currentTimeMillis());
        mLogger = null;
        BogackiShampine bs = new BogackiShampine();
        bs.setEpsilon(Math.pow(10, -PhysicsCoefficients.ARITHMETIC_PRECISION));
        bs.setSafetyFactor(2);
        mODESolver = new RungeKutta4();

        BroadCollisionFinder bf = null;
        NarrowCollisionFinder nf = new NaiveCollisionFinder();
        mCollisionDetector = new CollisionDetector(bf, nf);
    }

    public float mHitNoiseBound;
    public Random mRandomGenerator;
    public Logger mLogger;
    public ODESolver mODESolver;
    public CollisionDetector mCollisionDetector;
}
