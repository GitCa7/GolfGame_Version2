package PHY_Components;

import com.badlogic.ashley.core.Component;

public class Mass implements Component
{
	public Mass (float mass)
	{
		mMass = mass;
	}
	
	
	public float mMass;
}
