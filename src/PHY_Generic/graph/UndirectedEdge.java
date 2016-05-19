package PHY_Generic.graph;

/**
 * Class modeling an undirected edge in a graph. Connects two graph nodes. 
 * @author martin
 * @param <T> data type stored in nodes
 */
public class UndirectedEdge<T> 
{
	/**
	 * parametric constructor
	 * @param n1 node 1
	 * @param n2 node 2
	 */
	public UndirectedEdge (GraphNode<T> n1, GraphNode<T> n2)
	{
		mN1 = n1;
		mN2 = n2;
	}
	
	/**
	 * @return node 1
	 */
	public GraphNode<T> getFirst() { return mN1; }
	
	/**
	 * @return node 2
	 */
	public GraphNode<T> getSecond() { return mN2; }
	
	/**
	 * @param n one of the two nodes this connects
	 * @return the other node n is connected to
	 * Precondition: n is one of the nodes of this
	 */
	public GraphNode<T> other (GraphNode<T> n)
	{
		return getFirst().equals (n) ? getSecond() : getFirst();
	}
	
	/**
	 * @param comp edge to compare with
	 * @return true if this connects both nodes of comp
	 */
	public boolean equals (UndirectedEdge<T> comp)
	{
		return (this.connectsNode (comp.getFirst()) && this.connectsNode (comp.getSecond()));
	}
	
	/**
	 * @param n a graph node
	 * @return true if n is a graph node belonging to this
	 */
	public boolean connectsNode (GraphNode<T> n)
	{
		return (getFirst().equals (n) || getSecond().equals (n));
	}
	
	private GraphNode<T> mN1, mN2;
}
