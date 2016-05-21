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
	 * adds b to the set of component bundles, representing the systems in use or does not do anything if such a system
	 * was added previously.
	 * @param b component bundle to track
	 */
	public void track (ComponentBundle b)
	{
		mSystemsInUse.add (b);
	}


	HashSet<ComponentBundle> mSystemsInUse;
}
