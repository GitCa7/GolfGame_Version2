package physics.testing.generic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import physics.generic.PermutationGenerator;

public class PermutationTester 
{
	public static final boolean PRINT_DEBUG = true;
	
	/**
	 * @param in input to be provided to permutation generator
	 * @param expected minimum cases to be contained in the permutation
	 */
	public static boolean test (Character[] in, Character[][] expected)
	{
		PermutationGenerator<Character> gen = new PermutationGenerator<>(Arrays.asList (in));
		
		ArrayList<ArrayList<Character>> perms = null;
		if (expected.length > 0)
			perms = gen.generate (expected[0].length);
		else
			perms = gen.generate (0);
		
		if (expected.length == 0)
			return (perms.isEmpty());
		
		for (Character[] exp : expected)
		{
			int cTest = 0;
			boolean found = false;
			while (cTest < perms.size() && !found)
			{
				if (listEqual (new ArrayList<>(Arrays.asList (exp)), perms.get (cTest)))
					found = true;
				++cTest;
			}
			
			if (!found)
				return false;
		}
		return true;
	}
	
	/**
	 * @param l1 one list
	 * @param l2 another list
	 * @return true if both lists have the same number of elements and 
	 * elements in order of each list are equal
	 */
	public static boolean listEqual (ArrayList<Character> l1, ArrayList<Character> l2)
	{
		if (l1.size() != l2.size())
			return false;
		
		for (int cChar = 0; cChar < l1.size(); ++cChar)
		{
			if (!l1.get (cChar).equals (l2.get (cChar)))
				return false;
		}
		return true;
	}
	
	/**
	 * prints pool, expected and actually generated permutations
	 * @param pool pool of elements to generate permutations from
	 * @param expected set of expected permutations
	 */
	public static void printDebug (Character[] pool, Character[][] expected, String caseMessage)
	{
		if (PRINT_DEBUG)
		{
			System.out.println ("physics/testing " + caseMessage);
			System.out.println ("in ");
			printArray (Arrays.asList (pool));
			
			System.out.println ("expecting");
			for (Character[] set : expected)
				printArray (Arrays.asList (set));
			
			System.out.println ("obtained");
			PermutationGenerator<Character> pGen = new PermutationGenerator<> (Arrays.asList (pool));
			for (ArrayList<Character> perm : pGen.generate (expected[0].length))
				printArray (perm);
			System.out.println();
		}
	}
	
	public static void printArray (Iterable<Character> in)
	{
		for (Character elem : in)
			System.out.print (elem + ", ");
		System.out.println();
	}
	
	@Test
	public void testEmpty()
	{
		PermutationGenerator<Character> gen = new PermutationGenerator<> (new ArrayList<Character>());
		//should be allowed
		assertTrue (gen.generate (0).isEmpty());
		
		
		boolean throwsUp = false;
		try
		{
			gen.generate (1).isEmpty();
		}
		catch (Exception e)
		{
			throwsUp = true;
		}
		assertTrue (throwsUp);
	}
	
	@Test
	public void testOne()
	{
		Character[] pool = {'a'};
		Character[][] perms = new Character[1][];
		perms[0] = pool;
		
		if (!test(pool, perms))
			printDebug (pool, perms, "one element sequence");
		
		assertTrue (test (pool, perms));
	}
	
	@Test
	public void testFive()
	{
		Character[] pool = {'a', 'b', 'c', 'd', 'e'};
		Character[][] perms = new Character[10][];
		
		perms[0] = pool;
		Character[] perms1 = {'e', 'd', 'c', 'b', 'a'};
		Character[] perms2 = {'a', 'c', 'e', 'd', 'b'};
		Character[] perms3 = {'b', 'c', 'd', 'e', 'a'};
		Character[] perms4 = {'e', 'a', 'c', 'b', 'd'};
		Character[] perms5 = {'c', 'b', 'a', 'd', 'e'};
		Character[] perms6 = {'b', 'a', 'c', 'd', 'e'};
		Character[] perms7 = {'a', 'e', 'b', 'c', 'd'};
		Character[] perms8 = {'c', 'a', 'd', 'e', 'b'};
		Character[] perms9 = {'d', 'a', 'c', 'b', 'e'};
		perms[1] = perms1;
		perms[2] = perms2;
		perms[3] = perms3;
		perms[4] = perms4;
		perms[5] = perms5;
		perms[6] = perms6;
		perms[7] = perms7;
		perms[8] = perms8;
		perms[9] = perms9;
		
		if (!test (pool, perms))
			printDebug (pool, perms, "5 element permutations, full length, limited sampling");
		
		assertTrue (test (pool, perms));
	}
	
	@Test
	public void testLimited()
	{
		Character[] pool = {'a', 'b', 'c', 'd', 'e'};
		Character[][] perms = new Character[10][];
		
		Character[] perms0 = {'a', 'b', 'c'};
		Character[] perms1 = {'e', 'c', 'a'};
		Character[] perms2 = {'a', 'e', 'b'};
		Character[] perms3 = {'c', 'd', 'e'};
		Character[] perms4 = {'c', 'b', 'd'};
		Character[] perms5 = {'c', 'b', 'e'};
		Character[] perms6 = {'b', 'a', 'c'};
		Character[] perms7 = {'e', 'b', 'c'};
		Character[] perms8 = {'c', 'a', 'd'};
		Character[] perms9 = {'d', 'c', 'e'};
		perms[0] = perms0;
		perms[1] = perms1;
		perms[2] = perms2;
		perms[3] = perms3;
		perms[4] = perms4;
		perms[5] = perms5;
		perms[6] = perms6;
		perms[7] = perms7;
		perms[8] = perms8;
		perms[9] = perms9;
		
		if (!test (pool, perms))
			printDebug (pool, perms, "5 elements, 3 length cap, limited sampling");
		
		assertTrue (test (pool, perms));
	}
	
	@Test
	public void testZeroLimited()
	{
		Character[] pool = {'a', 'b', 'c', 'd', 'e'};
		PermutationGenerator<Character> gen = new PermutationGenerator<> (new ArrayList<Character>());
		
		if (!test (pool, new Character[0][0]))
			printDebug (pool, new Character[0][0], "5 elements, 0 length cap");
		
		assertTrue (gen.generate(0).size() == 0);
	}
	
	@Test
	public void testExhaustive()
	{
		Character[] pool = {'a', 'b', 'c'};
		Character[] perm0 = pool;
		Character[] perm1 = {'a', 'c', 'b'};
		Character[] perm2 = {'b', 'a', 'c'};
		Character[] perm3 = {'b', 'c', 'a'};
		Character[] perm4 = {'c', 'a', 'b'};
		Character[] perm5 = {'c', 'b', 'a'};
		
		Character[][] expected = new Character[6][3];
		expected[0] = perm0;
		expected[1] = perm1;
		expected[2] = perm2;
		expected[3] = perm3;
		expected[4] = perm4;
		expected[5] = perm5;
		
		if (!test (pool, expected))
			printDebug (pool, expected, "3 elements, 3 length cap, all permutations given");
		
		assertTrue (test (pool, expected));
	}
}
