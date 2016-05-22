package physics.components;


public class Mass implements Component
{
	public Mass (float mass)
	{
		mMass = mass;
	}
	

	public Mass clone()
	{
		return new Mass (mMass);
	}

	public float mMass;
}
