package framework.main;

import Entities.FollowCamera;
import com.badlogic.gdx.math.Vector3;
import framework.entities.Player;
import framework.logging.Logger;
import framework.main.Game;
import framework.main.GameObserver;

/**
 * Abstract base class for observers allowing players to hit the ball
 * created 16.06.16
 *
 * @author martin
 */
public abstract class PlayerObserver implements GameObserver
{
    private FollowCamera cam;

    public PlayerObserver()
    {
        mTurn = false;
    }

    public Logger getLog() { return mLogger; }

    /**
     * sets the player to match to match
     * @param match player to do input for
     */
    public void setMatchingPlayer(Player match)
    {
        mMatchingPlayer = match;
        mTurn = false;
    }
    public void setCam(FollowCamera cam){this.cam=cam;}
    public void update(Game state)
    {
        if (mMatchingPlayer == null)
            throw new IllegalStateException("player observer was used before player was set!");

        if (!state.isBusy())
        {
            Player current = state.getCurrentPlayers().get(0);
            if (mMatchingPlayer.equals(current))
            {
                state.hit(mMatchingPlayer, getForce(state));
            }
        }
    }

    protected void log(String name, String description, boolean closeSection)
    {
        if (mLogger != null)
        {
            mLogger.addItem(name, description);
            if (closeSection)
                mLogger.closeSection();
        }
    }

    /**
     * sets the logger
     * @param logger the logger to set
     */
    public void setLogger(Logger logger)
    {
        mLogger = logger;
    }

    /**
     * @return the force the player wishes to apply to the ball
     */
    public abstract  Vector3 getForce(Game state);

    /**
     * @return the player on whose behalf this object observes the game
     */
    protected Player getPlayer() { return mMatchingPlayer; }



    /** store the matching player */
    private Player mMatchingPlayer;
    /** store whether it was this player's turn at the previous update already */
    private boolean mTurn;

    private Logger mLogger;
}
