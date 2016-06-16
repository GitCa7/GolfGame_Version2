package testForAi;

import aiExtention.GolfState;
import aiExtention.GolfStateComparator;
import com.badlogic.ashley.core.Entity;
import  physics.components.Force;
import  physics.components.GravityForce;
import  physics.components.Position;
import  physics.entities.Ball;

public class StateComTest {

	public static void main(String[] args) {
		GolfStateComparator stateComparator = new GolfStateComparator(1000, 1000, 2);
		int radius = 1;
		int gravityConstant = 0;
		Ball ball = new Ball(new Entity());
		ball.mEntity.add(new Position(0, 0, 0));
		ball.mEntity.add(new Force());
		Position pos = ball.mEntity.getComponent(Position.class);
		Entity target = new Entity();
		target.add(new Position(100, 0, 0));
		GolfState state = new GolfState(ball, target);
		//stateComparator.isStateExplored(state);
		System.out.println(stateComparator.isStateExplored(state));
		
	}

}
