package GamePackage;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.badlogic.ashley.core.Entity;

import Entities.GolfBall;
import Entities.gameEntity;

public class PhysicsTranslator {
	
	List<GolfBall> golfBalls = new ArrayList<GolfBall>();
	Entity tmpEntity;
	Vector3f tmpVector;
	
	public PhysicsTranslator(ArrayList<gameEntity> golfBalls){
		golfBalls = golfBalls;
	}
	
	public void update()	{
		for(GolfBall gBall : golfBalls)	{
			tmpEntity = /*YOUR METHOD FOR READING OUT THE BALL OORDINATES*/;
			
			//Read into Vector3f
			tmpVector = new Vector3f();
			
			gBall.setPosition(tmpVector);

		}
	}

}
