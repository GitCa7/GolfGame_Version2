package testForAi;

import aiExtention.GolfAction;
import aiExtention.GolfState;
import aiExtention.Utils.GolfSearchPerformer;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import framework.Game;
import framework.GameConfigurator;
import framework.entities.Player;
import physics.components.Force;
import physics.components.Position;
import physics.components.Velocity;
import physics.entities.Ball;
import physics.geometry.planar.Triangle;
import physics.geometry.planar.TriangleBuilder;
import physics.systems.Movement;
import searchTree.TreeNode;
import physics.systems.ForceApply;
import physics.systems.FrictionSystem;

import java.util.ArrayList;


public class TestTrim {

	public static void main(String[] args) {

	//	Movement.DEBUG = true;


		float deltaTime = 1f;
		float radius = 1;
		float ballMass = 1;
		Vector3 ballPosition = new Vector3();

		Vector3 holePosition = new Vector3(100, 0, 0);
		float holeSize = 20;

		GameConfigurator config = new GameConfigurator();
		config.addBotAndBall("bot1", radius, ballMass, ballPosition);
		config.setHole(holePosition, holeSize);
		Triangle groundTriangle = new TriangleBuilder(new Vector3(-10000, 0, 10000), new Vector3(10000, 0, 10000), new Vector3(0, 0, -10000)).build();
		ArrayList<Triangle> groundList = new ArrayList<>();
		groundList.add(groundTriangle);
		config.setTerrain(groundList);

		Game game = config.game();

		Player bot = game.getCurrentPlayers().get(0);
		GolfSearchPerformer searchPerformer= new GolfSearchPerformer(bot, game);
		
		TreeNode<GolfState, GolfAction> solutionNode= searchPerformer.greedySolution();
		
		//displaySolution(engine, deltaTime, game.getBall(bot), solutionNode);

	}
	
	
	private static void displaySolution(Engine engine, float deltaTime,
			Ball ball, TreeNode<GolfState, GolfAction> solutionNode) {
		TreeNode<GolfState, GolfAction> tempNode = solutionNode;
		
		System.out.println("final ball pos " + solutionNode.getState().getPosition());
		for (int i = 0; i < solutionNode.getNodeDeapth(); i++) {
			algStep(engine, ball, deltaTime, solutionNode, tempNode, i + 1);
			System.out.println("ball possition :" + (i+1) + " "+ ball.mEntity.getComponent(Position.class));
			tempNode = solutionNode;
		}
	}
	public static void algStep(Engine engine, Ball ball, float deltaTime,
							   TreeNode<GolfState, GolfAction> solutionNode, TreeNode<GolfState, GolfAction> tempNode,
							   int counter) {
			while (tempNode.getNodeDeapth() != counter) {
			tempNode = tempNode.getParent();

		}
		// System.out.println("valueOFNode" + tempNode.getValueOfNode());
			
		Vector3 forceApply = tempNode.getAction().getForce();
	//	System.out.println("apply force: "+forceApply);
		ball.mEntity.getComponent(Force.class).add(forceApply.x, forceApply.y, forceApply.z);
		int i = 1;
		//engine.update(deltaTime);
		while (ball.mEntity.getComponent(Velocity.class).len() > 0.1 || i == 1) {
			engine.update(deltaTime);

			i++;
		}
		ball.mEntity.getComponent(Velocity.class).setZero();
		System.out.println("Solution Depth: " + tempNode.getNodeDeapth() + " ball pos " + tempNode.getState().getPosition());

	}
}
