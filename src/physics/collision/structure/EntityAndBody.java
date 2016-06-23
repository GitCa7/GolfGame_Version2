package physics.collision.structure;


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


	/**
	 * @param comp an entity and body object
	 * @return true if both store the same entity
     */
	public boolean equals (Object comp) { return this.mEntity.equals (((EntityAndBody) comp).mEntity); }

	/**
	 * @return The hash code for this object. Objects storing the same body and the same entity reference
	 * shall have the same hash code
     */
	public int hashCode()
	{
		return mEntity.hashCode() % mBody.hashCode();
	}

	public Body mBody;
	public Entity mEntity;
}