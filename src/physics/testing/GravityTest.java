package physics.testing;

import GamePackage.GameVisual;
import TerrainComponents.TerrainData;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import physics.components.*;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.constants.PhysicsCoefficients;
import physics.entities.Ball;
import physics.systems.ForceApply;
import physics.systems.FrictionSystem;
import physics.systems.GravitySystem;
import physics.systems.Movement;

public class GravityTest
{

	public static void main (String[] args)
	{
		Vector3 initBallPos = new Vector3(0, 0, 100);
		GravityTest test = new GravityTest(initBallPos);
		Vector3 hitForce = new Vector3 (10000, 0, 0);
	//	test.hitBall(hitForce);
		test.init();



		int iterations = 10;

		boolean truth = true;

		for (int cnt = 0; cnt < iterations; ++cnt)
		{
			test.updateEngine();
			test.printBallPosition();

		}

		test.close();
	}

	/** default time delta*/
	public static final float DT = 1f;

	/** Runner for graphics */
	public class VisualsRunner implements Runnable
	{
		public void run()
		{
			mVisualizer.setEngine(mEngine);
		}
	}

	/**
	 * instantiates a test for ball dynamics and initializes the ball position.
	 * Uses movement, force apply and friction systems.
	 * @param ballPos initial ball position.
     */
	public GravityTest(Vector3 ballPos)
	{
		mBall = new Ball (new Entity());
		//set and add components to ball
		Position initPos = new Position();
		initPos.set(ballPos);
		mBall.mEntity.add(initPos);
		mBall.mEntity.add(new Velocity());
		mBall.mEntity.add(new Mass (1));
		mBall.mEntity.add(new Force());
		mBall.mEntity.add(new GravityForce(new Vector3 (0, 0, -PhysicsCoefficients.GRAVITY_EARTH)));
		assert (Families.ACCELERABLE.matches(mBall.mEntity));
		
		mEngine = new Engine();
		//add ball to engine
		mEngine.addEntity (mBall.mEntity);
		assert (mEngine.getEntitiesFor(Families.ACCELERABLE).size() >= 1);
		//construct, set and add systems to engine
		Movement move = new Movement();
		ForceApply applyForce = new ForceApply();
		GravitySystem gravity = new GravitySystem();
		gravity.setPriority(1);
		applyForce.setPriority(2);
		move.setPriority(3);
		
		mEngine.addSystem (move);
		mEngine.addSystem (applyForce);
		mEngine.addSystem (gravity);
		
	/*	mVisualizer = new GameVisual();
		mVisualizer.setEngine(mEngine);
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
	//	mVisualizer.updateDisplay();
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
	 * starts up test
	 */
	public void init()
	{
	/*	mVisualizer.setEngine(mEngine);
		mVisualizer.startDisplay();	*/
	}

	/**
	 * ends test
	 */
	public void close()
	{
	//	mVisualizer.endDisplay();
	}
	
	
	public GameVisual mVisualizer;
	private Ball mBall;
	private Engine mEngine;
}
