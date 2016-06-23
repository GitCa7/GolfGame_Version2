package physics.generic.pool;

/**
 * interface for operation performed on an input parameter of type T, 
 * possibly returning an object of a different type.
 * @author martin
 *
 * @param <I> type of input parameter
 * @param <T> type of output parameter
 */
public interface Operation<I, O>
{
	/**
	 * performs operation
	 * @param in input object, shall not be modified
	 * @return the result of the operation on in
	 */
	public O operate(I in);
}
