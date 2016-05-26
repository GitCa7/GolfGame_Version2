package physics.systems;

import framework.ComponentBundle;

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
	/**
	 * default constructor initializing fields
	 */
	public SystemsTracker()
	{
		mSystemsInUse = new HashSet<>();
	}

	/**
	 * @return A list of newly constructed systems. For each component bundle stored, there is one system in this list.
	 */
	public ArrayList<EntitySystem> systemsInUse()
	{
		ArrayList<EntitySystem> using = new ArrayList<>();
		for (ComponentBundle cb : mSystemsInUse)
			using.add (cb.system());
		return using;
	}
	
	/**
	 * @param es a given entity system
	 * @return true if another system of the same tyepe 
	 * or the same entity system is already tracked
	 */
	public boolean hasSystem (EntitySystem es)
	{
		return mSystemsInUse.contains(es);
	}
	

	/**
	 * adds b to the set of component bundles, representing the systems in use or does not do anything if such a system
	 * was added previously.
	 * If a component bundle contains a 'null' system, nothing happens
	 * @param b component bundle to track
	 */
	public void track (ComponentBundle b)
	{
		if (b.system() != null)
			mSystemsInUse.add (b);
	}


	HashSet<ComponentBundle> mSystemsInUse;
}
