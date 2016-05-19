package PHY_Systems;

import java.util.*;

import PHY_Logic.FrictionComputer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Engine;

import PHY_Constants.*;

import PHY_Components.*;


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
	 * adds all moveable entities of e to this system
	 * is automatically called by engine
	 */
	public void addedToEngine (Engine e)
	{
		for (Entity ent : e.getEntitiesFor (Families.MOVING))
			entities().add (ent);
	}
	
	/**
	 * action happening on update of engine
	 * moves all entities proportionally to the time elapsed
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
}
