package framework.components;

import framework.entities.Player;
import physics.components.ComponentFactory;

/**
 * Factory producing next player components to handle player transitions.
 * Note that the player held in components cannot be copied, so all components produced
 * without setNextPlayer call will hold a transition to the same player.
 * @author martin
 */
public class NextPlayerFactory implements ComponentFactory
{
	/**
	 * default constructor initializes map
	 */
	public NextPlayerFactory()
	{
		mNextPlayer = null;
	}
	
	
	public NextPlayer produce()
	{
		return new NextPlayer(mNextPlayer);
	}
	
	/**
	 * adds to to the list of players following from.
	 * @param transition player to transition to
	 */
	public void setNextPlayer (Player transition)
	{
		mNextPlayer = transition;
	}
	
	
	
	private Player mNextPlayer;
}
