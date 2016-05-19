package physics.generic;

/**
 * class computing factorials
 * @author martin
 */
public class Factorial
{
	/**
	 * @param in integer object to use for calculation. Will not be cloned, 
	 * can be altered from the outside
	 */
	public Factorial (Integer in)
	{
		mIn = in;
	}
	
	/**
	 * @return factorial of number stored
	 */
	public long result()
	{
		long result = 1;
		for (int cnt = mIn; cnt > 1; --cnt)
			result *= cnt;
		return result;
	}
	
	private Integer mIn;
}
