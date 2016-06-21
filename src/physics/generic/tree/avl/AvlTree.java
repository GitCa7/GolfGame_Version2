package physics.generic.tree.avl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;

import physics.generic.QuickSort;
import physics.generic.tree.avl.BinTreeNode.Side;

/**
 * Avl tree implementation
 * offers possibility to query existence of a value
 * add a value or remove a value.
 * Certain methods retrieving elements store it in a cache variable
 * which will be used on next look up and overwritten
 * @author martin
 * @param <T> type to store in tree
 */
public class AvlTree<T> implements Cloneable
{
	/**
	 * exception thrown if node requested does not exist
	 * @author martin
	 */
	public static class NotInTreeException extends IllegalArgumentException
	{
		public NotInTreeException() {}
		
		public NotInTreeException (String mess) { super (mess); }
	}
	
	/**
	 * class used for non-existing nodes
	 * @author martin
	 */
	public class NullNode extends BinTreeNode<T>
	{
		public NullNode() 
		{
			super (null);
		}
		
		public boolean isNull (BinTreeNode<T> check)
		{
			return (check.getElement() == null && check.isLeaf() && check.isRoot());
		}
	}
	
	/**
	 * wrapper for binary tree nodes
	 * used to compare nodes depending on their value
	 * @author martin
	 */
	public class NodeOrderWrapper implements Comparable<NodeOrderWrapper>
	{
		/**
		 * @param n node to use as reference for comparison
		 */
		public NodeOrderWrapper (BinTreeNode<T> n)
		{
			mNode = n;
		}
		
		/**
		 * @param comp node wrapper to compare reference with
		 * @return result of comparison of element of reference node to element of
		 * comp
		 */
		public int compareTo (NodeOrderWrapper comp)
		{
			if (mNode == null && comp.mNode == null)
				return 0;
			else if (mNode == null)
				return -1;
			else if (comp.mNode == null)
				return 1;
			return mComparator.compare(mNode.getElement(), comp.mNode.getElement());
		}
		
		private BinTreeNode<T> mNode;
	}
	
	/**
	 * default constructor
	 */
	public AvlTree(Comparator<T> comparator)
	{
		mComparator = comparator;
		mSize = 0;
	}
	
	/**
	 * @return elements stored in order
	 */
	public ArrayList<T> getOrderedElements()
	{
		class Order implements Visitor<T>
		{
			public Order()
			{
				mOrdered = new ArrayList<>();
			}
			
			public void visit (T elem)
			{
				mOrdered.add (elem);
			}
			
			public ArrayList<T> mOrdered;
		}
		
		Order o = new Order();
		inorder (o);
		return o.mOrdered;
	}
	
	/**
	 * @return cloned avl tree
	 * Note: elements stored are shallowly copied
	 * but deep copy is performed on structure
	 */
	public AvlTree<T> clone()
	{
		class Cloner implements Visitor<T>
		{
			public Cloner()
			{
				mClone = new AvlTree<>(mComparator);
			}
			
			public void visit (T elem)
			{
				if (mLast == null || mComparator.compare(mLast.getElement(), elem) < 0)
				{
					mClone.add (elem);
					mLast = mClone.getNode (elem, true);
				}
				else
				{
					mLast.getChild (Side.LEFT).setChild (new BinTreeNode<> (elem), Side.LEFT);
					mLast = mLast.getChild (Side.LEFT);
				}
			}
			
			public AvlTree<T> mClone;
			private BinTreeNode<T> mLast;
		}
		
		Cloner c = new Cloner();
		inorder (c);
		return c.mClone;
	}
	
	
	public String toString()
	{
		ArrayList<BinTreeNode<T>> level = new ArrayList<>();
		level.add (mRoot);
		return treeToString (level);
	}
	
	/**
	 * @param val value to search for
	 * @return reference to object with same value as val
	 * Note: uses cache
	 */
	public T getElement (T val)
	{
		BinTreeNode<T> node = getNode (val, true);
		if (new NullNode().isNull (node))
			throw new NotInTreeException ("value requested is not in tree and cannot be replaced");
		return node.getElement();
	}
	
	/**
	 * @param val a given value in the tree
	 * @return height of first node of value val found
	 * Note: uses cache
	 */
	public int getHeight (T val)
	{
		BinTreeNode<T> node = getNode (val, true);
		if (new NullNode().isNull (node))
			throw new NotInTreeException ("value requested is not in tree and cannot be replaced");
		return node.getHeight();
	}
	
	/**
	 * @return number of nodes stored
	 */
	public int getSize() { return mSize; }
	
	/**
	 * @param val value to search for
	 * @return true if element was found in tree, false otherwise
	 * Note: uses cache
	 */
	public boolean hasElement (T val)
	{
		BinTreeNode<T> node = getNode (val, true);
		if (new NullNode().isNull (node))
			return false;
		else
			return true;
	}
	
	
	public void inorder (Visitor<T> v)
	{
		if (getSize() == 0)
			return;
		
		Stack<BinTreeNode<T>> nodesToVisit = new Stack<>();
		BinTreeNode<T> curr = mRoot;
		boolean done = false;
		
		do
		{
			//go down left
			if (curr.hasChild (Side.LEFT))
			{
				nodesToVisit.add (curr);
				curr = curr.getChild (Side.LEFT);
			}
			else
			{
				//visit
				v.visit (curr.getElement());
				if (curr.hasChild (Side.RIGHT))
					curr = curr.getChild (Side.RIGHT);
				else
				{
					boolean goneRight = false;
					//go up stack until front of stack has right child
					while (!goneRight && !nodesToVisit.isEmpty())
					{
						BinTreeNode<T> stacked = nodesToVisit.pop();
						v.visit (stacked.getElement());
						//go to right child
						if (stacked.hasChild (Side.RIGHT))
						{
							curr = stacked.getChild (Side.RIGHT);
							goneRight = true;
						}
					}
					if (!goneRight && nodesToVisit.isEmpty())
						done = true;
				}
			}
		}
		while (!done);
	}
	
	/**
	 * erases all elements stored
	 */
	public void clear()
	{
		mRoot = null;
		mCache = null;
		mSize = 0;
	}
	
	/**
	 * @param addVal value to add to tree
	 */
	public void add (T addVal)
	{
		if (mRoot == null)
			mRoot = new BinTreeNode<>(addVal);
		else
		{
			BinTreeNode<T> leaf = getClosestLeaf (addVal);
			//add new node to tree
			if (mComparator.compare(addVal, leaf.getElement()) < 0)
			{
				leaf.setChild (new BinTreeNode<> (addVal, leaf), Side.LEFT);
				leaf = leaf.getChild (Side.LEFT);
			}
			else
			{
				leaf.setChild (new BinTreeNode<> (addVal, leaf), Side.RIGHT);
				leaf = leaf.getChild (Side.RIGHT);
			}
			//check for restructuring
			BinTreeNode<T> child = null, grandchild = null;
			boolean restored = false;
			while (!leaf.isRoot() && !restored)
			{
				grandchild = child;
				child = leaf;
				leaf = leaf.getParent();
				
				if (grandchild != null)
				{
					int heightL = 0, heightR = 0;
					if (leaf.hasChild (Side.LEFT))
						heightL = leaf.getChild (Side.LEFT).getHeight();
					if (leaf.hasChild (Side.RIGHT))
						heightR = leaf.getChild (Side.RIGHT).getHeight();
					//restructure
					if (Math.abs (heightL - heightR) > 1)
					{
						restructure (leaf, child, grandchild);
						restored = true;
					}
				}				
			}
		}
		++mSize;
	}
	
	/**
	 * @param remVal value of node to remove
	 * Note: uses cache, clears cache
	 */
	public void remove (T remVal)
	{
		//search node
		BinTreeNode<T> node = getNode (remVal, false);
		if (new NullNode().isNull (node))
			throw new NotInTreeException ("value requested is not in tree and cannot be replaced");
		//binary tree removal
		/*if (node.isRoot())
			mRoot = null;*/
		if (node.hasChild (Side.LEFT) && node.hasChild (Side.RIGHT))
		{
			BinTreeNode<T> leaf = getClosestLeaf (remVal);
			node.setElement (leaf.getElement());
			leaf.makeRoot();	//practically removes leaf node
			
			/*Side remSide = node.getSide();
			node = node.getParent();
			node.setChild (leaf, remSide);*/
			//node.getParent().getChild (node.getSide()).setElement (leaf.getElement());
			
		}
		else
		{
			BinTreeNode<T> replace = null;
			if (node.getChild (Side.LEFT) != null)
				replace = node.getChild (Side.LEFT);
			else if (node.getChild (Side.RIGHT) != null)
				replace = node.getChild (Side.RIGHT);
			
			if (node.isRoot())
				mRoot = replace;
			else
			{
				Side remSide = node.getSide();
				node = node.getParent();
				node.setChild (replace, remSide);
			}
		}
		//trinode restructuring
		do
		{
			int heightL = 0, heightR = 0;
			if (node.hasChild (Side.LEFT))
				heightL = node.getChild (Side.LEFT).getHeight();
			if (node.hasChild (Side.RIGHT))
				heightR = node.getChild (Side.RIGHT).getHeight();
			//if unbalanced
			if (Math.abs (heightL - heightR) > 1)
			{
				BinTreeNode<T> lvl2, lvl3;
				Side sideChosen;
				if (heightL < heightR)
				{
					lvl2 = node.getChild (Side.RIGHT);
					sideChosen = Side.RIGHT;
				}
				else
				{
					lvl2 = node.getChild (Side.LEFT);
					sideChosen = Side.LEFT;
				}
				
				int heightL2, heightR2;
				heightL2 = lvl2.getChild (Side.LEFT).getHeight();
				heightR2 = lvl2.getChild (Side.RIGHT).getHeight();
				if (heightL2 > heightR2)
					lvl3 = lvl2.getChild (Side.LEFT);
				else if (heightL2 < heightR2)
					lvl3 = lvl2.getChild (Side.RIGHT);
				else
					lvl3 = lvl2.getChild (sideChosen);
				restructure (node, lvl2, lvl3);
			}
			if (!node.isRoot())
				node = node.getParent();
		} while (!node.isRoot());
		--mSize;
	}
	
	
	/*
	 * @param oldVal value stored in tree to replace
	 * @param newVal value to replace oldVal with
	 * the first occurrence of oldVal will be replaced
	 */
	/*
	public void replace (T oldVal, T newVal)
	{
		BinTreeNode<T> node = getNode (oldVal);
		if (new NullNode().isNull (node))
			throw new NotInTreeException ("value requested is not in tree and cannot be replaced");
		node.setElement (newVal);
	}
	*/
	
	/**
	 * @param elem value of node to retrieve
	 * @param caching boolean flag indicating whether to cache value retrieved (true) or 
	 * whether to set cache variable to null (false)
	 * @return node having elem as value or null node if none is found
	 */
	private BinTreeNode<T> getNode (T elem, boolean caching)
	{
		if (mCache != null && mComparator.compare(mCache.getElement(), elem) == 0)
		{
			if (!caching)
			{
				BinTreeNode<T> tmp = mCache;
				mCache = null;
				return tmp;
			}
			return mCache;
		}
		BinTreeNode<T> currNode = mRoot;
		while (currNode != null)
		{
			int indicator = mComparator.compare(currNode.getElement(), elem);
			if (indicator == 0)
			{
				if (caching)
					mCache = currNode;
				else
					mCache = null;
				return currNode;
			}
			else if (indicator > 0)
				currNode = currNode.getChild (Side.LEFT);
			else
				currNode = currNode.getChild (Side.RIGHT);
		}
		return new NullNode();
	}
	
	/**
	 * @param val target value of leaf node
	 * @return leaf node with closest value possible to elem
	 */
	private BinTreeNode<T> getClosestLeaf (T val)
	{
		BinTreeNode<T> curr = mRoot;
		while (!curr.isLeaf())
		{
			if (curr.hasChild (Side.LEFT) && curr.hasChild (Side.RIGHT))
			{
				if (mComparator.compare(val, curr.getElement()) < 0)
					curr = curr.getChild (Side.LEFT);
				else
					curr = curr.getChild (Side.RIGHT);
			}
			else if (curr.hasChild (Side.LEFT))
				curr = curr.getChild (Side.LEFT);
			else
				curr = curr.getChild (Side.RIGHT);
		}
		return curr;
	}
	
	private String treeToString (ArrayList<BinTreeNode<T>> cs)
	{
		String ret = new String();
		ArrayList<BinTreeNode<T>> next = new ArrayList<>();
		for (BinTreeNode<T> c : cs)
		{
			ret += c.getElement() + ", ";
			if (c.hasChild (Side.LEFT))
				next.add (c.getChild (Side.LEFT));
			if (c.hasChild (Side.RIGHT))
				next.add (c.getChild (Side.RIGHT));
		}
		ret += "\n";
		if (!next.isEmpty())
			ret += treeToString (next);
		return ret;
	}
	
	/**
	 * @param x bottom up first unbalanced node
	 * @param y higher child of x
	 * @param z higher child of y
	 */
	private void restructure (BinTreeNode<T> x, BinTreeNode<T> y, BinTreeNode<T> z)
	{
		//map x, y, z to inorder sequence
		QuickSort<NodeOrderWrapper> orderInput = new QuickSort<>();
		
		orderInput.add (new NodeOrderWrapper (x));
		orderInput.add (new NodeOrderWrapper (y));
		orderInput.add (new NodeOrderWrapper (z));
		
		orderInput.sort (0, orderInput.size() - 1);

		//store inorder of subtrees of x, y, z != x, y, z
		ArrayList<BinTreeNode<T>> orderSubtrees = new ArrayList<>();
		for (NodeOrderWrapper w : orderInput)
		{
			for (Side s : Side.values())
			{
				BinTreeNode<T> child = w.mNode.getChild (s);
				if (child != y && child != z)
					orderSubtrees.add (child);
			}
		}

		//restructure parameters
		if (!x.isRoot())
		{
			Side xSide = x.getParent().isChild (x, Side.LEFT) ? Side.LEFT : Side.RIGHT;
			x.getParent().setChild (orderInput.get (1).mNode, xSide);
		}
		else if (!orderInput.get (1).mNode.isRoot())
		{
			mRoot = orderInput.get (1).mNode;
			mRoot.makeRoot();
		}
		
		orderInput.get (1).mNode.setChild (orderInput.get (0).mNode, Side.LEFT);
		orderInput.get (1).mNode.setChild (orderInput.get (2).mNode, Side.RIGHT);
		//restructure subtrees
		orderInput.get (0).mNode.setChild (orderSubtrees.get (0), Side.LEFT);
		orderInput.get (0).mNode.setChild (orderSubtrees.get (1), Side.RIGHT);
		orderInput.get (2).mNode.setChild (orderSubtrees.get (2), Side.LEFT);
		orderInput.get (2).mNode.setChild (orderSubtrees.get (3), Side.RIGHT);
	}
	
	/**
	 * @param n1 a given node
	 * @param n2 another given node
	 * swaps values of n1, n2
	 */
	private void swapValues (BinTreeNode<T> n1, BinTreeNode<T> n2)
	{
		T temp = n1.getElement();
		n1.setElement (n2.getElement());
		n2.setElement (temp);
	}
	
	//stores root, last node accessed via getNode()
	private BinTreeNode<T> mRoot, mCache;
	private int mSize;

	private Comparator<T> mComparator;
}
