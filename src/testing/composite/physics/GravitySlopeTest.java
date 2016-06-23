package testing.composite.physics;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

import GamePackage.GameVisual;
import physics.collision.structure.CollisionRepository;
import physics.components.Body;
import physics.components.Force;
import physics.components.Friction;
import physics.components.GravityForce;
import physics.components.Mass;
import physics.components.Position;
import physics.components.Velocity;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.constants.PhysicsCoefficients;
import framework.entities.Ball;
import physics.geometry.spatial.Box;
import physics.geometry.spatial.BoxParameter;
import physics.geometry.spatial.BoxPool;
import physics.geometry.spatial.SolidTranslator;
import physics.systems.CollisionDetectionSystem;
import physics.systems.CollisionImpactSystem;
import physics.systems.ForceApply;
import physics.systems.GravitySystem;
import physics.systems.Movement;
import physics.systems.NormalForceSystem;

public class GravitySlopeTest
{

	public static void main (String[] args)
	{
		Vector3 initBallPos = new Vector3(0, 0, 0);
		GravitySlopeTest test = new GravitySlopeTest(initBallPos);
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
	public static final float DT = .1f;

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
	public GravitySlopeTest(Vector3 ballPos)
	{
		mBall = new Ball (new Entity());
		//set and add components to ball
		Position initPos = new Position();
		initPos.set(ballPos);
		Vector3 bD =new Vector3(1, 0, 0), bW = new Vector3(0, 1, 0), bH = new Vector3(0, 0, 1);
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
		mBall.mEntity.add(new GravityForce(new Vector3 (0, -PhysicsCoefficients.GRAVITY_EARTH, 0)));
		assert (Families.ACCELERABLE.matches(mBall.mEntity));
		assert (Families.COLLIDING.matches(mBall.mEntity));

		Entity ground = new Entity();
		//set components of ground
		Position groundPosition = new Position(-50, -50, 50);
		Body groundBody = new Body();
		BoxParameter groundBoxParam = new BoxParameter(new Vector3(100, 0, 0), new Vector3(0, 100, -100), new Vector3(0, -100, -100));
		Box groundBox  = BoxPool.getInstance().getInstance(groundBoxParam);
		groundBody.add(new SolidTranslator(groundBox, groundPosition));
		//add components to ground
		ground.add(groundPosition);
		ground.add(groundBody);
		assert (Families.COLLIDING.matches(ground));

		mEngine = new Engine();
		//add ball to engine
		mEngine.addEntity (mBall.mEntity);
		mEngine.addEntity(ground);
		assert (mEngine.getEntitiesFor(Families.ACCELERABLE).size() >= 1);
		assert (mEngine.getEntitiesFor(Families.COLLIDING).size() >= 2);
		//construct, set and add systems to engine
		Movement move = new Movement();
		ForceApply applyForce = new ForceApply();
		GravitySystem gravity = new GravitySystem();
		CollisionDetectionSystem collisionDetection = new CollisionDetectionSystem();
		NormalForceSystem normalForceApply = new NormalForceSystem();
		CollisionImpactSystem collisionImpact = new CollisionImpactSystem();
		//set collision repository for collision systems
		CollisionRepository collisionRepo = new CollisionRepository();
		collisionDetection.setRepository(collisionRepo);
		normalForceApply.setRepository(collisionRepo);
		collisionImpact.setRepository(collisionRepo);

		gravity.setPriority(1);
		collisionDetection.setPriority(2);
		normalForceApply.setPriority(3);
		collisionImpact.setPriority(4);
		applyForce.setPriority(5);
		move.setPriority(6);
		
		mEngine.addSystem (move);
		mEngine.addSystem (applyForce);
		mEngine.addSystem (gravity);
		mEngine.addSystem(collisionDetection);
		mEngine.addSystem(normalForceApply);
		mEngine.addSystem(collisionImpact);
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
