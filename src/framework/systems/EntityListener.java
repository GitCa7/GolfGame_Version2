package framework.systems;

import java.util.HashSet;

import com.badlogic.ashley.core.Entity;

import physics.systems.EntitySystem;

/**
 * listener updating the set of physics.entities of an EntitySystem
 * @author martin
 */
public class EntityListener implements com.badlogic.ashley.core.EntityListener
{
	/**
	 * @param system entity set to update
	 */
	public EntityListener(EntitySystem system)
	{
		mSystem = system;
	}
	
	@Override
	public void entityAdded(Entity added) 
	{
		mSystem.addEntity (added);
	}

	@Override
	public void entityRemoved(Entity removed) 
	{
		mSystem.removeEntity (removed);
	}
	
	
	private EntitySystem mSystem;
}
