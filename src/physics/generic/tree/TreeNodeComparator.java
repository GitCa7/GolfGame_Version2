package physics.generic.tree;

/**
 * created 20.06.16
 *
 * @author martin
 */
public interface TreeNodeComparator<T>
{
    public boolean equal(TreeNode<T> n1, TreeNode<T> n2);
}
