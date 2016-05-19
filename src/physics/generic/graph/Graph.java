package physics.generic.graph;

import java.util.Set;

/**
 * basic interface for graph data structure
 * @author martin
 *
 * @param <T> data type stored
 */
public interface Graph<T>
{	
	/**
	 * @param node value of a node contained in the graph
	 * @return the values of the nodes connected to node. 
	 */
	public Set<T> getConnectedNodes (T node);
	
	/**
	 * @param node value of a node contained in the graph
	 * @return the degree of node
	 */
	public int getDegree (T node);
	
	/**
	 * @return the number of nodes
	 */
	public int size();
	
	/**
	 * @param node a value
	 * @return true if node exists within the graph
	 */
	public boolean hasValue (T node);
	
	/**
	 * @param newNode node to add
	 * Precondition: graph has no element newNode
	 */
	public void add (T newNode);
	
	/**
	 * @param remNode value of node to remove
	 * Precondition: graph has element remNode
	 */
	public void remove (T remNode);
	
	/**
	 * @param n1 value of node 1
	 * @param n2 value of node 2
	 * Precondition: n1, n2 need to be existing values in the graph
	 * Postcondition: node 1 and node 2 will be connected
	 */
	public void connect (T n1, T n2);

}
