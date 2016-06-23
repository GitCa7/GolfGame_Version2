package testForAi;

import aiExtention.*;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector3;
import physics.components.Force;
import physics.components.Position;
import physics.components.Velocity;
import framework.entities.Ball;
import searchTree.AStar;
import searchTree.TreeNode;
import physics.systems.*;

import java.util.Arrays;
import java.util.List;


public class AstarTest {

	public static void main(String[] args) {

		Engine engine = new Engine();
		Engine engine2 = new Engine();
		int radius = 1;
		int gravityConstant = 0;
		Ball ball = new Ball(new Entity());
		ball.mEntity.add(new Position(0, 0, 0));
		ball.mEntity.add(new Force());

		engine.addEntity(ball.mEntity);

		Position pos = ball.mEntity.getComponent(Position.class);

		engine.addSystem(new GravitySystem());
		engine.addSystem(new ForceApply());
		engine.addSystem(new Movement());
		engine.addSystem(new FrictionSystem());

		List<EntitySystem> systems = Arrays.asList(new GravitySystem(), new ForceApply(), new Movement(),
				new FrictionSystem());
		for (int i = 0; i < systems.size(); i++) {
			engine2.addSystem(systems.get(i));
			engine2.addEntityListener(systems.get(i).getNewEntitiesListener());
		}

		float deltaTime = 1f;

		// RUN ALGORITHM
		Entity target = new Entity();

		target.add(new Position(500, 0, 0));

		GolfState rootState = new GolfState(ball, target);

		TreeNode<GolfState, GolfAction> rootNode = new TreeNode<GolfState, GolfAction>(null);

		AStarEvaluator evaluator = new AStarEvaluator();
		//GolfEvaluator evaluator = new GolfEvaluator();

		rootNode.setState(rootState);

		rootNode.setNodeDeapth(0);

		rootNode.setValueOfNode(evaluator.evaluateNode(rootNode));

		System.out.println("value root " + rootNode.getValueOfNode());

		GolfGenerator generator = new GolfGenerator(engine2,evaluator);

		GolfGoalTester goalTester = new GolfGoalTester();

		AStar<GolfState, GolfAction> treeOperator = new AStar<GolfState, GolfAction>(generator, goalTester,
				new GolfStateComparator(20000, 20000, 15));

		System.out.println("ball pos 1 " + pos);
		System.out.println("value root " + rootNode.getValueOfNode());

		TreeNode<GolfState, GolfAction> solutionNode = treeOperator.runAStar(rootNode);

		System.out.println(solutionNode.getState().getPosition());


		TreeNode<GolfState, GolfAction> tempNode = solutionNode;
//		deltaTime = 0.1f;
		for (int i = 0; i < solutionNode.getNodeDeapth(); i++) {
			algStep(engine, ball, deltaTime, solutionNode, tempNode, i + 1);
			System.out.println("ball possition :" + (i+1) + " "
					+ ball.mEntity.getComponent(Position.class));
			tempNode = solutionNode;
		}
System.out.println(goalTester.test(solutionNode.getState()));

//		algStep(engine, ball, deltaTime, solutionNode, tempNode, 1);
//		System.out.println("ball possition 2:" + ball.getComponent(Position.class));
//		tempNode = solutionNode;
//		algStep(engine, ball, deltaTime, solutionNode, tempNode, 2);
//		System.out.println("ball possition 3:" + ball.getComponent(Position.class));
//		algStep(engine, ball, deltaTime, solutionNode, tempNode, 3);
//		System.out.println("ball possition 4:" + ball.getComponent(Position.class));

//		algStep(engine, ball, deltaTime, solutionNode, tempNode, 4);
//		System.out.println("ball possition 5:" + ball.getComponent(Position.class));

		

	}

	private static void algStep(Engine engine, Ball ball, float deltaTime,
								TreeNode<GolfState, GolfAction> solutionNode, TreeNode<GolfState, GolfAction> tempNode,
								int counter) {
		while (tempNode.getNodeDeapth() != counter) {
			tempNode = tempNode.getParent();

		}
		// System.out.println("valueOFNode" + tempNode.getValueOfNode());

	Vector3 forceApply = tempNode.getAction().getForce();
// System.out.println(forceApply);
ball.mEntity.getComponent(Force.class).add(forceApply.x, forceApply.y, forceApply.z);
		int i = 1;
		while (ball.mEntity.getComponent(Velocity.class).len() > 0.1 || i == 1) {
		engine.update(deltaTime);

		i++;
		}
		ball.mEntity.getComponent(Velocity.class).setZero();
		System.out.println("Solution Depth: " + tempNode.getNodeDeapth() + " ball pos " + tempNode.getState().getPosition());

		}

		}

