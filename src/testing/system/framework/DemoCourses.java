package testing.system.framework;

import com.badlogic.gdx.math.Vector3;
import framework.main.GameConfigurator;
import physics.geometry.planar.Triangle;
import physics.geometry.planar.TriangleBuilder;

import java.util.ArrayList;

/**
 * created 23.06.16
 *
 * @author martin
 */
public class DemoCourses
{

    public static float BALL_RADIUS = 3;
    public static float BALL_MASS = 2;
    public static Vector3 INIT_BALL_POSITION = new Vector3();

    public static Vector3 HOLE_POSITION = new Vector3(100, 0, 0);
    public static float HOLE_SIZE = 25;

    public static GameConfigurator getDemoGame(int index)
    {
        switch(index)
        {
            case 1: return getGame1();

        }

        return null;
    }

    /**
     * @return game with flat terrain
     */
    public static GameConfigurator getGame1()
    {
        GameConfigurator config = new GameConfigurator();
        config.addHumanAndBall("dummy", BALL_RADIUS, BALL_MASS, INIT_BALL_POSITION, new DoNothingPlayer());
        config.setHole(HOLE_POSITION, HOLE_SIZE);

        Triangle bigFlat = new TriangleBuilder(new Vector3(-10000, 0, 10000), new Vector3(10000, 0, 10000), new Vector3(0, 0, -10000)).build();
        ArrayList<Triangle> flatList = new ArrayList<>();
        flatList.add(bigFlat);
        config.setTerrain(flatList);
        return config;
    }
}
