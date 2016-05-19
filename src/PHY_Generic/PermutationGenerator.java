package PHY_Generic;

import java.util.Collection;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * class generating permutations of a sequence of a given length. Extends the sequence
 * it operates on. Never attempts to clone anything.
 * @author martin
 *
 * @param <T> type of objects
 */
public class PermutationGenerator<T> extends ArrayList<T> 
{
	/**
	 * parametric constructor
	 * @param elems list of elements to generate permutations from
	 */
	public PermutationGenerator (Collection<T> elems)
	{
		super (elems);
	}
	
	/**
	 * @param length length of every permutation sequence
	 * @return nested arraylist storing permutations
	 */
	public ArrayList<ArrayList<T>> generate (int length)
	{
		if (length > size())
			throw new IllegalArgumentException ("only stores " + size() + " elements, cannot generate permutations of " + length);
		
		if (length > 0)
			return generate (new TreeSet<> (this), length);
		
		return new ArrayList<ArrayList<T>>();
	}
	
	/**
	 * @param available tree set of available elements
	 * @param depthCount counts how many recursions are to be made
	 * @return list of permutations of length == depthCount
	 */
	private ArrayList<ArrayList<T>> generate (TreeSet<T> available, int depthCount)
	{
		ArrayList<ArrayList<T>> perms = new ArrayList<>();
		for (T elem : available)
		{	
			if (depthCount > 1)
			{
				TreeSet<T> remaining = (TreeSet<T>) available.clone();
				remaining.remove (elem);
				for (ArrayList<T> smaller : generate (remaining, depthCount - 1))
				{
					ArrayList<T> complete = new ArrayList<>();
					complete.add (elem);
					complete.addAll (1, smaller);
					perms.add (complete);
				}
			}
			else
			{
				ArrayList<T> perm = new ArrayList<>();
				perm.add (elem);
				perms.add (perm);
			}
		}
		
		return perms;
	}
}
