package GamePackage;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import com.badlogic.gdx.math.Vector3;

import Entities.GolfBall;

public class GameUpdater {
	
	public void update(ArrayList<GolfBall> golfBalls)	{
		
		Vector3f vec;
		Vector3 entityVector;
		
		for(GolfBall ball : golfBalls)	{
			//put the new calculated Vector here
			entityVector = null;
			
			//The Vector is here translated for the visuals into a Vector3f
			vec = new Vector3f(entityVector.x, entityVector.y, entityVector.z);
			
			ball.setPosition(vec);
		}
	}

}
