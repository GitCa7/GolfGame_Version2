package physics.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

import framework.systems.EntitySystem;
import physics.collision.reaction.ColliderClosestSideFinder;
import physics.collision.structure.ColliderEntity;
import physics.collision.structure.ColliderPair;
import physics.collision.structure.CollisionRepository;
import physics.components.Force;
import physics.components.Friction;
import physics.components.GravityForce;
import physics.components.Mass;
import physics.components.Velocity;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.constants.GlobalObjects;
import physics.geometry.VectorProjector;
import physics.geometry.planar.Plane;

/**
 * System applying the impact of friction to the velocities of entities affected
 * @author martin
 */
public class FrictionSystem extends EntitySystem implements RepositoryEntitySystem
{

	public static boolean DEBUG = false;

	public FrictionSystem()
	{
		mRepo = null;
	}


	public FrictionSystem clone()
	{
		FrictionSystem newSystem = new FrictionSystem();
		newSystem.setPriority(priority);
		return newSystem;
	}

	public void addedToEngine(Engine e)
	{
		
	}
	
	@Override
	public void addEntity(Entity e) 
	{
		
	}

	@Override
	public void removeEntity(Entity e) 
	{
		
	}
	
	/**
	 * the friction force is proportional to the magnitude of the normal force
	 * in the direction opposing the entity's movement. If the friction would arithmetically result in the ball
	 * moving the other direction, the velocity is set to zero.
	 * @param dTime time difference between this update and the last update
	 */
	public void update (float dTime)
	{
		
		for (ColliderPair<ColliderEntity> pairs : mRepo.getColliderPairs())
		{
			if (	pairs.getFirst().isActive() && 
					Families.FRICTION.matches(pairs.getFirst().getEntity()) &&
					hasFriction(pairs.getFirst(), pairs.getSecond()))
			{
				Vector3 friction = computeFriction(pairs.getFirst(), pairs.getSecond(), dTime);
				Force activeForce = CompoMappers.FORCE.get(pairs.getFirst().getEntity());
				activeForce.add(friction);
			}

			if (	pairs.getSecond().isActive() &&
					Families.FRICTION.matches(pairs.getSecond().getEntity()) &&
					hasFriction(pairs.getSecond(), pairs.getFirst()))
			{
				Vector3 friction = computeFriction(pairs.getSecond(), pairs.getFirst(), dTime);
				Force activeForce = CompoMappers.FORCE.get(pairs.getSecond().getEntity());
				activeForce.add(friction);
			}
		}
	}

	/**
	 * set the collision repository shared with another object updating its information
	 * @param repo the repository
     */
	public void setRepository(CollisionRepository repo) 
	{ 
		mRepo = repo; 
	}

	/**
	 * @param active the moving entity
	 * @param passive the entity considered static
     * @return true if there is friction caused by the normal force
     */
	private boolean hasFriction(ColliderEntity active, ColliderEntity passive)
	{
		Vector3 gravity = CompoMappers.GRAVITY_FORCE.get(active.getEntity());
		Vector3 velocity = CompoMappers.VELOCITY.get(active.getEntity());

		Plane collisionPlane = new ColliderClosestSideFinder(active, passive).find();

		//true if gravity does not point inwards the object
		float gravityDotNormal = gravity.dot(collisionPlane.getNormal());

		return (gravityDotNormal > 0 /* && !GlobalObjects.ROUND.epsilonEquals(gravityDotNormal, 0f)*/);
	}

	/**
	 * @param active the moving entity
	 * @param passive the entity considered static
     * @return the force of friction excerted on the active entity
     */
	private Vector3 computeFriction (ColliderEntity active, ColliderEntity passive, float dTime)
	{
		Friction fric = CompoMappers.FRICTION.get(active.getEntity());
		GravityForce grav = CompoMappers.GRAVITY_FORCE.get(active.getEntity());
		Mass m = CompoMappers.MASS.get(active.getEntity());
		Velocity v = CompoMappers.VELOCITY.get(active.getEntity());
		Force force = CompoMappers.FORCE.get(active.getEntity());

		float fricCoeff = 0;
		if (!GlobalObjects.ROUND.epsilonEquals(v.len(), 0))
			fricCoeff = fric.get(Friction.State.DYNAMIC, Friction.Type.MOVE);
		else
			fricCoeff = fric.get(Friction.State.STATIC, Friction.Type.MOVE);

		Plane collisionPlane = new ColliderClosestSideFinder(active, passive).find();
		Vector3 velocityOnNormal = new VectorProjector(collisionPlane.getNormal()).project(v);
		Vector3 velocityOnPlane = v.cpy().sub(velocityOnNormal);

		float magFriction = new VectorProjector(collisionPlane.getNormal()).project(grav).len();

		Vector3 fricForce;

		if (magFriction *dTime / m.mMass > velocityOnPlane.len())
		{
			if(DEBUG)
			{
				System.out.print("set zero; ");
			}
			fricForce =  velocityOnPlane.scl(-1 * m.mMass);
		}
		else
			fricForce = velocityOnPlane.scl(-1).setLength(magFriction);

		if (DEBUG)
			System.out.println(" friction of " + fricForce);

		return fricForce;
	}
	
	
	private CollisionRepository mRepo;
}
