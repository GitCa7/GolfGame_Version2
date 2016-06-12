package framework;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.FloatCounter;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import framework.components.*;
import framework.entities.Player;
import framework.internal.components.Active;
import framework.systems.EntityListener;
import framework.systems.GoalSystemFactory;
import framework.systems.TurnSystemFactory;
import physics.collision.CollisionRepository;
import physics.components.*;
import physics.constants.CompoMappers;
import physics.constants.PhysicsCoefficients;
import physics.entities.Ball;
import framework.entities.EntityFactory;
import physics.entities.Hole;
import physics.geometry.planar.Rectangle;
import physics.geometry.planar.Shape;
import physics.geometry.planar.Triangle;
import physics.geometry.spatial.*;
import physics.systems.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Class responsible for configuring an engine stored such that it contains all physics.entities, physics.components and physics.systems the user desires.
 * This class is also used as a factory for game classes sharing the same engine.
 * @autor martin
 * created 17.05.2016
 */
public class GameConfigurator
{
	/**
	 * constructs game configurator using a new engine with an empty map of balls, no hole set and no physics.systems stored
	 */
	public GameConfigurator ()
	{
		mSystemsTracker = new SystemsTracker();
		mEngine = new Engine();
		mBallMap = new HashMap<>();

        mBallFactory = new EntityFactory(mSystemsTracker);
        mPlayerFactory = new EntityFactory(mSystemsTracker);

        mBallPositionFactory = new PositionFactory();
        mBallMassFactory = new MassFactory();
        mBallBodyFactory = new BodyFactory();

        mBallGoalFactory = new GoalFactory();

        mPlayerNameFactory = new NameFactory();

        mObstacleBodyFactory = new BodyFactory();
        mObstaclePositionFactory = new PositionFactory();

        mSetHole = false;

        initFactories();
	}

	/**
	 *
	 * @return an entity factory fit for use with this game configurator object
	 */
	public EntityFactory entityFactory()
	{
		return new EntityFactory (mSystemsTracker);
	}

	/**
	 * Precondition: there is at least one ball stored and the hole is set.
	 * @return a new game object. Games constructed by the same game configurator share the same engine.
	 */
	public Game game()
	{
		if (mBallMap.size() < 1)
			throw new IllegalStateException ("there are no players/balls");


        if (!mSetHole)
			throw new IllegalStateException ("the hole is not set");

        //set components indicating which player succeeds another
        setNextPlayers();
        //add entity systems processing components to the engine
		addAllSystems();
		return new Game (mEngine, mBallMap);
	}

    /**
     * Construct a new player and a new ball. Map this player to this ball. Players will take turns in the order
     * they were added. The player added will contain all the necessary components for a turn-taking entitiy (see framework
     * families) and the ball will contain all entities associated with the moving, accelerable, gravity-attracted, colliding and
     * friction families (see physics families).
     * @param name player's name
     * @param ballRadius ball's radius
     * @param ballMass ball's mass
     * @param initBallPos ball's initial position
     */
    public void addPlayerAndBall(String name, float ballRadius, float ballMass, Vector3 initBallPos)
    {
        Ball b = constructBall(ballRadius, ballMass, initBallPos);
        Player p = constructPlayer(name);
        //set the first player as active player
        if (mBallMap.isEmpty())
        {
            assert(framework.constants.CompoMappers.TURN.has(p.mEntity));
            Turn playerTurn = framework.constants.CompoMappers.TURN.get(p.mEntity);
            playerTurn.mTurn = true;
        }

        mBallMap.put(p, b);
        mEngine.addEntity(b.mEntity);
        mEngine.addEntity(p.mEntity);
    }

    /**
     * Constructs a new obstacle. Every obstacle added this way will contain all the entities associated with the colliding
     * family (see physics families).
     * @param bodySolids a collection of solids. The vertex with the smallest euclidean distance to the origin is fixed as the
     *                   position of the obstacle.
     */
    public void addObstacle(Collection<SolidTranslator> bodySolids)
    {
        //find translation vector of smallest magnitude
        SolidTranslator min = null;
        float minLen = Float.MAX_VALUE;
        for (SolidTranslator bodyPart : bodySolids)
        {
            float partLen = bodyPart.getPosition().len();
            if (minLen > partLen)
            {
                minLen = partLen;
                min = bodyPart;
            }
        }

        //set as position
        mObstaclePositionFactory.setVector(min.getPosition());
        mObstacleBodyFactory.clear();
        for (SolidTranslator bodyPart : bodySolids)
                mObstacleBodyFactory.addSolid(bodyPart);


    }

    /**
     * Constructs a new hole. This hole has all components associtated with the NAME_THEM_HERE! families.
     * @param position The shape of the hole, embedding the position where the hole is located.
     * @param size length of a side of the cube delimiting the hole
     */
    public void setHole(Vector3 position, float size)
    {
        Vector3 d = new Vector3(size, 0, 0);
        Vector3 w = new Vector3(0, size, 0);
        Vector3 h = new Vector3(0, 0, size);
        Box holeBox = BoxPool.getInstance().getInstance(new BoxParameter(w,d,h));

        mBallGoalFactory.setGoal(holeBox, position);
        //replace previous goal component
        for (Ball ballsAdded : mBallMap.values())
        {
            ballsAdded.mEntity.remove(Goal.class);
            ballsAdded.mEntity.add(mBallGoalFactory.produce());
        }
    }

    /**
     * Constructs a new obstacle for the terrain within the given area. This will transform the given mesh triangles
     * into tetrahedra used for collision checking.
     * @param meshTriangles a collection of triangles approximating the surface of the terrain.
     */
    public void setTerrain(Collection<Triangle> meshTriangles)
    {
        //@TODO implement terrain setting
        throw new UnsupportedOperationException("the terrain is not yet relevant and thus not implemented");
    }

    /**
     * constructs ball of given radius, mass and sets the initial position of it
     * SIMPLIFICATION: A BOX IS USED TO APPROXIMATE THE BALL
     * @param radius ball's radius
     * @param mass ball's mass
     * @param initPos ball's initial position
     * @return a ball instance containing a ball entity with components for the desired properties
     */
    private Ball constructBall(float radius, float mass, Vector3 initPos)
    {
        //@TODO tetrahedrize sphere
        mBallPositionFactory.setVector(initPos);
        mBallMassFactory.setParameter(mass);
        mBallBodyFactory.clear();

        Vector3 sphereDep = new Vector3 (2*radius, 0, 0);
        Vector3 sphereWid = new Vector3 (0, 2*radius, 0);
        Vector3 sphereHig = new Vector3 (0, 0, 2*radius);
        BoxParameter sphereBound = new BoxParameter(sphereDep, sphereWid, sphereHig);
        Box sphereBoundingBox = BoxPool.getInstance().getInstance(sphereBound);
        mBallBodyFactory.addSolid(new SolidTranslator (sphereBoundingBox, initPos));
        return new Ball (mBallFactory.produce());
    }

    /**
     * constructs player. The next player component still needs to be set properly, as it contains null by default.
     * @param name player's name
     * @return a new player wrapper containing a entity having components for the desired properties
     */
    private Player constructPlayer(String name)
    {
        mPlayerNameFactory.set(name);
        return new Player (mPlayerFactory.produce());
    }

    /**
     * constructs component bundles neccesary for the entity factories to work
     */
    private void initFactories()
    {
        //additional component factories for balls
        VelocityFactory ballVelocityFactory = new VelocityFactory();
        ForceFactory ballForceFactory = new ForceFactory();
        GravityForceFactory ballGravityFactory = new GravityForceFactory();
        FrictionFactory ballFrictionFactory = new FrictionFactory();
        //construct system factories connected to collision repository
        CollisionDetectionSystemFactory collisionDetectionFactory = new CollisionDetectionSystemFactory();
        CollisionImpactSystemFactory collisionImpactFactory = new CollisionImpactSystemFactory();
        NormalForceSystemFactory normalForceFactory = new NormalForceSystemFactory();
        NonPenetrationSystemFactory nonPenetrationFactory = new NonPenetrationSystemFactory();
        //set repository
        CollisionRepository collisionRepo = new CollisionRepository();
        collisionDetectionFactory.setRepository(collisionRepo);
        collisionImpactFactory.setRepository(collisionRepo);
        normalForceFactory.setRepository(collisionRepo);
        nonPenetrationFactory.setRepository(collisionRepo);
        //set default parameters of component factories for balls we dont need to change
        ballVelocityFactory.setVector(new Vector3());
        ballForceFactory.setVector(new Vector3());
        ballGravityFactory.setParameter(new Vector3());
        ballFrictionFactory.setParameter(PhysicsCoefficients.STATIC_FRICTION, PhysicsCoefficients.DYNAMIC_FRICTION, 0, 0);
        ballGravityFactory.setParameter(new Vector3 (0, 0, -PhysicsCoefficients.GRAVITY_EARTH));
        //construct ball component bundles
        ComponentBundle ballPosition = new ComponentBundle(mBallPositionFactory);
        ComponentBundle ballVelocity = new ComponentBundle(ballVelocityFactory, new MovementFactory());
        ComponentBundle ballForce = new ComponentBundle(ballForceFactory, new ForceApplyFactory());
        ComponentBundle ballFriction = new ComponentBundle(ballFrictionFactory, new FrictionSystemFactory());
        ComponentBundle ballMass = new ComponentBundle(mBallMassFactory);
        ComponentBundle ballBody = new ComponentBundle(mBallBodyFactory, collisionDetectionFactory, collisionImpactFactory, nonPenetrationFactory);
        ComponentBundle ballGravity = new ComponentBundle(ballGravityFactory, new GravitySystemFactory(), normalForceFactory, nonPenetrationFactory);
        ComponentBundle ballGoal = new ComponentBundle(mBallGoalFactory, new GoalSystemFactory());
        //add bundles to ball factory
        mBallFactory.addComponent(ballPosition, ballVelocity, ballFriction);
        mBallFactory.addComponent(ballForce, ballGravity);
        mBallFactory.addComponent(ballMass, ballBody, ballGoal);

        //additional component factories for players
        TurnFactory playerTurnFactory = new TurnFactory();
        NextPlayerFactory playerNextFactory = new NextPlayerFactory();
        //set default parameter of component facotires for players we dont need to change
        playerNextFactory.setNextPlayer(null);
        //construct player component bundles
        ComponentBundle playerTurn = new ComponentBundle(playerTurnFactory, new TurnSystemFactory());
        ComponentBundle playerName = new ComponentBundle(mPlayerNameFactory);
        //add bunldes to player factory
        mPlayerFactory.addComponent(playerTurn, playerName);


        //construct obstacle component bundles
        ComponentBundle obsBody = new ComponentBundle(mObstacleBodyFactory);
        ComponentBundle obsPosition = new ComponentBundle(mObstaclePositionFactory);

        //add bunldes to obstacle factory
        mObstacleFactory.addComponent(obsBody, obsPosition);
    }

	/**
	 * adds all systems tracked by the systems tracker to the engine
	 */
	private void addAllSystems()
	{
		for (EntitySystem es : mSystemsTracker.systemsInUse())
        {
            mEngine.addSystem (es);
            EntityListener listenEntityChanges = new EntityListener(es);
            mEngine.addEntityListener(listenEntityChanges);
        }
	}

    /**
     * adds components indicating turn transitions between players
     */
    private void setNextPlayers()
    {
        ArrayList<Player> players = new ArrayList<>(mBallMap.keySet());

        for (int cPlayer = 0; cPlayer < players.size(); ++cPlayer)
        {
            Player next = players.get((cPlayer + 1) % players.size());
            players.get(cPlayer).mEntity.add(new NextPlayer(next));
        }
    }

	/** mapping of players to their balls */
	private HashMap<Player, Ball> mBallMap;
	/** engine to configure */
	private Engine mEngine;
	/** objects tracking physics.systems in use */
	private SystemsTracker mSystemsTracker;
    /** entity factories used to instantiate certain kinds of entities */
    private EntityFactory mBallFactory, mPlayerFactory, mObstacleFactory;
    /** handles for component factories used in entity factories */
    /** ball */
    private PositionFactory mBallPositionFactory;
    private MassFactory mBallMassFactory;
    private BodyFactory mBallBodyFactory;
    private GoalFactory mBallGoalFactory;
    /** player */
    private NameFactory mPlayerNameFactory;
    /** obstacles */
    private BodyFactory mObstacleBodyFactory;
    private PositionFactory mObstaclePositionFactory;

    //boolean flags for conditions fulfilled before start
    boolean mSetHole;
}
