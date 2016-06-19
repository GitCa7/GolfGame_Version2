package physics.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

import framework.EntitySystem;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.components.Force;
import physics.components.GravityForce;

public class GravitySystem extends EntitySystem
{
	public GravitySystem()
	{
		
	}


	public GravitySystem clone()
	{
		GravitySystem newSystem = new GravitySystem();
		newSystem.setPriority(priority);
		return newSystem;
	}
	
	public void addedToEngine (Engine e)
	{
		for (Entity add : e.getEntities())
		{
			if (Families.GRAVITY_ATTRACTED.matches(add))
				entities().add (add);
		}
	}
	
	/**
	 * applies gravity to all physics.entities being GRAVITY_ATTRACTED,
	 * thus adding gravity attraction to their force component
	 */
	public void update (float dTime)
	{
		for (Entity update : entities())
		{
			System.out.println ("apply gravity");

			float mass = CompoMappers.MASS.get (update).mMass;
			Force f = CompoMappers.FORCE.get (update);
			GravityForce g = CompoMappers.GRAVITY_FORCE.get (update);
			
			f.add (g.cpy().scl (mass));
		}
	}

	@Override
	public void addEntity(Entity e) {
		if (Families.GRAVITY_ATTRACTED.matches((e))) {
			entities().add(e);
		}
	}


	@Override
	public void removeEntity(Entity e) {
		if (Families.GRAVITY_ATTRACTED.matches((e))) {
			entities().remove(e);
		}

	}
}
