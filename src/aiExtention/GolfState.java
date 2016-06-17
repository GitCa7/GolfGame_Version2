package aiExtention;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import physics.components.Force;
import physics.components.GravityForce;
import physics.components.Position;
import physics.components.Velocity;
import physics.entities.Ball;
import searchTree.SearchState;

public class GolfState extends SearchState {
	private Ball ball;
	private Entity target;

	public GolfState(Ball ball, Entity target) {
		this.ball = ball;
		this.target = target;
	}
	
	public Vector3 getPosition() {
		return ball.mEntity.getComponent(Position.class);
	}

	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}

	public Entity getTarget() {
		return target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	public GolfState cloneState() {
		Ball newBall = new Ball(new Entity());
		newBall.mEntity.add(new Position(ball.mEntity.getComponent(Position.class).x, ball.mEntity.getComponent(Position.class).y, ball
				.mEntity.getComponent(Position.class).z));
		newBall.mEntity.add(new Velocity(0, 0, 0));
		newBall.mEntity.add(new Force());
		GolfState cloneState = new GolfState(newBall, target);
		return cloneState;
	}

}
