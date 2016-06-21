package physics.generic.tree;

import java.util.Collection;

/**
 * created 20.06.16
 *
 * @author martin
 */
public class SetTree<V> extends MultiTree<V, SetTreeNode<V>>
{

    public SetTree(TreeNode<V> rootNode)
    {
        super(rootNode);
    }

    @Override
    public Collection<V> getChildren(V value)
    {
        return null;
    }

    @Override
    public void addChild(V nodeValue, V childValue)
    {

    }

    @Override
    public void removeChild(V nodeValue, V childValue)
    {

    }
}
