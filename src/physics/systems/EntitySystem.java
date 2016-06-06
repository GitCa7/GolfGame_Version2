package physics.systems;

import com.badlogic.ashley.core.Entity;

import framework.systems.EntityListener;

import java.util.HashSet;

public abstract class EntitySystem extends com.badlogic.ashley.core.EntitySystem
{
	/**
	 * default constructor
	 */
	public EntitySystem()
	{
		mEntities = new HashSet<>();
	}
	
	/**
	 * @return a listener updating the physics.entities of this entity system
	 */
	public EntityListener getNewEntitiesListener()
	{
		return new EntityListener(this);
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
	 * @param comp entity system to compare with
	 * @return true if comp and this are of the same class type
	 */
	public boolean equals(EntitySystem comp)
	{
		return (this.getClass().equals (comp.getClass()));
	}

    /**
     * checks whether e belongs to the system and adds e if on successful check
     * @param e a given entity
     */
    public abstract void addEntity (Entity e);

    /**
     * removes e from the system if e was in the system. otherwise nothing happens.
     * @param e a given entity
     */
    public abstract void removeEntity (Entity e);

    /**
	 * @return iterable ImmutableArray of internal physics.entities
	 */
	protected HashSet<Entity> entities()
	{
		return mEntities;
	}

	
	private HashSet<Entity> mEntities;
}
