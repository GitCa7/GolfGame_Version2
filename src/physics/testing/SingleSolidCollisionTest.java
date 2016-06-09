package physics.testing;

import GamePackage.GameVisual;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import physics.collision.CollisionRepository;
import physics.components.*;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.entities.Ball;
import physics.geometry.spatial.Box;
import physics.geometry.spatial.BoxParameter;
import physics.geometry.spatial.BoxPool;
import physics.geometry.spatial.SolidTranslator;
import physics.systems.*;

public class SingleSolidCollisionTest
{

	public static void main (String[] args)
	{
		Vector3 initBallPos = new Vector3 (0, 0, 1);
		SingleSolidCollisionTest test = new SingleSolidCollisionTest(initBallPos);
		//test.display();

		Vector3 hitForce = new Vector3 (2.75f, 3f, 0f);
		test.hitBall(hitForce);

		int iterations = 10;

		for (int cnt = 0; cnt < iterations; ++cnt)
		{
			test.updateEngine();
			test.printBallPosition();
			try
			{
				Thread.sleep(50);
			}
			catch (Exception e) { System.out.println ("oh no"); }
		}
	}

	/** default time delta*/
	public static final float DT = 1;

	/** Runner for graphics */
	public class VisualsRunner implements Runnable
	{
		public void run()
		{
			mVisualizer.setEngine(mEngine);
		}
	}

	/**
	 * instantiates a test for ball-obstacle collision and initializes the ball position.
	 * Places a cubic obstacle of size 2x5x3 at (10|10|0)
	 * Uses movement, force apply systems.
	 * Uses collision detection, collision impact systems
	 * @param ballPos initial ball position.
     */
	public SingleSolidCollisionTest(Vector3 ballPos)
	{
		mBall = new Ball (new Entity());
		//set and add components to ball
		Position initPos = new Position();
		initPos.set(ballPos);
		Vector3 bD =new Vector3(2, 0, 0), bW = new Vector3(0, 2, 0), bH = new Vector3(0, 0, 2);
		Box ballBodyBox = BoxPool.getInstance().getInstance(new BoxParameter(bD, bW, bH));
		Body ballBody = new Body();
		ballBody.add(new SolidTranslator(ballBodyBox, initPos.cpy()));
		mBall.mEntity.add(initPos);
		mBall.mEntity.add(new Velocity());
		mBall.mEntity.add(new Friction(.5f, .5f, 0, 0));
		mBall.mEntity.add(new Mass (1));
		mBall.mEntity.add(new Force());
		mBall.mEntity.add(new GravityForce(new Vector3 (0, 0, -10)));
		mBall.mEntity.add(ballBody);
		assert (Families.ACCELERABLE.matches(mBall.mEntity));
		assert (Families.COLLIDING.matches(mBall.mEntity));
		//set and add components to obstacle
		Entity obstacle = new Entity();
		Position obsPos = new Position();
		obsPos.set(10, 10, 0);
		Body obsBody = new Body();
		Vector3 oD = new Vector3(2, 0, 0), oW = new Vector3(0, 5, 0), oH = new Vector3(0, 0, 3);
		Box obsBodyBox = BoxPool.getInstance().getInstance(new BoxParameter(oD, oW, oH));
		obsBody.add(new SolidTranslator(obsBodyBox, obsPos.cpy()));
		obstacle.add(obsPos);
		obstacle.add(obsBody);
		assert (Families.COLLIDING.matches(obstacle));

		mEngine = new Engine();
		//add ball to engine
		mEngine.addEntity (mBall.mEntity);
		assert (mEngine.getEntitiesFor(Families.ACCELERABLE).size() >= 1);
		//add obstacle to game
		mEngine.addEntity(obstacle);
		assert (mEngine.getEntitiesFor(Families.COLLIDING).size() >= 2);
		//construct, set and add systems to engine
		Movement move = new Movement();
		ForceApply applyForce = new ForceApply();
		CollisionRepository collisionRepo = new CollisionRepository();
		CollisionDetectionSystem detectCollision = new CollisionDetectionSystem();
		detectCollision.setRepository(collisionRepo);
		CollisionImpactSystem impactCollision = new CollisionImpactSystem();
		impactCollision.setRepository(collisionRepo);

		detectCollision.setPriority(1);
		impactCollision.setPriority(2);
		applyForce.setPriority(4);
		move.setPriority(5);

		mEngine.addSystem(detectCollision);
		mEngine.addSystem(impactCollision);
		mEngine.addSystem (move);
		mEngine.addSystem (applyForce);

		/*
		mVisualizer = new GameVisual();
		mVisualizer.setTerrain(new TerrainData());
		*/
	}

	/**
	 * applies force to the ball
	 * @param force a given directed force
     */
	public void hitBall (Vector3 force)
	{
		Force f = CompoMappers.FORCE.get(mBall.mEntity);
		f.add (force);
	}

	/**
	 * advances the state of the engine
	 */
	public void updateEngine()
	{
		mEngine.update (DT);
	}

	/**
	 * prints the ball position and velocity to the console
	 */
	public void printBallPosition()
	{
		Position ballPos = CompoMappers.POSITION.get (mBall.mEntity);
		Velocity ballV = CompoMappers.VELOCITY.get(mBall.mEntity);
		System.out.println ("ball position " + ballPos + " velocity " + ballV);
	}

	/**
	 * starts a new thread for the graphics and runs it.
	 */
	public void display()
	{
		Thread t = new Thread (new VisualsRunner());
		t.setDaemon(true);
		t.run();
	}
	
	
	private GameVisual mVisualizer;
	private Ball mBall;
	private Engine mEngine;
}
