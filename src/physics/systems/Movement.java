package physics.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Engine;

import com.badlogic.gdx.math.Vector3;
import framework.systems.EntitySystem;
import physics.components.*;
import physics.constants.CompoMappers;
import physics.constants.Families;

import physics.constants.PhysicsCoefficients;
import physics.generic.numerical.ode.ODEquation;
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
		newSystem.setODESolver(getODESolver());
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

			Vector3 vvec = v.cpy(), pvec = p.cpy();

			solveODE(f, vvec, pvec, m.mMass, dTime, new XExtractor());
			solveODE(f, vvec, pvec, m.mMass, dTime, new YExtractor());
			solveODE(f, vvec, pvec, m.mMass, dTime, new ZExtractor());

			v.set(vvec);
			p.set(pvec);

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
	 * solves the de for a particular coordinate and set the values
	 * @param force
	 * @param velocity
	 * @param position
	 * @param mass
	 * @param time
     * @param extractor
     * @return the displacement for the given coordinate
     */
	private void solveODE(Vector3 force, Vector3 velocity, Vector3 position, double mass, float time, CoordinateExtractor extractor)
	{

		class VelocityDE implements ODEquation
		{
			@Override
			public double evaluate(double t, double[] ys)
			{
				return ys[0] / t;
			}
		}

		class PositionDE implements ODEquation
		{

			public PositionDE(double v0) { mV0 = v0; }

			@Override
			public double evaluate(double t, double[] ys)
			{
				return (mV0 + ys[0]) * t;
			}

			private double mV0;
		}

		double v0 = extractor.extract(velocity);

		double initV = extractor.extract(force)  / mass * PhysicsCoefficients.FORCE_TO_MOMENTUM_COEFFICIENT;
		double initP = 0;

		getODESolver().setEquations(new VelocityDE(), new PositionDE(v0));
		getODESolver().setInitialYs(initV, initP);
		getODESolver().setInitialT(1);

		double[] solution = getODESolver().solve(time + 1, PhysicsCoefficients.ODE_STEPS);
		extractor.set(velocity, (float) solution[0] + extractor.extract(velocity));
		extractor.set(position, (float) solution[1] + extractor.extract(position));
	}


}
