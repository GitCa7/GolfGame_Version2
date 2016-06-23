package physics.collision;


import com.badlogic.ashley.core.Entity;
import physics.components.Body;
import physics.constants.CompoMappers;

/**
 * class storing an entity together with its body
 * @author martin
 */
public class EntityAndBody
{
	/**
	 *
	 * @param e entity belonging to the colliding family
	 */
	public EntityAndBody(Entity e)
	{
		mEntity = e;
		mBody = CompoMappers.BODY.get (e);
	}


	public boolean equals (EntityAndBody comp) { return this.mEntity.equals (comp.mEntity); }

	public Body mBody;
	public Entity mEntity;
}