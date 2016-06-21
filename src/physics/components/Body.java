package physics.components;


import com.badlogic.gdx.math.Vector3;
import physics.geometry.spatial.BoundingBox;
import physics.geometry.spatial.Solid;
import physics.geometry.spatial.SolidTranslator;

import java.util.Collection;
import java.util.HashSet;

/**
 * @autor martin
 * created 12.05.2016
 */
public class Body extends HashSet<SolidTranslator> implements Component
{


	public Body()
	{
		mBound = null;
	}


	public Body(Collection<SolidTranslator> solids)
	{
		super (solids);
		mBound = new BoundingBox(solids);
	}

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

	public BoundingBox getBound()
	{
		return mBound;
	}

	public boolean add(SolidTranslator add)
	{
		boolean success = super.add (add);
		if (success)
			mBound = new BoundingBox(this);
		return success;
	}

	public boolean remove(SolidTranslator remove)
	{
		boolean success = super.remove(remove);
		if (success)
			mBound = new BoundingBox(this);
		return success;
	}


	public void setPosition(Vector3 newPosition)
	{
		for (SolidTranslator s : this)
			s.setPosition(newPosition);
	}


	private BoundingBox mBound;
}
