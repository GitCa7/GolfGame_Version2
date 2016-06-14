package searchTree;

import searchTree.SearchState;
import searchTree.TreeNode;

public abstract class NodeEvaluator<E extends SearchState> {
	
	public abstract double evaluateNode(TreeNode<E, ?> aNode);
}

