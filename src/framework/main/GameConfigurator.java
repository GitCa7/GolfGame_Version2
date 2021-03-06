package framework.main;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector3;

import framework.components.ComponentBundle;
import framework.systems.EntityListener;
import framework.systems.EntitySystem;
import framework.components.*;
import framework.entities.Ball;
import framework.entities.Player;

import framework.entities.EntityFactory;


import framework.systems.GoalSystemFactory;
import framework.systems.TurnSystemFactory;
import physics.collision.detection.*;
import physics.collision.structure.CollisionRepository;
import physics.components.*;
import physics.constants.PhysicsCoefficients;

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
        mGameObservers = new ArrayList<>();

        mBallFactory = new EntityFactory(mSystemsTracker);
        mPlayerFactory = new EntityFactory(mSystemsTracker);
        mObstacleFactory = new EntityFactory(mSystemsTracker);

        mBallPositionFactory = new PositionFactory();
        mBallMassFactory = new MassFactory();
        mBallBodyFactory = new BodyFactory();

        mBallGoalFactory = new GoalFactory();
        mBallOwnershipFactory = new OwnershipFactory();

        mPlayerNameFactory = new NameFactory();

        mObstacleBodyFactory = new BodyFactory();
        mObstaclePositionFactory = new PositionFactory();

        mMovementFactory = new MovementFactory();
        mForceApplyFactory = new ForceApplyFactory();
        mFrictionSystemFactory = new FrictionSystemFactory();

        mSettings = null;

        mSetHole = false;

        setSettings(new GameSettings());
        initFactories();
	}

    /**
     * constructs game configurator using a new engine with an empty map of balls, no hole set and no physics.systems stored
     */
    public GameConfigurator (GameSettings settings)
    {
        mSystemsTracker = new SystemsTracker();
        mEngine = new Engine();
        mBallMap = new HashMap<>();
        mGameObservers = new ArrayList<>();

        mBallFactory = new EntityFactory(mSystemsTracker);
        mPlayerFactory = new EntityFactory(mSystemsTracker);
        mObstacleFactory = new EntityFactory(mSystemsTracker);

        mBallPositionFactory = new PositionFactory();
        mBallMassFactory = new MassFactory();
        mBallBodyFactory = new BodyFactory();

        mBallGoalFactory = new GoalFactory();
        mBallOwnershipFactory = new OwnershipFactory();

        mPlayerNameFactory = new NameFactory();

        mObstacleBodyFactory = new BodyFactory();
        mObstaclePositionFactory = new PositionFactory();

        mMovementFactory = new MovementFactory();
        mForceApplyFactory = new ForceApplyFactory();
        mFrictionSystemFactory = new FrictionSystemFactory();

        mSettings = null;

        mSetHole = false;

        setSettings(settings);
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

        if (!checkSettings())
            throw new IllegalStateException("settings are not set properly");

        if (!mSetHole)
			throw new IllegalStateException ("the hole is not set");

        //set components indicating which player succeeds another
        setNextPlayers();

        //general engine update before systems are added to refresh
        mEngine.update(1);

        //add entity systems processing components to the engine
		addAllSystems();

		Game game =  new Game (mEngine, mBallMap, mSettings);
        //add input observers
        addAllObservers(game);

        return  game;
	}

    /**
     * sets the game settings
     * @param settings
     */
    public void setSettings(GameSettings settings)
    {
        mSettings = settings;

        mMovementFactory.setODESolver(mSettings.mODESolver);
        mForceApplyFactory.setODESolver(mSettings.mODESolver);
        mFrictionSystemFactory.setODESolver(mSettings.mODESolver);
    }

    /**
     * Construct a new human player and a new ball. Map this player to this ball. Players will take turns in the order
     * they were added. The player added will contain all the necessary components for a turn-taking entitiy (see framework
     * families) and the ball will contain all entities associated with the moving, accelerable, gravity-attracted, colliding and
     * friction families (see physics families).
     * @param name player's name
     * @param ballRadius ball's radius
     * @param ballMass ball's mass
     * @param initBallPos ball's initial position
     */
    public void addHumanAndBall(String name, float ballRadius, float ballMass, Vector3 initBallPos, PlayerObserver a)
    {
        addPlayerAndBall(name, ballRadius, ballMass, initBallPos,a);
    }

    /**
     * Construct a new bot player and a new ball. Map this player to this ball. Players will take turns in the order
     * they were added. The player added will contain all the necessary components for a turn-taking entitiy (see framework
     * families) and the ball will contain all entities associated with the moving, accelerable, gravity-attracted, colliding and
     * friction families (see physics families).
     * @param name player's name
     * @param ballRadius ball's radius
     * @param ballMass ball's mass
     * @param initBallPos ball's initial position
     */
    public void addBotAndBall(String name, float ballRadius, float ballMass, Vector3 initBallPos, PlayerObserver b)
    {
        addPlayerAndBall(name, ballRadius, ballMass, initBallPos, b);
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

        mEngine.addEntity(mObstacleFactory.produce());
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
        mSetHole = true;
    }

    /**
     * Constructs a new obstacle for the terrain within the given area. This will transform the given mesh triangles
     * into tetrahedra used for collision checking.
     * @param terrainPoints a collection of triangles approximating the surface of the terrain.
     */
    public void setTerrain(Collection<Triangle> terrainPoints)
    {

        Triangle[] triangleArray = terrainPoints.toArray(new Triangle[terrainPoints.size()]);
        TerrainTetrahedronBuilder tetBuilder = new TerrainTetrahedronBuilder(triangleArray);

        mObstacleBodyFactory.clear();

        Vector3 minTerrainPosition = null;
        float minTerrainDistance = Float.MAX_VALUE;

        for (SolidTranslator tetrahedron : tetBuilder.build(PhysicsCoefficients.TERRAIN_THICKNESS))
        {
            //add tetrahedron to body
            mObstacleBodyFactory.addSolid(tetrahedron);
            //compare & update minimum offset
            float offsetDistance = tetrahedron.getPosition().dot(tetrahedron.getPosition());
            if (minTerrainDistance > offsetDistance)
            {
                minTerrainDistance = offsetDistance;
                minTerrainPosition = tetrahedron.getPosition();
            }
        }

        mObstaclePositionFactory.setVector(minTerrainPosition.cpy());

        /*
        Vector3 min = new Vector3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        Vector3 max = new Vector3(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);
        for (Triangle t : terrainPoints)
        {
            for (Vector3 v : t.getVertices())
            {
                if (v.x < min.x)
                    min.x = v.x;
                if (v.y < min.y)
                    min.y = v.y;
                if (v.z < min.z)
                    min.z = v.z;

                if (v.x > max.x)
                    max.x = v.x;
                if (v.y > max.y)
                    max.y = v.y;
                if (v.z > max.z)
                    max.z = v.z;
            }
        }
        */

/*
        mObstacleBodyFactory.clear();
        BoxParameter groundParameter = new BoxParameter(new Vector3(10000, 0, 0), new Vector3(0, -10000, 0), new Vector3(0, 0, 10000));
        Box ground = BoxPool.getInstance().getInstance(groundParameter);
        mObstacleBodyFactory.addSolid(new SolidTranslator(ground, new Vector3(-5000, 0, -5000)));
        mObstaclePositionFactory.setVector(new Vector3(-5000, 0, -5000));
*/

        //add terrain obstacle to the engine
        mEngine.addEntity(mObstacleFactory.produce());

    }


    /**
     * constructs ball of given radius, mass and sets the initial position of it
     * SIMPLIFICATION: A BOX IS USED TO APPROXIMATE THE BALL
     * @param radius ball's radius
     * @param mass ball's mass
     * @param initPos ball's initial position
     * @return a ball instance containing a ball entity with components for the desired properties
     */
    private Ball constructBall(float radius, float mass, Vector3 initPos, Player owner)
    {
        //@TODO tetrahedrize sphere
        Vector3 ballCenter = new Vector3(initPos.x + radius, initPos.y + radius, initPos.z + radius);
        SphereTetrahedrizer tetSphere = new SphereTetrahedrizer(ballCenter, radius);

        mBallPositionFactory.setVector(initPos);
        mBallMassFactory.setParameter(mass);
        mBallOwnershipFactory.setOwner(owner.mEntity);
        mBallBodyFactory.clear();

    /*
        Vector3 sphereDep = new Vector3 (2*radius, 2*radius, 0);
        Vector3 sphereWid = new Vector3 (2*radius, -2*radius, 0);
        Vector3 sphereHig = new Vector3 (0, -2*radius, 2*radius);
    */
        Vector3 sphereDep = new Vector3 (2*radius*.5f, 2*radius*.70711f, 2*radius*-.5f);
        Vector3 sphereWid = new Vector3 (2*radius*-.5f, 2*radius*.70711f, 2*radius*.5f);
        Vector3 sphereHig = new Vector3 (2*radius*.70711f, 0, 2*radius*.70711f);

        BoxParameter sphereBound = new BoxParameter(sphereDep, sphereWid, sphereHig);
        Box sphereBoundingBox = BoxPool.getInstance().getInstance(sphereBound);
        mBallBodyFactory.addSolid(new SolidTranslator (sphereBoundingBox, initPos));
    /*
        int horizontal = 2;
        int halfVertical = 1;
        mBallBodyFactory.clear();
        for (SolidTranslator tets : tetSphere.tetrahedrize(halfVertical, horizontal))
        {
            mBallBodyFactory.addSolid(tets);
        }
     */
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

    private boolean checkSettings()
    {
        if (mSettings == null)
            return false;
        if (mSettings.mRandomGenerator == null)
            return false;
        if (mSettings.mODESolver == null)
            return false;

        return true;
    }

    /**
     * constructs component bundles neccesary for the entity factories to work
     */
    private void initFactories()
    {
        //dynamics system factories
        GravitySystemFactory gravitySystemFactory = new GravitySystemFactory();
        WindSystemFactory windSystemFactory = new WindSystemFactory();
        GoalSystemFactory goalSystemFactory = new GoalSystemFactory();
        //collision system factories
        CollisionDetectionSystemFactory collisionDetectionFactory = new CollisionDetectionSystemFactory();
        CollisionImpactSystemFactory collisionImpactFactory = new CollisionImpactSystemFactory();
        NormalForceSystemFactory normalForceFactory = new NormalForceSystemFactory();
        NonPenetrationSystemFactory nonPenetrationFactory = new NonPenetrationSystemFactory();
        //player system factories
        TurnSystemFactory turnSystemFactory = new TurnSystemFactory();

        RoundingSystemFactory roundingSystemFactory = new RoundingSystemFactory();

        //set system's priorities
        collisionDetectionFactory.setSystemPriority(1);
        nonPenetrationFactory.setSystemPriority(2);
        gravitySystemFactory.setSystemPriority(3);
        mFrictionSystemFactory.setSystemPriority(5);
        windSystemFactory.setSystemPriority(6);
        normalForceFactory.setSystemPriority(8);
        collisionImpactFactory.setSystemPriority(9);
        mForceApplyFactory.setSystemPriority(10);

        mMovementFactory.setSystemPriority(12);
        goalSystemFactory.setSystemPriority(16);
        turnSystemFactory.setSystemPriority(20);
        roundingSystemFactory.setSystemPriority(25);

        //set engine to attach listener
        collisionDetectionFactory.setEngine(mEngine);
        collisionDetectionFactory.setEngine(mEngine);
        collisionImpactFactory.setEngine(mEngine);
        normalForceFactory.setEngine(mEngine);
        gravitySystemFactory.setEngine(mEngine);
        windSystemFactory.setEngine(mEngine);
        mFrictionSystemFactory.setEngine(mEngine);
        mForceApplyFactory.setEngine(mEngine);
        mMovementFactory.setEngine(mEngine);
        nonPenetrationFactory.setEngine(mEngine);
        goalSystemFactory.setEngine(mEngine);
        turnSystemFactory.setEngine(mEngine);
        roundingSystemFactory.setEngine(mEngine);

        //set repository
        CollisionRepository collisionRepo = new CollisionRepository();
        collisionDetectionFactory.setRepository(collisionRepo);
        collisionImpactFactory.setRepository(collisionRepo);
        normalForceFactory.setRepository(collisionRepo);
        nonPenetrationFactory.setRepository(collisionRepo);
        mFrictionSystemFactory.setRepository(collisionRepo);

        //set collision detector in collision detection system
        collisionDetectionFactory.setCollisionDetector(mSettings.mCollisionDetector.clone());

        //additional component factories for balls
        VelocityFactory ballVelocityFactory = new VelocityFactory();
        ForceFactory ballForceFactory = new ForceFactory();
        GravityForceFactory ballGravityFactory = new GravityForceFactory();
        FrictionFactory ballFrictionFactory = new FrictionFactory();
        WindFactory ballWindFactory = new WindFactory();

        //set default parameters of component factories for balls we dont need to change
        ballVelocityFactory.setVector(new Vector3());
        ballForceFactory.setVector(new Vector3());
        ballFrictionFactory.setParameter(PhysicsCoefficients.STATIC_FRICTION, PhysicsCoefficients.DYNAMIC_FRICTION, 0, 0);
        ballFrictionFactory.setFluctuation(PhysicsCoefficients.FRICTION_FLUCTUATION);
        ballGravityFactory.setParameter(new Vector3 (0, -PhysicsCoefficients.GRAVITY_EARTH, 0));
        ballWindFactory.setParameter(   PhysicsCoefficients.WIND_MIN_INTENSITY,
                                        PhysicsCoefficients.WIND_MAX_INTENSITY,
                                        PhysicsCoefficients.WIND_FREQUENCY,
                                        PhysicsCoefficients.WIND_MIN_DURATION,
                                        PhysicsCoefficients.WIND_MAX_DURATION);
        //construct ball component bundles

        ComponentBundle ballPosition = new ComponentBundle(mBallPositionFactory);
        ComponentBundle ballVelocity = new ComponentBundle(ballVelocityFactory, mMovementFactory, roundingSystemFactory);
        ComponentBundle ballForce = new ComponentBundle(ballForceFactory);
        ComponentBundle ballFriction = new ComponentBundle(ballFrictionFactory, mFrictionSystemFactory);
        ComponentBundle ballMass = new ComponentBundle(mBallMassFactory);
        ComponentBundle ballBody = new ComponentBundle(mBallBodyFactory, collisionDetectionFactory, collisionImpactFactory, nonPenetrationFactory);
        ComponentBundle ballGravity = new ComponentBundle(ballGravityFactory, gravitySystemFactory, normalForceFactory);
        ComponentBundle ballWind = new ComponentBundle(ballWindFactory, windSystemFactory);
        ComponentBundle ballGoal = new ComponentBundle(mBallGoalFactory, goalSystemFactory);
        ComponentBundle ballOwnership = new ComponentBundle(mBallOwnershipFactory);


        //simplyfied
    /*    ComponentBundle ballPosition = new ComponentBundle(mBallPositionFactory);
        ComponentBundle ballVelocity = new ComponentBundle(ballVelocityFactory, movementFactory, roundingSystemFactory);
        ComponentBundle ballForce = new ComponentBundle(ballForceFactory, forceApplyFactory);
        ComponentBundle ballFriction = new ComponentBundle(ballFrictionFactory, frictionSystemFactory);
        ComponentBundle ballMass = new ComponentBundle(mBallMassFactory);
        ComponentBundle ballBody = new ComponentBundle(mBallBodyFactory, collisionDetectionFactory, collisionImpactFactory);
        ComponentBundle ballGravity = new ComponentBundle(ballGravityFactory, gravitySystemFactory, normalForceFactory);
        ComponentBundle ballGoal = new ComponentBundle(mBallGoalFactory, goalSystemFactory);
    */
        //add bundles to ball factory
        mBallFactory.addComponent(ballPosition, ballVelocity, ballFriction);
        mBallFactory.addComponent(ballForce, ballGravity, ballWind);
        mBallFactory.addComponent(ballMass, ballBody, ballGoal);
        mBallFactory.addComponent(ballOwnership);

        //additional component factories for players
        TurnFactory playerTurnFactory = new TurnFactory();
        PlayerOrderFactory playerNextFactory = new PlayerOrderFactory();
        //set default parameter of component facotires for players we dont need to change
        playerNextFactory.setNextPlayer(null);
        //construct player component bundles
        ComponentBundle playerTurn = new ComponentBundle(playerTurnFactory, turnSystemFactory);
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
     * Construct a new player and a new ball. Map this player to this ball. Players will take turns in the order
     * they were added. The player added will contain all the necessary components for a turn-taking entitiy (see framework
     * families) and the ball will contain all entities associated with the moving, accelerable, gravity-attracted, colliding and
     * friction families (see physics families).
     * @param name player's name
     * @param ballRadius ball's radius
     * @param ballMass ball's mass
     * @param initBallPos ball's initial position
     */
    private void addPlayerAndBall(String name, float ballRadius, float ballMass, Vector3 initBallPos, PlayerObserver inputMethod)
    {
        Player p = constructPlayer(name);
        Ball b = constructBall(ballRadius, ballMass, initBallPos, p);

        //set the first player as active player
        if (mBallMap.isEmpty())
        {
            assert(framework.constants.CompoMappers.TURN.has(p.mEntity));
            Turn playerTurn = framework.constants.CompoMappers.TURN.get(p.mEntity);
            playerTurn.mTurn = true;
        }

        mBallMap.put(p, b);

        inputMethod.setMatchingPlayer(p);
        mGameObservers.add(inputMethod);

        mEngine.addEntity(b.mEntity);
        mEngine.addEntity(p.mEntity);

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
     * attaches all stored observers to the game
     * @param game game to attach observers to
     */
    private void addAllObservers(Game game)
    {
        for (GameObserver observer : mGameObservers)
            game.attachObserver(observer);
    }

    /**
     * adds components indicating turn transitions between players
     */
    private void setNextPlayers()
    {
        ArrayList<Player> players = new ArrayList<>(mBallMap.keySet());

        for (int cPlayer = 0; cPlayer < players.size(); ++cPlayer)
        {
            Player previous = players.get((2 * players.size() - 1 + cPlayer) % players.size());
            Player next = players.get((cPlayer + 1) % players.size());
            players.get(cPlayer).mEntity.add(new PlayerOrder(previous, next));
        }
    }


	/** mapping of players to their balls */
	private HashMap<Player, Ball> mBallMap;
    /** stores all observers which need to be added to the game before it runs */
    private ArrayList<GameObserver> mGameObservers;
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
    private OwnershipFactory mBallOwnershipFactory;
    /** player */
    private NameFactory mPlayerNameFactory;
    /** obstacles */
    private BodyFactory mObstacleBodyFactory;
    private PositionFactory mObstaclePositionFactory;
    /** system factories depending on ode solver */
    private MovementFactory mMovementFactory;
    private ForceApplyFactory mForceApplyFactory;
    private FrictionSystemFactory mFrictionSystemFactory;

    private GameSettings mSettings;

    //boolean flags for conditions fulfilled before start
    boolean mSetHole;
}
