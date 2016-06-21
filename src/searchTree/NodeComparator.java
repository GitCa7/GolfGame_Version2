package searchTree;

import java.util.Comparator;

//Class used for the priority queue implementation in the SearchTree class
public class NodeComparator<E extends SearchState, Action extends AbstractAction<E>>
		implements Comparator<TreeNode<E, ?>> {

	@Override
	public int compare(TreeNode<E, ?> o1, TreeNode<E, ?> o2) {
		if ((o1.getValueOfNode() - o2.getValueOfNode() == 0)) {
			return o2.getNodeDeapth() - o1.getNodeDeapth();
		}
		return (int) (o1.getValueOfNode() - o2.getValueOfNode());
	}

}
