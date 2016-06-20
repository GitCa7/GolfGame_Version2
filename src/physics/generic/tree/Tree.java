package physics.generic.tree;

/**
 * created 20.06.16
 *
 * @author martin
 */
public abstract class Tree<V, T extends TreeNode<V>>
{

    public Tree(TreeNode<V> rootNode)
    {
        mRoot = rootNode;
    }

    public V getRoot()
    {
        return mRoot.getValue();
    }

    public void setComparator(TreeNodeComparator<V> comparator)
    {
        mComparator = comparator;
    }

    private TreeNode<V> mRoot;
    private TreeNodeComparator<V> mComparator;
}
