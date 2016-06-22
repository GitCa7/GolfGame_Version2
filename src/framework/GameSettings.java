package framework;

import framework.logging.Logger;
import physics.generic.ode.Euler;
import physics.generic.ode.ODESolver;
import physics.generic.ode.RungeKutta4;

import java.util.Random;

/**
 * Data class for storing the game settings
 * created 22.06.16
 *
 * @author martin
 */
public class GameSettings
{
    public float mHitNoiseBound = 0f;
    public Random mRandomGenerator = new Random(System.currentTimeMillis());
    public Logger mLogger = null;
    public ODESolver mODESolver = new RungeKutta4();
}
