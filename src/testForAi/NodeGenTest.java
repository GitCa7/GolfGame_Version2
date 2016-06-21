package testForAi;

import aiExtention.GolfAction;
import aiExtention.GolfEvaluator;
import aiExtention.GolfGenerator;
import aiExtention.GolfState;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import physics.components.Force;
import physics.components.GravityForce;
import physics.components.Position;
import physics.entities.Ball;
import searchTree.TreeNode;
import physics.systems.ForceApply;
import physics.systems.FrictionSystem;
import physics.systems.GravitySystem;
import  physics.systems.Movement;

public class NodeGenTest {

	public static void main(String[] args) {

		Engine engine = new Engine();
		int radius = 1;
		int gravityConstant = 0;
		Ball ball = new Ball(new Entity());
		ball.mEntity.add(new Position(0, 0, 0));
		ball.mEntity.add(new Force());

		engine.addEntity(ball.mEntity);


		engine.addSystem(new GravitySystem());
		engine.addSystem(new ForceApply());
		engine.addSystem(new Movement());
		engine.addSystem(new FrictionSystem());



		
		Entity target = new Entity();
		target.add(new Position(50, 0, 0));


		//GolfState rootState = new GolfState(ball, target);
		TreeNode<GolfState, GolfAction> rootNode = new TreeNode<GolfState, GolfAction>(null);
		//rootNode.setState(rootState);
		rootNode.setNodeDeapth(0);
		// System.out.println(ball);
		GolfGenerator generator = new GolfGenerator(engine,new GolfEvaluator(),0.02f);

		TreeNode<GolfState, GolfAction> testNode = generator.generateChildNode(rootNode);

		System.out.println(testNode.getState().getBall().mEntity.getComponent(Position.class));
		System.out.println("Force " + testNode.getAction().getForce());

		// TreeNode<GolfTestState, GolfTestAction> testNode2 = generator.generateChildNode(rootNode);

	}

}
