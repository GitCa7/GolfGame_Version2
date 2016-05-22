package physics.generic.graph;

import java.util.*;

/**
 * class modeling an entry of an incidence graph
 * @author martin
 *
 * @param <T> data type to be stored
 */
public class NodeEntry<T> extends GraphNode<T> 
{
	/**
	 * @param value value to store
	 */
	public NodeEntry (T value)
	{
		super (value);
		mEdges = new HashSet<>();
	}
	
	/**
	 * @return set of edges formed by connections
	 */
	public Set<UndirectedEdge<T>> getEdges() { return mEdges; }
	
	/**
	 * @param e another node entry
	 * @return true if this is connected to e
	 */
	public boolean isConnected (NodeEntry<T> e) 
	{
		UndirectedEdge<T> edge = new UndirectedEdge<> (this, e);
		return mEdges.contains (edge);
	}
	
	/**
	 * @param edge node to connect with
	 * Precondition: edge is not yet connected to this
	 * Postcondition: connects edge with this
	 */
	public void addEdge (UndirectedEdge<T> edge)
	{
		filter (edge);
		mEdges.add (edge);
	}
	
	
	public void filter (UndirectedEdge<T> edge)
	{
		if (mEdges.contains (edge))
			throw new IllegalArgumentException ("cannot add edge, is already contained");
	}
	
	private Set<UndirectedEdge<T>> mEdges;
}
