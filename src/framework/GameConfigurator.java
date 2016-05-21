package framework;

import com.badlogic.ashley.core.Engine;
import physics.entities.Ball;
import physics.entities.EntityFactory;
import physics.entities.Hole;
import physics.systems.SystemsTracker;

import java.util.HashMap;

/**
 * Class responsible for configuring an engine stored such that it contains all physics.entities, physics.components and physics.systems the user desires.
 * This class is also used as a factory for game classes sharing the same engine.
 * @autor martin
 * created 17.05.2016
 */
public class GameConfigurator
{
	/**
	 * constructs game configurator using a new engine with an empty map of balls, no hole set and no physics.systems stored
	 */
	public GameConfigurator ()
	{
		mEngine = new Engine();
	}

	/**
	 *
	 * @return an entity factory fit for use with this game configurator object
	 */
	public EntityFactory entityFactory()
	{

	}

	/**
	 * Precondition: there is at least one ball stored and the hole is set.
	 * @return a new game object. Games constructed by the same game configurator share the same engine.
	 */
	public Game game()
	{

	}

	/**
	 * adds a new ball b for player p.
	 * Precondition: there are no balls associated with p yet.
	 * @param p a player object
	 * @param b a ball object
	 */
	public void addBall (Player p, Ball b)
	{

	}

	/**
	 * sets the hole stored to h
	 * @param h a hole object
	 */
	public void setHole (Hole h)
	{

	}

	/**
	 * Produces count physics.entities using e and adds them to the engine.
	 * @param e a factory object producing physics.entities
	 * @param count the number of physics.entities to produce in e
	 */
	public void addEntities (EntityFactory e, int count)
	{

	}

	/** mapping of players to their balls */
	private HashMap<Player, Ball> mBallMap;
	/** the hole entity to be used */
	private Hole mHole;
	/** engine to configure */
	private Engine mEngine;
	/** objects tracking physics.systems in use */
	private SystemsTracker mSystemsTracker;
}
