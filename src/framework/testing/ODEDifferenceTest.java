package framework.testing;

import com.badlogic.gdx.math.Vector3;
import framework.Game;
import framework.GameConfigurator;
import framework.GameSettings;
import framework.logging.Logger;
import physics.components.Position;
import physics.constants.CompoMappers;
import physics.constants.PhysicsCoefficients;
import physics.entities.Ball;
import physics.generic.ode.BogackiShampine;
import physics.generic.ode.Euler;
import physics.generic.ode.ODESolver;
import physics.generic.ode.RungeKutta4;

import java.io.File;
import java.io.IOException;

/**
 * created 23.06.16
 *
 * @author martin
 */
public class ODEDifferenceTest
{

    public static void main(String[] args)
    {
        ODEDifferenceTest test = new ODEDifferenceTest();
        Logger logger = new Logger();

        test.run(new Euler(), "EULER", logger);
        test.run(new RungeKutta4(), "RK4", logger);

        BogackiShampine bogackiShampine = new BogackiShampine();
        bogackiShampine.setEpsilon(Math.pow(10, PhysicsCoefficients.ARITHMETIC_PRECISION));
        bogackiShampine.setSafetyFactor(2);
   //     test.run(bogackiShampine, "BOGSHAM", logger);

        String[] names = new String[maxIteration + 1];
        names[0] = NAME_TAG;
        for (int cIterationName = 0; cIterationName < maxIteration; ++cIterationName)
            names[cIterationName + 1] = ITERATION_TAG + cIterationName;

        try
        {
            logger.exportLog(new File("/home/martin/Schreibtisch/@block5/project/ODEDifferenceTest.csv"), names);
        }
        catch (IOException ioe)
        {
            System.out.println("could not export");
        }
    }

    public static int START_STEPS = 10, END_STEPS = 100;
    public static int STEPS_INCREMENT = 10;

    public static float TIME_TICK = .001f;

    public static int ITERATION_LIMIT = 2500;

    public static Vector3[] hits = new Vector3[3];

    public static int maxIteration  = 0;

    public static String NAME_TAG = "method";
    public static String ITERATION_TAG = "u";

    public void run(ODESolver solver, String methodName, Logger logger)
    {
        hits[0] = new Vector3(1000, 0, 0);
        hits[1] = new Vector3(0, 1500, 0);
        hits[2] = new Vector3(800, 800, 800);

        for (int cSteps = START_STEPS; cSteps <= END_STEPS; cSteps += STEPS_INCREMENT)
        {
            GameConfigurator config = DemoCourses.getDemoGame(1);
            GameSettings settings = new GameSettings();
            settings.mODESolver = solver;
            PhysicsCoefficients.ODE_STEPS = cSteps;

            config.setSettings(settings);

            Game game = config.game();

            logger.addItem(NAME_TAG, methodName + cSteps);
            runGame(game, logger);
            logger.closeSection();

            System.out.println (methodName + " " + cSteps);
        }


    }


    private void runGame(Game game, Logger logger)
    {
        Ball onlyBall = game.getBall(game.getCurrentPlayers().get(0));
        Position ballPosition = CompoMappers.POSITION.get(onlyBall.mEntity);

        int cUpdate = 0;
        for (int cHit = 0; cHit < hits.length; ++cHit)
        {
            game.hit(game.getCurrentPlayers().get(0), hits[cHit]);
            logger.addItem("u" + cUpdate, Float.toString(ballPosition.len()));
            do
            {
                ++cUpdate;
                game.tick(TIME_TICK);

                logger.addItem(ITERATION_TAG + cUpdate, Float.toString(ballPosition.len()));
            } while (game.isBusy() && cUpdate < ITERATION_LIMIT);
        }

        if (cUpdate > maxIteration)
            maxIteration = cUpdate;

    }
}
