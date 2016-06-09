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
	 */
	public Game (Engine e, HashMap<Player, Ball> ballMap)
	{
		mEngine = e;
		mBallMap = ballMap;
		mGlobalState = new Entity();
        init();
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
			Turn turn = CompoMappers.TURN.get (p.mEntity);
			if (turn.mTurn)
				turnPlayers.add (p);
		}
		return turnPlayers;
	}

	/**
	 * 
	 * @return all ball entities stored
	 */
	public ArrayList<Ball> getBalls() 
	{ return new ArrayList<Ball> (mBallMap.values()); }
	
	/**
	 *
	 * @param p a player participating in this game
	 * @return the ball associated with p
	 */
	public Ball getBall (Player p)
	{
		if (!mBallMap.containsKey(p))
			throw new IllegalArgumentException ("player is not contained and is not mapped to ball.");
		return mBallMap.get(p);
	}

	/**
	 *
	 * @return true if the simulation is currently running (i.e. balls are moving) and no hit can be executed
	 */
	public boolean isBusy()
	{
		return CompoMappers.BUSY.get(mGlobalState).mBusy;
	}

	/**
	 * @return true if there is at least one player whose ball is not in the hole.
     */
	public boolean isActive()
	{
		return CompoMappers.ACTIVE.get(mGlobalState).mActive;
	}

	/**
	 * makes the current player hit the ball with force
	 * Precondition: the game is not busy
	 * @param force directed force to excert on ball
	 */
	public void hit (Player p, Vector3 force)
	{
		Ball ballHit = getBall (p);
		Force forceComp = physics.constants.CompoMappers.FORCE.get(ballHit.mEntity);
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
		//set default
		active.mActive = true;
		busy.mBusy = false;

		mGlobalState.add(active);
		mGlobalState.add (busy);
		
		
	}

	private Engine mEngine;
	private HashMap<Player, Ball> mBallMap;
	private Entity mGlobalState;
}
