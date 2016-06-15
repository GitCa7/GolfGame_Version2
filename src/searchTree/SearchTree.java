package searchTree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

//class  used for storing retrieving nodes according to their value
public class SearchTree<E extends SearchState, Action extends AbstractAction<E>> {
	

	private ArrayList<PriorityQueue<TreeNode<E, Action>>> treeTable; // new ArrayList<PriorityQueue<TreeNode<E,
																		// Action>>>();

	private Comparator<TreeNode<E, ?>> comparator;

	private TreeNode<E, Action> rootNode;

	public SearchTree(TreeNode<E, Action> rootNode) {
		this.rootNode = rootNode;
		this.comparator = new NodeComparator<E, Action>();
		treeTable = new ArrayList<PriorityQueue<TreeNode<E, Action>>>();
		addNode(rootNode);


	}

	public void addNode(TreeNode<E, Action> aNode) {
		int depth = aNode.getNodeDeapth();
		if (treeTable.size() <= depth) {
			// if (aNode.getNodeDeapth() > depth) {
			PriorityQueue<TreeNode<E, Action>> queue = new PriorityQueue<TreeNode<E, Action>>(comparator);
			treeTable.add(queue);
		}
		treeTable.get(depth).add(aNode);
	}

	public void deleteNode(int depth, int xPosit) {
		treeTable.get(depth).remove(xPosit);
	}

	public PriorityQueue<TreeNode<E, Action>> getListOfNodes(int deapthOfNodes) {
		return treeTable.get(deapthOfNodes);
	}

	public TreeNode<E, Action> getRootNode() {
		return rootNode;
	}

	public void setRootNode(TreeNode<E, Action> rootNode) {
		this.rootNode = rootNode;
	}
}
