package physics.generic;

/**
 * Class for caching a certain object. Able to forget and reremember again,
 * though the current object needs to be forgotten first before a new one can be remembered.
 * Provides getters for memorized object and the current state.
 * @autor martin
 * created 20.05.2016
 */
public class Cacher<T>
{
	/** custom exception class for cacher */
	public class CacherException extends IllegalStateException
	{
		public CacherException (String message) { super (message); }
	}

	/**
	 * default constructor: empty memorized object
	 */
	public Cacher()
	{
		mMemo = null;
	}

	/**
	 * @param comp cacher to compare with
	 * @return true if comp and this store equivalent objects
	 */
	public boolean equals (Cacher<T> comp)
	{
		if (this.isEmpty())
			return (comp.isEmpty());
		return this.mMemo.equals (comp.mMemo);
	}

	/**
	 * @return true if this is not remembering anything at the moment and can thus remember something.
	 */
	public boolean isEmpty()
	{
		return (mMemo == null);
	}

	/**
	 * shortcut combining call to remember and forget.
	 * @param memo object to remember
	 */
	public void forgetAndRemember (T memo)
	{
		forget();
		remember (memo);
	}

	/**
	 * Sets memo as the current remembered object.
	 * @param memo object to remember
	 * @throws CacherException if there is already an object that is remembered
	 */
	public void remember (T memo)
	{
		if (mMemo != null)
			throw new CacherException ("cacher currently contains non-null object " + mMemo);
		mMemo = memo;
	}

	/**
	 * resets the currently remembered object
	 */
	public void forget()
	{
		mMemo = null;
	}


	private T mMemo;
}
