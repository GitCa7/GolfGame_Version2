package physics.generic.tree;

import java.util.HashSet;

/**
 * created 20.06.16
 *
 * @author martin
 */
public class SetTreeNode<T> extends MultiTreeNode<T>
{

    public SetTreeNode(T value)
    {
        super(value);
        mChildren = null;
    }

    @Override
    public  void addChild(MultiTreeNode<T> child)
    {
        if (!isInternal())
            mChildren = new HashSet<>();
        else if (mChildren.contains(child))
            throw new TreeNodeException("cannot add child, already contains child " + child);
        mChildren.add(child);
    }

    @Override
    public void removeChild(MultiTreeNode<T> child)
    {
        if (!isInternal())
            throw new TreeNodeException("cannot remove child, is leaf");
        if (!hasChild(child))
            throw new TreeNodeException("cannot remove child, does not hold child " + child);

        mChildren.remove(child);
    }

    @Override
    public int getNumberOfChildren()
    {
        if (!isInternal())
            return 0;
        return mChildren.size();
    }

    @Override
    public boolean isInternal()
    {
        if (mChildren == null || mChildren.size() == 0)
            return false;
        return true;
    }

    @Override
    public boolean hasChild(TreeNode<T> child)
    {
        if (mChildren == null || !mChildren.contains(child))
            return false;
        return true;
    }


    private HashSet<MultiTreeNode<T>> mChildren;
}
