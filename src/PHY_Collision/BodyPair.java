package PHY_Collision;

import com.badlogic.ashley.core.Entity;
import components.Body;
import constants.CompoMappers;

/**
 * class storing an entity together with its body
 * @author martin
 */
public class BodyPair
{
	/**
	 *
	 * @param e entity belonging to the colliding family
	 */
	public BodyPair (Entity e)
	{
		mEntity = e;
		mBody = CompoMappers.BODY.get (e);
	}


	public boolean equals (BodyPair comp) { return this.mEntity.equals (comp.mEntity); }

	public Body mBody;
	public Entity mEntity;
}