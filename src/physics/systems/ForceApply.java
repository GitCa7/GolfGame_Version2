package physics.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.*;

import framework.EntitySystem;
import physics.constants.CompoMappers;
import physics.constants.Families;

import physics.components.*;

public class  ForceApply extends EntitySystem
{
	public ForceApply()
	{
		
	}

	public ForceApply clone()
	{
		return new ForceApply();
	}
	
	public void addedToEngine (Engine e)
	{
		for (Entity add : e.getEntitiesFor (Families.ACCELERABLE))
			entities().add (add);
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
			
			//F=m*a => F=m*dv/dt => dv = F*dt/m
			Vector3 a = f.cpy();
			a.scl (1 / m.mMass);
			
			//dv = a * t
			a.scl (dTime);
			v.add (a);
			
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

}
