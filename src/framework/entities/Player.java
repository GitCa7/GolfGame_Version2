package framework.entities;

import com.badlogic.ashley.core.Entity;

import physics.entities.Hole;

/**
 * Tag class for player objects signalling special treatment.
 * Additionally, every entity labelled as player shall belong to the turn taking
 * family and needs to provide a turn, next player component.
 * @author martin
 */
public class Player
{
	public Player (Entity playerEntity)
	{
		mEntity = playerEntity;
	}
	
	public boolean equals(Player comp)
	{
		return (comp.mEntity == this.mEntity);
	}
	
	public Entity mEntity;
}
