package physics.entities;

import com.badlogic.ashley.core.Entity;

/**
 * @autor martin
 * created 19.05.2016
 */
public class Hole
{
	
	public Hole(Entity holeEntity)
	{
		mEntity = holeEntity;
	}
	
	public boolean equals(Hole comp)
	{
		return (comp.mEntity == this.mEntity);
	}
	
	public Entity mEntity;
}
