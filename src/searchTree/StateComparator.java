package searchTree;

public abstract class StateComparator<E extends SearchState> {

	public abstract boolean isStateExplored(E searchState);

	public abstract void markExplored(E searchState);

}
