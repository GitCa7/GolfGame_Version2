package physics.testing;

import TerrainComponents.Terrain;
import framework.ComponentBundle;
import framework.GameConfigurator;
import framework.components.*;
import physics.components.*;
import physics.constants.PhysicsCoefficients;
import physics.entities.Ball;
import physics.geometry.spatial.SphereTetrahedrizer;
import framework.systems.*;
import physics.systems.*;
import framework.entities.*;

import physics.geometry.*;
import physics.geometry.spatial.*;

public class Loader 
{
	
	public static final int NUMBER_PLAYERS= 2;
	
	public GameConfigurator load()
	{
		mConfig = new GameConfigurator();
		
		
		return mConfig;
	}
	
	
	public void addPlayer()
	{
		EntityFactory produceEntities = mConfig.entityFactory();
		ComponentBundle name = new ComponentBundle (mProduceNames, null);
		ComponentBundle turn = new ComponentBundle (mProduceTurns, null);
		ComponentBundle nextPlayer = new ComponentBundle (mProduceNextPlayer, null);
		
		mProduceNames.set("player 1");
		Player p1 = new Player (produceEntities.produce());
		
		mProduceNames.set("player 2");
		Player p2 = new Player (produceEntities.produce());
		
		mProduceNextPlayer.setNextPlayer(p2);
		p1.mEntity.add(mProduceNextPlayer.produce());
		
		mProduceNextPlayer.setNextPlayer(p1);
		p2.mEntity.add(mProduceNextPlayer.produce());
		
		mPlayers = new Player[NUMBER_PLAYERS];
		mPlayers[0] = p1;
		mPlayers[1] = p2;
	}
	
	public void addBalls()
	{
		ComponentBundle velocity = new ComponentBundle (mProduceVelocity, null);
		ComponentBundle force = new ComponentBundle(mProduceForce , null);
		ComponentBundle friction = new ComponentBundle(mProduceFriction , null);
		ComponentBundle position = new ComponentBundle(mProducePosition, null);
		ComponentBundle mass = new ComponentBundle(mProduceMass, null);
		ComponentBundle body = new ComponentBundle(mProduceBody, null);
		
		EntityFactory ef = mConfig.entityFactory();
		
		//generic for balls
		mProduceVelocity.setVector(new Vector3());
		mProduceForce.setVector(new Vector3());
		mProduceFriction.setParameter(PhysicsCoefficients.STATIC_FRICTION, 
									PhysicsCoefficients.DYNAMIC_FRICTION, 
									0f, 0f);
		
		Vector3 ballCenter = new Vector3(3, 2, 7);
		double radius = 5;
		SphereTetrahedrizer tetShpere = new SphereTetrahedrizer(ballCenter, radius);
		mProduceBody.clear();
		for (Tetrahedron t : tetShpere.tetrahedrize(2, 5))
			mProduceBody.addSolid(t);
		
		//specific
		mProducePosition.setVector(new Vector3(2, 4, 6));
		mProduceMass.setParameter(2.5f);
		
		mBalls = new Ball[NUMBER_PLAYERS];
		mBalls[0] = new Ball(ef.produce());
		
		mProducePosition.setVector(new Vector3(1, 1, 1));
		mProduceMass.setParameter(3);
		mBalls[1] = new Ball(ef.produce());
		
		
	}
	
	
	public void addTerrain()
	{
		TerrainData terraData = new TerrainData();
		TerrainGeometryCalc terraCalc =  new TerraingGeometryCalc terraData
		
		
		terra.toData();
		TerrainModel terraModel
	}
	
	
	public void init()
	{
		mProduceNames = new NameFactory();
		mProduceTurns = new TurnFactory();
		mProduceNextPlayer = new NextPlayerFactory();
		
		mProducePosition = new PositionFactory();
		mProduceVelocity = new VelocityFactory();
		mProduceMass = new MassFactory();
		mProduceForce = new ForceFactory();
		mProduceBody = new BodyFactory();
		mProduceFriction = new FrictionFactory();
	}
	
	
	private GameConfigurator mConfig;
	private Player[] mPlayers;
	private Ball[] mBalls;
	
	private NameFactory mProduceNames;
	private TurnFactory mProduceTurns;
	private NextPlayerFactory mProduceNextPlayer;
	
	private PositionFactory mProducePosition;
	private VelocityFactory mProduceVelocity;
	private MassFactory mProduceMass;
	private ForceFactory mProduceForce;
	private BodyFactory mProduceBody;
	private FrictionFactory mProduceFriction;
}
