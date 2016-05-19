package PHY_Testing.generic;


import static org.junit.Assert.*;

import PHY_Generic.PowersetGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import org.junit.Test;

public class PowersetGeneratorTest 
{
	/**
	 * @param pool elements of the set from which to generate the power set
	 * @return the power set of pool
	 */
	public ArrayList<ArrayList<Character>> generate (Character[] pool)
	{
		PowersetGenerator<Character> gen = new PowersetGenerator<> (Arrays.asList (pool));
		return gen.generate();
	}
	
	/**
	 * @param pool elements of the set from which to generate the power set
	 * @param min minimum size of elements in the capped power set
	 * @param max maximum size of elements in the capped power set
	 * @return the power set of pool restricted to elements of cardinality between min and max
	 */
	public ArrayList<ArrayList<Character>> generate (Character[] pool, int min, int max)
	{
		PowersetGenerator<Character> gen = new PowersetGenerator<> (Arrays.asList (pool));
		return gen.generate (min, max);
	}
	
	/**
	 * @param nested a nested collection of arraylists of characters
	 * @return the nested list transformed to a raw 2d array
	 */
	public static Character[][] to2dArray (ArrayList<ArrayList<Character>> nested)
	{
		Character[][] arr = new Character[nested.size()][];
		for (int cList = 0; cList < nested.size(); ++cList)
		{
			ArrayList<Character> list = nested.get (cList);
			arr[cList] = new Character[list.size()];
			for (int cElem = 0; cElem < list.size(); ++cElem)
				arr[cList][cElem] = list.get (cElem);
		}
		return arr;
	}
	
	public static Character[] randomSample (Character[] pool)
	{
		Random r = new Random (System.currentTimeMillis());
		int len = r.nextInt(pool.length + 1);
		Character[] sample = new Character[len];
		LinkedList<Character> linkedPool = new LinkedList<Character> (Arrays.asList (pool));
		
		for (int cLen = 0; cLen < len; ++cLen)
		{
			int choose = r.nextInt (linkedPool.size());
			sample[cLen] = linkedPool.get (choose);
			linkedPool.remove (choose);
		}
		return sample;
	}
	
	
	/**
	 * @param pool pool of available elements
	 * @param expected expected elements of powerset of pool
	 * @return true if all of expected were in the generated powerset
	 */
	public static boolean contained (ArrayList<ArrayList<Character>> obtained, Character[][] expected)
	{
		for (Character[] expect : expected)
		{
			boolean found = false;
			int cSearch = 0;
			while (cSearch < obtained.size() && !found)
			{
				if (setEquals (obtained.get (cSearch), expect))
					found = true;
				++cSearch;
			}
			
			if (!found)
				return false;
		}
		return true;
	}
	
	/**
	 * performs set equality
	 * @param s1 first set
	 * @param s2 second set
	 */
	public static boolean setEquals (ArrayList<Character> s1, Character[] s2)
	{
		if (s1.size() != s2.length)
			return false;
		
		for (Character c : s1)
		{
			boolean found = false;
			int cSearch = 0;
			while (cSearch < s2.length && !found)
			{
				if (c == s2[cSearch])
					found = true;
				++cSearch;
			}
			
			if (!found)
				return false;
		}
		
		for (Character c : s2)
		{
			boolean found = false;
			int cSearch = 0;
			while (cSearch < s1.size() && !found)
			{
				if (c == s1.get (cSearch))
					found = true;
				++cSearch;
			}
			
			if (!found)
				return false;
		}
		return true;
	}
	
	/**
	 * @param print array to print
	 * prints print and adds a new line
	 */
	public static void print (Character[] print)
	{
		for (Character elem : print)
			System.out.print (elem + ", ");
		System.out.println();
	}
	
	/**
	 * @param print nested array to print
	 * prints each element of the nested array in one line
	 */
	public static void print (Character[][] print)
	{
		for (Character[] elem : print)
			print (elem);
	}
	
	@Test
	/**
	 * tests for empty input set
	 */
	public void testEmpty()
	{
		Character[] pool = new Character[0];
		PowersetGenerator<Character> gen = new PowersetGenerator<> (Arrays.asList (pool));
		assertTrue (gen.generate().size() == 1);
	}
	
	@Test
	/**
	 * tests small input set exhaustively
	 */
	public void testExhaustive()
	{
		Character[] pool = {'a', 'b', 'c'};
		
		Character[] ps0 = new Character[0];
		Character[] ps1 = {'a'};
		Character[] ps2 = {'b'};
		Character[] ps3 = {'c'};
		Character[] ps4 = {'a', 'b'};
		Character[] ps5 = {'a', 'c'};
		Character[] ps6 = {'b', 'c'};
		
		Character[][] exhaustive = new Character[8][];
		exhaustive[0] = ps0;
		exhaustive[1] = ps1;
		exhaustive[2] = ps2;
		exhaustive[3] = ps3;
		exhaustive[4] = ps4;
		exhaustive[5] = ps5;
		exhaustive[6] = ps6;
		exhaustive[7] = pool;
		
		ArrayList<ArrayList<Character>> pSet = generate (pool);
		assertTrue (contained (pSet, exhaustive));
	}
	
	@Test
	/**
	 * tests larger input set for randomly generated samples
	 */
	public void testLargeSample()
	{
		Character[] pool = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
		int sampleSize = 1000;
		
		Character[][] samples = new Character[sampleSize][];
		for (int cSample = 0; cSample < samples.length; ++cSample)
			samples[cSample] = randomSample (pool);
		
		ArrayList<ArrayList<Character>> pSet = generate (pool);
		assertTrue (contained (pSet, samples));
	}
	
	@Test
	/**
	 * tests capped generator by generating the entire power set first
	 * and removing the elements of inadequate size
	 */
	public void testCapped()
	{
		Character[] pool = {'a', 'b', 'c', 'd'};
		int min = 2, max = 3;
		PowersetGenerator<Character> gen = new PowersetGenerator<> (Arrays.asList (pool));
		//remove elements too small/large
		ArrayList<ArrayList<Character>> generated = gen.generate();
		for (int cCheck = 0; cCheck < generated.size(); ++cCheck)
		{
			int size = generated.get (cCheck).size();
			if (size < min || size > max)
			{
				generated.remove (cCheck);
				--cCheck;
			}
		}
		
		ArrayList<ArrayList<Character>> pSet = generate (pool, min, max);
		Character[][] expected = to2dArray (generated);
		assertTrue (contained (pSet, expected));
		
		System.out.println ("generated");
		print (expected);
		System.out.println ("obtained");
		print (to2dArray (pSet));
	}
}
