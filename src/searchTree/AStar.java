package searchTree;

import java.util.Comparator;
import java.util.PriorityQueue;

import com.badlogic.gdx.math.Vector3;

//import aiExtention.GolfState;

public class AStar<E extends SearchState, Action extends AbstractAction<E>> {
	private PriorityQueue<TreeNode<E, Action>> queue;
	private Comparator<TreeNode<E, ?>> comparator;

	private NodeGenerator<E, Action> generator;
	private final int noOfGenerChild = 1000;

	private StateComparator<E> stateComparator;

	private GoalAchived<E> goalTester;

	public AStar(NodeGenerator<E, Action> generator, GoalAchived<E> goalTester, StateComparator<E> stateComparator) {
		this.comparator = new NodeComparator<E, Action>();
		this.queue = new PriorityQueue<TreeNode<E, Action>>(comparator);
		this.generator = generator;
		this.stateComparator = stateComparator;
		this.goalTester = goalTester;
	}

	public TreeNode<E, Action> runAStar(TreeNode<E, Action> rootNode) {
		int nodesExpanded = 0;
		queue.add(rootNode);

		TreeNode<E, Action> solutionNode = queue.poll();
		while (solutionNode.getNodeDeapth() < 20000 && !goalTester.test(solutionNode.getState())) {
			nodesExpanded++;

			/*
			 * if(nodesExpanded % 100 == 0) { Vector3 position = ((GolfState)
			 * solutionNode.getState()).getPosition() System.out.println(
			 * "Working really hard " + nodesExpanded +
			 * " nodes expanded, queue size: " + queue.size() +position );
			 * 
			 * }
			 */
			// System.out.println("new expantion");

			for (int i = 0; i < noOfGenerChild; i++) {

				TreeNode<E, Action> child = generator.generateChildNode(solutionNode);

				// if (stateComparator.isStateExplored(child.getState())) {
				// } else {
				// Vector3 position = ((GolfState)
				// child.getState()).getPosition();
				stateComparator.markExplored(child.getState());
				queue.add(child);

				// }
			}
			solutionNode = queue.poll();
		}
		System.out.println("Total nodes expanded " + nodesExpanded);
		return solutionNode;

	}

}