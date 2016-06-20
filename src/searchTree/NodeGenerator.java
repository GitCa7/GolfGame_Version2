package searchTree;

import searchTree.AbstractAction;
import searchTree.SearchState;
import searchTree.TreeNode;

public abstract class NodeGenerator<E extends SearchState, Action extends AbstractAction<E>> {
	private NodeEvaluator<E> evaluator;

	public NodeGenerator(NodeEvaluator<E> evaluator) {
		this.evaluator = evaluator;
	}

	public abstract TreeNode<E, Action> generateChildNode(TreeNode<E, Action> aNode);
}
