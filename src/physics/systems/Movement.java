package physics.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Engine;

import com.badlogic.gdx.math.Vector3;
import physics.components.Body;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.constants.GlobalObjects;

import physics.components.Position;
import physics.components.Velocity;
import physics.geometry.spatial.SolidTranslator;

/**
 * entity system applying movement to every entity
 * @author martin
 */
public class Movement extends EntitySystem
{	
	/**
	 * default constructor
	 */
	public Movement()
	{
		
	}
	
	/**
	 * @param e engine to which this was added
	 * adds all moveable physics.entities of e to this system
	 * is automatically called by engine
	 */
	public void addedToEngine (Engine e)
	{
		for (Entity ent : e.getEntitiesFor (Families.MOVING))
			entities().add (ent);
	}
	
	/**
	 * action happening on update of engine
	 * moves all physics.entities proportionally to the time elapsed. If the velocity of an entity is below the
	 * arithmetic precision set, the velocity will be set to zero.
	 */
	public void update (float dTime)
	{
		for (Entity move : entities())
		{	
			Velocity v = CompoMappers.VELOCITY.get (move);
			
			//set velocity to 0 if magnitude is below arithmetic precision
			if (GlobalObjects.ROUND.epsilonEquals (0f, v.len()))
				v.setZero();
			
			//alter position
			Vector3 change = v.cpy().scl(dTime);

			Position p = CompoMappers.POSITION.get (move);
			p.add(change);

			if (CompoMappers.BODY.has(move))
				moveBody(CompoMappers.BODY.get(move), change);
		}
	}

	@Override
	public void addEntity(Entity e) {
		if (Families.MOVING.matches((e))) {
			entities().add(e);
		}

	}

	@Override
	public void removeEntity(Entity e) {
		if (Families.MOVING.matches((e))) {
			entities().remove(e);
		}

	}

	/**
	 * translates every body by the vector change
	 * @param b a given body
	 * @param change translation vector
     */
	private void moveBody(Body b, Vector3 change)
	{
		for (SolidTranslator move : b)
			move.addPosition(change);
	}
}
