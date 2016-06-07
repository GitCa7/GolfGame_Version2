package physics.components;


import physics.geometry.spatial.Solid;

import java.util.HashSet;

/**
 * @autor martin
 * created 12.05.2016
 */
public class Body extends HashSet<Solid> implements Component
{
	//@TODO use translator

	/**
	 * @return a new body containing shallowly copied solids (they are immutable).
     */
	public Body clone()
	{
		Body newBody = new Body();
		newBody.addAll(this);
		return newBody;
	}

}
