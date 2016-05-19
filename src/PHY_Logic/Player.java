package PHY_Logic;

/**
 * Class storing a player's name and hit count during the game.
 * @autor martin
 * created 17.05.2016
 */
public class Player
{
	/**
	 * constructs player object and initialized the hit count to 0
	 * @param name name of the player
	 */
	public Player (String name)
	{
		mName = name;
		mHits = 0;
	}

	/**
	 *
	 * @return the player's name
	 */
	public String getName() { return mName; }

	/**
	 *
	 * @return the player's hit count
	 */
	public int getHits() { return mHits; }

	/**
	 *
	 * @param comp a player to compare this with
	 * @return true if the names of both objects are equal
	 */
	public boolean equals (Player comp)
	{

	}

	/**
	 * increments the hit counter
	 */
	public void incrementHit()
	{
		++mHits;
	}


	private String mName;
	private int mHits;
}
