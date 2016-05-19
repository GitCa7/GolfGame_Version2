package PHY_Testing;


/**
 * groups static methods for convenient use of arrays
 * @author martin
 */
public class ArrayUtil
{
	@SafeVarargs
	public static <T> T[] construct (T ... vals) 
	{
		return vals;
	}
}
