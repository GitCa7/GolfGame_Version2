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
		Vector3 initBallPos = new Vector3 (0, 0, 0);
		MovementTest test = new MovementTest (initBallPos);
		test.display();
		
		Vector3 hitForce = new Vector3 (100, 0, 0);
		test.hitBall(hitForce);
		
		int iterations = 10;
		
		for (int cnt = 0; cnt < iterations; ++cnt)
		{
			test.updateEngine();
			test.printBallPosition();
			try
			{
				Thread.sleep(1000);
			}
			catch (Exception e) { System.out.println ("oh no"); }
		}
	}
	
	public static final float DT = 1;
	
	public class VisualsRunner implements Runnable
	{
		public void run()
		{
			mVisualizer.setEngine(mEngine);
		}
	}
	
	public MovementTest(Vector3 ballPos)
	{
		mBall = new Ball (new Entity());
		
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
		
		mEngine.addEntity (mBall.mEntity);
		assert (mEngine.getEntitiesFor(Families.ACCELERABLE).size() > 1);
		
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
		mVisualizer.setTerrain(new TerrainData());
	}
	
	public void hitBall (Vector3 force)
	{
		Force f = CompoMappers.FORCE.get(mBall.mEntity);
		f.add (force);
	}
	
	public void updateEngine()
	{
		mEngine.update (DT);
	}
	
	public void printBallPosition()
	{
		Position ballPos = CompoMappers.POSITION.get (mBall.mEntity);
		Velocity ballV = CompoMappers.VELOCITY.get(mBall.mEntity);
		System.out.println ("ball position " + ballPos + " velocity " + ballV);
	}

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
