package framework;

import Entities.entity;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

import framework.components.Turn;
import framework.constants.CompoMappers;
import framework.constants.Families;
import framework.entities.Player;
import framework.internal.components.*;
import framework.internal.systems.*;
import framework.systems.EntityListener;
import physics.components.Force;
import physics.entities.Ball;
import physics.entities.Hole;

import java.util.ArrayList;
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
		mEngine = e;
		mBallMap = ballMap;
		mHole = h;
		mGlobalState = new Entity();
	}

	/**
	 *
	 * @return the player whose turn it currently is. This comprises also the period during which the simulation is running following
	 * a hit.
	 */
	public ArrayList<Player> getCurrentPlayers()
	{
		ArrayList<Player> turnPlayers = new ArrayList<>();
		for (Player p : mBallMap.keySet())
		{
			Turn turn = CompoMappers.TURN.get (p);
			if (turn.mTurn)
				turnPlayers.add (p);
		}
		return turnPlayers;
	}

	/**
	 *
	 * @param p a player participating in this game
	 * @return the ball associated with p
	 * @throws some exception
	 */
	public Ball getBall (Player p)
	{
		if (!mBallMap.containsKey(p))
			throw new IllegalArgumentException ("player is not contained and is not mapped to ball.");
		mBallMap.get(p);
	}

	/**
	 * @return the hole entity set for this game
	 */
	public Hole getHole()
	{
		return mHole;
	}

	/**
	 *
	 * @return true if the simulation is currently running (i.e. balls are moving) and no hit can be executed
	 */
	public boolean isBusy()
	{
		return CompoMappers.BUSY.get(mGlobalState);
	}

	/**
	 * makes the current player hit the ball with force
	 * Precondition: the game is not busy
	 * @param force directed force to excert on ball
	 */
	public void hit (Player p, Vector3 force)
	{
		Ball ballHit = getBall (p);
		Force forceComp = physics.constants.CompoMappers.FORCE.get(ballHit);
		forceComp.add (force);
	}

	/**
	 * Advances the game by ticks units.
	 * @param ticks amount of ticks greater than 0 by which to advance the state of the game
	 */
	public void tick (float ticks)
	{
		mEngine.update (ticks);
	}
	
	/**
	 * initializes internal systems
	 */
	private void init()
	{
		//internal systems
		ActiveSystem activeSystem = new ActiveSystem();
		BusySystem busySystem = new BusySystem();
		mEngine.addSystem (activeSystem);
		mEngine.addSystem (busySystem);
		//add entity listeners for systems added
		mEngine.addEntityListener (new EntityListener (activeSystem));
		mEngine.addEntityListener (new EntityListener (busySystem));
		
		//internal components of global state
		Active active = new Active();
		Busy busy = new Busy();
		mGlobalState.addComponent (active);
		mGlobalState.addComponent (busy);
		
		
	}

	private Engine mEngine;
	private HashMap<Player, Ball> mBallMap;
	private Hole mHole;
	private Entity mGlobalState;
}
