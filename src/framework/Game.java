package framework;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector3;
import physics.entities.Ball;
import physics.entities.Hole;

import java.util.HashMap;

/**
 * Class owning an engine establishing the rules of the game, a mapping of player to balls and a unique hole.
 * Provides an interface to modify those resources and ensures that any modification compolies with the rules set out
 * by the engine used.
 * @autor martin
 * created 17.05.2016
 */
public class Game
{
	/**
	 * parametric constructor
	 * @param e engine to use
	 * @param ballMap mapping of players to balls
	 * @param h unique hole of the course
	 */
	public Game (Engine e, HashMap<Player, Ball> ballMap, Hole h)
	{

	}

	/**
	 *
	 * @return the player whose turn it currently is. This comprises also the period during which the simulation is running following
	 * a hit.
	 */
	public Player getCurrentPlayer()
	{

	}

	/**
	 *
	 * @param p a player participating in this game
	 * @return the ball associated with p
	 * @throws some exception
	 */
	public Ball getBall (Player p)
	{

	}

	/**
	 *
	 * @return true if the simulation is currently running (i.e. balls are moving) and no hit can be executed
	 */
	public boolean isBusy()
	{

	}

	/**
	 * makes the current player hit the ball with force
	 * Precondition: the game is not busy
	 * @param force directed force to excert on ball
	 */
	public void hit (Vector3 force)
	{

	}

	/**
	 * Advances the game by ticks units.
	 * @param ticks amount of ticks greater than 0 by which to advance the state of the game
	 */
	public void tick (float ticks)
	{

	}

	private Engine mEngine;
	private HashMap<Player, Ball> mBallMap;
	private Hole mHole;
}
