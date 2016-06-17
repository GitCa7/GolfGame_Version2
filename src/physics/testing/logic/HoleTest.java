package physics.testing.logic;

import GamePackage.GameVisual;
import TerrainComponents.TerrainData;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.math.Vector3;
import framework.components.Goal;
import framework.components.Ownership;
import framework.systems.GoalSystem;
import physics.components.*;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.entities.Ball;
import physics.geometry.spatial.Box;
import physics.geometry.spatial.BoxParameter;
import physics.geometry.spatial.BoxPool;
import physics.geometry.spatial.SolidTranslator;
import physics.systems.ForceApply;
import physics.systems.FrictionSystem;
import physics.systems.Movement;

public class HoleTest
{

	public static void main (String[] args)
	{
		Vector3 initBallPos = new Vector3 (0, 0, 0);
        Vector3 holePos = new Vector3 (7.5f, -2.5f, -2.5f);
		HoleTest test = new HoleTest(initBallPos, holePos);
	//	test.display();

		Vector3 hitForce = new Vector3 (60, 0, 0);
		test.hitBall(hitForce);

		int iterations = 10;

		for (int cnt = 0; cnt < iterations; ++cnt)
		{
			test.updateEngine();
			test.printBallPosition();/*
			try
			{
				Thread.sleep(1000);
			}
			catch (Exception e) { System.out.println ("oh no"); }*/
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
	 * instantiates a test for ball dynamics and initializes the ball position.
	 * Uses movement, force apply and friction systems.
	 * @param ballPos initial ball position.
     */
	public HoleTest(Vector3 ballPos, Vector3 holePos)
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

        //construct goal box
        BoxParameter paraBox = new BoxParameter (new Vector3 (5, 0, 0), new Vector3(0, 5, 0), new Vector3 (0, 0, 5));
        Box goalBox = BoxPool.getInstance().getInstance(paraBox);
		Goal goal = new Goal(new SolidTranslator(goalBox, holePos));
		//construct ball body
        paraBox = new BoxParameter(new Vector3(1, 0, 0), new Vector3(0, 1, 0), new Vector3(0, 0, 1));
        Box ballBox = BoxPool.getInstance().getInstance(paraBox);
        Body ballBody = new Body();
        ballBody.add(new SolidTranslator(ballBox, ballPos));
        //add hole related components
        mBall.mEntity.add (goal);
        mBall.mEntity.add(ballBody);
        mBall.mEntity.add(new Ownership(new Entity()));
        assert (framework.constants.Families.GLOBAL_STATE.matches(mBall.mEntity));
        assert (framework.constants.Families.OWNED.matches(mBall.mEntity));

		
		mEngine = new Engine();
		//add ball to engine
		mEngine.addEntity (mBall.mEntity);
		assert (mEngine.getEntitiesFor(Families.ACCELERABLE).size() > 1);
		//construct, set and add systems to engine
        //movement systems
		Movement move = new Movement();
		ForceApply applyForce = new ForceApply();
		FrictionSystem applyFriction = new FrictionSystem();
		applyFriction.setPriority(1);
		applyForce.setPriority(2);
		move.setPriority(3);
        //hole system
        GoalSystem goalSystem = new GoalSystem();
        goalSystem.setPriority(10);

		mEngine.addSystem (new Movement());
		mEngine.addSystem (new ForceApply());
		mEngine.addSystem (new FrictionSystem());
        mEngine.addSystem(goalSystem);
        //add listener to detect when ball is in hole
        mEngine.addEntityListener(new EntityListener() {
            @Override
            public void entityAdded(Entity entity)
            {

            }

            @Override
            public void entityRemoved(Entity entity)
            {
                if (framework.constants.Families.GOAL_SEEKING.matches(entity))
                    System.out.println ("ball arrived at hole");
            }
        });
		
/*		mVisualizer = new GameVisual();
		mVisualizer.setTerrain(new TerrainData());  */
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
