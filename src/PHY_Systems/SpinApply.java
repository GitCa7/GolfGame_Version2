package PHY_Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

import PHY_Components.Mass;
import PHY_Components.Spin;
import PHY_Components.Velocity;
import PHY_Constants.*;


public class SpinApply extends EntitySystem 
{
	public SpinApply()
	{
		
	}
	
	
	public void addedToEngine (Engine e)
	{
		for (Entity add : e.getEntitiesFor (Families.SPINNING))
			entities().add (add);
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
}
