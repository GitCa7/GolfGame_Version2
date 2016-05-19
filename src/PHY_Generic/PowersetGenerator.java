package PHY_Generic;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * provides utility for generating the powerset and computing
 * the number of elements in a powerset of a given set
 * @author martin
 *
 * @param <T>
 */
public class PowersetGenerator<T> extends ArrayList<T> 
{
	/**
	 * @param in set from which to generate the powerset
	 */
	public PowersetGenerator (Collection<T> in)
	{
		super (in);
	}
	
	/**
	 * @return the powerset of the set of elements stored
	 */
	public ArrayList<ArrayList<T>> generate()
	{
		ArrayList<ArrayList<T>> powerSet = new ArrayList<>();
		//add empty set
		powerSet.add (new ArrayList<T>());
		
		for (T elem : this)
		{
			ArrayList<ArrayList<T>> newPowerSet = (ArrayList<ArrayList<T>>) powerSet.clone();
			for (ArrayList<T> exist : powerSet)
			{
				ArrayList<T> clone = (ArrayList<T>) exist.clone();
				clone.add (elem);
				newPowerSet.add (clone);
			}
			powerSet = newPowerSet;
		}
		return powerSet;
	}
	
	/**
	 * @param minLength minimum cardinality of element of powerset
	 * @param maxLength maximum cardinality of element of powerset
	 * @return the powerset of the set stored reduced to elements of cardinalty
	 * between minLength (including) and maxLength (including)
	 */
	public ArrayList<ArrayList<T>> generate (int minLength, int maxLength)
	{
		ArrayList<ArrayList<T>> powerSet = new ArrayList<>();
		powerSet.add (new ArrayList<T>());
		
		for (int cElem = 0; cElem < size(); ++cElem)
		{
			ArrayList<ArrayList<T>> newPowerSet = new ArrayList<>();
			//iterate over existing subsets
			for (ArrayList<T> exist : powerSet)
			{
				//if element does not contain max elements
				if (exist.size() < maxLength)
				{
					int maxSize = exist.size() + size() - cElem;
					//if size - cElem + length - 1 == minlength:
					if (maxSize == minLength)
					{
						//append current element, dont clone
						exist.add (get (cElem));
						newPowerSet.add (exist);
					}
					//else if size - cElem + length - 1 > minlength
					else if (maxSize > minLength)
					{
						//append existing
						newPowerSet.add (exist);
						//clone, append to clone
						ArrayList<T> clone = (ArrayList<T>) exist.clone();
						clone.add (get(cElem));
						newPowerSet.add (clone);
					}
					//else
						//element will never reach minlength, dont add
				}
				else
					newPowerSet.add (exist);
			}
			powerSet = newPowerSet;
		}
		return powerSet;
	}
	
	/**
	 * @return the number of elements in the powerset
	 */
	public int sizeOfPowerset()
	{
		return (int) (Math.pow (2, size()));
	}
	
	/**
	 * @param minLen minimum number of elements in each element of power set
	 * @param maxLen maximum number of elements in each element of power set
	 * @return the number of elements in this capped power set
	 */
	public int sizeOfPowerset (int minLen, int maxLen)
	{
		int size = 0;
		for (int cLen = minLen; cLen <= maxLen; ++cLen)
		{
			int allPerms = (int) new Factorial (cLen).result();
			int duplicates = (int) new Factorial (cLen).result();
			int overhead = (int) new Factorial (size() - cLen).result();
			
			size += allPerms / (duplicates * overhead);
		}
		return size;
	}
}
