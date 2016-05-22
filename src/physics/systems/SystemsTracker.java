package physics.systems;

import physics.logic.ComponentBundle;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class managing a set of component bundles. This class is designed to construct the physics.systems paired with the physics.components
 * in the component bundle.
 * @autor martin
 * created 17.05.2016
 */
public class SystemsTracker
{

	public SystemsTracker()
	{
		//@TODO implement
	}

	/**
	 *
	 * @return A list of newly constructed physics.systems. For each component bundle stored, there is one system in this list.
	 */
	public ArrayList<EntitySystem> systemsInUse()
	{
		//@TODO implement
		return null;
	}

	/**
	 * @param c a given component bundle
	 * @return true if there is a component bundle stored with the same identifier as c
	 */
	public boolean hasComponent (ComponentBundle c)
	{
		//@TODO implement
		return false;
	}

	/**
	 * adds b to the set of component bundles, representing the physics.systems in use.
	 * Precondition: no component bundle with the same identifier as b is stored already.
	 * @param b component bundle to track
	 */
	public void track (ComponentBundle b)
	{

	}


	HashSet<ComponentBundle> mSystemsInUse;
}
