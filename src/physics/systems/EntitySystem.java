package physics.systems;

import java.util.HashSet;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;

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
	 * @return iterable ImmutableArray of internal physics.entities
	 */
	protected HashSet<Entity> entities()
	{
		return mEntities;
	}
	
	private HashSet<Entity> mEntities;
}
