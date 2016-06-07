package physics.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

import physics.components.Force;
import physics.components.Friction;
import physics.components.GravityForce;
import physics.components.Mass;
import physics.components.Velocity;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.constants.GlobalObjects;

/**
 * System applying the impact of friction to the velocities of entities affected
 * @author martin
 */
public class FrictionSystem extends EntitySystem
{
	//@TODO check whether normal force exists (collision checking)
	
	public void addedToEngine(Engine e)
	{
		for (Entity add : e.getEntitiesFor(Families.FRICTION))
			entities().add(add);
	}
	
	@Override
	public void addEntity(Entity e) 
	{
		if (Families.FRICTION.matches(e))
			entities().add(e);
	}

	@Override
	public void removeEntity(Entity e) 
	{
		if (Families.FRICTION.matches(e))
			entities().remove(e);
	}
	
	/**
	 * the friction force is proportional to the magnitude of the normal force
	 * in the direction opposing the entity's movement. If the friction would arithmetically result in the ball
	 * moving the other direction, the velocity is set to zero.
	 * @param dTime time difference between this update and the last update
	 */
	public void update (float dTime)
	{
		
		if (entities().isEmpty())
			System.out.println("no entities in friction");
		
		for (Entity update : entities())
		{
			Friction fric = CompoMappers.FRICTION.get(update);
			GravityForce grav = CompoMappers.GRAVITY_FORCE.get(update);
			Mass m = CompoMappers.MASS.get(update);
			Velocity v = CompoMappers.VELOCITY.get(update);
			Force force = CompoMappers.FORCE.get(update);
			
			float fricCoeff = 0;
			if (!GlobalObjects.ROUND.epsilonEquals(v.len(), 0))
				fricCoeff = fric.get(Friction.State.DYNAMIC, Friction.Type.MOVE);
			else
				fricCoeff = fric.get(Friction.State.STATIC, Friction.Type.MOVE);
			
			float fricAccMagnitude = grav.len() * fricCoeff;
            if (fricAccMagnitude > v.len())
                v.setZero();
            else
            {
                Vector3 fricAcc = v.cpy().setLength(fricAccMagnitude);
                v.add(fricAcc.scl (-1 *dTime));
            }


		}
	}

}
