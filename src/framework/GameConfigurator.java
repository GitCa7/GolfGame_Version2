package framework;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import framework.components.NameFactory;
import framework.components.NextPlayerFactory;
import framework.components.Turn;
import framework.components.TurnFactory;
import framework.entities.Player;
import framework.internal.components.Active;
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
		mHole = null;

        mBallFactory = new EntityFactory(mSystemsTracker);
        mPlayerFactory = new EntityFactory(mSystemsTracker);

        mBallPositionFactory = new PositionFactory();
        mBallMassFactory = new MassFactory();
        mBallBodyFactory = new BodyFactory();

        mPlayerNameFactory = new NameFactory();

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

        //@TODO use
        /*
        if (mHole == null)
			throw new IllegalStateException ("the hole is not set");
        */
		addAllSystems();
		return new Game (mEngine, mBallMap, mHole);
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
            playerTurn.mDone = false;
        }

        mBallMap.put(p, b);
        mEngine.addEntity(b.mEntity);
        mEngine.addEntity(p.mEntity);
    }

    /**
     * Constructs a new obstacle. Every obstacle added this way will contain all the entities associated with the colliding
     * family (see physics families).
     * @param mass the mass of the obstacle
     * @param bodySolids a collection of solids. The vertex with the smallest euclidean distance to the origin is fixed as the
     *                   position of the obstacle.
     */
    public void addObstacle(float mass, Collection<Solid> bodySolids)
    {
        //@TODO implement extrinsic state of flyweight solids. Check this after implementing.
        throw new UnsupportedOperationException("obstacles are not yet supported and thus not implemented");
    }

    /**
     * Constructs a new hole. This hole has all components associtated with the NAME_THEM_HERE! families.
     * @param holeShape The shape of the hole, embedding the position where the hole is located.
     */
    public void setHole(Shape holeShape)
    {
        //@TODO implement hole
        throw new UnsupportedOperationException("the hole is not yet relevant and thus not implemented");
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
        mBallBodyFactory.addSolid(sphereBoundingBox);
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
        ComponentBundle ballBody = new ComponentBundle(mBallBodyFactory);
        ComponentBundle ballGravity = new ComponentBundle(ballGravityFactory);
        //add bundles to ball factory
        mBallFactory.addComponent(ballPosition, ballVelocity, ballForce, ballFriction, ballMass, ballBody, ballGravity);

        //additional component factories for players
        TurnFactory playerTurnFactory = new TurnFactory();
        NextPlayerFactory playerNextFactory = new NextPlayerFactory();
        //set default parameter of component facotires for players we dont need to change
        playerNextFactory.setNextPlayer(null);
        //construct player component bundles
        ComponentBundle playerTurn = new ComponentBundle(playerTurnFactory);
        ComponentBundle playerNext = new ComponentBundle(playerNextFactory);
        ComponentBundle playerName = new ComponentBundle(mPlayerNameFactory);
        //add bunldes to player factory
        mPlayerFactory.addComponent(playerTurn, playerNext, playerName);
    }

	/**
	 * adds all systems tracked by the systems tracker to the engine
	 */
	private void addAllSystems()
	{
		for (EntitySystem es : mSystemsTracker.systemsInUse())
        {
            mEngine.addSystem (es);
            //@TODO add listeners
        }

	}

	/** mapping of players to their balls */
	private HashMap<Player, Ball> mBallMap;
	/** the hole entity to be used */
	private Hole mHole;
	/** engine to configure */
	private Engine mEngine;
	/** objects tracking physics.systems in use */
	private SystemsTracker mSystemsTracker;
    /** entity factories used to instantiate certain kinds of entities */
    private EntityFactory mBallFactory, mPlayerFactory;
    /** handles for component factories used in entity factories */
    private PositionFactory mBallPositionFactory;
    private MassFactory mBallMassFactory;
    private BodyFactory mBallBodyFactory;
    private NameFactory mPlayerNameFactory;
}
