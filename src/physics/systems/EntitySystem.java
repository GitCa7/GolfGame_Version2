package physics.systems;

import com.badlogic.ashley.core.Entity;

import java.util.HashSet;

public class EntitySystem extends com.badlogic.ashley.core.EntitySystem 
{
	/**
	 * @return a listener updating the physics.entities of this entity system
	 */
	public NewEntitiesListener getNewEntitiesListener()
	{
		return new NewEntitiesListener (mEntities);
	}

	/**
	 * sets the priority value to priority. The lower the value, the sooner a system is updated.
	 * @param priority
	 */
	public void setPriority (int priority)
	{
		this.priority = priority;
	}

	/**
	 * @return iterable ImmutableArray of internal physics.entities
	 */
	protected HashSet<Entity> entities()
	{
		return mEntities;
	}
	
	private HashSet<Entity> mEntities;
}
