package testing.system.framework;

import com.badlogic.gdx.math.Vector3;
import framework.main.Game;
import framework.main.GameConfigurator;

/**
 * @author martin
 */
public class MockLoader
{
    public MockLoader() {}

    public Game load()
    {
        GameConfigurator config = new GameConfigurator();
        config.addHumanAndBall("me", 3, 5, new Vector3(0, 0, 0));
        return config.game();
    }
}
