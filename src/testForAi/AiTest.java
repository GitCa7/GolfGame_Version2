package testForAi;

import java.util.ArrayList;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

import GamePackage.GameLoader;
import GamePackage.GameVisual;
import TerrainComponents.TerrainData;
import TerrainComponents.TerrainGeometryCalc;
import framework.main.Game;
import framework.main.GameConfigurator;
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
import framework.entities.Ball;
import physics.geometry.planar.Triangle;
import physics.geometry.spatial.Box;
import physics.geometry.spatial.BoxParameter;
import physics.geometry.spatial.BoxPool;
import physics.geometry.spatial.SolidTranslator;
import physics.geometry.spatial.TetrahedronParameter;
import physics.geometry.spatial.TetrahedronPool;
import physics.systems.CollisionDetectionSystem;
import physics.systems.ForceApply;
import physics.systems.FrictionSystem;
import physics.systems.Movement;

public class AiTest {
	public static void main (String[] args)
	{

		Vector3 initBallPos = new Vector3(-100, 0,-100);

		AiTest test = new AiTest (initBallPos);
		Vector3 hitForce = new Vector3 (-800, 0, -800);

		test.init();

		int iterations = 20;


		boolean truth = true;
		
		while(test.mVisualizer.stillDispalyed())
		{
			test.updateEngine();

			
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
	public AiTest(Vector3 ballPos)
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
		mBall.mEntity.add(new GravityForce(new Vector3 (0, -10, 0)));
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
			System.out.println("Will deliver force: " + force);
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

	public void initGame() throws Exception
    {
        GameConfigurator config = new GameConfigurator();

        //terrain from huge triangle
        TerrainData terrainDat = new TerrainData(4, 300);
        TerrainGeometryCalc calcualte = new TerrainGeometryCalc();
        
        ArrayList<Triangle> terrainMesh = calcualte.getAllTris(terrainDat);
        config.setTerrain(terrainMesh);

        //add a box obstacle
        Vector3 boxPos = new Vector3(10, -5, 10);
        BoxParameter bp = new BoxParameter(new Vector3(-20, 0, 0), new Vector3(0, 0, 25), new Vector3(0, 20, 0));
        ArrayList<SolidTranslator> boxBodyList = new ArrayList<>();
        boxBodyList.add(new SolidTranslator(BoxPool.getInstance().getInstance(bp), boxPos));
        config.addObstacle(boxBodyList);

        //add a tetrahedron obstacle
        Vector3 tetPos = new Vector3(75, 0, -40);
        TetrahedronParameter tp = new TetrahedronParameter(new Vector3(15, 0, 0), new Vector3(0, 0, 15), new Vector3(8, 8, 10));
        ArrayList<SolidTranslator> tetBodyList = new ArrayList<>();
        tetBodyList.add(new SolidTranslator(TetrahedronPool.getInstance().getInstance(tp), tetPos));
        config.addObstacle(tetBodyList);

        //set the hole
        Vector3 holePosition = new Vector3(100, 0, 0);
        float holeSize = 20;
        config.setHole(holePosition, holeSize);

        float ballRadius = 3, ballMass = 5;
        Vector3 initBallPos = new Vector3();
        config.addHumanAndBall("martin", ballRadius, ballMass, initBallPos);
        //comment above and uncomment below for bot
     //   config.addBotAndBall("bot", ballRadius, ballMass, initBallPos);
        mGame = config.game();
    }
	
	/**
	 * ends test
	 */
	public void close()
	{
		mVisualizer.endDisplay();
	}

	private Game mGame;
	public GameVisual mVisualizer;
	private Ball mBall;
	private Engine mEngine;
	private GameLoader mLoader;
    

}
