package framework;

import com.badlogic.ashley.core.Component;
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
	 *
	 * @param c a component factory
	 * @param s a system factory
	 */
	public ComponentBundle (ComponentFactory c, EntitySystemFactory s)
	{
		mComponentProducer = c;
		mSystemProducer = s;
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
	 * @return an entity system as returned by system factory stored
	 */
	public EntitySystem system()
	{
		return mSystemProducer.produce();
	}

	/**
	 *
	 * @param comp an entity bundle to compare with this
	 * @return true if the class types of the component factories and the class types of the system factories are equal
	 */
	public boolean equals (ComponentBundle comp)
	{
		return (mComponentProducer.getClass().equals (comp.mComponentProducer.getClass()) &&
				mSystemProducer.getClass().equals (comp.mSystemProducer.getClass()));
	}

	private ComponentFactory mComponentProducer;
	private EntitySystemFactory mSystemProducer;
}
