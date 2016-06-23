package physics.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.*;

import framework.systems.EntitySystem;
import physics.constants.CompoMappers;
import physics.constants.Families;

import physics.components.*;

public class  ForceApply extends EntitySystem
{

	public static boolean DEBUG = false;

	public ForceApply()
	{
		
	}

	public ForceApply clone()
	{
		ForceApply newSystem = new ForceApply();
		newSystem.setPriority(priority);
		return newSystem;
	}
	
	public void addedToEngine (Engine e)
	{
		for (Entity add : e.getEntities())
		{
			if (Families.ACCELERABLE.matches(add))
				entities().add(add);
		}
	}
	
	/**
	 * alters velocity of physics.entities\n
	 * uses accumulated force in force component, 
	 * computes resulting acceleration given by a = F / m and 
	 * adds t*a to the velocity vector
	 */
	public void update (float dTime)
	{
		for (Entity update : entities())
		{
			Force f = CompoMappers.FORCE.get (update);
			Mass m = CompoMappers.MASS.get (update);
			Velocity v = CompoMappers.VELOCITY.get (update);
			
			//F=m*a
			Vector3 a = f.cpy();
			a.scl (1 / m.mMass);
			
			//v* = v + a * t
			a.scl (dTime);
			v.add (a);

			if (DEBUG)
				debugOut(update, f);

			f.setZero();

		}
	}

	@Override
	public void addEntity(Entity e) {
		if (Families.ACCELERABLE.matches((e))) {
			entities().add(e);
		}
	}

	@Override
	public void removeEntity(Entity e) {
		if (Families.ACCELERABLE.matches((e))) {
			entities().remove(e);
		}
	}


	public void debugOut(Entity appliedTo, Vector3 totalForce)
	{
		System.out.println("applying total force " + totalForce + " to " + appliedTo + " at " + CompoMappers.POSITION.get(appliedTo));
	}

}
