package physics.testing;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

import GamePackage.GameVisual;
import TerrainComponents.TerrainData;

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


public class MovementTest 
{
	
	public static void main (String[] args)
	{
		Vector3 initBallPos = new Vector3(0, 0, 0);
		MovementTest test = new MovementTest (initBallPos);
		Vector3 hitForce = new Vector3 (-800, 0, -800);
		//test.hitBall(hitForce);
		test.init();
		
		

		int iterations = 20;
		
		boolean truth = true;
		
		while(test.mVisualizer.stillDispalyed())
		{
			test.updateEngine();
			if(test.mVisualizer.hasForce())	{
				test.hitBall(test.mVisualizer.deliverForce());
				test.mVisualizer.setForcePresent(false);
			}
			
			test.printBallPosition();
			
		}

		test.close();
	}

	/** default time delta*/
	public static final float DT = 0.1f;

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
		
		Vector3 bD = new Vector3(1, 0, 0), bW = new Vector3(0, 1, 0), bH = new Vector3(0, 0, 1);
		Box ballBodyBox = BoxPool.getInstance().getInstance(new BoxParameter(bD, bW, bH));
		Body ballBody = new Body();
		ballBody.add(new SolidTranslator(ballBodyBox, initPos.cpy()));
		
		mBall.mEntity.add(initPos);
		mBall.mEntity.add(new Velocity());
		mBall.mEntity.add(new Friction(.5f, .5f, 0, 0));
		mBall.mEntity.add(new Mass (5));
		mBall.mEntity.add(new Force());
		mBall.mEntity.add(new GravityForce(new Vector3 (0, 0, -10)));
		mBall.mEntity.add(ballBody);
		assert (Families.ACCELERABLE.matches(mBall.mEntity));


		Entity ground = new Entity();
		//set components of ground
		Position groundPosition = new Position(-500, 0, -500);
		Body groundBody = new Body();
		BoxParameter groundBoxParam = new BoxParameter(new Vector3(1000, 0, 0), new Vector3(0, -200, 0), new Vector3(0, 0, 1000));
		Box groundBox  = BoxPool.getInstance().getInstance(groundBoxParam);
		groundBody.add(new SolidTranslator(groundBox, groundPosition));
		//add components to ground
		ground.add(groundPosition);
		ground.add(groundBody);
		assert (Families.COLLIDING.matches(ground));

		mEngine = new Engine();
		//add ball to engine
		mEngine.addEntity (mBall.mEntity);
		assert (mEngine.getEntitiesFor(Families.ACCELERABLE).size() >= 1);
		//add ground
		mEngine.addEntity(ground);
		assert (mEngine.getEntitiesFor(Families.COLLIDING).size() >= 2);
		//construct, set and add systems to engine
		Movement move = new Movement();
		ForceApply applyForce = new ForceApply();
		FrictionSystem applyFriction = new FrictionSystem();
		CollisionDetectionSystem detectCollisions = new CollisionDetectionSystem();

		CollisionRepository repo = new CollisionRepository();
		applyFriction.setRepository(repo);
		detectCollisions.setRepository(repo);

		detectCollisions.setPriority(0);
		applyFriction.setPriority(1);
		applyForce.setPriority(2);
		move.setPriority(3);
		
		mEngine.addSystem (move);
		mEngine.addSystem (applyForce);
		mEngine.addSystem (applyFriction);
		mEngine.addSystem (detectCollisions);
		
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
		if(force != null)	{
			System.out.println("Wil deliver force: " + force);
			Force f = CompoMappers.FORCE.get(mBall.mEntity);
			f.add (force);
		}
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
		System.out.println ("ball position " + ballPos + " velocity " + ballV);
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