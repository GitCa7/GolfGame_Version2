package physics.generic.tree;

import java.util.HashSet;

/**
 * created 20.06.16
 *
 * @author martin
 */
public abstract class TreeNode<T>
{

    public static class TreeNodeException extends RuntimeException
    {
        public TreeNodeException(String message)
        {
            super (message);
        }
    }

    public TreeNode(T value)
    {
        mValue = value;
        mParent = null;
    }

    public TreeNode<T> getParent()
    {
        return mParent;
    }

    public T getValue()
    {
        return mValue;
    }

    public abstract int getNumberOfChildren();

    public boolean isRoot()
    {
        return (mParent == null);
    }

    public abstract boolean isInternal();


    public boolean equals(TreeNode<T> compare)
    {
        return this.mValue.equals(compare.mValue);
    }

    public abstract boolean hasChild(TreeNode<T> child);

    public void setValue(T value)
    {
        mValue = value;
    }

    public void setParent(TreeNode<T> newParent)
    {
        if (newParent == null)
            throw new TreeNodeException("cannot set parent to " + newParent);
        mParent = newParent;
    }

    private TreeNode mParent;
    private T mValue;
}
