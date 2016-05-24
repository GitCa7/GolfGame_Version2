package framework.entities;

import com.badlogic.ashley.core.Entity;
import framework.ComponentBundle;
import physics.systems.SystemsTracker;

import java.util.ArrayList;

/**
 * Class producing physics.entities. Uses a set of component factories to produce physics.components. Equips each produced entity with one component
 * produced by each component factory stored.
 * This class needs to be constructed by an instance of GameConfigurator in order to ensure that all necessary physics.systems are added to
 * the engine.
 * @autor martin
 * created 17.05.2016
 */
public class EntityFactory
{

	/**
	 * @param tracker the systems tracker used by a superior class (i.e. GameConfigurator)
	 */
	public EntityFactory (SystemsTracker tracker)
	{
		mTrackSystems = tracker;
	}

	/**
	 *
	 * @return a new entity equipped with one instance of component for each component factory stored.
	 */
	public Entity produce()
	{
		//@TODO implement
		return null;
	}

	/**
	 *
	 * @param c a given component bundle
	 * @return true if this factory stores a bundle with the same identifier
	 */
	public boolean hasComponent (ComponentBundle ... c)
	{
		//@TODO implement
		return false;
	}

	/**
	 * adds c to the set of component bundles stored
	 * Precondition: no bundle with the same identifier was added before
	 * @param cs an instance of component bundle
	 */
	public void addComponent (ComponentBundle ... cs)
	{

	}

	/**
	 * removes c from the set of component bundles stored
	 * Precondition: c exists within the set of component bundles
	 * @param c the component bundle to remove
	 */
	public void removeComponents (ComponentBundle ... c)
	{

	}


	private ArrayList<ComponentBundle> mComponentProducers;
	private SystemsTracker mTrackSystems;
}
