package physics.components;


public class Mass implements Component
{
	public Mass (float mass)
	{
		mMass = mass;
	}
	

	public Mass clone()
	{
		float newMass=mMass;
		return new Mass (newMass);
	}

	public float mMass;
}
