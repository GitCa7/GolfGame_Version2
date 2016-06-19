package framework.testing;

import com.badlogic.gdx.math.Vector3;
import framework.Game;
import framework.GameConfigurator;
import framework.GameState;
import framework.SimulatedGame;
import org.junit.Test;
import physics.geometry.planar.Triangle;
import physics.geometry.planar.TriangleBuilder;
import physics.geometry.spatial.Box;
import physics.geometry.spatial.BoxParameter;
import physics.geometry.spatial.BoxPool;
import physics.geometry.spatial.SolidTranslator;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * @author martin
 */
public class SimulationTest
{



    @Test
    /**
     * test whether applying a force in a cloned game yields two different games
     */
    public void testDeepCopy()
    {
        GameConfigurator config = new GameConfigurator();

        Triangle ground = new TriangleBuilder(new Vector3(-10000, 0, 10000), new Vector3(10000, 0, 10000), new Vector3(0, 0, -10000)).build();
        ArrayList<Triangle> groundList = new ArrayList<>();
        groundList.add(ground);
        config.setTerrain(groundList);
        config.setHole(new Vector3(10000, 0, 0), 50);

        config.addHumanAndBall("tester", 1, 1, new Vector3());

        Game game = config.game();

        Vector3 hitForce = new Vector3(50, 0, 50);
        float dt = .1f;

        SimulatedGame simulation = game.getGameSimulation(game.getCurrentPlayers().get(0));
        GameState simulationCopyState = simulation.getGameState();
        simulation.play(hitForce, dt);


        assertTrue(!simulation.getBallPosition().epsilonEquals(simulationCopyState.getBallPosition(), 0.01f));
    }


    @Test
    /**
     * test whether applying the same force using the same step size in two independent simulations yields the same result
     */
    public void parallelSimulationTest()
    {
        GameConfigurator config = new GameConfigurator();

        Triangle ground = new TriangleBuilder(new Vector3(-10000, 0, 10000), new Vector3(10000, 0, 10000), new Vector3(0, 0, -10000)).build();
        ArrayList<Triangle> groundList = new ArrayList<>();
        groundList.add(ground);
        config.setTerrain(groundList);

        config.setHole(new Vector3(10000, 0, 0), 50);

        config.addHumanAndBall("tester", 1, 1, new Vector3());

        Game game = config.game();

        Vector3 hitForce = new Vector3(50, 0, 50);
        float dt = .1f;

        SimulatedGame simulation = game.getGameSimulation(game.getCurrentPlayers().get(0));
        GameState simulationCopyState = simulation.getGameState();
        simulation.play(hitForce, dt);
        GameState simulationEndState = simulation.getGameState();

        simulation.setGameState(simulationCopyState);
        simulation.play(hitForce, dt);

        assertTrue(simulationEndState.getBallPosition().epsilonEquals(simulationCopyState.getBallPosition(), 0.01f));
    }

}
