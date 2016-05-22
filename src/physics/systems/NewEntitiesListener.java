package physics.systems;

import java.util.HashSet;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;

/**
 * listener updating the set of physics.entities of an EntitySystem
 * @author martin
 */
public class NewEntitiesListener implements EntityListener 
{
	/**
	 * @param entitySet entity set to update
	 */
	public NewEntitiesListener (HashSet<Entity> entitySet)
	{
		mEntitySet = entitySet;
	}
	
	@Override
	public void entityAdded(Entity added) 
	{
		mEntitySet.add (added);
	}

	@Override
	public void entityRemoved(Entity removed) 
	{
		mEntitySet.remove (removed);
	}
	
	
	private HashSet<Entity> mEntitySet;
}
