package framework.entities;

import com.badlogic.ashley.core.Entity;

/**
 * ball entity, tag class
 * @author martin
 */
public class Ball 
{
	
	public Ball(Entity ballEntity)
	{
		mEntity = ballEntity;
	}
	
	public boolean equals(Ball comp)
	{
		return (comp.mEntity == this.mEntity);
	}
	
	public Entity mEntity;
}
