package physics.generic;

/**
 * class performing a for each loop on every element for a stored operation
 * @author martin
 *
 */
public class ForEach<I, O>
{
	/**
	 * default constructor
	 */
	public ForEach() {}
	
	/**
	 * parametric constructor, sets operation of initialization
	 * @param operation operation to initialize
	 */
	public ForEach (Operation<I, O> operation)
	{
		mOperation = operation;
	}
	
	/**
	 * @param outputArray array of size matching the number of entries of in
	 * @param in input array
	 * @return outputArray containing the result of each operation
	 */
	public O[] operate (O[] outputArray, I ... in)
	{
		if (mOperation == null)
			throw new IllegalStateException ("for each has no operation stored");
			
		for (int nCnt = 0; nCnt < in.length; ++nCnt)
			outputArray[nCnt] = mOperation.operate (in[nCnt]);
		return outputArray;
	}
	
	/**
	 * sets the operation
	 * @param operation a given operation of matching types
	 */
	public void setOperation (Operation<I, O> operation)
	{
		mOperation = operation;
	}
	
	
	private Operation<I, O> mOperation;
}
