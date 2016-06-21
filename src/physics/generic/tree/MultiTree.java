package physics.generic.tree;

import java.util.Collection;

/**
 * created 20.06.16
 *
 * @author martin
 */
public abstract class MultiTree<V, T extends MultiTreeNode<V>> extends Tree<V, T>
{

    public MultiTree(TreeNode<V> rootNode)
    {
        super(rootNode);
    }

    public abstract Collection<V> getChildren (V value);


    public abstract void addChild(V nodeValue, V childValue);


    public abstract void removeChild(V nodeValue, V childValue);
}
