package physics.components;


import physics.geometry.spatial.Solid;
import physics.geometry.spatial.SolidTranslator;

import java.util.HashSet;

/**
 * @autor martin
 * created 12.05.2016
 */
public class Body extends HashSet<SolidTranslator> implements Component
{

	/**
	 * @return a new body containing deep-copied solid translators
     */
	public Body clone()
	{
		Body newBody = new Body();
		for (SolidTranslator copy : this)
			newBody.add(new SolidTranslator(copy.getSolid(), copy.getPosition()));
		return newBody;
	}

}
