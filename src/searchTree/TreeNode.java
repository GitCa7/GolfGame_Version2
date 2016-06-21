package searchTree;

import java.util.ArrayList;

public class TreeNode<E extends SearchState, Action extends AbstractAction<E>> {

	private E state;
	/** Action that led to this Node(state) from the parent Node(state) */
	private Action action;
	private TreeNode<E, Action> parentNode;
	// not using this delete to be considered
	private ArrayList<TreeNode<E, Action>> childNodeList = new ArrayList<>();// not
																				// needed???
	/** Depth of the specific node */
	private int nodeDepth;
	/** Value of the Node */
	private double valueOfNode;

	public TreeNode(TreeNode<E, Action> parentNode) {
		this.parentNode = parentNode;
	}

	public void addChildNode(TreeNode<E, Action> aChild) {
		if (childNodeList == null)
			childNodeList.add(aChild);
	}

	public ArrayList<TreeNode<E, Action>> getChildNodeList() {
		return childNodeList;
	}

	public TreeNode<E, Action> getParent() {
		return parentNode;
	}

	public void setParent(TreeNode<E, Action> parentNode) {
		this.parentNode = parentNode;
	}

	public int getNodeDeapth() {
		return nodeDepth;
	}

	public void setNodeDeapth(int deapth) {
		this.nodeDepth = deapth;
	}

	public double getValueOfNode() {
		return valueOfNode;
	}

	public void setValueOfNode(double valueOfNode) {
		this.valueOfNode = valueOfNode;
	}

	public E getState() {
		return state;
	}

	public void setState(E state) {
		this.state = state;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

}
