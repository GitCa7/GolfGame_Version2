package physics.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

import framework.EntitySystem;
import framework.testing.RepositoryEntitySystem;
import physics.collision.ColliderClosestSideFinder;
import physics.collision.ColliderEntity;
import physics.collision.ColliderPair;
import physics.collision.CollisionRepository;
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
	
	public FrictionSystem()
	{
		mRepo = null;
	}


	public FrictionSystem clone()
	{
		return new FrictionSystem();
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

		Plane collisionPlane = new ColliderClosestSideFinder(active, passive).find();
		//let normal point inwards => towards any vertex not in plane
		Vector3[] solidCollidingVertices = passive.getCollidingSolid().getVertices();
		boolean foundOutsidePlane = false;
		int cVertex = 0;
		while (cVertex < solidCollidingVertices.length && !foundOutsidePlane)
		{
			if (collisionPlane.testPoint(solidCollidingVertices[cVertex]) != 0)
			{
				collisionPlane.setNormalOrientation(solidCollidingVertices[cVertex]);
				foundOutsidePlane = true;
			}
			++cVertex;
		}

		//true if gravity does not point inwards the object
		return (gravity.dot(collisionPlane.getNormal()) > 0);
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

		float magFriction = new VectorProjector(collisionPlane.getNormal()).project(grav).len();

		if (magFriction *dTime / m.mMass > v.len())
		{
			if(mDebug) {
				System.out.println("set zero");
			}
			return v.cpy().scl(-1 * m.mMass / dTime);
		}

		return v.cpy().scl(-1).setLength(magFriction);
	}
	
	
	private CollisionRepository mRepo;
	private final boolean mDebug = false;
}
