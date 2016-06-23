package framework;

import com.badlogic.ashley.core.Engine;
import framework.systems.EntityListener;
import physics.generic.ode.ODESolver;

/**
 * Interface for factory classes producing entity physics.systems.
 * @autor martin
 * created 17.05.2016
 */

public abstract class EntitySystemFactory
{

	public EntitySystemFactory()
	{
		mListenEngine = null;
		mPriority = Integer.MIN_VALUE;
	}

	/**
	 * @throws IllegalStateException if the priority has not been set
	 * @return a new reference to an entity system.
	 */
	public abstract EntitySystem produce();

	/**
	 * @return true if the priority value has been set
     */
	public boolean hasPriority() { return mPriority != Integer.MIN_VALUE; }

	/**
	 * sets the engine to which newly produced systems will attach a listener
	 * @param e the engine
     */
	public void setEngine(Engine e)
	{
		mListenEngine = e;
	}

	/**
	 * Sets the priority value for systems. This shall be a non-negative integer.
	 * The lower the value, the higher the priority of the associated system.
	 * @param priority
     */
	public void setSystemPriority(int priority)
	{
		mPriority = priority;
	}

	/**
	 * set the ode solver to solver
	 * @param solver
     */
	public void setODESolver(ODESolver solver) { mODESolver = solver; }

	/**
	 * initialize a new entity system for proper use in clients
	 * @param init the system to initialize
     */
	protected void initSystem(EntitySystem init)
	{
		checkAndThrowPriorityException();
		init.setPriority(mPriority);
		attachListener(init);
	}

	/**
	 * checks whether is set and sets the ode solver in the system to initialize
	 * @param init
	 * @throws IllegalStateException if no ode solver is set in the factory
     */
	protected void initSystemODE(EntitySystem init)
	{
		if (mODESolver == null)
			throw new IllegalStateException("ode solver is not set but required");
		init.setODESolver(mODESolver);
	}

	/**
	 * attaches an entity listener instance to the engine stored updating newSystem
	 * @param newSystem an entity system instance
	 * @throws IllegalStateException if no engine was set
     */
	private void attachListener(EntitySystem newSystem)
	{
		if (mListenEngine == null)
			throw new IllegalStateException("no engine set, cannot attach listener for system");
		EntityListener listener = new EntityListener(newSystem);
		mListenEngine.addEntityListener(listener);
	}

	/**
	 * checks whether the priority value has been set and throws a standard exception if this is
	 * not the case.
	 * @throws IllegalArgumentException if the priority value has not been set
	 */
	private void checkAndThrowPriorityException()
	{
		if (!hasPriority())
			throw new IllegalStateException("system factory cannot operate, priority value has not been set yet");
	}

	private Engine mListenEngine;
	private ODESolver mODESolver;
	private int mPriority;
}
