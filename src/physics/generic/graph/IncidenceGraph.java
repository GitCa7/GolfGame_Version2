package physics.generic.graph;

import java.util.*;

/**
 * graph implemented by incidence list
 * assuming unique nodes
 * @author martin
 * @param <T> type of node stored
 */
public class IncidenceGraph<T> implements Graph<T>
{
	/**
	 * constructs empty graph
	 */
	public IncidenceGraph ()
	{
		mNodes = new LinkedList<>();
	}
	
	/**
	 * @param value value of node in this graph
	 * @return the set of nodes connected to value
	 */
	public Set<T> getConnectedNodes (T value)
	{
		if (!hasValue (value))
			throw new IllegalArgumentException ("node does not exist, cannot get connected nodes");
		
		Set<T> values = new HashSet<>();
		
		NodeEntry<T> n = findEntry (value);
		for (UndirectedEdge<T> edge : n.getEdges())
			values.add (edge.other (n).getValue());
		return values;
	}
	
	public String toString()
	{
		String ret = new String();
		for (NodeEntry<T> n : mNodes)
			ret += n.getValue() + "\n";
		return ret;
	}
	
	/**
	 * @param val value of node in this graph
	 * @return the degree of val
	 */
	public int getDegree (T val)
	{
		NodeEntry<T> n = findEntry (val);
		if (n == null)
			throw new IllegalArgumentException ("node does not exist, cannot retrieve degree");
		return n.getEdges().size();
	}
	
	/**
	 * @return number of nodes in this graph
	 */
	public int size() { return mNodes.size(); }
	
	/**
	 * @param val value of node in this graph
	 * @return true if this contains val
	 */
	public boolean hasValue (T val) 
	{ 
		return (findEntry (val) != null);
	}
	
	/**
	 * @param newNode node to add
	 * Precondition: graph has no element newNode
	 */
	public void add (T newVal)
	{
		NodeEntry<T> n = findEntry (newVal);
		if (n != null)
			throw new IllegalArgumentException ("node already exists");
		
		mNodes.add (new NodeEntry<> (newVal));
	}
	
	/**
	 * @param remVal value of node in this graph
	 * Precondition: graph has value remVal
	 * Postcondition: remVal will be removed from this graph
	 */
	public void remove (T remVal)
	{
		NodeEntry<T> n = findEntry (remVal);
		if (n == null)
			throw new IllegalArgumentException ("node does not exist and cannot be removed");
		
		for (UndirectedEdge<T> e : n.getEdges())
		{
			NodeEntry<T> connected = findEntry (e.other (n).getValue());
			connected.getEdges().remove (n);
		}
		
		mNodes.remove (n);
	}
	
	/**
	 * @param v1 value of node in this graph
	 * @param v2 value of node in this graph
	 * Precondition: v1, v2 exit in this graph
	 * Postcondition: nodes of v1, v2 will be connected
	 */
	public void connect (T v1, T v2)
	{
		if (!hasValue (v1) || !hasValue (v2))
			throw new IllegalArgumentException ("at least one node does not exist, cannot connect");
		
		UndirectedEdge<T> newEdge = makeEdge(v1, v2);
		findEntry (v1).addEdge (newEdge);
		findEntry (v2).addEdge (newEdge);
	}
	
	//finds node entry corresponding to value
	private NodeEntry<T> findEntry (T value)
	{
		for (NodeEntry<T> n : mNodes)
		{
			if (n.equals (new GraphNode<T> (value)))
				return n;
		}
		return null;
	}
	
	//constructs a new edge connecting v1, v2
	private UndirectedEdge<T> makeEdge (T v1, T v2)
	{
		GraphNode<T> n1 = new GraphNode<> (v1), n2 = new GraphNode<> (v2);
		return new UndirectedEdge<> (n1, n2);
	}
	
	private LinkedList<NodeEntry<T>> mNodes;
}
