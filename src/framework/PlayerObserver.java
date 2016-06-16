package framework;

import com.badlogic.gdx.math.Vector3;
import framework.constants.CompoMappers;
import framework.entities.Player;
import physics.entities.Ball;

/**
 * Abstract base class for observers allowing players to hit the ball
 * created 16.06.16
 *
 * @author martin
 */
public abstract class PlayerObserver implements GameObserver
{

    public PlayerObserver(Player match)
    {
        mMatchingPlayer = match;
        mTurn = false;
    }

    public void update(Game state)
    {
        Player current = state.getCurrentPlayers().get(0);
        if (mMatchingPlayer.equals(current))
        {
            if (!mTurn)
            {
                mTurn = true;
                state.hit(mMatchingPlayer, getForce(state));
            }
        }
        else
            mTurn = false;
    }

    /**
     * @return the force the player wishes to apply to the ball
     */
    public abstract  Vector3 getForce(Game state);

    /** store the matching player */
    private Player mMatchingPlayer;
    /** store whether it was this player's turn at the previous update already */
    private boolean mTurn;
}
