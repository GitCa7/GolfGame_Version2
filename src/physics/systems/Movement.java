package physics.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Engine;

import com.badlogic.gdx.math.Vector3;
import framework.EntitySystem;
import physics.components.*;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.constants.GlobalObjects;

import physics.constants.PhysicsCoefficients;
import physics.generic.ode.ODEquation;
import physics.geometry.spatial.SolidTranslator;
import physics.vectorUtil.CoordinateExtractor;
import physics.vectorUtil.XExtractor;
import physics.vectorUtil.YExtractor;
import physics.vectorUtil.ZExtractor;

/**
 * entity system applying movement to every entity
 * @author martin
 */
public class Movement extends EntitySystem
{

	public static boolean DEBUG = false;

	/**
	 * default constructor
	 */
	public Movement()
	{

	}


	public Movement clone()
	{
		Movement newSystem = new Movement();
		newSystem.setPriority(priority);
		return newSystem;
	}

	/**
	 * @param e engine to which this was added
	 * adds all moveable physics.entities of e to this system
	 * is automatically called by engine
	 */
	public void addedToEngine (Engine e)
	{
		for (Entity ent : e.getEntities())
		{
			if (Families.MOVING.matches(ent))
				entities().add(ent);
		}
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
			//initial conditions
			Force f = CompoMappers.FORCE.get(move);
			Mass m = CompoMappers.MASS.get(move);
			Velocity v = CompoMappers.VELOCITY.get (move);
			Position p = CompoMappers.POSITION.get(move);

			//odes
			//a = dv / dt
			//v = dp / dt
			//initial condition y = [dv, dp] = [F / m, p]

			float x = solveODE(f, v, p, m.mMass, dTime, new XExtractor());
			float y = solveODE(f, v, p, m.mMass, dTime, new YExtractor());
			float z = solveODE(f, v, p, m.mMass, dTime, new ZExtractor());

			p.set(x, y, z);

			if (CompoMappers.BODY.has(move))
				moveBody(CompoMappers.BODY.get(move), p);

			if (DEBUG)
				debugOut(move);

			f.setZero();
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


	public void debugOut(Entity moving)
	{
		System.out.print ("entity at " + CompoMappers.POSITION.get(moving));
		System.out.print (" v= " + CompoMappers.VELOCITY.get(moving));
		System.out.println (" entity " + moving);
	}

	/**
	 * translates every body by the vector change
	 * @param b a given body
	 * @param position new position of the body
     */
	private void moveBody(Body b, Vector3 position)
	{
		b.setPosition(position);
	}

	/**
	 *
	 * @param force
	 * @param velocity
	 * @param position
	 * @param mass
	 * @param time
     * @param extractor
     * @return the displacement for the given coordinate
     */
	private float solveODE(Vector3 force, Vector3 velocity, Vector3 position, double mass, float time, CoordinateExtractor extractor)
	{
		double initA = extractor.extract(force) / mass;
		double initV = extractor.extract(velocity);
		double initP = extractor.extract(position);

		ODEquation deAcceleration = new ODEquation() {
			@Override
			public double evaluate(double t, double[] ys) {
				return ys[0];
			}
		};

		ODEquation deVelocity = new ODEquation() {
			@Override
			public double evaluate(double t, double[] ys)
			{
				//a = dv / dt
				return ys[0] / t;
			}
		};
		ODEquation dePosition = new ODEquation() {
			@Override
			public double evaluate(double t, double[] ys) {
				//v = dp / dt
				return ys[1] / t;
			}
		};

		getODESolver().setEquations(deAcceleration, deVelocity, dePosition);
		getODESolver().setInitialYs(initA, initV, initP);
		getODESolver().setInitialT(1);

		return (float) getODESolver().solve(time + 1, PhysicsCoefficients.ODE_STEPS)[1];
	}


}
