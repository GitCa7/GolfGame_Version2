package framework.components;

import framework.entities.Player;
import physics.components.ComponentFactory;

/**
 * Factory producing next player components to handle player transitions.
 * Note that the player held in components cannot be copied, so all components produced
 * without setNextPlayer call will hold a transition to the same player.
 * @author martin
 */
public class PlayerOrderFactory implements ComponentFactory
{
	/**
	 * default constructor initializes map
	 */
	public PlayerOrderFactory()
	{
		mPreviousPlayer = null;
		mNextPlayer = null;
	}
	
	
	public PlayerOrder produce()
	{
		if (mNextPlayer == null || mPreviousPlayer == null)
			throw new IllegalStateException("cannot produce order component, resources not set");
		return new PlayerOrder(mPreviousPlayer, mNextPlayer);
	}

	/**
	 * sets the previous and next player for component construction
	 * @param previous the preceding player
	 * @param next the following player
     */
	public void setPlayers(Player previous, Player next)
	{
		setPreviousPlayer(previous);
		setNextPlayer(next);
	}

	/**
	 * sets player to use for preceding player
	 * @param previous the previous player
     */
	public void setPreviousPlayer (Player previous) { mPreviousPlayer = previous; }

	/**
	 * adds to to the list of players following from.
	 * @param transition player to transition to
	 */
	public void setNextPlayer (Player transition)
	{
		mNextPlayer = transition;
	}
	
	
	
	private Player mPreviousPlayer, mNextPlayer;
}
