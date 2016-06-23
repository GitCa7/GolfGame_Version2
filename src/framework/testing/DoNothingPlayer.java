package framework.testing;

import com.badlogic.gdx.math.Vector3;
import framework.Game;
import framework.PlayerObserver;

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
