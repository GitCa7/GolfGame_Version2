package framework.testing;

import GamePackage.GameVisual;
import TerrainComponents.TerrainData;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import framework.components.Name;
import framework.components.NextPlayer;
import framework.components.Ownership;
import framework.components.Turn;
import framework.entities.Player;
import framework.systems.TurnSystem;
import physics.components.*;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.entities.Ball;
import physics.systems.ForceApply;
import physics.systems.FrictionSystem;
import physics.systems.Movement;

public class PlayerTransitionTest
{

	public static void main (String[] args)
	{
		Vector3 initBallPos = new Vector3(-100, 20, -100);
		PlayerTransitionTest test = new PlayerTransitionTest(initBallPos);
		Vector3 hitForce = new Vector3 (75, 0, 0);
		test.hitBall(hitForce);
		test.init();



		int iterations = 10;


		for (int cnt = 0; cnt < iterations; ++cnt)
		{
			test.updateEngine();
			test.printBallPosition();
			test.printCurrentPlayer();

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
	public PlayerTransitionTest(Vector3 ballPos)
	{
		mBall = new Ball (new Entity());
		mPlayer1 = new Player(new Entity());
		mPlayer2 = new Player(new Entity());
		//set and add components to ball
		Position initPos = new Position();
		initPos.set(ballPos);
		mBall.mEntity.add(initPos);
		mBall.mEntity.add(new Velocity());
		mBall.mEntity.add(new Friction(.5f, .5f, 0, 0));
		mBall.mEntity.add(new Mass (5));
		mBall.mEntity.add(new Force());
		mBall.mEntity.add(new GravityForce(new Vector3 (0, 0, -10)));
		mBall.mEntity.add(new Ownership(mPlayer1.mEntity));
		assert (Families.ACCELERABLE.matches(mBall.mEntity));
		assert (framework.constants.Families.OWNED.matches(mBall.mEntity));

		//set and add components to player
		Turn player1Turn = new Turn();
		player1Turn.mTurn = true;

		mPlayer1.mEntity.add(new NextPlayer(mPlayer2));
		mPlayer1.mEntity.add(player1Turn);
		mPlayer1.mEntity.add(new Name("p1"));
		mPlayer2.mEntity.add(new NextPlayer(mPlayer1));
		mPlayer2.mEntity.add(new Turn());
		mPlayer2.mEntity.add(new Name("p2"));
		assert (framework.constants.Families.TURN_TAKING.matches(mPlayer1.mEntity));
		assert (framework.constants.Families.TURN_TAKING.matches(mPlayer2.mEntity));

		mEngine = new Engine();
		//add ball to engine
		mEngine.addEntity (mBall.mEntity);
		assert (mEngine.getEntitiesFor(Families.ACCELERABLE).size() >= 1);

		//add the players
		mEngine.addEntity (mPlayer1.mEntity);
		mEngine.addEntity (mPlayer2.mEntity);
		assert (mEngine.getEntitiesFor(framework.constants.Families.TURN_TAKING).size() >= 2);

		//construct, set and add systems to engine
		Movement move = new Movement();
		ForceApply applyForce = new ForceApply();
		FrictionSystem applyFriction = new FrictionSystem();
		TurnSystem turnTransition = new TurnSystem();

		applyFriction.setPriority(1);
		applyForce.setPriority(2);
		move.setPriority(3);
		turnTransition.setPriority(4);
		
		mEngine.addSystem (new Movement());
		mEngine.addSystem (new ForceApply());
		mEngine.addSystem (new FrictionSystem());
		mEngine.addSystem (turnTransition);
	/*
		mVisualizer = new GameVisual();
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
	 * prints the name of the player whose turn it currently is
	 */
	public void printCurrentPlayer()
	{
		Turn t1 = framework.constants.CompoMappers.TURN.get(mPlayer1.mEntity);
		Turn t2 = framework.constants.CompoMappers.TURN.get(mPlayer2.mEntity);

		if (t1.mTurn)
			System.out.println ("it's player 1's turn");
		if (t2.mTurn)
			System.out.println ("it's player 2's turn");
	}

	/**
	 * starts up test
	 */
	public void init()
	{
	/*
		mVisualizer.setEngine(mEngine);
		mVisualizer.startDisplay();
	*/
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
	private Player mPlayer1, mPlayer2;
	private Engine mEngine;
}
