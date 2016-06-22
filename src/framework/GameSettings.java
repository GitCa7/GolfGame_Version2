package framework;

import framework.logging.Logger;
import physics.generic.ode.ODESolver;

import java.util.Random;

/**
 * Data class for storing the game settings
 * created 22.06.16
 *
 * @author martin
 */
public class GameSettings
{
    public float mHitNoiseBound;
    public Random mRandomGenerator;
    public Logger mLogger;
    public ODESolver mODESolver;
}
