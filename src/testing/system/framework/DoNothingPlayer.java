package testing.system.framework;

import com.badlogic.gdx.math.Vector3;
import framework.main.Game;
import framework.main.PlayerObserver;

/**
 * created 23.06.16
 *
 * @author martin
 */
public class DoNothingPlayer extends PlayerObserver
{

    @Override
    public Vector3 getForce(Game state)
    {
        return new Vector3();
    }
}
