package framework.testing;

import com.badlogic.gdx.math.Vector3;
import framework.Game;
import framework.GameConfigurator;

/**
 * @author martin
 */
public class MockLoader
{
    public MockLoader() {}

    public Game load()
    {
        GameConfigurator config = new GameConfigurator();
        config.addPlayerAndBall("me", 3, 5, new Vector3(0, 0, 0));
        return config.game();
    }
}
