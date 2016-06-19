package aiExtention.Utils;

import aiExtention.*;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import framework.EntitySystem;
import framework.Game;
import framework.SimulatedGame;
import framework.entities.Player;
import physics.entities.Ball;
import searchTree.*;
import physics.systems.*;


import java.util.Arrays;
import java.util.List;

public class GolfSearchPerformer {

	private SimulatedGame mSimulation;
	private Engine aiEngine;
	private float deltaTime = 1f;
	private NodeEvaluator<GolfState> evaluator;
	private NodeGenerator<GolfState,GolfAction> generator;
	private GoalAchived<GolfState> goalTester;
	private TreeNode<GolfState,GolfAction> rootNode;

	/*
	public GolfSearchPerformer(Ball ball, Entity target)
	{
		aiEngine= new Engine();
		List<EntitySystem> systems = Arrays.asList(new GravitySystem(), new ForceApply(), new Movement(),
				new FrictionSystem());
		for (int i = 0; i < systems.size(); i++) {
			aiEngine.addSystem(systems.get(i));
			aiEngine.addEntityListener(systems.get(i).getNewEntitiesListener());
		}
		constructRoot(ball, target);
	}
	*/

	public GolfSearchPerformer(Player me, Game game)
	{
		mSimulation = game.getGameSimulation(me);
		constructRoot(mSimulation);

	}
	
	public void constructRoot(SimulatedGame simulation)
	{
		GolfState rootState = new GolfState(simulation, simulation.getGameState());

		TreeNode<GolfState, GolfAction> rootNode = new TreeNode<GolfState, GolfAction>(null);

		GolfEvaluator evaluator = new GolfEvaluator();

		rootNode.setState(rootState);

		rootNode.setNodeDeapth(0);

		rootNode.setValueOfNode(evaluator.evaluateNode(rootNode));
		System.out.println("rootvalue"+rootNode.getValueOfNode());
		
		this.rootNode=rootNode;		
	}

	public TreeNode<GolfState, GolfAction> aStarSolution(){
		this.evaluator= new AStarEvaluator();
		this.generator= new GolfGenerator(aiEngine, evaluator);
		this.goalTester=new GolfGoalTester();
		return rootNode;
		
	}
	public TreeNode<GolfState, GolfAction> greedySolution(){
		this.evaluator= new GolfEvaluator();
		this.generator= new GolfGenerator(aiEngine, evaluator);
		this.goalTester=new GolfGoalTester();
		TreeOperator<GolfState, GolfAction> treeOperator = new TreeOperator<GolfState, GolfAction>(
				rootNode, generator, goalTester);
		System.out.println("RUN");
		return treeOperator.runSearch();
		
	}
	

}
