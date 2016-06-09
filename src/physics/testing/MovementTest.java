package physics.testing;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

import GamePackage.GameVisual;
import TerrainComponents.Terrain;
import TerrainComponents.TerrainData;
import physics.components.*;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.entities.Ball;
import physics.systems.*;

public class MovementTest 
{
	
	public static void main (String[] args)
	{
		Vector3 initBallPos = new Vector3 (-100, 20, -100);
		MovementTest test = new MovementTest (initBallPos);
		Vector3 hitForce = new Vector3 (10000, 0, 0);
		test.hitBall(hitForce);
		test.init();
		
		

		int iterations = 1000;
		
		boolean truth = true;
		
		while(truth && mVisualizer.)
		{
			test.updateEngine();
			test.printBallPosition();
			try
			{
				Thread.sleep(1000);
			}
			catch (Exception e) { System.out.println ("oh no"); }
		}

		test.close();
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
	 * instantiates a test for ball dynamics and initializes the ball position.
	 * Uses movement, force apply and friction systems.
	 * @param ballPos initial ball position.
     */
	public MovementTest(Vector3 ballPos)
	{
		mBall = new Ball (new Entity());
		//set and add components to ball
		Position initPos = new Position();
		initPos.set(ballPos);
		mBall.mEntity.add(initPos);
		mBall.mEntity.add(new Velocity());
		mBall.mEntity.add(new Friction(.5f, .5f, 0, 0));
		mBall.mEntity.add(new Mass (5));
		mBall.mEntity.add(new Force());
		mBall.mEntity.add(new GravityForce(new Vector3 (0, 0, -10)));
		assert (Families.ACCELERABLE.matches(mBall.mEntity));
		
		mEngine = new Engine();
		//add ball to engine
		mEngine.addEntity (mBall.mEntity);
		assert (mEngine.getEntitiesFor(Families.ACCELERABLE).size() > 1);
		//construct, set and add systems to engine
		Movement move = new Movement();
		ForceApply applyForce = new ForceApply();
		FrictionSystem applyFriction = new FrictionSystem();
		applyFriction.setPriority(1);
		applyForce.setPriority(2);
		move.setPriority(3);
		
		mEngine.addSystem (new Movement());
		mEngine.addSystem (new ForceApply());
		mEngine.addSystem (new FrictionSystem());
		
		mVisualizer = new GameVisual();
		mVisualizer.setEngine(mEngine);
		mVisualizer.setTerrain(new TerrainData());
		
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
		mVisualizer.updateDisplay();
	}

	/**
	 * prints the ball position and velocity to the console
	 */
	public void printBallPosition()
	{
		Position ballPos = CompoMappers.POSITION.get (mBall.mEntity);
		Velocity ballV = CompoMappers.VELOCITY.get(mBall.mEntity);
		//System.out.println ("ball position " + ballPos + " velocity " + ballV);
	}

	/**
	 * starts up test
	 */
	public void init()
	{
		mVisualizer.setEngine(mEngine);
		mVisualizer.startDisplay();
	}

	/**
	 * ends test
	 */
	public void close()
	{
		mVisualizer.endDisplay();
	}
	
	
	public GameVisual mVisualizer;
	private Ball mBall;
	private Engine mEngine;
}
