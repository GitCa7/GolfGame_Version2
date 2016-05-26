package physics.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Engine;

import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.constants.GlobalObjects;

import physics.components.Position;
import physics.components.Velocity;

/**
 * entity system applying movement to every entity
 * @author martin
 */
public class Movement extends EntitySystem
{	
	/**
	 * default constructor
	 */
	public Movement()
	{
		
	}
	
	/**
	 * @param e engine to which this was added
	 * adds all moveable physics.entities of e to this system
	 * is automatically called by engine
	 */
	public void addedToEngine (Engine e)
	{
		for (Entity ent : e.getEntitiesFor (Families.MOVING))
			entities().add (ent);
	}
	
	/**
	 * action happening on update of engine
	 * moves all physics.entities proportionally to the time elapsed
	 */
	public void update (float dTime)
	{
		for (Entity move : entities())
		{	
			Velocity v = CompoMappers.VELOCITY.get (move);
			
			//apply friction
			if (Families.FRICTION.matches (move))
			{
				FrictionComputer compFric = new FrictionComputer (move);
				v.sub (compFric.getMovingFriction());
			}
			
			//set velocity to 0 if magnitude is below arithmetic precision
			if (GlobalObjects.ROUND.epsilonEquals (0f, v.len()))
				v.setZero();
			
			//alter position
			Position p = CompoMappers.POSITION.get (move);
			p.mulAdd (v, dTime);
		}
	}

	@Override
	public void addEntity(Entity e) {
		if (Families.MOVING.matches((e))) {
			entities().add(e);
		}

	}

	@Override
	public void removeEntity(Entity e) {
		if (Families.MOVING.matches((e))) {
			entities().remove(e);
		}

	}
}
