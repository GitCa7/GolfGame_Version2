package physics.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

import framework.EntitySystem;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.constants.PhysicsCoefficients;
import physics.components.*;

public class SpinApply extends EntitySystem
{
	public SpinApply()
	{
		
	}

	public SpinApply clone()
	{
		SpinApply newSystem = new SpinApply();
		newSystem.setPriority(priority);
		return newSystem;
	}
	
	public void addedToEngine (Engine e)
	{
		for (Entity add : e.getEntities())
		{
			if (Families.SPINNING.matches(add))
				entities().add(add);
		}
	}
	
	/**
	 * @param dT time in seconds\n
	 * applies spin of ball to velocity\n
	 * adding a quantity proportional to the number of revolutions and the time elapsed, 
	 * in the direction of the spin
	 */
	public void update (float dT)
	{
		for (Entity update : entities())
		{
			Velocity v = CompoMappers.VELOCITY.get (update);
			Spin sp = CompoMappers.SPIN.get (update);
			Mass m = CompoMappers.MASS.get (update);
			
			v.add (sp.spinVector().scl (dT * PhysicsCoefficients.SPIN_COEFFICIENT));
			
			sp.scl ((1 - PhysicsCoefficients.SPIN_FRICTION));
		}
	}

	@Override
	public void addEntity(Entity e) {
		if (Families.SPINNING.matches((e))) {
			entities().add(e);
		}

	}

	@Override
	public void removeEntity(Entity e) {
		if (Families.SPINNING.matches((e))) {
			entities().remove(e);
		}

	}
}
