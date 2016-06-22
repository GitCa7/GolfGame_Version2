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
import framework.logging.Logger;
import framework.systems.EntityListener;
import physics.components.Force;
import physics.entities.Ball;
import physics.entities.Hole;

import java.util.*;

/**
 * Class owning an engine establishing the rules of the game, a mapping of player to balls and a unique hole.
 * Provides an interface to modify those resources and ensures that any modification compolies with the rules set out
 * by the engine used.
 * @autor martin
 * created 17.05.2016
 */
public class Game
{
	public static boolean DEBUG = false;

	public static final String HIT_NAME = "HIT", ENGINE_TIME_NAME = "ENGINE_TIME", OBSERVER_TIME_NAME = "OBSERVER_TIME";

	/**
	 * parametric constructor
	 * @param e engine to use
	 * @param ballMap mapping of players to balls
	 */
	public Game (Engine e, HashMap<Player, Ball> ballMap, float hitNoiseBound)
	{
		mEngine = e;
		mBallMap = ballMap;
		mHitNoiseBound = hitNoiseBound;
		mGlobalState = new Entity();
		mObservers = new HashSet<>();

		mGen = new Random(System.currentTimeMillis());
		mLogger = null;
        init();
	}

	public Engine getEnigine(){
		return mEngine;
	}

	/**
	 * @param simulationPlayer the player accessing the simulation
	 * @return a simulation for simulationPlayer
     */
	public SimulatedGame getGameSimulation(Player simulationPlayer)
	{
		return new SimulatedGame(mEngine, getBall(simulationPlayer));
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


	public Logger getLogger() { return mLogger; }

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


	public void setLogger(Logger logger)
	{
		mLogger = logger;
	}

	/**
	 * makes the current player hit the ball with force
	 * Precondition: the game is not busy
	 * @param force directed force to excert on ball
	 */
	public void hit (Player p, Vector3 force)
	{
		if (DEBUG)
			System.out.println ("hit ball with " + force);

		force = force.cpy();
		force.x += mGen.nextGaussian() * mHitNoiseBound;
		force.y += mGen.nextGaussian() * mHitNoiseBound;
		force.z += mGen.nextGaussian() * mHitNoiseBound;

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
		long time = System.currentTimeMillis();
		mEngine.update (ticks);
  		if (mLogger != null)
			mLogger.addItem(ENGINE_TIME_NAME, Long.toString(System.currentTimeMillis() - time));

		time = System.currentTimeMillis();
		updateObservers();
		if (mLogger != null)
			mLogger.addItem(OBSERVER_TIME_NAME, Long.toString(System.currentTimeMillis() - time));
	}

	/**
	 * Attaches obs as an observer. This will make obs receive update calls whenever
	 * an event triggers.
	 * @param obs game observer to attach
	 * @throws IllegalArgumentException if obs is already attached and will not be attached again
     */
	public void attachObserver(GameObserver obs)
	{
		if (mObservers.contains(obs))
			throw new IllegalArgumentException("cannot attach this observer to the game, is already attached.");
		mObservers.add(obs);
	}

	/**
	 * Detaches obs as an observer.
	 * @param obs game observer to detach
	 * @throws IllegalArgumentException if obs was not attached previously
     */
	public void detachObserver(GameObserver obs)
	{
		if (!mObservers.contains(obs))
			throw new IllegalArgumentException("cannot detach this observer from game, was never attached before");
		mObservers.remove(obs);
	}

	/**
	 * updates all observers attached
	 */
	public void updateObservers()
	{
		/* add mechanism to avoid piling up of updates/data races
		class UpdateRunner implements Runnable
		{
			UpdateRunner (Game updateSource, Collection<GameObserver> updating)
			{
				mSource = updateSource;
				mUpdating = updating;
			}

			public void run()
			{
				for (GameObserver update : mUpdating)
					update.update(mSource);
			}

			private Game mSource;
			private Collection<GameObserver> mUpdating;
		}

		new Thread(new UpdateRunner(this, mObservers)).run();
		*/

		for (GameObserver update : mObservers)
			update.update(this);
	}

	/**
	 * initializes internal systems
	 */
	private void init()
	{
		//internal components of global state
		Active active = new Active();
		Busy busy = new Busy();
		//set default
		active.mActive = true;
		busy.mBusy = false;

		mGlobalState.add(active);
		mGlobalState.add (busy);
		mEngine.addEntity(mGlobalState);

		//internal systems
		ActiveSystem activeSystem = new ActiveSystem();
		BusySystem busySystem = new BusySystem();

		activeSystem.setPriority(1000);
		busySystem.setPriority(1001);

		mEngine.addSystem (activeSystem);
		mEngine.addSystem (busySystem);
		//add entity listeners for systems added
		mEngine.addEntityListener (new EntityListener (activeSystem));
		mEngine.addEntityListener (new EntityListener (busySystem));
	}

	private Engine mEngine;
	private HashMap<Player, Ball> mBallMap;
	private Entity mGlobalState;
	private HashSet<GameObserver> mObservers;

	private float mHitNoiseBound;
	private Random mGen;

	private Logger mLogger;
}
