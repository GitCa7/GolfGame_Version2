package physics.generic.tree;

/**
 * created 20.06.16
 *
 * @author martin
 */
public abstract class MultiTreeNode<T> extends TreeNode<T>
{

    public MultiTreeNode(T value)
    {
        super(value);
    }

    public abstract void addChild(MultiTreeNode<T> child);

    public abstract void removeChild(MultiTreeNode<T> child);
}
