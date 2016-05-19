package physics.generic.graph;

/**
 * node wrapper class for value
 * @author martin
 * @param <T> type to be stored
 */
public class GraphNode<T>
{
	/**
	 * parametric constructor
	 * @param val value to be stored in this node
	 */
	public GraphNode (T val)
	{
		mVal = val;
	}
	
	/**
	 * @return the value stored
	 */
	public T getValue() { return mVal; }
	
	/**
	 * @param comp another graph node
	 * @return true if the comparator of the type stored is true, false otherwise
	 */
	public boolean equals (GraphNode<T> comp)
	{
		return (comp.getValue().equals (this.getValue()));
	}
	
	private T mVal;
}
