package framework;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import physics.components.ComponentFactory;
import physics.systems.EntitySystem;
import physics.systems.EntitySystemFactory;

/**
 * Class pairing a component and the respective system. The class is uniquely identified by the class types of
 * the component factory and the entity factory.
 * @autor martin
 * created 17.05.2016
 */
public class ComponentBundle
{

    /**
     * constructor for components not associated with a certain system. On construction of the game no system will be
     * added for a component of this type
     * @param c a component factory
     */
    public ComponentBundle (ComponentFactory c)
    {

    }

	/**
	 * constructor for components associated with a certain system. When the game is constructed, an instance of each of
     * the stored systems will be added.
	 * @param c a component factory
	 * @param s a system factory
	 */
	public ComponentBundle (ComponentFactory c, EntitySystemFactory ... s)
	{
		mComponentProducer = c;
		mSystemProducers = s;
	}

	/**
	 *
	 * @return a component instance as returned by component factory stored
	 */
	public Component component()
	{
		return mComponentProducer.produce();
	}

	/**
	 *
	 * @return an array of entity systems as returned by system factory stored or null if the component stored is not
     * associated with any system.
	 */
	public EntitySystem[] systems()
	{
		if (mSystemProducers == null)
            return null;

        EntitySystem[] systems = new EntitySystem[mSystemProducers.length];
        for (int cSys = 0; cSys < systems.length; ++cSys)
            systems[cSys] = mSystemProducers[cSys].produce();
		return systems;
	}

	/**
	 *
	 * @param comp an entity bundle to compare with this
	 * @return true if the class types of the component factories and the class types of the system factories are equal
	 */
	public boolean equals (ComponentBundle comp)
	{
        if (!mComponentProducer.getClass().equals (comp.mComponentProducer.getClass()))
            return false;
        for (EntitySystemFactory ownEsf : mSystemProducers)
        {
            int cComp = 0;
            boolean found = false;
            while (cComp < comp.mSystemProducers.length && !found)
            {
                if (ownEsf.getClass().equals(comp.mSystemProducers[cComp]))
                    found = true;
                ++cComp;
            }

            if (!found)
                return false;
        }
        return true;
	}

	private ComponentFactory mComponentProducer;
	private EntitySystemFactory[] mSystemProducers;
}
