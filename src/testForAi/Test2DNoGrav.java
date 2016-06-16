/*package testForAi;

import com.badlogic.ashley.core.Engine;
import components.*;
import entities.Ball;
import systems.ForceApply;
import systems.FricsionSystem;
import systems.Gravity;
import systems.Movement;

public class Test2DNoGrav {

	public static void main(String[] args) {
		Engine engine = new Engine();

		int radius = 1;
		int gravityConstant = 0;
		Ball ball = new Ball(radius, 1, new GravityForce.Builder(gravityConstant));
		ball.add(new Position(0, 0, 0));
		ball.add(new Force());

		engine.addEntity(ball);

		Position pos = ball.getComponent(Position.class);
		Mass mass = ball.getComponent(Mass.class);
		Force force = ball.getComponent(Force.class);

		// System.out.println("mass " + mass.mMass);
		// System.out.println("ball mass " + ball.mass(radius, 1));

		// Family family = Family.all(GravityForce.class).get();

		// ImmutableArray<Entity> gravityEntities = engine.getEntitiesFor(family);
		engine.addSystem(new Gravity());
		engine.addSystem(new ForceApply());
		engine.addSystem(new Movement());
		engine.addSystem(new FricsionSystem());


		System.out.println("ball pos 1 " + pos);

		// force.add(mass.mMass, 0, 0);

		float deltaTime = 1f;
		ball.getComponent(Force.class).add(10, 0, 0);
		int i = 1;
		while (ball.getComponent(Velocity.class).len() > 0.1 || i == 1) {
		engine.update(deltaTime);
		System.out.println("ball pos 4 " + pos);
			i++;
		}

	}

	}
*/
