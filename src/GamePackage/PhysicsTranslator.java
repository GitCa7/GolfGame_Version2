package GamePackage;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import framework.Game;
import framework.constants.CompoMappers;
import org.lwjgl.util.vector.Vector3f;

import com.badlogic.ashley.core.Entity;

import Entities.GolfBall;
import Entities.gameEntity;

public class PhysicsTranslator {
	
	List<GolfBall> golfBalls = new ArrayList<GolfBall>();
	Entity tmpEntity;
	Vector3 vec;
	Vector3f tmpVector;
	Game parent;
	
	public PhysicsTranslator(ArrayList<GolfBall> golfBallst){
		golfBalls = golfBallst;
		this.parent = parent;
	}
	public void setGame(Game game){
		parent = game;
	}

	public void update()	{
		//if(golfBalls.size() != parent.getBalls().size()) {

		//}
		//else	{
		int offset = 0;
			for (GolfBall gBall : golfBalls) {
				tmpEntity = parent.getBalls().get(offset).mEntity;
				vec = physics.constants.CompoMappers.POSITION.get(tmpEntity);

				//Read into Vector3f
				tmpVector = new Vector3f(vec.x,vec.y,vec.z);

				gBall.setPosition(tmpVector);

			}
		//}
	}

}
