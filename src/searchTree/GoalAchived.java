package searchTree;

public abstract class GoalAchived<E extends SearchState> {

	/** Goal-test for a given state */
	public abstract boolean test(E aState);
}